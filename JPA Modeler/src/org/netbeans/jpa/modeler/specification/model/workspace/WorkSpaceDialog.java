/**
 * Copyright [2017] Gaurav Gupta
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
package org.netbeans.jpa.modeler.specification.model.workspace;

import java.util.Set;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import org.apache.commons.lang3.StringUtils;
import org.netbeans.jpa.modeler.properties.classmember.EntityMappingMemberPanel;
import org.netbeans.jpa.modeler.spec.EntityMappings;
import org.netbeans.jpa.modeler.spec.extend.IAttributes;
import org.netbeans.jpa.modeler.spec.extend.JavaClass;
import org.netbeans.jpa.modeler.spec.workspace.WorkSpace;
import org.netbeans.jpa.modeler.spec.workspace.WorkSpaceItem;
import org.netbeans.jpa.modeler.specification.model.scene.JPAModelerScene;
import static org.netbeans.jpa.modeler.specification.model.util.JPAModelerUtil.WORKSPACE_ICON;
import static org.netbeans.jpa.modeler.specification.model.workspace.WorkSpaceManager.findDependents;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.properties.window.GenericDialog;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 *
 * @author jGauravGupta
 */
public class WorkSpaceDialog extends GenericDialog {

    private final JPAModelerScene scene;
    private WorkSpace workSpace;
    private final boolean createWorkSpace;
    private final EntityMappings entityMappings;
    
    /**
     * Creates new form EntityGenerationSettingDialog
     * @param scene
     * @param entityMappings
     */
    public WorkSpaceDialog(JPAModelerScene scene, WorkSpace workSpace) {
        this.scene=scene;
        this.workSpace=workSpace;
        this.createWorkSpace = workSpace==null;
        initComponents();
        
        entityMappings = scene.getBaseElementSpec();
        if (this.workSpace == null) {
            this.workSpace = new WorkSpace();
            this.workSpace.setId(NBModelerUtil.getAutoGeneratedStringId());
        }
        nameTextField.setText(this.workSpace.getName());
        
        entityMappingPanel = (EntityMappingMemberPanel) classGenerationPanel;
        entityMappingPanel.init();
        entityMappingPanel.setClassCheckable(javaClass -> {
            return this.workSpace.getItems().contains(new WorkSpaceItem(javaClass));
                });
        entityMappingPanel.setValue(entityMappings);
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor. 
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        classGenerationPanel = new EntityMappingMemberPanel("WorkSpace Entities", scene);
        action_LayeredPane = new javax.swing.JLayeredPane();
        save_Button = new javax.swing.JButton();
        cancel_Button = new javax.swing.JButton();
        nameLayeredPane = new javax.swing.JLayeredPane();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(WorkSpaceDialog.class, "WorkSpaceDialog.title")); // NOI18N
        setIconImage(WORKSPACE_ICON.getImage());

        javax.swing.GroupLayout classGenerationPanelLayout = new javax.swing.GroupLayout(classGenerationPanel);
        classGenerationPanel.setLayout(classGenerationPanelLayout);
        classGenerationPanelLayout.setHorizontalGroup(
            classGenerationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        classGenerationPanelLayout.setVerticalGroup(
            classGenerationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(save_Button, getSaveButtonText());
        save_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_ButtonActionPerformed(evt);
            }
        });
        setDefaultButton(save_Button);

        org.openide.awt.Mnemonics.setLocalizedText(cancel_Button, org.openide.util.NbBundle.getMessage(WorkSpaceDialog.class, "WorkSpaceDialog.cancel_Button.text")); // NOI18N
        cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_ButtonActionPerformed(evt);
            }
        });

        action_LayeredPane.setLayer(save_Button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        action_LayeredPane.setLayer(cancel_Button, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout action_LayeredPaneLayout = new javax.swing.GroupLayout(action_LayeredPane);
        action_LayeredPane.setLayout(action_LayeredPaneLayout);
        action_LayeredPaneLayout.setHorizontalGroup(
            action_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(action_LayeredPaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(save_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancel_Button))
        );
        action_LayeredPaneLayout.setVerticalGroup(
            action_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(action_LayeredPaneLayout.createSequentialGroup()
                .addGroup(action_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save_Button)
                    .addComponent(cancel_Button))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(WorkSpaceDialog.class, "WorkSpaceDialog.nameLabel.text")); // NOI18N

        nameTextField.setText(org.openide.util.NbBundle.getMessage(WorkSpaceDialog.class, "WorkSpaceDialog.nameTextField.text")); // NOI18N

        nameLayeredPane.setLayer(nameLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        nameLayeredPane.setLayer(nameTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout nameLayeredPaneLayout = new javax.swing.GroupLayout(nameLayeredPane);
        nameLayeredPane.setLayout(nameLayeredPaneLayout);
        nameLayeredPaneLayout.setHorizontalGroup(
            nameLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nameLayeredPaneLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );
        nameLayeredPaneLayout.setVerticalGroup(
            nameLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nameLayeredPaneLayout.createSequentialGroup()
                .addGroup(nameLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(classGenerationPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(nameLayeredPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(action_LayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(classGenerationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(action_LayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private boolean validateField() {
        if (StringUtils.isBlank(nameTextField.getText())) {
            showMessageDialog(this, "Name can't be empty", "Invalid Value", WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    private void save_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_ButtonActionPerformed
        if (!validateField()) {
            return;
        }
        
        //add dependantClasses
        Set<JavaClass<? extends IAttributes>> selectedClasses = entityMappingPanel.getSelectedJavaClass()
                        .stream()
                        .collect(toSet());
        Set<JavaClass<? extends IAttributes>> dependantClasses = findDependents(selectedClasses);
        String dependantClassesText = dependantClasses.stream().map(JavaClass::getClazz).collect(joining(","));
        int MAX_CHAR = 100;
        dependantClassesText = dependantClassesText.substring(0, Math.min(MAX_CHAR, dependantClassesText.length())) + "...";
        if (dependantClasses.size() > 0) {
            int option = showConfirmDialog(
                    WindowManager.getDefault().getMainWindow(), 
                    String.format("Workspace have dependecies on [%s] classes, Are you sure you want to proceed by adding \n [%s]?", dependantClasses.size(), dependantClassesText), 
                    "Dependant class", 
                    YES_NO_OPTION);
            if (option == OK_OPTION) {
                selectedClasses.addAll(dependantClasses);
            } else {
                return;
            }
        }
        
        //save data
        workSpace.setItems(
                selectedClasses
                        .stream()
                        .map(WorkSpaceItem::new)
                        .collect(toSet())
        );
        workSpace.setName(nameTextField.getText());
        scene.getModelerPanelTopComponent().changePersistenceState(false);
        saveActionPerformed(evt);
    }//GEN-LAST:event_save_ButtonActionPerformed

    private void cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_ButtonActionPerformed
        cancelActionPerformed(evt);
    }//GEN-LAST:event_cancel_ButtonActionPerformed

    @NbBundle.Messages({
        "CREATE_BUTTON=Create",
        "SAVE_BUTTON=Save"
    })
    private String getSaveButtonText(){
        if(createWorkSpace){
            return Bundle.CREATE_BUTTON();
        } else {
            return Bundle.SAVE_BUTTON();
        }
    }

    /**
     * @return the workSpace
     */
    public WorkSpace getWorkSpace() {
        return workSpace;
    }
    
    private final EntityMappingMemberPanel entityMappingPanel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane action_LayeredPane;
    private javax.swing.JButton cancel_Button;
    private javax.swing.JPanel classGenerationPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLayeredPane nameLayeredPane;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton save_Button;
    // End of variables declaration//GEN-END:variables
}
