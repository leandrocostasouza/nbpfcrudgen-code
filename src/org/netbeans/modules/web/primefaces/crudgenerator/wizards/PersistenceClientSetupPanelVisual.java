/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
/*
 * Kay Wrobel elects to include this software in this distribution under the
 * GPL Version 2 license.
 */
package org.netbeans.modules.web.primefaces.crudgenerator.wizards;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.modules.j2ee.common.J2eeProjectCapabilities;
import org.netbeans.modules.j2ee.core.api.support.SourceGroups;
import org.netbeans.modules.j2ee.core.api.support.java.JavaIdentifiers;
import org.netbeans.modules.j2ee.persistence.wizard.Util;
import org.netbeans.modules.j2ee.persistence.wizard.fromdb.SourceGroupUISupport;
import org.netbeans.modules.web.api.webmodule.WebModule;
import org.netbeans.modules.web.api.webmodule.WebProjectConstants;
import org.netbeans.modules.web.jsf.JSFConfigUtilities;
import org.netbeans.modules.web.jsf.JSFUtils;
import org.netbeans.modules.web.jsf.api.facesmodel.JSFVersion;
import org.netbeans.modules.web.jsf.dialogs.BrowseFolders;
import org.netbeans.modules.web.jsf.palette.items.CancellableDialog;
//import org.netbeans.modules.web.jsf.palette.items.ManagedBeanCustomizer.OpenTemplateAction;
import org.netbeans.modules.web.primefaces.crudgenerator.util.LibraryUtil;
import org.netbeans.modules.web.primefaces.crudgenerator.util.Version;
import org.netbeans.spi.java.project.support.ui.PackageView;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;

/**
 *
 * @author Pavel Buzek
 */
public class PersistenceClientSetupPanelVisual extends javax.swing.JPanel implements DocumentListener, CancellableDialog {

    public static final String PRIMEFACES_VIEW_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/view.ftl"; // NOI18N
    public static final String PRIMEFACES_LIST_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/list.ftl"; // NOI18N
    public static final String PRIMEFACES_CREATE_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/create.ftl"; // NOI18N
    public static final String PRIMEFACES_EDIT_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/edit.ftl"; // NOI18N
    public static final String PRIMEFACES_BUNDLE_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/bundle.ftl"; // NOI18N
    public static final String PRIMEFACES_CONTROLLER_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/controller.ftl"; // NOI18N
    public static final String PRIMEFACES_CONVERTER_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/converter.ftl"; // NOI18N
    public static final String PRIMEFACES_ABSTRACTCONTROLLER_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/abstractcontroller.ftl"; // NOI18N
    public static final String PRIMEFACES_TEMPLATE_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/template.ftl"; // NOI18N
    public static final String PRIMEFACES_APPMENU_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/appmenu.ftl"; // NOI18N
    public static final String PRIMEFACES_APPINDEX_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/appindex.ftl"; // NOI18N
    public static final String UTIL_TEMPLATE = "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/JsfUtil.ftl"; // NOI18N
    private WizardDescriptor wizard;
    private Project project;
    private JTextComponent jpaPackageComboBoxEditor, jsfPackageComboBoxEditor, converterPackageComboBoxEditor;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private boolean cancelled = false;
    private WebModule wm;

