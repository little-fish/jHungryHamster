
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

import cz.babi.java.jhungryhamster.data.Icons;
import cz.babi.java.jhungryhamster.entity.CookBook.Recipe;
import cz.babi.java.jhungryhamster.entity.MyTree;
import cz.babi.java.jhungryhamster.entity.Settings;
import cz.babi.java.jhungryhamster.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 *
 * @author babi
 */
public class PrintRecipeDialog extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CLOSE = 0;
    
    private final Recipe recipe;
    
    private final Style bold, regular, smallEmptyLine, recipeImage;
//    private final Style ratingStarEmpty, ratingStarHalf, ratingStarFull;
    
    private static Settings settings = Settings.getInstance();

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintRecipeDialog.class);
    
    /* pro tisk je nutné nastavit obrázku maximálně tuto velikost */
    private static final Dimension IMAGE_RESOLUTION = new Dimension(450, 338);

    /** Creates new form PrintRecipeDialog */
    public PrintRecipeDialog(java.awt.Frame parent, boolean modal, Recipe recipe) {
        super(parent, modal);
        
        this. recipe = recipe;
        
        initComponents();
        
        this.setIconImage(Icons.HAMSTER_LOGO.getImage());

        // Close the dialog when Esc is pressed
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                    doClose(RET_CLOSE);
                }
            }
        });

        this.setLocationRelativeTo(null);
                
        StyledDocument styledDocument = txtpPreview.getStyledDocument();
        
        Style defaultStyle = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);
        
        regular = styledDocument.addStyle("regular", defaultStyle);
        StyleConstants.setFontFamily(defaultStyle, "DejaVu Sans");
        StyleConstants.setFontSize(defaultStyle, 10);
        
        bold = styledDocument.addStyle("bold", regular);
        StyleConstants.setBold(bold, true);
        StyleConstants.setAlignment(bold, StyleConstants.ALIGN_JUSTIFIED);
        StyleConstants.setFontSize(bold, 11);
        
        smallEmptyLine = styledDocument.addStyle("smallEmptyLine", regular);
        StyleConstants.setFontSize(smallEmptyLine, 5);
        
        /* příprava pro tisk hvězd, momentálně nepotřebné */
//        ratingStarEmpty = styledDocument.addStyle("ratingStarEmpty", regular);
//        StyleConstants.setAlignment(ratingStarEmpty, StyleConstants.ALIGN_LEFT);
//        StyleConstants.setIcon(ratingStarEmpty, Icons.STAR_EMPTY);
//        
//        ratingStarHalf = styledDocument.addStyle("ratingStarHalf", regular);
//        StyleConstants.setAlignment(ratingStarHalf, StyleConstants.ALIGN_LEFT);
//        StyleConstants.setIcon(ratingStarHalf, Icons.STAR_HALF);
//        
//        ratingStarFull = styledDocument.addStyle("ratingStarFull", regular);
//        StyleConstants.setAlignment(ratingStarFull, StyleConstants.ALIGN_LEFT);
//        StyleConstants.setIcon(ratingStarFull, Icons.STAR_FULL);
        
        //TODO: max 450px x 338px
        recipeImage = styledDocument.addStyle("recipeImage", regular);
        StyleConstants.setAlignment(recipeImage, StyleConstants.ALIGN_CENTER);
//        StyleConstants.setIcon(recipeImage, new ImageIcon(recipe.getRecipeImage()));
        if(recipe.getRecipeImage()!=null) {
            StyleConstants.setIcon(recipeImage, new ImageIcon(
                    Common.getScaledImageForPrint(IMAGE_RESOLUTION.width,
                            IMAGE_RESOLUTION.height, recipe.getRecipeImage())));
        }
             
        /* odsazení, momentálně nepotřebné, pro použití přidat "\t" */
