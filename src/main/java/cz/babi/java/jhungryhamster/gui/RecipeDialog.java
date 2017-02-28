
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
import cz.babi.java.jhungryhamster.data.Icons;
import cz.babi.java.jhungryhamster.entity.CookBook;
import cz.babi.java.jhungryhamster.entity.CookBook.Recipe;
import cz.babi.java.jhungryhamster.entity.MyTree;
import cz.babi.java.jhungryhamster.entity.Rating;
import cz.babi.java.jhungryhamster.entity.Settings;
import cz.babi.java.jhungryhamster.entity.TreeNodes.Node;
import cz.babi.java.jhungryhamster.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Třída definující vzhled jednoduchého dialogu pro vytvoření nového receptu,
 * popřípadě editované kategorie.
 * 
 * @author babi
 */
public class RecipeDialog extends javax.swing.JDialog {

    /* A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /* A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrame.class);
    
    public static boolean changeTitle;
    public static boolean changeCategory;
    
    private final String[] okFileExtensions = new String[]{"jpg", "jpeg", "png", "bmp"};
    
    private static Settings settings = Settings.getInstance();
    private static FileOperations fileOperations = FileOperations.getInstance();
    
    private CookBook.Recipe recipe = null;
    private Rating rating = Rating.ZERO;
    private BufferedImage tempRecipeImage = null;
    
    private boolean needDeleteImage = false;
    private boolean needNewImage = false;

    /** Creates new form RecipeDialog */
    public RecipeDialog(java.awt.Frame parent, boolean modal, String title, Recipe recipe) {
        super(parent, modal);
        this.recipe = recipe;

        changeCategory = false;
        changeTitle = false;

        initComponents();
        this.setIconImage(Icons.HAMSTER_LOGO.getImage());

        this.setTitle(title);
        this.setLocationRelativeTo(null);

        if (recipe.getTitle() != null) {
            txtfTitle.setText(recipe.getTitle());
            txtfAuthor.setText(recipe.getAuthor());
            txtaNote.setText(recipe.getNote());
            txtaIngredients.setText(recipe.getIngredients());
            txtaMethod.setText(recipe.getMethods());
            if (recipe.getRecipeImage() != null) {
                txtfPicture.setText("- " + RESOURCE_BUNDLE.getString("RecipeDialog.txtfPicture.pictureExists.text") + " -");
                btnDeleteImage.setEnabled(true);
            } else {
                txtfPicture.setText("-- " + RESOURCE_BUNDLE.getString("RecipeDialog.txtfPicture.pictureNotExists.text") + " --");
            }
            setRatingStars(recipe.getRating());
            btnOk.setEnabled(true);

            for (int i = 0; i < MyTree.getAllSortedCategories().length; i++) {
                if (MyTree.getAllSortedCategories()[i].getIdNode() == recipe.getIdCategry()) {
                    cmbCategory.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            if (MyTree.getCurrentCategory() != null) {
                for (int i = 0; i < MyTree.getAllSortedCategories().length; i++) {
                    if (MyTree.getAllSortedCategories()[i].getIdNode() == MyTree.getCurrentCategory().getIdNode()) {
                        cmbCategory.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
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

        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        lblAuthor = new javax.swing.JLabel();
        lblNote = new javax.swing.JLabel();
        lblIngredients = new javax.swing.JLabel();
        lblMethod = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtaIngredients = new javax.swing.JTextArea();
        txtfTitle = new javax.swing.JTextField();
        txtfAuthor = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtaNote = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtaMethod = new javax.swing.JTextArea();
        txtfPicture = new javax.swing.JTextField();
        btnChooseImage = new javax.swing.JButton();
        lblPicture = new javax.swing.JLabel();
        lblCategory = new javax.swing.JLabel();
        lblStar1 = new javax.swing.JLabel();
        lblStar2 = new javax.swing.JLabel();
        lblStar3 = new javax.swing.JLabel();
        lblStar4 = new javax.swing.JLabel();
        lblStar5 = new javax.swing.JLabel();
        btnDeleteImage = new javax.swing.JButton();
        lblRating = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox();

        setName("Form");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        btnOk.setText(RESOURCE_BUNDLE.getString("RecipeDialog.btnOk.text"));
        btnOk.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDialog.btnOk.text"));
        btnOk.setEnabled(false);
        btnOk.setName("btnOk");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnCancel.setText(RESOURCE_BUNDLE.getString("RecipeDialog.btnCancel.text"));
        btnCancel.setToolTipText(RESOURCE_BUNDLE.getString("Application.text.closeWindow"));
        btnCancel.setName("btnCancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblTitle.setForeground(new java.awt.Color(110, 108, 104));
        lblTitle.setText(RESOURCE_BUNDLE.getString("RecipeDialog.lblTitle.text") + ":");
        lblTitle.setName("lblTitle");

        lblAuthor.setForeground(new java.awt.Color(110, 108, 104));
        lblAuthor.setText(RESOURCE_BUNDLE.getString("RecipeDialog.lblAuthor.text") + ":");
        lblAuthor.setName("lblAuthor");

        lblNote.setForeground(new java.awt.Color(110, 108, 104));
        lblNote.setText(RESOURCE_BUNDLE.getString("RecipeDialog.lblNote.text") + ":");
        lblNote.setName("lblNote");

        lblIngredients.setForeground(new java.awt.Color(110, 108, 104));
        lblIngredients.setText(RESOURCE_BUNDLE.getString("RecipeDialog.lblIngredients.text") + ":");
        lblIngredients.setName("lblIngredients");

        lblMethod.setForeground(new java.awt.Color(110, 108, 104));
        lblMethod.setText(RESOURCE_BUNDLE.getString("RecipeDialog.lblMethod.text") + ":");
        lblMethod.setName("lblMethod");

        jScrollPane1.setName("jScrollPane1");

        txtaIngredients.setColumns(20);
        txtaIngredients.setLineWrap(true);
        txtaIngredients.setRows(3);
        txtaIngredients.setText("");
        txtaIngredients.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDialog.lblIngredients.text"));
        txtaIngredients.setWrapStyleWord(true);
        txtaIngredients.setName("txtaIngredients");
        jScrollPane1.setViewportView(txtaIngredients);

        txtfTitle.setText("");
        txtfTitle.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDialog.lblTitle.text"));
        txtfTitle.setName("txtfTitle");
        txtfTitle.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtfTitleCaretUpdate(evt);
            }
        });

        txtfAuthor.setText("");
        txtfAuthor.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDialog.lblAuthor.text"));
        txtfAuthor.setName("txtfAuthor");

        jScrollPane2.setName("jScrollPane2");

        txtaNote.setColumns(20);
        txtaNote.setLineWrap(true);
        txtaNote.setRows(2);
        txtaNote.setText("");
        txtaNote.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDialog.lblNote.text"));
        txtaNote.setWrapStyleWord(true);
        txtaNote.setName("txtaNote");
        jScrollPane2.setViewportView(txtaNote);

        jScrollPane3.setName("jScrollPane3");

        txtaMethod.setColumns(20);
        txtaMethod.setLineWrap(true);
        txtaMethod.setRows(5);
        txtaMethod.setText("");
        txtaMethod.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDialog.lblMethod.text"));
        txtaMethod.setWrapStyleWord(true);
        txtaMethod.setName("txtaMethod");
        jScrollPane3.setViewportView(txtaMethod);

        txtfPicture.setEditable(false);
        txtfPicture.setText("");
        txtfPicture.setName("txtfPicture");

        btnChooseImage.setText(RESOURCE_BUNDLE.getString("RecipeDialog.btnChoose.text"));
        btnChooseImage.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDialog.btnChoose.tooltip"));
        btnChooseImage.setName("btnChooseImage");
        btnChooseImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseImageActionPerformed(evt);
            }
        });

        lblPicture.setForeground(new java.awt.Color(110, 108, 104));
        lblPicture.setText(RESOURCE_BUNDLE.getString("RecipeDialog.lblPicture.text"));
        lblPicture.setName("lblPicture");

        lblCategory.setForeground(new java.awt.Color(110, 108, 104));
        lblCategory.setText(RESOURCE_BUNDLE.getString("RecipeDialog.lblCategory.text"));
        lblCategory.setName("lblCategory");

        lblStar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/star-empty-20.png")));
        lblStar1.setName("lblStar1");
        lblStar1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblStar1MouseMoved(evt);
            }
        });

        lblStar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/star-empty-20.png")));
        lblStar2.setName("lblStar2");
        lblStar2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblStar2MouseMoved(evt);
            }
        });

        lblStar3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/star-empty-20.png")));
        lblStar3.setName("lblStar3");
        lblStar3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblStar3MouseMoved(evt);
            }
        });

        lblStar4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/star-empty-20.png")));
        lblStar4.setName("lblStar4");
        lblStar4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblStar4MouseMoved(evt);
            }
        });

        lblStar5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/star-empty-20.png")));
        lblStar5.setName("lblStar5");
        lblStar5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblStar5MouseMoved(evt);
            }
        });

        btnDeleteImage.setIcon(Icons.DELETE_RECIPE_IMAGE);
        btnDeleteImage.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDialog.btnDeleteImage.tooltip"));
        btnDeleteImage.setEnabled(false);
        btnDeleteImage.setName("btnDeleteImage");
        btnDeleteImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteImageActionPerformed(evt);
            }
        });

        lblRating.setForeground(new java.awt.Color(110, 108, 104));
        lblRating.setText(RESOURCE_BUNDLE.getString("RecipeDialog.lblRating.text"));
        lblRating.setName("lblRating");

        cmbCategory.setModel(new DefaultComboBoxModel(MyTree.getAllSortedCategories()));
        cmbCategory.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDialog.lblCategory.text"));
        cmbCategory.setName("cmbCategory");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTitle)
                            .addComponent(lblAuthor)
                            .addComponent(lblNote)
                            .addComponent(lblIngredients))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtfAuthor, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                            .addComponent(txtfTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(lblMethod)))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(397, 397, 397)
                        .addComponent(btnOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCategory)
                            .addComponent(lblPicture))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtfPicture, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeleteImage)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnChooseImage, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbCategory, 0, 429, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblRating)
                        .addGap(18, 18, 18)
                        .addComponent(lblStar1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStar2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStar3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStar4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStar5)
                        .addGap(0, 305, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(txtfTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAuthor)
                    .addComponent(txtfAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNote)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIngredients)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMethod)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCategory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtfPicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPicture)
                        .addComponent(btnChooseImage))
                    .addComponent(btnDeleteImage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRating)
                    .addComponent(lblStar1)
                    .addComponent(lblStar2)
                    .addComponent(lblStar3)
                    .addComponent(lblStar4)
                    .addComponent(lblStar5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOk))
                .addContainerGap())
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

    private void lblStar1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStar1MouseMoved
        if (evt.getPoint().getX() < 2) {
            setRatingStars(Rating.ZERO);
        } else if (evt.getPoint().getX() < 10) {
            setRatingStars(Rating.ZERO_AND_HALF);
        } else {
            setRatingStars(Rating.ONE);
        }
    }//GEN-LAST:event_lblStar1MouseMoved

    private void lblStar2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStar2MouseMoved
        if (evt.getPoint().getX() < 10) {
            setRatingStars(Rating.ONE_AND_HALF);
        } else {
            setRatingStars(Rating.TWO);
        }
    }//GEN-LAST:event_lblStar2MouseMoved

    private void lblStar3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStar3MouseMoved
        if (evt.getPoint().getX() < 10) {
            setRatingStars(Rating.TWO_AND_HALF);
        } else {
            setRatingStars(Rating.THREE);
        }
    }//GEN-LAST:event_lblStar3MouseMoved

    private void lblStar4MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStar4MouseMoved
        if (evt.getPoint().getX() < 10) {
            setRatingStars(Rating.THREE_AND_HALF);
        } else {
            setRatingStars(Rating.FOUR);
        }
    }//GEN-LAST:event_lblStar4MouseMoved

    private void lblStar5MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStar5MouseMoved
        if (evt.getPoint().getX() < 10) {
            setRatingStars(Rating.FOUR_AND_HALF);
        } else {
            setRatingStars(Rating.FIVE);
        }
    }//GEN-LAST:event_lblStar5MouseMoved

    private void btnChooseImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseImageActionPerformed
        BufferedImage origImage = null;
        BufferedImage scaledImage = null;
        boolean canContinue = false;

        File file = getImageFile();

        if (file != null) {
            for (String extension : okFileExtensions) {
                if (file.getName().toLowerCase().endsWith(extension)) {
                    canContinue = true;
                    break;
                }
            }
        }

        if (canContinue) {
            origImage = fileOperations.loadImageFromFile(file);

            if (settings.getImageResolution().getWidth() != 0) {
                scaledImage = Common.getScaledImage(
                        settings.getImageResolution().getWidth(),
                        settings.getImageResolution().getHeight(),
                        origImage);
            } else {
                scaledImage = origImage;
            }

            tempRecipeImage = scaledImage;
            txtfPicture.setText(file.getPath());

            btnDeleteImage.setEnabled(true);
            needNewImage = true;
        }
    }//GEN-LAST:event_btnChooseImageActionPerformed

    private void txtfTitleCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtfTitleCaretUpdate
        if (!txtfTitle.getText().isEmpty() && !txtfTitle.getText().trim().equals("")) {
            btnOk.setEnabled(true);
        } else {
            btnOk.setEnabled(false);
        }
    }//GEN-LAST:event_txtfTitleCaretUpdate

    private void btnDeleteImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteImageActionPerformed
        needDeleteImage = true;
        needNewImage = false;
        btnDeleteImage.setEnabled(false);
        txtfPicture.setText("-- " + RESOURCE_BUNDLE.getString("RecipeDialog.txtfPicture.pictureNotExists.text") + " --");
    }//GEN-LAST:event_btnDeleteImageActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;

        if (returnStatus == RET_OK) {
            if (needDeleteImage) {
                recipe.setRecipeImage(null);
            }
            if (recipe.getAdded() == null) {
                recipe.setAdded(new Date());
            }

            if (recipe.getTitle() != null) {
                if (recipe.getTitle().compareTo(txtfTitle.getText()) != 0) {
                    changeTitle = true;
                    recipe.setTitle(txtfTitle.getText());
                }
            } else {
                recipe.setTitle(txtfTitle.getText());
            }

            recipe.setAuthor(txtfAuthor.getText());
            recipe.setRating(rating);
            recipe.setModified(new Date());
            recipe.setNote(txtaNote.getText());
            recipe.setIngredients(txtaIngredients.getText());
            recipe.setMethods(txtaMethod.getText());
            if (needNewImage) {
                recipe.setRecipeImage(tempRecipeImage);
            }

            Node selectedCategory = (Node) cmbCategory.getSelectedItem();
            if (recipe.getTitle() != null) {
                if (recipe.getIdCategry() != selectedCategory.getIdNode()) {
                    changeCategory = true;
                }
            }
            
            recipe.setIdCategory(selectedCategory.getIdNode());
        }

        setVisible(false);
        dispose();
    }

    private void setRatingStars(Rating rating) {
        switch (rating) {
            case ZERO:
                lblStar1.setIcon(Icons.STAR_EMPTY);
                lblStar2.setIcon(Icons.STAR_EMPTY);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                this.rating = Rating.ZERO;
                break;
            case ZERO_AND_HALF:
                lblStar1.setIcon(Icons.STAR_HALF);
                lblStar2.setIcon(Icons.STAR_EMPTY);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                this.rating = Rating.ZERO_AND_HALF;
                break;
            case ONE:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_EMPTY);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                this.rating = Rating.ONE;
                break;
            case ONE_AND_HALF:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_HALF);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                this.rating = Rating.ONE_AND_HALF;
                break;
            case TWO:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                this.rating = Rating.TWO;
                break;
            case TWO_AND_HALF:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_HALF);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                this.rating = Rating.TWO_AND_HALF;
                break;
            case THREE:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                this.rating = Rating.THREE;
                break;
            case THREE_AND_HALF:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_HALF);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                this.rating = Rating.THREE_AND_HALF;
                break;
            case FOUR:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_FULL);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                this.rating = Rating.FOUR;
                break;
            case FOUR_AND_HALF:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_FULL);
                lblStar5.setIcon(Icons.STAR_HALF);
                this.rating = Rating.FOUR_AND_HALF;
                break;
            case FIVE:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_FULL);
                lblStar5.setIcon(Icons.STAR_FULL);
                this.rating = Rating.FIVE;
                break;
        }
    }

    private File getImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        PreviewPane previewPane = new PreviewPane();
        
        fileChooser.setAccessory(previewPane);
        fileChooser.addPropertyChangeListener(previewPane);

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileFilter(getImageFileFilter());
        fileChooser.setDialogTitle(RESOURCE_BUNDLE.getString("RecipeDialog.fileChooser.title"));
        fileChooser.setApproveButtonText(RESOURCE_BUNDLE.getString("Application.text.choose"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    private FileFilter getImageFileFilter() {

        return new FileFilter() {

            @Override
            public boolean accept(File file) {
                for (String extension : okFileExtensions) {
                    if (file.getName().toLowerCase().endsWith(extension)) {
                        return true;
                    }
                }

                if (file.isDirectory()) {
                    return true;
                }

                return false;
            }

            @Override
            public String getDescription() {
                return "*.jpg, *.jpeg, *.png, *.bmp";
            }
        };
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnChooseImage;
    private javax.swing.JButton btnDeleteImage;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox cmbCategory;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAuthor;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblIngredients;
    private javax.swing.JLabel lblMethod;
    private javax.swing.JLabel lblNote;
    private javax.swing.JLabel lblPicture;
    private javax.swing.JLabel lblRating;
    private javax.swing.JLabel lblStar1;
    private javax.swing.JLabel lblStar2;
    private javax.swing.JLabel lblStar3;
    private javax.swing.JLabel lblStar4;
    private javax.swing.JLabel lblStar5;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextArea txtaIngredients;
    private javax.swing.JTextArea txtaMethod;
    private javax.swing.JTextArea txtaNote;
    private javax.swing.JTextField txtfAuthor;
    private javax.swing.JTextField txtfPicture;
    private javax.swing.JTextField txtfTitle;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    /**
     * @return the recipe
     */
    public CookBook.Recipe getRecipe() {
        return recipe;
    }

    /**
     * @param recipe the recipe to set
     */
    public void setRecipe(CookBook.Recipe recipe) {
        this.recipe = recipe;
    }

    /**
     * Třída, která zobrazí náhled obrázku v okně pro výběr souborů-
     */
    static class PreviewPane extends JPanel implements PropertyChangeListener {

        private JLabel lblPreview;
        private int maxImgWidth;

        public PreviewPane() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
            add(new JLabel(RESOURCE_BUNDLE.getString("RecipeDialog.PreviewPane.preview.text") + ":"), BorderLayout.NORTH);
            lblPreview = new JLabel();
            lblPreview.setBackground(Color.WHITE);
            lblPreview.setOpaque(true);
            lblPreview.setPreferredSize(new Dimension(200, 200));
            maxImgWidth = 200;
            lblPreview.setBorder(BorderFactory.createEtchedBorder());
            add(lblPreview, BorderLayout.CENTER);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            Icon icon = null;
            if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
                File newFile = (File) evt.getNewValue();
                if (newFile != null) {
                    String path = newFile.getAbsolutePath();
                    if (path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".png") || 
                            path.toLowerCase().endsWith(".bmp") || path.toLowerCase().endsWith(".jpeg")) {
                        try {
                            BufferedImage img = ImageIO.read(newFile);
                            float width = img.getWidth();
                            float height = img.getHeight();
                            float scale = height / width;
                            width = maxImgWidth;
                            height = (width * scale); // height should be scaled from new width							
                            icon = new ImageIcon(Common.getScaledImage(Math.max(1, (int) width),
                                    Math.max(1, (int) height), img));
                        } catch (IOException e) {
                            LOGGER.error("Can not load preview.", e);
                        }
                    }
                }

                lblPreview.setIcon(icon);
                this.repaint();
            }
        }
    }
}