    /**
     * Creates new form CrudSetupPanel
     */
    public PersistenceClientSetupPanelVisual(WizardDescriptor wizard) {
        this.wizard = wizard;
        initComponents();
        jpaPackageComboBoxEditor = (JTextComponent) jpaPackageComboBox.getEditor().getEditorComponent();
        jpaPackageComboBoxEditor.getDocument().addDocumentListener(this);
        jsfPackageComboBoxEditor = (JTextComponent) jsfPackageComboBox.getEditor().getEditorComponent();
        jsfPackageComboBoxEditor.getDocument().addDocumentListener(this);
        converterPackageComboBoxEditor = (JTextComponent) converterPackageComboBox.getEditor().getEditorComponent();
        converterPackageComboBoxEditor.getDocument().addDocumentListener(this);
        jsfFolder.getDocument().addDocumentListener(this);
        genericIncludeFolder.getDocument().addDocumentListener(this);
        entityIncludeFolder.getDocument().addDocumentListener(this);
        localizationBundleTextField.getDocument().addDocumentListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jsfFolder = new javax.swing.JTextField();
        browseFolderButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        projectLabel = new javax.swing.JLabel();
        projectTextField = new javax.swing.JTextField();
        locationLabel = new javax.swing.JLabel();
        locationComboBox = new javax.swing.JComboBox();
        jsfPackageLabel = new javax.swing.JLabel();
        jsfPackageComboBox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jpaPackageLabel = new javax.swing.JLabel();
        jpaPackageComboBox = new javax.swing.JComboBox();
        overrideExistingCheckBox = new javax.swing.JCheckBox();
        localizationBundleLabel = new javax.swing.JLabel();
        localizationBundleTextField = new javax.swing.JTextField();
        defaultRowsLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        defaultRowsPerPageTemplateLabel = new javax.swing.JLabel();
        defaultRowsPerPageTemplate = new javax.swing.JTextField();
        defaultRowsTextField = new javax.swing.JTextField();
        primeFacesVersionForLabel = new javax.swing.JLabel();
        primeFacesVersionLabel = new javax.swing.JLabel();
        codiVersionForLabel = new javax.swing.JLabel();
        codiVersionLabel = new javax.swing.JLabel();
        searchLabelsLabel = new javax.swing.JLabel();
        searchLabelsTextBox = new javax.swing.JTextField();
        converterPackageComboBox = new javax.swing.JComboBox();
        converterPackageLabel = new javax.swing.JLabel();
        createFunctionCheckBox = new javax.swing.JCheckBox();
        updateFunctionCheckBox = new javax.swing.JCheckBox();
        deleteFunctionCheckBox = new javax.swing.JCheckBox();
        readFunctionCheckBox = new javax.swing.JCheckBox();
        sortFunctionCheckBox = new javax.swing.JCheckBox();
        filterFunctionCheckBox = new javax.swing.JCheckBox();
        growlCheckBox = new javax.swing.JCheckBox();
        growlLifeSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jsfVersionLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        genericIncludeFolder = new javax.swing.JTextField();
        browseGIFolderButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        entityIncludeFolder = new javax.swing.JTextField();
        browseEIFolderButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cdiLabel = new javax.swing.JLabel();
        tooltipMessagesCheckBox = new javax.swing.JCheckBox();

        setName(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_JSFPagesAndClasses")); // NOI18N

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel2.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/web/primefaces/crudgenerator/wizards/Bundle").getString("MNE_JSF_Pages").charAt(0));
        jLabel2.setLabelFor(jsfFolder);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/netbeans/modules/web/primefaces/crudgenerator/wizards/Bundle"); // NOI18N
        jLabel2.setText(bundle.getString("LBL_JSF_pages_folder")); // NOI18N

        browseFolderButton.setMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/web/primefaces/crudgenerator/wizards/Bundle").getString("MNE_Browse").charAt(0));
        browseFolderButton.setText(bundle.getString("LBL_Browse")); // NOI18N
        browseFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseFolderButtonActionPerformed(evt);
            }
        });

        jLabel4.setText(bundle.getString("MSG_Jsf_Pages_Location")); // NOI18N

        projectLabel.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/web/primefaces/crudgenerator/wizards/Bundle").getString("MNE_Project").charAt(0));
        projectLabel.setLabelFor(projectTextField);
        projectLabel.setText(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Project")); // NOI18N

        projectTextField.setEditable(false);

        locationLabel.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/web/primefaces/crudgenerator/wizards/Bundle").getString("MNE_Location").charAt(0));
        locationLabel.setLabelFor(locationComboBox);
        org.openide.awt.Mnemonics.setLocalizedText(locationLabel, org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_SrcLocation")); // NOI18N

        locationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationComboBoxActionPerformed(evt);
            }
        });

        jsfPackageLabel.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/web/primefaces/crudgenerator/wizards/Bundle").getString("MNE_Package").charAt(0));
        jsfPackageLabel.setLabelFor(jsfPackageComboBox);
        jsfPackageLabel.setText(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Package")); // NOI18N

        jsfPackageComboBox.setEditable(true);

        jLabel6.setText(bundle.getString("MSG_Jpa_Jsf_Packages")); // NOI18N

        jpaPackageLabel.setLabelFor(jpaPackageComboBox);
        jpaPackageLabel.setText(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Jpa_Controller_Package")); // NOI18N

        jpaPackageComboBox.setEditable(true);

        overrideExistingCheckBox.setText(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "PersistenceClientSetupPanelVisual.overrideExistingFiles")); // NOI18N
        overrideExistingCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overrideExistingCheckBoxActionPerformed(evt);
            }
        });

        localizationBundleLabel.setLabelFor(localizationBundleTextField);
        localizationBundleLabel.setText(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "PersistenceClientSetupPanelVisual.localizationBundle")); // NOI18N

        defaultRowsLabel.setText("Default Rows Displayed:");

        jLabel3.setText("Specify some default paging information for data lists");

        defaultRowsPerPageTemplateLabel.setText("Default Page Selector:");

        defaultRowsPerPageTemplate.setText("10,20,30");

        defaultRowsTextField.setText("10");

        primeFacesVersionForLabel.setText("PrimeFaces Version:");

        primeFacesVersionLabel.setText("jLabel8");

        codiVersionForLabel.setText("MyFaces CODI Version:");

        codiVersionLabel.setText("jLabel7");

        searchLabelsLabel.setText("Field name artifacts for foreign fields:");

        searchLabelsTextBox.setText("descr,name");
        searchLabelsTextBox.setToolTipText("Comma-separated list of field name artifacts to look for");

        converterPackageComboBox.setEditable(true);

        converterPackageLabel.setText("Converter Package:");

        createFunctionCheckBox.setSelected(true);
        createFunctionCheckBox.setText("Create");
        createFunctionCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createFunctionCheckBoxActionPerformed(evt);
            }
        });

        updateFunctionCheckBox.setSelected(true);
        updateFunctionCheckBox.setText("Edit");

        deleteFunctionCheckBox.setSelected(true);
        deleteFunctionCheckBox.setText("Delete");

        readFunctionCheckBox.setSelected(true);
        readFunctionCheckBox.setText("View");

        sortFunctionCheckBox.setSelected(true);
        sortFunctionCheckBox.setText("Sort");

        filterFunctionCheckBox.setSelected(true);
        filterFunctionCheckBox.setText("Filter");

        growlCheckBox.setSelected(true);
        growlCheckBox.setText("Growl");
        growlCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                growlCheckBoxActionPerformed(evt);
            }
        });

        growlLifeSpinner.setValue(3000);

        jLabel1.setText("JSF:");

        jsfVersionLabel.setText("jLabel5");

        jLabel5.setLabelFor(genericIncludeFolder);
        jLabel5.setText("Generic Include Folder:");

        browseGIFolderButton.setText(bundle.getString("LBL_Browse")); // NOI18N
        browseGIFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseGIFolderButtonActionPerformed(evt);
            }
        });

        jLabel7.setLabelFor(entityIncludeFolder);
        jLabel7.setText("Entity Include Folder:");

        browseEIFolderButton.setText(bundle.getString("LBL_Browse")); // NOI18N
        browseEIFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseEIFolderButtonActionPerformed(evt);
            }
        });

        jLabel8.setText("CDI:");

        cdiLabel.setText("jLabel9");

        tooltipMessagesCheckBox.setText("Tooltip Messages");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(searchLabelsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchLabelsTextBox))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(projectLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(locationLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jpaPackageLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jsfPackageLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(converterPackageLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(localizationBundleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(entityIncludeFolder)
                            .addComponent(genericIncludeFolder, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(localizationBundleTextField)
                            .addComponent(jsfFolder, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 70, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(projectTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(locationComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jpaPackageComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 227, Short.MAX_VALUE)
                                    .addComponent(jsfPackageComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(converterPackageComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(browseFolderButton)
                                .addComponent(browseGIFolderButton))
                            .addComponent(browseEIFolderButton, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(defaultRowsPerPageTemplateLabel)
                                    .addComponent(defaultRowsLabel))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(defaultRowsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(defaultRowsPerPageTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(overrideExistingCheckBox)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(createFunctionCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(updateFunctionCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteFunctionCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(readFunctionCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sortFunctionCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(filterFunctionCheckBox))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(growlCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(growlLifeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tooltipMessagesCheckBox))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(primeFacesVersionForLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(primeFacesVersionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(codiVersionForLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(codiVersionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jsfVersionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cdiLabel)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectLabel)
                    .addComponent(projectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locationLabel)
                    .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jpaPackageLabel)
                    .addComponent(jpaPackageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jsfPackageLabel)
                    .addComponent(jsfPackageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(converterPackageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(converterPackageLabel))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jsfFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseFolderButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(genericIncludeFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseGIFolderButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(browseEIFolderButton)
                    .addComponent(entityIncludeFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(localizationBundleLabel)
                    .addComponent(localizationBundleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(defaultRowsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(defaultRowsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(defaultRowsPerPageTemplateLabel)
                    .addComponent(defaultRowsPerPageTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchLabelsLabel)
                    .addComponent(searchLabelsTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createFunctionCheckBox)
                    .addComponent(updateFunctionCheckBox)
                    .addComponent(deleteFunctionCheckBox)
                    .addComponent(readFunctionCheckBox)
                    .addComponent(sortFunctionCheckBox)
                    .addComponent(filterFunctionCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(growlCheckBox)
                    .addComponent(growlLifeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tooltipMessagesCheckBox))
                .addGap(18, 18, 18)
                .addComponent(overrideExistingCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(primeFacesVersionForLabel)
                    .addComponent(primeFacesVersionLabel)
                    .addComponent(codiVersionForLabel)
                    .addComponent(codiVersionLabel)
                    .addComponent(jLabel1)
                    .addComponent(jsfVersionLabel)
                    .addComponent(jLabel8)
                    .addComponent(cdiLabel))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel2.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_JSF_pages_folder")); // NOI18N
        jLabel2.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_JSF_pages_folder")); // NOI18N
        jsfFolder.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ACSD_JSF_Pages")); // NOI18N
        browseFolderButton.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ACSD_Browser")); // NOI18N
        jLabel4.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "MSG_Jsf_Pages_Location")); // NOI18N
        jLabel4.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "MSG_Jsf_Pages_Location")); // NOI18N
        projectLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Project")); // NOI18N
        projectLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Project")); // NOI18N
        projectTextField.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ACSD_Project")); // NOI18N
        locationLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_SrcLocation")); // NOI18N
        locationLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_SrcLocation")); // NOI18N
        locationComboBox.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ACSD_Location")); // NOI18N
        jsfPackageLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Package")); // NOI18N
        jsfPackageLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Package")); // NOI18N
        jsfPackageComboBox.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ACSD_Package")); // NOI18N
        jLabel6.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "MSG_Jpa_Jsf_Packages")); // NOI18N
        jLabel6.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "MSG_Jpa_Jsf_Packages")); // NOI18N
        jpaPackageLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Jpa_Controller_Package")); // NOI18N
        jpaPackageLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Jpa_Controller_Package")); // NOI18N
        jpaPackageComboBox.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ACSD_Package")); // NOI18N
        converterPackageComboBox.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ACSD_Package")); // NOI18N

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void browseEIFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseEIFolderButtonActionPerformed
        Sources s = (Sources) ProjectUtils.getSources(Templates.getProject(wizard));
        org.netbeans.api.project.SourceGroup[] groups = s.getSourceGroups(WebProjectConstants.TYPE_DOC_ROOT);
        org.openide.filesystems.FileObject fo = BrowseFolders.showDialog(groups);
        if (fo != null) {
            String res = "/" + JSFConfigUtilities.getResourcePath(groups, fo, '/', true);
            entityIncludeFolder.setText(res);
        }
    }//GEN-LAST:event_browseEIFolderButtonActionPerformed

    private void browseGIFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseGIFolderButtonActionPerformed
        Sources s = (Sources) ProjectUtils.getSources(Templates.getProject(wizard));
        org.netbeans.api.project.SourceGroup[] groups = s.getSourceGroups(WebProjectConstants.TYPE_DOC_ROOT);
        org.openide.filesystems.FileObject fo = BrowseFolders.showDialog(groups);
        if (fo != null) {
            String res = "/" + JSFConfigUtilities.getResourcePath(groups, fo, '/', true);
            genericIncludeFolder.setText(res);
        }
    }//GEN-LAST:event_browseGIFolderButtonActionPerformed

    private void growlCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_growlCheckBoxActionPerformed
        this.growlLifeSpinner.setEnabled(this.growlCheckBox.isSelected());
    }//GEN-LAST:event_growlCheckBoxActionPerformed

    private void createFunctionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFunctionCheckBoxActionPerformed
        if (createFunctionCheckBox.isSelected()) {
            updateFunctionCheckBox.setSelected(true);
        }
    }//GEN-LAST:event_createFunctionCheckBoxActionPerformed

    private void overrideExistingCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overrideExistingCheckBoxActionPerformed
        changeSupport.fireChange();
    }//GEN-LAST:event_overrideExistingCheckBoxActionPerformed

    private void locationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationComboBoxActionPerformed
        locationChanged();
    }//GEN-LAST:event_locationComboBoxActionPerformed

    private void browseFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseFolderButtonActionPerformed
        Sources s = (Sources) ProjectUtils.getSources(Templates.getProject(wizard));
        org.netbeans.api.project.SourceGroup[] groups = s.getSourceGroups(WebProjectConstants.TYPE_DOC_ROOT);
        org.openide.filesystems.FileObject fo = BrowseFolders.showDialog(groups);
        if (fo != null) {
            String res = "/" + JSFConfigUtilities.getResourcePath(groups, fo, '/', true);
            jsfFolder.setText(res);
        }
    }//GEN-LAST:event_browseFolderButtonActionPerformed

    private void supportPrimeFacesCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
        changeSupport.fireChange();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseEIFolderButton;
    private javax.swing.JButton browseFolderButton;
    private javax.swing.JButton browseGIFolderButton;
    private javax.swing.JLabel cdiLabel;
    private javax.swing.JLabel codiVersionForLabel;
    private javax.swing.JLabel codiVersionLabel;
    private javax.swing.JComboBox converterPackageComboBox;
    private javax.swing.JLabel converterPackageLabel;
    private javax.swing.JCheckBox createFunctionCheckBox;
    private javax.swing.JLabel defaultRowsLabel;
    private javax.swing.JTextField defaultRowsPerPageTemplate;
    private javax.swing.JLabel defaultRowsPerPageTemplateLabel;
    private javax.swing.JTextField defaultRowsTextField;
    private javax.swing.JCheckBox deleteFunctionCheckBox;
    private javax.swing.JTextField entityIncludeFolder;
    private javax.swing.JCheckBox filterFunctionCheckBox;
    private javax.swing.JTextField genericIncludeFolder;
    private javax.swing.JCheckBox growlCheckBox;
    private javax.swing.JSpinner growlLifeSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox jpaPackageComboBox;
    private javax.swing.JLabel jpaPackageLabel;
    private javax.swing.JTextField jsfFolder;
    private javax.swing.JComboBox jsfPackageComboBox;
    private javax.swing.JLabel jsfPackageLabel;
    private javax.swing.JLabel jsfVersionLabel;
    private javax.swing.JLabel localizationBundleLabel;
    private javax.swing.JTextField localizationBundleTextField;
    private javax.swing.JComboBox locationComboBox;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JCheckBox overrideExistingCheckBox;
    private javax.swing.JLabel primeFacesVersionForLabel;
    private javax.swing.JLabel primeFacesVersionLabel;
    private javax.swing.JLabel projectLabel;
    private javax.swing.JTextField projectTextField;
    private javax.swing.JCheckBox readFunctionCheckBox;
    private javax.swing.JLabel searchLabelsLabel;
    private javax.swing.JTextField searchLabelsTextBox;
    private javax.swing.JCheckBox sortFunctionCheckBox;
    private javax.swing.JCheckBox tooltipMessagesCheckBox;
    private javax.swing.JCheckBox updateFunctionCheckBox;
    // End of variables declaration//GEN-END:variables

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    boolean valid(WizardDescriptor wizard) {
        //        List<Entity> entities = (List<Entity>) wizard.getProperty(WizardProperties.ENTITY_CLASS);
        //        String controllerPkg = getJsfPackage();
        //
        //        boolean filesAlreadyExist = false;
        //        String troubleMaker = "";
        //        for (Entity entity : entities) {
        //            String entityClass = entity.getClass2();
        //            String simpleClassName = JSFClientGenerator.simpleClassName(entityClass);
        //            String firstLower = simpleClassName.substring(0, 1).toLowerCase() + simpleClassName.substring(1);
        //            String folder = jsfFolder.getText().endsWith("/") ? jsfFolder.getText() : jsfFolder.getText() + "/";
        //            folder = folder + firstLower;
        //            String controller = controllerPkg + "." + simpleClassName + "Controller";
        //            String fqn = getJsfPackage().length() > 0 ? getJsfPackage().replace('.', '/') + "/" + simpleClassName : simpleClassName;
        //            if (getLocationValue().getRootFolder().getFileObject(fqn + "Controller.java") != null) {
        //                filesAlreadyExist = true;
        //                troubleMaker = controllerPkg + "." + simpleClassName + "Controller.java";
        //                break;
        //            }
        //            if (getLocationValue().getRootFolder().getFileObject(fqn + "Converter.java") != null) {
        //                filesAlreadyExist = true;
        //                troubleMaker = controllerPkg + "." + simpleClassName + "Converter.java";
        //                break;
        //            }
        //        }
        //        if (filesAlreadyExist) {
        //            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE,                                  // NOI18N
        //                NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "MSG_FilesAlreadyExist", troubleMaker));
        //            return false;
        //        }
        //        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, null); // NOI18N

        //2013-0215 Kay Wrobel: Stop the wizard if JSF is not 2.0
        boolean jsf2Generator = "true".equals(wizard.getProperty(PersistenceClientIterator.JSF2_GENERATOR_PROPERTY));
        if (!jsf2Generator) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_Not_JSF20"));
            return false;
        }

        ClassPath cp = ClassPath.getClassPath(getLocationValue().getRootFolder(), ClassPath.COMPILE);
        ClassLoader cl = null;
        if (cp != null) {
            cl = cp.getClassLoader(true);
        }

        Version pfVersion = LibraryUtil.getVersion(cl, "primefaces");
        String pfVersionString = pfVersion != null ? pfVersion.toString() : "";
        wizard.putProperty(WizardProperties.PRIMEFACES_VERSION, pfVersionString);

        Version codiVersion = LibraryUtil.getVersion(cl, "MyFaces Extensions-CDI Core-API");
        String codiVersionString = codiVersion != null ? codiVersion.toString() : "";
        wizard.putProperty(WizardProperties.MYFACES_CODI_VERSION, codiVersionString);
        
        Version jsfVersion = new Version(JSFVersion.get(wm,true).getShortName().replace("JSF ", ""));
        String jsfVersionString = jsfVersion != null ? jsfVersion.toString() : "";
        wizard.putProperty(WizardProperties.JSF_VERSION, jsfVersionString);

        if (pfVersion == null) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_PrimeFaces_NotFound"));
            return false;
        }

        if (Util.isContainerManaged(project)) {
            try {
                Class.forName("javax.transaction.UserTransaction", false, cl);
            } catch (ClassNotFoundException cnfe) {
                wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_UserTransactionUnavailable"));
                return false;
            } catch (UnsupportedClassVersionError ucve) {
                Logger.getLogger(PersistenceClientSetupPanelVisual.class.getName()).log(Level.WARNING, ucve.toString());
            }
        }

        Sources srcs = (Sources) ProjectUtils.getSources(project);
        SourceGroup sgWeb[] = srcs.getSourceGroups(WebProjectConstants.TYPE_DOC_ROOT);
        FileObject pagesRootFolder = sgWeb[0].getRootFolder();
        File pagesRootFolderAsFile = FileUtil.toFile(pagesRootFolder);
        String jsfFolderText = jsfFolder.getText();
        try {
            String canonPath = new File(pagesRootFolderAsFile, jsfFolderText).getCanonicalPath();
        } catch (IOException ioe) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_JsfTargetChooser_InvalidJsfFolder"));
            return false;
        }

        String genericIncludeFolderText = genericIncludeFolder.getText();
        try {
            String canonPath = new File(pagesRootFolderAsFile, genericIncludeFolderText).getCanonicalPath();
        } catch (IOException ioe) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_JsfTargetChooser_InvalidGenericIncludeFolder"));
            return false;
        }

        String entityIncludeFolderText = entityIncludeFolder.getText();
        try {
            String canonPath = new File(pagesRootFolderAsFile, entityIncludeFolderText).getCanonicalPath();
        } catch (IOException ioe) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_JsfTargetChooser_InvalidEntityIncludeFolder"));
            return false;
        }

        String[] packageNames = {getJpaPackage(), getJsfPackage(), getConverterPackage()};
        for (int i = 0; i < packageNames.length; i++) {
            if (packageNames[i].trim().equals("")) { // NOI18N
                wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_JavaTargetChooser_CantUseDefaultPackage"));
                return false;
            }

            if (!JavaIdentifiers.isValidPackageName(packageNames[i])) {
                wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_JavaTargetChooser_InvalidPackage")); //NOI18N
                return false;
            }

            if (!SourceGroups.isFolderWritable(getLocationValue(), packageNames[i])) {
                wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_JavaTargetChooser_UnwritablePackage")); //NOI18N
                return false;
            }
        }
        if (overrideExistingCheckBox.isVisible()) {
            boolean conflict = PersistenceClientIterator.doesSomeFileExistAlready(
                    getLocationValue().getRootFolder(), pagesRootFolder, getJpaPackage(),
                    getJsfPackage(), getConverterPackage(), jsfFolder.getText(), genericIncludeFolder.getText(), entityIncludeFolder.getText(), (List<String>) wizard.getProperty(WizardProperties.ENTITY_CLASS),
                    localizationBundleTextField.getText());
            if (conflict) {
                if (overrideExistingCheckBox.isSelected()) {
                    wizard.putProperty(WizardDescriptor.PROP_WARNING_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_JavaTargetChooser_FileAlreadyExistWarning")); //NOI18N
                } else {
                    wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_JavaTargetChooser_FileAlreadyExist")); //NOI18N
                    return false;
                }
            }
        }
        if (localizationBundleTextField.isVisible() && localizationBundleTextField.getText().length() == 0) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "ERR_JavaTargetChooser_MissingBundleName")); //NOI18N
            return false;
        }

        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, null); // NOI18N
        return true;
    }

    public SourceGroup getLocationValue() {
        return (SourceGroup) locationComboBox.getSelectedItem();
    }

    public String getJsfPackage() {
        return jsfPackageComboBoxEditor.getText();
    }

    public String getJpaPackage() {
        return jpaPackageComboBoxEditor.getText();
    }

    public String getConverterPackage() {
        return converterPackageComboBoxEditor.getText();
    }

    private void locationChanged() {
        updateSourceGroupPackages();
//        changeSupport.fireChange();
    }

    void read(WizardDescriptor settings) {
        jsfFolder.setText((String) settings.getProperty(WizardProperties.JSF_FOLDER));
        genericIncludeFolder.setText((String) settings.getProperty(WizardProperties.JSF_GI_FOLDER));
        entityIncludeFolder.setText((String) settings.getProperty(WizardProperties.JSF_EI_FOLDER));

        project = Templates.getProject(settings);
        //2013-10-04 Kay Wrobel: Also retrieve the web module
        wm = WebModule.getWebModule(project.getProjectDirectory());

        projectTextField.setText(ProjectUtils.getInformation(project).getDisplayName());

        SourceGroup[] sourceGroups = SourceGroups.getJavaSourceGroups(project);
        SourceGroupUISupport.connect(locationComboBox, sourceGroups);

        jsfPackageComboBox.setRenderer(PackageView.listRenderer());
        converterPackageComboBox.setRenderer(PackageView.listRenderer());

        updateSourceGroupPackages();

        if (J2eeProjectCapabilities.forProject(project).isEjb31LiteSupported()) {
            //change label if we will generate session beans
            jpaPackageLabel.setText(org.openide.util.NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "LBL_Jpa_SessionBean_Package")); // NOI18N
        }
        if (localizationBundleTextField.getText().length() == 0) {
            localizationBundleTextField.setText(NbBundle.getMessage(PersistenceClientSetupPanelVisual.class, "PersistenceClientSetupPanelVisual.defaultBundleName")); // NOI18N
        }

        String primeFacesVersion = (String) settings.getProperty(WizardProperties.PRIMEFACES_VERSION);
        String codiVersion = (String) settings.getProperty(WizardProperties.MYFACES_CODI_VERSION);
        String jsfVersion = (String) settings.getProperty(WizardProperties.JSF_VERSION);
        
        primeFacesVersionLabel.setText(primeFacesVersion);
        codiVersionLabel.setText(codiVersion);
        jsfVersionLabel.setText(jsfVersion);
        cdiLabel.setText((new Boolean(PersistenceClientIterator.isCdiEnabled(project))).toString());

    }

    void store(WizardDescriptor settings) {
        settings.putProperty(WizardProperties.JSF_FOLDER, jsfFolder.getText());
        settings.putProperty(WizardProperties.JSF_GI_FOLDER, genericIncludeFolder.getText());
        settings.putProperty(WizardProperties.JSF_EI_FOLDER, entityIncludeFolder.getText());
        String jpaPkg = getJpaPackage();
        String jsfPkg = getJsfPackage();
        String cnvPkg = getConverterPackage();
        settings.putProperty(WizardProperties.JPA_CLASSES_PACKAGE, jpaPkg);
        settings.putProperty(WizardProperties.JSF_CLASSES_PACKAGE, jsfPkg);
        settings.putProperty(WizardProperties.JSF_CONVERTER_PACKAGE, cnvPkg);
        settings.putProperty(WizardProperties.JAVA_PACKAGE_ROOT_FILE_OBJECT, getLocationValue().getRootFolder());
        settings.putProperty(WizardProperties.LOCALIZATION_BUNDLE_NAME, localizationBundleTextField.getText());
        //2013-01-17 Kay Wrobel
        settings.putProperty(WizardProperties.DEFAULT_DATATABLE_ROWS, defaultRowsTextField.getText());
        settings.putProperty(WizardProperties.DEFAULT_DATATABLE_ROWSPERPAGETEMPLATE, defaultRowsPerPageTemplate.getText());
        settings.putProperty(WizardProperties.PRIMEFACES_VERSION, primeFacesVersionLabel.getText());
        settings.putProperty(WizardProperties.MYFACES_CODI_VERSION, codiVersionLabel.getText());
        //2013-01-25 Kay Wrobel
        settings.putProperty(WizardProperties.SEARCH_LABEL_ARTIFACTS, searchLabelsTextBox.getText());
        //2013-02-09 Kay Wrobel
        settings.putProperty(WizardProperties.CREATE_FUNCTION, Boolean.valueOf(createFunctionCheckBox.isSelected()));
        settings.putProperty(WizardProperties.READ_FUNCTION, Boolean.valueOf(readFunctionCheckBox.isSelected()));
        settings.putProperty(WizardProperties.UPDATE_FUNCTION, Boolean.valueOf(updateFunctionCheckBox.isSelected()));
        settings.putProperty(WizardProperties.DELETE_FUNCTION, Boolean.valueOf(deleteFunctionCheckBox.isSelected()));
        settings.putProperty(WizardProperties.SORT_FUNCTION, Boolean.valueOf(sortFunctionCheckBox.isSelected()));
        settings.putProperty(WizardProperties.FILTER_FUNCTION, Boolean.valueOf(filterFunctionCheckBox.isSelected()));
        settings.putProperty(WizardProperties.GROWL_MESSAGES, Boolean.valueOf(growlCheckBox.isSelected()));
        settings.putProperty(WizardProperties.GROWL_LIFE, growlLifeSpinner.getValue());
        //2013-10-04 Kay Wrobel
        settings.putProperty(WizardProperties.JSF_VERSION, jsfVersionLabel.getText());
        settings.putProperty(WizardProperties.TOOLTIP_MESSAGES, Boolean.valueOf(tooltipMessagesCheckBox.isSelected()));

    }

    private void updateSourceGroupPackages() {
        SourceGroup sourceGroup = (SourceGroup) locationComboBox.getSelectedItem();
        JComboBox[] combos = {jpaPackageComboBox, jsfPackageComboBox, converterPackageComboBox};
        for (JComboBox combo : combos) {
            ComboBoxModel model = PackageView.createListView(sourceGroup);
            if (model.getSelectedItem() != null && model.getSelectedItem().toString().startsWith("META-INF")
                    && model.getSize() > 1) { // NOI18N
                model.setSelectedItem(model.getElementAt(1));
            }
            combo.setModel(model);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changeSupport.fireChange();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changeSupport.fireChange();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        changeSupport.fireChange();
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
