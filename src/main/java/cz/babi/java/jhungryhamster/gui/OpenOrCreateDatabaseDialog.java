
/*
 * Copyright 2017 Martin Misiarz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.babi.java.jhungryhamster.gui;

import static cz.babi.java.jhungryhamster.utils.Common.RESOURCE_BUNDLE;

import cz.babi.java.jhungryhamster.data.FileOperations;
import cz.babi.java.jhungryhamster.entity.Settings;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Dialogové okno, které se zobrazí při prvotním spuštění aplikace, nebo 
 * pokud není vybráno automatické načítání databáze.
 * @author babi
 */
public class OpenOrCreateDatabaseDialog extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    
    public static Settings settings = Settings.getInstance();
    public static FileOperations fileOperations = FileOperations.getInstance();

    /** Creates new form OpenOrCreateDatabaseDialog */
    public OpenOrCreateDatabaseDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        txtfNewDatabase.requestFocusInWindow();
        
        myInit();
        
        this.setLocationRelativeTo(null);
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }
    
    /**
     * Vlastní inicializace.
     */
    private void myInit() {
        if(!settings.isAutoLoadDatabase() && 
                fileOperations.checkFilePath(FileOperations.getUserDatabaseFile())) {
            txtfOpenDatabase.setText(FileOperations.getUserDatabaseFile().getAbsolutePath());
            rbtnOpenDatabase.setSelected(true);
            rbtnOpenDatabase.requestFocusInWindow();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbtngAction = new javax.swing.ButtonGroup();
        btnOk = new javax.swing.JButton();
        btnQuit = new javax.swing.JButton();
        rbtnNewDatabase = new javax.swing.JRadioButton();
        rbtnOpenDatabase = new javax.swing.JRadioButton();
        txtfOpenDatabase = new javax.swing.JTextField();
        txtfNewDatabase = new javax.swing.JTextField();
        btnChooseDatabase = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.title"));
        setName("Form");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        btnOk.setText(RESOURCE_BUNDLE.getString("Application.text.accept"));
        btnOk.setToolTipText(RESOURCE_BUNDLE.getString("Application.text.accept"));
        btnOk.setName("btnOk");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnQuit.setText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.btnQuit.text"));
        btnQuit.setToolTipText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.btnQuit.tooltip"));
        btnQuit.setName("btnQuit");
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        rbtngAction.add(rbtnNewDatabase);
        rbtnNewDatabase.setSelected(true);
        rbtnNewDatabase.setText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.rbtnNewDatabase.text"));
        rbtnNewDatabase.setToolTipText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.rbtnNewDatabase.text"));
        rbtnNewDatabase.setName("rbtnNewDatabase");
        rbtnNewDatabase.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtnNewDatabaseItemStateChanged(evt);
            }
        });

        rbtngAction.add(rbtnOpenDatabase);
        rbtnOpenDatabase.setText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.rbtnOpenDatabase.text"));
        rbtnOpenDatabase.setToolTipText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.rbtnOpenDatabase.text"));
        rbtnOpenDatabase.setName("rbtnOpenDatabase");

        txtfOpenDatabase.setEditable(false);
        txtfOpenDatabase.setToolTipText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.txtfOpenDatabase.tooltip"));
        txtfOpenDatabase.setEnabled(false);
        txtfOpenDatabase.setName("txtfOpenDatabase");
        txtfOpenDatabase.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtfOpenDatabaseCaretUpdate(evt);
            }
        });

        txtfNewDatabase.setToolTipText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.txtfNewDatabase.tooltip"));
        txtfNewDatabase.setName("txtfNewDatabase");

        btnChooseDatabase.setText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.btnChooseDatabase.text"));
        btnChooseDatabase.setToolTipText(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.btnChooseDatabase.tooltip"));
        btnChooseDatabase.setEnabled(false);
        btnChooseDatabase.setName("btnChooseDatabase");
        btnChooseDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseDatabaseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtnNewDatabase)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtfNewDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
                    .addComponent(rbtnOpenDatabase)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(txtfOpenDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                            .addComponent(btnOk))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnQuit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnChooseDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbtnNewDatabase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtfNewDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnOpenDatabase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfOpenDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChooseDatabase))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuit)
                    .addComponent(btnOk))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getRootPane().setDefaultButton(btnOk);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_btnOkActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_btnCancelActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void rbtnNewDatabaseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtnNewDatabaseItemStateChanged
        txtfNewDatabase.setEnabled(rbtnNewDatabase.isSelected());
        txtfOpenDatabase.setEnabled(!rbtnNewDatabase.isSelected());
        btnChooseDatabase.setEnabled(!rbtnNewDatabase.isSelected());
        
        if(rbtnNewDatabase.isSelected()) btnOk.setEnabled(true);
        else if(txtfOpenDatabase.getText().isEmpty()) btnOk.setEnabled(false);
    }//GEN-LAST:event_rbtnNewDatabaseItemStateChanged

    private void btnChooseDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseDatabaseActionPerformed
        chooseDatabase();
    }//GEN-LAST:event_btnChooseDatabaseActionPerformed

    private void txtfOpenDatabaseCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtfOpenDatabaseCaretUpdate
        btnOk.setEnabled(!txtfOpenDatabase.getText().isEmpty());
    }//GEN-LAST:event_txtfOpenDatabaseCaretUpdate
    
    /**
     * Metoda zobrazí dialog pro otevření existující databáze.
     */
    private void chooseDatabase() {
        JFileChooser fileChooser = new JFileChooser();
        
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileFilter(getDatabaseFileFilter());
        fileChooser.setDialogTitle(RESOURCE_BUNDLE.getString("OpenOrCreateDatabaseDialog.fileChooser.title"));
        fileChooser.setApproveButtonText(RESOURCE_BUNDLE.getString("Application.text.open"));
        
        int result = fileChooser.showOpenDialog(this);
        
        if(result==JFileChooser.APPROVE_OPTION)
            txtfOpenDatabase.setText(fileChooser.getSelectedFile().getAbsolutePath());
    }
    
    /**
     * Metoda vrátí FileFiltr pro .sqlite soubory.
     * @return FileFiltr.
     */
    private FileFilter getDatabaseFileFilter() {
        
        return new FileFilter() {

            @Override
            public boolean accept(File file) {
                if(file.getName().toLowerCase().endsWith(".sqlite")) return true;
                
                if(file.isDirectory()) return true;
                
                return false;
            }

            @Override
            public String getDescription() {
                return "*.sqlite " + RESOURCE_BUNDLE.getString("Application.text.files");
            }
        };          
    }
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnQuit;
    private javax.swing.JButton btnChooseDatabase;
    private javax.swing.JButton btnOk;
    public static javax.swing.JRadioButton rbtnNewDatabase;
    private javax.swing.JRadioButton rbtnOpenDatabase;
    private javax.swing.ButtonGroup rbtngAction;
    public static javax.swing.JTextField txtfNewDatabase;
    public static javax.swing.JTextField txtfOpenDatabase;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}