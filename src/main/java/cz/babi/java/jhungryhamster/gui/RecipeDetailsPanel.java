
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
import cz.babi.java.jhungryhamster.entity.MyTree;
import cz.babi.java.jhungryhamster.entity.Rating;
import cz.babi.java.jhungryhamster.utils.Common;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.util.Date;

/**
 *
 * @author babi
 */
public class RecipeDetailsPanel extends javax.swing.JPanel {
    
    private static final Dimension LBL_IMAGE_DIMENSION = new Dimension(183, 183);
    private ImagePopupDialog imagePopupDialog;

    /** Creates new form RecipeDetailsPanel */
    public RecipeDetailsPanel() {
        initComponents();
        
        lblImage.setIcon(Icons.RECIP_NO_IMAGE);
    }
    
    public static void setAuthor(String author) {
        lblAuthor.setText(author);
    }
    
    public static void setCatebory(String category) {
        lblCatebory.setText(category);
    }
    
    public static void setRating(Rating rating) {
        switch(rating) {
            case ZERO:
                lblStar1.setIcon(Icons.STAR_EMPTY);
                lblStar2.setIcon(Icons.STAR_EMPTY);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                break;
            case ZERO_AND_HALF:
                lblStar1.setIcon(Icons.STAR_HALF);
                lblStar2.setIcon(Icons.STAR_EMPTY);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                break;
            case ONE:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_EMPTY);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                break;
            case ONE_AND_HALF:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_HALF);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                break;
            case TWO:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_EMPTY);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                break;
            case TWO_AND_HALF:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_HALF);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                break;
            case THREE:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_EMPTY);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                break;
            case THREE_AND_HALF:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_HALF);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                break;
            case FOUR:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_FULL);
                lblStar5.setIcon(Icons.STAR_EMPTY);
                break;
            case FOUR_AND_HALF:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_FULL);
                lblStar5.setIcon(Icons.STAR_HALF);
                break;
            case FIVE:
                lblStar1.setIcon(Icons.STAR_FULL);
                lblStar2.setIcon(Icons.STAR_FULL);
                lblStar3.setIcon(Icons.STAR_FULL);
                lblStar4.setIcon(Icons.STAR_FULL);
                lblStar5.setIcon(Icons.STAR_FULL);
                break;
        }
    }
    
    public static void setAdded(Date added) {
        if(added==null) lblAdded.setText("");
        else lblAdded.setText(Common.convertDateToString(Common.DATEFORMAT_RECIPE, added));
    }
    
    public static void setModified(Date modified) {
        if(modified==null) lblModified.setText("");
        else lblModified.setText(Common.convertDateToString(Common.DATEFORMAT_RECIPE, modified));
    }
    
    public static void setNote(String note) {
        txtaNote.setText(note);
    }
    
    public static void setImage(BufferedImage image) {
        if(image!=null) {
            lblImage.setSize(LBL_IMAGE_DIMENSION);
            lblImage.setPreferredSize(LBL_IMAGE_DIMENSION);
            BufferedImage scaledImage = Common.getScaledImage(
                    LBL_IMAGE_DIMENSION.width,
                    LBL_IMAGE_DIMENSION.height,
                    image);
            
            lblImage.setPreferredSize(new Dimension(scaledImage.getWidth(), scaledImage.getHeight()));
            lblImage.setSize(new Dimension(scaledImage.getWidth(), scaledImage.getHeight()));
            lblImage.setIcon(new ImageIcon(scaledImage));
            lblImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            lblImage.setPreferredSize(LBL_IMAGE_DIMENSION);
            lblImage.setIcon(Icons.RECIP_NO_IMAGE);
            lblImage.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlRecipeDetails = new javax.swing.JPanel();
        _lblAuthor = new javax.swing.JLabel();
        _lblRating = new javax.swing.JLabel();
        _lblCategory = new javax.swing.JLabel();
        _lblAdded = new javax.swing.JLabel();
        _lblModified = new javax.swing.JLabel();
        _lblNote = new javax.swing.JLabel();
        lblCatebory = new javax.swing.JLabel();
        lblAuthor = new javax.swing.JLabel();
        lblAdded = new javax.swing.JLabel();
        lblModified = new javax.swing.JLabel();
        pnlRecipeImage = new javax.swing.JPanel(new GridBagLayout());
        lblImage = new javax.swing.JLabel(Icons.RECIP_NO_IMAGE, SwingConstants.LEFT);
        spNote = new javax.swing.JScrollPane();
        txtaNote = new javax.swing.JTextArea();
        lblStar1 = new javax.swing.JLabel();
        lblStar2 = new javax.swing.JLabel();
        lblStar3 = new javax.swing.JLabel();
        lblStar4 = new javax.swing.JLabel();
        lblStar5 = new javax.swing.JLabel();

        setName("Form");

        pnlRecipeDetails.setMaximumSize(new java.awt.Dimension(510, 197));
        pnlRecipeDetails.setMinimumSize(new java.awt.Dimension(510, 197));
        pnlRecipeDetails.setName("pnlRecipeDetails");
        pnlRecipeDetails.setPreferredSize(new java.awt.Dimension(510, 185));

        _lblAuthor.setText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblAuthor.text") + ":");
        _lblAuthor.setMaximumSize(new java.awt.Dimension(85, 17));
        _lblAuthor.setMinimumSize(new java.awt.Dimension(85, 17));
        _lblAuthor.setName("_lblAuthor");
        _lblAuthor.setPreferredSize(new java.awt.Dimension(85, 17));

        _lblRating.setText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblRating.text") + ":");
        _lblRating.setMaximumSize(new java.awt.Dimension(85, 17));
        _lblRating.setMinimumSize(new java.awt.Dimension(85, 17));
        _lblRating.setName("_lblRating");
        _lblRating.setPreferredSize(new java.awt.Dimension(85, 17));

        _lblCategory.setText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblCategory.text") + ":");
        _lblCategory.setMaximumSize(new java.awt.Dimension(85, 17));
        _lblCategory.setMinimumSize(new java.awt.Dimension(85, 17));
        _lblCategory.setName("_lblCategory");
        _lblCategory.setPreferredSize(new java.awt.Dimension(85, 17));

        _lblAdded.setText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblAdded.text") + ":");
        _lblAdded.setMaximumSize(new java.awt.Dimension(85, 17));
        _lblAdded.setMinimumSize(new java.awt.Dimension(85, 17));
        _lblAdded.setName("_lblAdded");
        _lblAdded.setPreferredSize(new java.awt.Dimension(85, 17));

        _lblModified.setText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblModified.text") + ":");
        _lblModified.setMaximumSize(new java.awt.Dimension(85, 17));
        _lblModified.setMinimumSize(new java.awt.Dimension(85, 17));
        _lblModified.setName("_lblModified");
        _lblModified.setPreferredSize(new java.awt.Dimension(85, 17));

        _lblNote.setText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblNote.text") + ":");
        _lblNote.setMaximumSize(new java.awt.Dimension(85, 17));
        _lblNote.setMinimumSize(new java.awt.Dimension(85, 17));
        _lblNote.setName("_lblNote");
        _lblNote.setPreferredSize(new java.awt.Dimension(85, 17));

        lblCatebory.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblCategory.text"));
        lblCatebory.setName("lblCatebory");

        lblAuthor.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblAuthor.text"));
        lblAuthor.setName("lblAuthor");

        lblAdded.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblAdded.text"));
        lblAdded.setName("lblAdded");

        lblModified.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblModified.text"));
        lblModified.setName("lblModified");

        pnlRecipeImage.setBorder(null);
        pnlRecipeImage.setMaximumSize(new java.awt.Dimension(185, 185));
        pnlRecipeImage.setMinimumSize(new java.awt.Dimension(185, 185));
        pnlRecipeImage.setName("pnlRecipeImage");
        pnlRecipeImage.setPreferredSize(new java.awt.Dimension(185, 185));

        lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImage.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel.lblImage.tooltip"));
        lblImage.setBorder(null);
        lblImage.setName("lblImage");
        lblImage.setPreferredSize(new java.awt.Dimension(183, 183));
        lblImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblImageMouseReleased(evt);
            }
        });

        pnlRecipeImage.add(lblImage);

        spNote.setBorder(null);
        spNote.setViewportBorder(null);
        spNote.setName("spNote");

        txtaNote.setColumns(20);
        txtaNote.setEditable(false);
        txtaNote.setLineWrap(true);
        txtaNote.setRows(4);
        txtaNote.setToolTipText(RESOURCE_BUNDLE.getString("RecipeDetailsPanel._lblNote.text"));
        txtaNote.setWrapStyleWord(true);
        txtaNote.setBorder(null);
        txtaNote.setName("txtaNote");
        txtaNote.setOpaque(false);
        spNote.setViewportView(txtaNote);

        lblStar1.setIcon(Icons.STAR_EMPTY);
        lblStar1.setName("lblStar1");

        lblStar2.setIcon(Icons.STAR_EMPTY);
        lblStar2.setName("lblStar2");

        lblStar3.setIcon(Icons.STAR_EMPTY);
        lblStar3.setName("lblStar3");

        lblStar4.setIcon(Icons.STAR_EMPTY);
        lblStar4.setName("lblStar4");

        lblStar5.setIcon(Icons.STAR_EMPTY);
        lblStar5.setName("lblStar5");

        javax.swing.GroupLayout pnlRecipeDetailsLayout = new javax.swing.GroupLayout(pnlRecipeDetails);
        pnlRecipeDetails.setLayout(pnlRecipeDetailsLayout);
        pnlRecipeDetailsLayout.setHorizontalGroup(
            pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRecipeDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(_lblCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRecipeDetailsLayout.createSequentialGroup()
                        .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_lblAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(_lblAdded, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(_lblModified, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(_lblRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(_lblNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlRecipeDetailsLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblAdded, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                                    .addComponent(lblCatebory, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                                    .addComponent(lblAuthor, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                                    .addComponent(lblModified, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                                    .addComponent(spNote, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)))
                            .addGroup(pnlRecipeDetailsLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStar1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStar2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStar3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStar4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStar5)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlRecipeImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlRecipeDetailsLayout.setVerticalGroup(
            pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRecipeDetailsLayout.createSequentialGroup()
                .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_lblAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAuthor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_lblCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCatebory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_lblAdded, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAdded))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_lblModified, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModified))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRecipeDetailsLayout.createSequentialGroup()
                        .addComponent(_lblNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addComponent(_lblRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRecipeDetailsLayout.createSequentialGroup()
                        .addComponent(spNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlRecipeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStar1)
                            .addComponent(lblStar2)
                            .addComponent(lblStar3)
                            .addComponent(lblStar4)
                            .addComponent(lblStar5)))))
            .addComponent(pnlRecipeImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRecipeDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRecipeDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblImageMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageMouseReleased
        if(MyTree.getCurrentRecipe()!=null) {
            if(MyTree.getCurrentRecipe().getRecipeImage()!=null) {
                imagePopupDialog = new ImagePopupDialog((JFrame) this.getTopLevelAncestor(), true, MyTree.getCurrentRecipe());
                imagePopupDialog.setVisible(true);
            }
        }
    }//GEN-LAST:event_lblImageMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel _lblAdded;
    private javax.swing.JLabel _lblAuthor;
    private javax.swing.JLabel _lblCategory;
    private javax.swing.JLabel _lblModified;
    private javax.swing.JLabel _lblNote;
    private javax.swing.JLabel _lblRating;
    private static javax.swing.JLabel lblAdded;
    private static javax.swing.JLabel lblAuthor;
    private static javax.swing.JLabel lblCatebory;
    private static javax.swing.JLabel lblImage;
    private static javax.swing.JLabel lblModified;
    private static javax.swing.JLabel lblStar1;
    private static javax.swing.JLabel lblStar2;
    private static javax.swing.JLabel lblStar3;
    private static javax.swing.JLabel lblStar4;
    private static javax.swing.JLabel lblStar5;
    private javax.swing.JPanel pnlRecipeDetails;
    private static javax.swing.JPanel pnlRecipeImage;
    private javax.swing.JScrollPane spNote;
    private static javax.swing.JTextArea txtaNote;
    // End of variables declaration//GEN-END:variables

}
