/**
 * Copyright [2014] Gaurav Gupta
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
package org.netbeans.jpa.modeler.properties.extend;

import javax.swing.JEditorPane;
import org.apache.commons.lang3.StringUtils;
import org.netbeans.jpa.modeler.spec.extend.ReferenceClass;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.properties.embedded.GenericEmbeddedEditor;

/**
 *
 * @author Gaurav Gupta
 */
public class ClassSelectionPanel extends GenericEmbeddedEditor<ReferenceClass> {

    private final ModelerFile modelerFile;
    private ReferenceClass referenceClass;

    @Override
    public void init() {
        initComponents();
        class_EditorPane = NBModelerUtil.getJavaSingleLineEditor(class_WrapperPanel, null, null).second();
    }

    @Override
    public ReferenceClass getValue() {
        if (StringUtils.isNotEmpty(class_EditorPane.getText())) {
            referenceClass = new ReferenceClass(class_EditorPane.getText());
        } else {
            referenceClass = null;
        }
        return referenceClass;
    }

    @Override
    public void setValue(ReferenceClass referenceClass) {
        this.referenceClass = referenceClass;
        if (referenceClass != null && StringUtils.isNotEmpty(referenceClass.getName())) {
            class_EditorPane.setText(referenceClass.getName());
        } else {
            dataType_ActionActionPerformed(null);
        }
    }

    public ClassSelectionPanel(ModelerFile modelerFile) {
        this.modelerFile = modelerFile;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        root_jLayeredPane = new javax.swing.JLayeredPane();
        dataType_Action = new javax.swing.JButton();
        class_WrapperPanel = new javax.swing.JLayeredPane();

        dataType_Action.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/jpa/modeler/properties/resource/searchbutton.png"))); // NOI18N
        dataType_Action.setPreferredSize(new java.awt.Dimension(37, 37));
        dataType_Action.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataType_ActionActionPerformed(evt);
            }
        });

        class_WrapperPanel.setMinimumSize(new java.awt.Dimension(306, 21));

        javax.swing.GroupLayout class_WrapperPanelLayout = new javax.swing.GroupLayout(class_WrapperPanel);
        class_WrapperPanel.setLayout(class_WrapperPanelLayout);
        class_WrapperPanelLayout.setHorizontalGroup(
            class_WrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 406, Short.MAX_VALUE)
        );
        class_WrapperPanelLayout.setVerticalGroup(
            class_WrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        root_jLayeredPane.setLayer(dataType_Action, javax.swing.JLayeredPane.DEFAULT_LAYER);
        root_jLayeredPane.setLayer(class_WrapperPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout root_jLayeredPaneLayout = new javax.swing.GroupLayout(root_jLayeredPane);
        root_jLayeredPane.setLayout(root_jLayeredPaneLayout);
        root_jLayeredPaneLayout.setHorizontalGroup(
            root_jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(root_jLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(class_WrapperPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataType_Action, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        root_jLayeredPaneLayout.setVerticalGroup(
            root_jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(root_jLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(root_jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dataType_Action, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(class_WrapperPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(root_jLayeredPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(root_jLayeredPane)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void dataType_ActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataType_ActionActionPerformed
        String dataType = NBModelerUtil.browseClass(modelerFile, class_EditorPane.getText());
        if (StringUtils.isNotEmpty(dataType)) {
            class_EditorPane.setText(dataType);
        }
    }//GEN-LAST:event_dataType_ActionActionPerformed
    private JEditorPane class_EditorPane;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane class_WrapperPanel;
    private javax.swing.JButton dataType_Action;
    private javax.swing.JLayeredPane root_jLayeredPane;
    // End of variables declaration//GEN-END:variables
}
