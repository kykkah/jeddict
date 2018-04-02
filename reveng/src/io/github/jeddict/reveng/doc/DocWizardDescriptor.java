/**
 * Copyright 2013-2018 the original author or authors from the Jeddict project (https://jeddict.github.io/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.jeddict.reveng.doc;

import io.github.jeddict.analytics.JeddictLogger;
import io.github.jeddict.jaxb.spec.JaxbVariableType;
import static io.github.jeddict.jcode.util.AttributeType.BOOLEAN;
import static io.github.jeddict.jcode.util.AttributeType.DOUBLE;
import static io.github.jeddict.jcode.util.AttributeType.INT;
import static io.github.jeddict.jcode.util.AttributeType.LONG;
import static io.github.jeddict.jcode.util.AttributeType.STRING;
import static io.github.jeddict.jcode.util.StringHelper.camelCase;
import static io.github.jeddict.jcode.util.StringHelper.firstLower;
import static io.github.jeddict.jcode.util.StringHelper.firstUpper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.aggregate.AggregateProgressFactory;
import org.netbeans.api.progress.aggregate.AggregateProgressHandle;
import org.netbeans.api.progress.aggregate.ProgressContributor;
import org.netbeans.api.project.Project;
import org.netbeans.api.templates.TemplateRegistration;
import io.github.jeddict.jpa.modeler.initializer.JPAModelerUtil;
import static io.github.jeddict.jpa.modeler.initializer.JPAModelerUtil.getModelerFileVersion;
import io.github.jeddict.jpa.spec.Basic;
import io.github.jeddict.jpa.spec.ElementCollection;
import io.github.jeddict.jpa.spec.Entity;
import io.github.jeddict.jpa.spec.EntityMappings;
import io.github.jeddict.jpa.spec.OneToMany;
import io.github.jeddict.jpa.spec.OneToOne;
import io.github.jeddict.jpa.spec.bean.BeanAttribute;
import io.github.jeddict.jpa.spec.bean.BeanClass;
import io.github.jeddict.jpa.spec.bean.BeanCollectionAttribute;
import io.github.jeddict.jpa.spec.bean.OneToManyAssociation;
import io.github.jeddict.jpa.spec.bean.OneToOneAssociation;
import io.github.jeddict.jpa.spec.extend.Attribute;
import io.github.jeddict.jpa.spec.extend.JavaClass;
import io.github.jeddict.reveng.BaseWizardDescriptor;
import static io.github.jeddict.reveng.doc.DocSetupPanelVisual.JAXB_SUPPORT;
import static io.github.jeddict.reveng.doc.DocSetupPanelVisual.JPA_SUPPORT;
import static io.github.jeddict.reveng.doc.DocSetupPanelVisual.JSONB_SUPPORT;
import static io.github.jeddict.reveng.doc.DocSetupPanelVisual.JSON_FILE;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import org.netbeans.modeler.core.exception.ProcessInterruptedException;
import org.netbeans.modules.j2ee.persistence.wizard.fromdb.ProgressPanel;
import org.netbeans.modules.j2ee.persistence.wizard.jpacontroller.ProgressReporter;
import org.netbeans.modules.j2ee.persistence.wizard.jpacontroller.ProgressReporterDelegate;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.util.NbBundle;
import static org.openide.util.NbBundle.getMessage;
import org.openide.util.RequestProcessor;
import javax.json.*;
import static javax.json.JsonValue.ValueType.FALSE;
import static javax.json.JsonValue.ValueType.TRUE;
import static org.netbeans.modeler.core.NBModelerUtil.getAutoGeneratedStringId;

@TemplateRegistration(
        folder = "Persistence",
        position = 2,
        displayName = "#DocWizardDescriptor_displayName",
        iconBase = "io/github/jeddict/reveng/doc/resources/DOC_ICON.png",
        description = "resources/DOC_RE_DESC.html",
        category = "persistence")
public final class DocWizardDescriptor extends BaseWizardDescriptor {

    private WizardDescriptor wizard;
    private Project project;

    private FileObject packageFileObject;
    private String fileName, jsonFileLocation;
    private boolean jpaSupport, jsonbSupport, jaxbSupport;

    private ProgressReporter reporter;
    private int progressIndex = 0;

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
        index = 0;
        if (project == null) {
            project = Templates.getProject(wizard);
        }
        WizardDescriptor.Panel secondPanel = new DocSetupPanel(project, wizard);
        String names[];

        panels = new ArrayList<>();
        panels.add(secondPanel);
        names = new String[]{
            getMessage(DocWizardDescriptor.class, "LBL_Doc_Setup")
        };

        wizard.putProperty("NewFileWizard_Title", getMessage(DocWizardDescriptor.class, "NewFileWizard_Title"));
        org.netbeans.modeler.component.Wizards.mergeSteps(wizard, panels.toArray(new WizardDescriptor.Panel[0]), names);
    }

    @Override
    public Set<?> instantiate() throws IOException {
        if (project == null) {
            project = Templates.getProject(wizard);
        }
        packageFileObject = Templates.getTargetFolder(wizard);
        fileName = Templates.getTargetName(wizard);
        jsonFileLocation = (String) wizard.getProperty(JSON_FILE);
        jpaSupport = (Boolean) wizard.getProperty(JPA_SUPPORT);
        jsonbSupport = (Boolean) wizard.getProperty(JSONB_SUPPORT);
        jaxbSupport = (Boolean) wizard.getProperty(JAXB_SUPPORT);

        final String title = NbBundle.getMessage(DocWizardDescriptor.class, "TITLE_Progress_Class_Diagram"); //NOI18N
        return instantiateJSONREProcess(title);
    }

    @Override
    public String name() {
        return NbBundle.getMessage(DocWizardDescriptor.class, "LBL_WizardTitle");
    }

    private Set<?> instantiateJSONREProcess(final String title) throws IOException {
        final ProgressContributor progressContributor = AggregateProgressFactory.createProgressContributor(title);
        final AggregateProgressHandle handle = AggregateProgressFactory.createHandle(title, new ProgressContributor[]{progressContributor}, null, null);
        final ProgressPanel progressPanel = new ProgressPanel();
        final JComponent progressComponent = AggregateProgressFactory.createProgressComponent(handle);
        reporter = new ProgressReporterDelegate(progressContributor, progressPanel);
        final Runnable r = () -> {
            File jsonFile = new File(jsonFileLocation);
            try (Reader reader = new FileReader(jsonFile)) {
                handle.start();
                JsonReader jsonReader = Json.createReader(reader);
                JsonObject jsonObject = jsonReader.readObject();
                int progressStepCount = getProgressStepCount(10);
                progressContributor.start(progressStepCount);
                generateModel(jsonObject);
                progressContributor.progress(progressStepCount);
            } catch (IOException ioe) {
                Logger.getLogger(DocWizardDescriptor.class.getName()).log(Level.INFO, null, ioe);
                NotifyDescriptor nd = new NotifyDescriptor.Message(ioe.getLocalizedMessage(), NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);
            } catch (ProcessInterruptedException ce) {
                Logger.getLogger(DocWizardDescriptor.class.getName()).log(Level.INFO, null, ce);
            } finally {
                progressContributor.finish();
                SwingUtilities.invokeLater(progressPanel::close);
                JeddictLogger.createModelerFile("DOC-REV-ENG");
                handle.finish();
            }
        };

        SwingUtilities.invokeLater(new Runnable() {
            private boolean first = true;

            @Override
            public void run() {
                if (!first) {
                    RequestProcessor.getDefault().post(r);
                    progressPanel.open(progressComponent, title);
                } else {
                    first = false;
                    SwingUtilities.invokeLater(this);
                }
            }
        });
        return Collections.singleton(DataFolder.findFolder(packageFileObject));
    }

    public static int getProgressStepCount(int baseCount) {
        return baseCount + 2;
    }

    private EntityMappings generateModel(JsonObject jsonObject) throws IOException, ProcessInterruptedException {
        String progressMsg = getMessage(DocWizardDescriptor.class, "MSG_Progress_Class_Diagram_Pre"); //NOI18N;
        reporter.progress(progressMsg, progressIndex++);

        String version = getModelerFileVersion();
        final EntityMappings entityMappingsSpec = EntityMappings.getNewInstance(version);
        entityMappingsSpec.setGenerated();

        JavaClass javaClass;
        if (jpaSupport) {
            javaClass = generateEntity(entityMappingsSpec, "RootClass", jsonObject);
        } else {
            javaClass = generateClass(entityMappingsSpec, "RootClass", jsonObject);
        }
        javaClass.setXmlRootElement(jaxbSupport);
        entityMappingsSpec.setJaxbSupport(jaxbSupport);

        JPAModelerUtil.createNewModelerFile(entityMappingsSpec, packageFileObject, fileName, true, true);
        return entityMappingsSpec;
    }

    private JavaClass generateClass(EntityMappings entityMapping, String className, JsonObject jsonObject) {
        reporter.progress(getMessage(DocWizardDescriptor.class, "MSG_Progress_Class_Parsing", className), progressIndex++);

        BeanClass beanClass = new BeanClass();
        beanClass.setId(getAutoGeneratedStringId());
        beanClass.setClazz(firstUpper(camelCase(className)));
        jsonObject.forEach((key, value) -> {
            Attribute baseAttribute;
            if (value instanceof JsonString) {
                baseAttribute = createBeanAttribute(beanClass, STRING);
            } else if (value instanceof JsonNumber) {
                baseAttribute = createBeanAttribute(beanClass, STRING);
            } else if (value instanceof JsonArray) {
                JsonArray jsonArray = (JsonArray) value;
                if (!jsonArray.isEmpty()) {
                    JsonValue arrayElement = jsonArray.get(0);
                    if (arrayElement instanceof JsonString) {
                        baseAttribute = createBeanCollection(beanClass, STRING);
                    } else if (arrayElement instanceof JsonNumber) {
                        baseAttribute = createBeanCollection(beanClass, getJsonNumberType((JsonNumber) arrayElement));
                    } else if (arrayElement instanceof JsonObject) {
                        OneToManyAssociation attribute;
                        baseAttribute = attribute = new OneToManyAssociation();
                        attribute.setOwner(true);
                        attribute.setConnectedClass(generateClass(entityMapping, key, (JsonObject) arrayElement));
                        beanClass.getAttributes().addOneToMany(attribute);
                    } else {
                        baseAttribute = createBeanCollection(beanClass, STRING);
                    }
                } else {
                    baseAttribute = createBeanCollection(beanClass, STRING);
                }
            } else if (value instanceof JsonObject) {
                OneToOneAssociation attribute;
                baseAttribute = attribute = new OneToOneAssociation();
                attribute.setOwner(true);
                attribute.setConnectedClass(generateClass(entityMapping, key, (JsonObject) value));
                beanClass.getAttributes().addOneToOne(attribute);
            } else if (value instanceof JsonValue) {
                if (((JsonValue) value).getValueType() == TRUE || ((JsonValue) value).getValueType() == FALSE) {
                    baseAttribute = createBeanAttribute(beanClass, BOOLEAN);
                } else {
                    baseAttribute = createBeanAttribute(beanClass, STRING);
                }
            } else {
                baseAttribute = createBeanAttribute(beanClass, STRING);
            }
            updateAttributeName(baseAttribute, key);
        });
        entityMapping.addBeanClass(beanClass);
        return beanClass;
    }

    private BeanAttribute createBeanAttribute(BeanClass beanClass, String attributeType) {
        BeanAttribute attribute = new BeanAttribute();
        attribute.setAttributeType(attributeType);
        beanClass.getAttributes().addBasic(attribute);
        if (jaxbSupport) {
            attribute.setJaxbVariableType(JaxbVariableType.XML_ATTRIBUTE);
        }
        return attribute;
    }

    private BeanCollectionAttribute createBeanCollection(BeanClass beanClass, String attributeType) {
        BeanCollectionAttribute attribute = new BeanCollectionAttribute();
        attribute.setAttributeType(attributeType);
        beanClass.getAttributes().addElementCollection(attribute);
        if (jaxbSupport) {
            attribute.setJaxbVariableType(JaxbVariableType.XML_ELEMENT);
        }
        return attribute;
    }

    private Entity generateEntity(EntityMappings entityMapping, String className, JsonObject jsonObject) {
        reporter.progress(getMessage(DocWizardDescriptor.class, "MSG_Progress_Class_Parsing", className), progressIndex++);

        Entity entity = new Entity();
        entity.setId(getAutoGeneratedStringId());
        entity.setClazz(firstUpper(camelCase(className)));
        jsonObject.forEach((key, value) -> {
            Attribute baseAttribute;
            if (value instanceof JsonString) {
                baseAttribute = createBasicAttribute(entity, STRING);
            } else if (value instanceof JsonNumber) {
                baseAttribute = createBasicAttribute(entity, getJsonNumberType((JsonNumber) value));
            } else if (value instanceof JsonArray) {
                JsonArray jsonArray = (JsonArray) value;
                if (!jsonArray.isEmpty()) {
                    JsonValue arrayElement = jsonArray.get(0);
                    if (arrayElement instanceof JsonString) {
                        baseAttribute = createElementCollection(entity, STRING);
                    } else if (arrayElement instanceof JsonNumber) {
                        baseAttribute = createElementCollection(entity, getJsonNumberType((JsonNumber) arrayElement));
                    } else if (arrayElement instanceof JsonObject) {
                        OneToMany attribute;
                        baseAttribute = attribute = new OneToMany();
                        attribute.setOwner(true);
                        attribute.setConnectedEntity(generateEntity(entityMapping, key, (JsonObject) arrayElement));
                        entity.getAttributes().addOneToMany(attribute);
                    } else {
                        baseAttribute = createElementCollection(entity, STRING);
                    }
                } else {
                    baseAttribute = createElementCollection(entity, STRING);
                }
            } else if (value instanceof JsonObject) {
                OneToOne attribute;
                baseAttribute = attribute = new OneToOne();
                attribute.setOwner(true);
                attribute.setConnectedEntity(generateEntity(entityMapping, key, (JsonObject) value));
                entity.getAttributes().addOneToOne(attribute);
            } else if (value instanceof JsonValue) {
                if (((JsonValue) value).getValueType() == TRUE || ((JsonValue) value).getValueType() == FALSE) {
                    baseAttribute = createBasicAttribute(entity, BOOLEAN);
                } else {
                    baseAttribute = createBasicAttribute(entity, STRING);
                }
            } else {
                baseAttribute = createBasicAttribute(entity, STRING);
            }
            updateAttributeName(baseAttribute, key);
        });
        entityMapping.addEntity(entity);
        return entity;
    }

    private void updateAttributeName(Attribute attribute, String key) {
        if (attribute != null) {
            attribute.setId(getAutoGeneratedStringId());
            String name = firstLower(camelCase(key));
            if (jsonbSupport && !name.equalsIgnoreCase(key)) {
                attribute.setJsonbProperty(key);
            }
            if (jaxbSupport && !name.equalsIgnoreCase(key)) {
                attribute.getJaxbMetadata().setName(key);
            }
            attribute.setName(name);
        }
    }

    private Basic createBasicAttribute(Entity entity, String attributeType) {
        Basic attribute = new Basic();
        attribute.setAttributeType(attributeType);
        entity.getAttributes().addBasic(attribute);
        if (jaxbSupport) {
            attribute.setJaxbVariableType(JaxbVariableType.XML_ATTRIBUTE);
        }
        return attribute;
    }

    private ElementCollection createElementCollection(Entity entity, String attributeType) {
        ElementCollection attribute = new ElementCollection();
        attribute.setTargetClass(attributeType);
        entity.getAttributes().addElementCollection(attribute);
        if (jaxbSupport) {
            attribute.setJaxbVariableType(JaxbVariableType.XML_ELEMENT);
        }
        return attribute;
    }

    private String getJsonNumberType(JsonNumber jsonNumber) {
        String type = LONG;
        if (null != jsonNumber.getClass().getSimpleName()) {
            switch (jsonNumber.getClass().getSimpleName()) {
                case "JsonIntNumber":
                    type = INT;
                    break;
                case "JsonLongNumber":
                    type = LONG;
                    break;
                default:
                    type = DOUBLE;
                    break;
            }
        }
        return type;
    }

}