//        TabStop[] tabStop = new TabStop[1];
//        tabStop[0] = new TabStop(100, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
//        TabSet tabSet = new TabSet(tabStop);
//        
//        StyleContext styleContext = StyleContext.getDefaultStyleContext();
//        AttributeSet attributeSet = styleContext.addAttribute(
//                SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabSet);
//        txtpPreview.setParagraphAttributes(attributeSet, false);
        
        refreshPreview();
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CLOSE */
    public int getReturnStatus() {
        return returnStatus;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ResourceBundle bundle = Common.RESOURCE_BUNDLE;

        this.setTitle(bundle.getString("PrintRecipeDialog.title") + ": " + recipe.getTitle());

        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        chbTitle = new javax.swing.JCheckBox();
        chbAuthor = new javax.swing.JCheckBox();
        chbCategory = new javax.swing.JCheckBox();
        chbAdded = new javax.swing.JCheckBox();
        chbModified = new javax.swing.JCheckBox();
        chbNote = new javax.swing.JCheckBox();
        chbIngredience = new javax.swing.JCheckBox();
        chbMethod = new javax.swing.JCheckBox();
        chbImage = new javax.swing.JCheckBox();
        chbHeader = new javax.swing.JCheckBox();
        txtfHeader = new javax.swing.JTextField();
        chbFooter = new javax.swing.JCheckBox();
        txtfFooter = new javax.swing.JTextField();
        chbRating = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtpPreview = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        chbBackgroundPrint = new javax.swing.JCheckBox();
        chbShowPrint = new javax.swing.JCheckBox();
        chbEditPrintText = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        btnPrint.setText(bundle.getString("PrintRecipeDialog.btnPrint.text"));
        btnPrint.setToolTipText(bundle.getString("PrintRecipeDialog.btnPrint.tooltip"));
        btnPrint.setName("btnPrint");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        btnClose.setText(bundle.getString("PrintRecipeDialog.btnClose.text"));
        btnClose.setToolTipText(bundle.getString("PrintRecipeDialog.btnClose.tooltip"));
        btnClose.setName("btnClose");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, RESOURCE_BUNDLE.getString("PrintRecipeDialog.pnlDetails.border.title"),
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("DejaVu Sans", 0, 13)));
        jPanel1.setName("jPanel1");

        chbTitle.setSelected(settings.isPrintTitle());
        chbTitle.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.title").toLowerCase());
        chbTitle.setName("chbTitle");
        chbTitle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbTitleItemStateChanged(evt);
            }
        });

        chbAuthor.setSelected(settings.isPrintAuthor());
        chbAuthor.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.author").toLowerCase());
        chbAuthor.setName("chbAuthor");
        chbAuthor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbAuthorItemStateChanged(evt);
            }
        });

        chbCategory.setSelected(settings.isPrintCategory());
        chbCategory.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.category").toLowerCase());
        chbCategory.setName("chbCategory");
        chbCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbCategoryItemStateChanged(evt);
            }
        });

        chbAdded.setSelected(settings.isPrintAdded());
        chbAdded.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.added").toLowerCase());
        chbAdded.setName("chbAdded");
        chbAdded.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbAddedItemStateChanged(evt);
            }
        });

        chbModified.setSelected(settings.isPrintModified());
        chbModified.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.changed").toLowerCase());
        chbModified.setName("chbModified");
        chbModified.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbModifiedItemStateChanged(evt);
            }
        });

        chbNote.setSelected(settings.isPrintNote());
        chbNote.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.note").toLowerCase());
        chbNote.setName("chbNote");
        chbNote.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbNoteItemStateChanged(evt);
            }
        });

        chbIngredience.setSelected(settings.isPrintIngredience());
        chbIngredience.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.ingredients").toLowerCase());
        chbIngredience.setName("chbIngredience");
        chbIngredience.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbIngredienceItemStateChanged(evt);
            }
        });

        chbMethod.setSelected(settings.isPrintMethod());
        chbMethod.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.method").toLowerCase());
        chbMethod.setName("chbMethod");
        chbMethod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbMethodItemStateChanged(evt);
            }
        });

        chbImage.setSelected(settings.isPrintRecipeImage());
        chbImage.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.image").toLowerCase());
        chbImage.setName("chbImage");
        chbImage.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbImageItemStateChanged(evt);
            }
        });
        if(recipe.getRecipeImage()==null) chbImage.setEnabled(false);

        chbHeader.setSelected(settings.isPrintHeader());
        chbHeader.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.header").toLowerCase());
        chbHeader.setName("chbHeader");
        chbHeader.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbHeaderItemStateChanged(evt);
            }
        });

        txtfHeader.setText(settings.getPrintHeaderText());
        txtfHeader.setEnabled(settings.isPrintHeader());
        txtfHeader.setName("txtfHeader");
        txtfHeader.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtfHeaderCaretUpdate(evt);
            }
        });

        chbFooter.setSelected(settings.isPrintFooter());
        chbFooter.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.footer").toLowerCase());
        chbFooter.setName("chbFooter");
        chbFooter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbFooterItemStateChanged(evt);
            }
        });

        txtfFooter.setText(settings.getPrintFooterText());
        txtfFooter.setEnabled(settings.isPrintFooter());
        txtfFooter.setName("txtfFooter");
        txtfFooter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtfFooterCaretUpdate(evt);
            }
        });

        chbRating.setSelected(settings.isPrintRating());
        chbRating.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.rating").toLowerCase());
        chbRating.setName("chbRating");
        chbRating.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbRatingItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chbTitle)
                    .addComponent(chbAuthor)
                    .addComponent(chbCategory)
                    .addComponent(chbAdded)
                    .addComponent(chbModified)
                    .addComponent(chbNote)
                    .addComponent(chbRating)
                    .addComponent(chbIngredience)
                    .addComponent(chbMethod)
                    .addComponent(chbImage)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(txtfHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                        .addComponent(chbHeader)
                        .addComponent(chbFooter)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(txtfFooter))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(chbTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbAuthor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbAdded)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbModified)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbNote)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbRating)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbIngredience)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbMethod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtfHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbFooter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtfFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, RESOURCE_BUNDLE.getString("PrintRecipeDialog.pnlPreview.text"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 13)));
        jPanel2.setToolTipText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.pnlPreview.text"));
        jPanel2.setName("jPanel2");

        jScrollPane1.setName("jScrollPane1");

        txtpPreview.setBackground(Color.white);
        txtpPreview.setEditable(settings.isEditPrintText());
        txtpPreview.setToolTipText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.pnlPreview.text"));
        txtpPreview.setName("txtpPreview");
        jScrollPane1.setViewportView(txtpPreview);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, RESOURCE_BUNDLE.getString("PrintRecipeDialog.pnlOtherSettings.border"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 13)));
        jPanel3.setName("jPanel3");

        chbBackgroundPrint.setSelected(settings.isBackgroundPrint());
        chbBackgroundPrint.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.settings.printInTheBackground.text"));
        chbBackgroundPrint.setName("chbBackgroundPrint");
        chbBackgroundPrint.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbBackgroundPrintItemStateChanged(evt);
            }
        });

        chbShowPrint.setSelected(settings.isShowPrint());
        chbShowPrint.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.settings.showPrintProgress.text"));
        chbShowPrint.setName("chbShowPrint");
        chbShowPrint.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbShowPrintItemStateChanged(evt);
            }
        });

        chbEditPrintText.setSelected(settings.isEditPrintText());
        chbEditPrintText.setText(RESOURCE_BUNDLE.getString("PrintRecipeDialog.settings.editPreview.text"));
        chbEditPrintText.setName("chbEditPrintText");
        chbEditPrintText.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbEditPrintTextItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chbBackgroundPrint)
                    .addComponent(chbShowPrint)
                    .addComponent(chbEditPrintText))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(chbBackgroundPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbShowPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbEditPrintText)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                        .addComponent(btnClose))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnClose, btnPrint});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClose)
                            .addComponent(btnPrint)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(btnPrint);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        doClose(RET_CLOSE);
    }//GEN-LAST:event_btnCloseActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CLOSE);
    }//GEN-LAST:event_closeDialog

    private void chbTitleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbTitleItemStateChanged
        settings.setPrintTitle(chbTitle.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbTitleItemStateChanged

    private void chbAuthorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbAuthorItemStateChanged
        settings.setPrintAuthor(chbAuthor.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbAuthorItemStateChanged

    private void chbAddedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbAddedItemStateChanged
        settings.setPrintAdded(chbAdded.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbAddedItemStateChanged

    private void chbModifiedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbModifiedItemStateChanged
        settings.setPrintModified(chbModified.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbModifiedItemStateChanged

    private void chbNoteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbNoteItemStateChanged
        settings.setPrintNote(chbNote.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbNoteItemStateChanged

    private void chbRatingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbRatingItemStateChanged
        settings.setPrintRating(chbRating.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbRatingItemStateChanged

    private void chbIngredienceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbIngredienceItemStateChanged
        settings.setPrintIngredience(chbIngredience.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbIngredienceItemStateChanged

    private void chbMethodItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbMethodItemStateChanged
        settings.setPrintMethod(chbMethod.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbMethodItemStateChanged

    private void chbImageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbImageItemStateChanged
        settings.setPrintRecipeImage(chbImage.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbImageItemStateChanged

    private void chbHeaderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbHeaderItemStateChanged
        settings.setPrintHeader(chbHeader.isSelected());
        txtfHeader.setEnabled(settings.isPrintHeader());
    }//GEN-LAST:event_chbHeaderItemStateChanged

    private void txtfHeaderCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtfHeaderCaretUpdate
        settings.setPrintHeaderText(txtfHeader.getText());
    }//GEN-LAST:event_txtfHeaderCaretUpdate

    private void chbFooterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbFooterItemStateChanged
        settings.setPrintFooter(chbFooter.isSelected());
        txtfFooter.setEnabled(settings.isPrintFooter());
    }//GEN-LAST:event_chbFooterItemStateChanged

    private void txtfFooterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtfFooterCaretUpdate
        settings.setPrintFooterText(txtfFooter.getText());
    }//GEN-LAST:event_txtfFooterCaretUpdate

    private void chbBackgroundPrintItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbBackgroundPrintItemStateChanged
        settings.setBackgroundPrint(chbBackgroundPrint.isSelected());
    }//GEN-LAST:event_chbBackgroundPrintItemStateChanged

    private void chbShowPrintItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbShowPrintItemStateChanged
        settings.setShowPrint(chbShowPrint.isSelected());
    }//GEN-LAST:event_chbShowPrintItemStateChanged

    private void chbEditPrintTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbEditPrintTextItemStateChanged
        settings.setEditPrintText(chbEditPrintText.isSelected());
        txtpPreview.setEditable(settings.isEditPrintText());
    }//GEN-LAST:event_chbEditPrintTextItemStateChanged

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        printDocument();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void chbCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbCategoryItemStateChanged
        settings.setPrintCategory(chbCategory.isSelected());
        refreshPreview();
    }//GEN-LAST:event_chbCategoryItemStateChanged
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
    /**
     * Metoda připravuje prostor pro tisk dokumentu.
     */
    private void printDocument() {
        String headerText, footerText;

        if(settings.isPrintHeader()) headerText = settings.getPrintHeaderText();
        else headerText = "";
        
        if(settings.isPrintFooter()) footerText = settings.getPrintFooterText();
        else footerText = "";
        
        MessageFormat header = new MessageFormat(headerText);
        MessageFormat footer = new MessageFormat(footerText);
        boolean interactive = settings.isShowPrint();

        PrintTask task = new PrintTask(header, footer, interactive, txtpPreview, this);
	if(settings.isBackgroundPrint()) task.execute();
        else task.run();
    }
    
    /**
     * Metoda vykreslí do náhledu tisknutelné položky.
     */
    private void refreshPreview() {
        StyledDocument styledDocument = txtpPreview.getStyledDocument();
        
        try {
            styledDocument.remove(0, styledDocument.getLength());
        } catch(BadLocationException ble) {
            LOGGER.error("Bad location." + ble);
        }
        
        if(settings.isPrintTitle()) {
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.title") + ":\n", bold);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), recipe.getTitle()+"\n", regular);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), "\n", smallEmptyLine);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
        }
        
        if(settings.isPrintAuthor()) {
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.author") + ":\n", bold);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        recipe.getAuthor() + "\n", regular);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), "\n", smallEmptyLine);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
        }
        
        if(settings.isPrintCategory()) {
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.category") + ":\n", bold);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        MyTree.getCategoryMap().get(recipe.getIdCategry()) + "\n", regular);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), "\n", smallEmptyLine);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
        }
        
        if(settings.isPrintAdded()) {
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.added") + ":\n", bold);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        Common.convertDateToString(Common.DATEFORMAT_RECIPE, recipe.getAdded()) + "\n", regular);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), "\n", smallEmptyLine);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
        }
        
        if(settings.isPrintModified()) {
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.changed") + ":\n", bold);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        Common.convertDateToString(Common.DATEFORMAT_RECIPE, recipe.getModified()) + "\n", regular);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), "\n", smallEmptyLine);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
        }
        
        if(settings.isPrintNote()) {
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.note") + ":\n", bold);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        recipe.getNote() + "\n", regular);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), "\n", smallEmptyLine);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
        }
        
        if(settings.isPrintRating()) {
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.rating") + ":\n", bold);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        Common.getPercentFromRating(recipe.getRating()) + "\n", regular);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), "\n", smallEmptyLine);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
        }
        
        if(settings.isPrintIngredience()) {
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.ingredients") + ":\n", bold);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        recipe.getIngredients() + "\n", regular);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), "\n", smallEmptyLine);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
        }
        
        if(settings.isPrintMethod()) {
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        RESOURCE_BUNDLE.getString("PrintRecipeDialog.print.method") + ":\n", bold);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(),
                        recipe.getMethods() + "\n", regular);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
            
            try {
                styledDocument.insertString(styledDocument.getLength(), "\n", smallEmptyLine);
            } catch(BadLocationException ble) {
                LOGGER.error("Bad text location." + ble);
            }
        }
        
        if(settings.isPrintRecipeImage()) {
            if(recipe.getRecipeImage()!=null) {
                try {
                    styledDocument.insertString(styledDocument.getLength(),
                            "\n", regular);
                } catch(BadLocationException ble) {
                    LOGGER.error("Bad text location."+ble);
                }

                try {
                    styledDocument.insertString(styledDocument.getLength(),
                            "...", recipeImage);
                } catch(BadLocationException ble) {
                    LOGGER.error("Bad text location."+ble);
                }
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPrint;
    private javax.swing.JCheckBox chbAdded;
    private javax.swing.JCheckBox chbAuthor;
    private javax.swing.JCheckBox chbBackgroundPrint;
    private javax.swing.JCheckBox chbCategory;
    private javax.swing.JCheckBox chbEditPrintText;
    private javax.swing.JCheckBox chbFooter;
    private javax.swing.JCheckBox chbHeader;
    private javax.swing.JCheckBox chbImage;
    private javax.swing.JCheckBox chbIngredience;
    private javax.swing.JCheckBox chbMethod;
    private javax.swing.JCheckBox chbModified;
    private javax.swing.JCheckBox chbNote;
    private javax.swing.JCheckBox chbRating;
    private javax.swing.JCheckBox chbShowPrint;
    private javax.swing.JCheckBox chbTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtfFooter;
    private javax.swing.JTextField txtfHeader;
    private javax.swing.JTextPane txtpPreview;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CLOSE;
    
    /**
     * Třída která se stará o tisk dokumentu.
     *
     * @author Martin Misiarz
     * @author dev.misiarz@gmail.com
     * @date 9.3.2011
     */
    private class PrintTask extends SwingWorker<Object, Object> {
        private final MessageFormat headerFormat;
        private final MessageFormat footerFormat;
        private final boolean interactive;
        private final JTextPane txtpPreview;
        private final JDialog parent;
        private volatile boolean complete = false;
        private volatile String message;

        /**
         * Konstruktor třídy PrintingTask.
         * @param header hlavička dokumentu
         * @param footer patička dokumentu
         * @param interactive průběh tisku
         * @param txtpPreview JTextPane s tisknutým textem
         */
        public PrintTask(MessageFormat header, MessageFormat footer, boolean interactive,
                JTextPane txtpPreview, JDialog parent) {
            this.headerFormat = header;
            this.footerFormat = footer;
            this.interactive = interactive;
            this.txtpPreview = txtpPreview;
            this.parent = parent;
        }

        /**
         * Přetížená metoda tisku.
         * @return
         */
        @Override
        protected Object doInBackground() {
            try {
                complete = txtpPreview.print(headerFormat, footerFormat, true,
                        null, null, interactive);
                message = RESOURCE_BUNDLE.getString("PrintRecipeDialog.message.recipe") + " '" +
                        recipe.getTitle() + "' " + RESOURCE_BUNDLE.getString("PrintRecipeDialog.message.hasBeen") +
                        " " + (complete ? (RESOURCE_BUNDLE.getString("PrintRecipeDialog.message.successful") + ".") :
                        (RESOURCE_BUNDLE.getString("PrintRecipeDialog.message.failed") + "."));
            } catch (PrinterException ex) {
                message = RESOURCE_BUNDLE.getString("PrintRecipeDialog.message.printerError");
                LOGGER.error(message, ex);
            } catch (SecurityException ex) {
                message = RESOURCE_BUNDLE.getString("PrintRecipeDialog.message.connectionError");
                LOGGER.error(message, ex);
            }
            return null;
        }

        /**
         * Při dokončení tisku se zavolá metoda pro zobrazení popupu s výsledkem
         *  tisku.
         */
        @Override
        protected void done() {
            message(!complete, message);
        }

        /**
         * Metoda zobrazí uživateli popupokno s výsledkem tisku.
         * @param error true/false; zdařil/nezdařil se tisk
         * @param msg doplňující zpráva
         */
        private void message(boolean error, String msg) {
            int type = (error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(parent, msg, RESOURCE_BUNDLE.getString("PrintRecipeDialog.message.recipePrint") +
                    " '" + recipe.getTitle() + "'", type);
        }
    }
}
