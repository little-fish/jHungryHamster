
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
import cz.babi.java.jhungryhamster.entity.CookBook;
import cz.babi.java.jhungryhamster.entity.MyTree;
import cz.babi.java.jhungryhamster.entity.TreeNodes;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Třída definující vzhled a funkčnost toolbaru.
 *
 * @author babi
 */
public class MyToolbar extends JToolBar {
    
    private static final MyToolbar instance = new MyToolbar();
    
    private JLabel lblSpace;
    private JButton btnAddNew;
    private JButton btnAddNewCategory;
    private JButton btnDeleteSelected;
    private JButton btnEditSelected;
    private JButton btnPrintSelected;
    private JButton btnSettings;
    private JToolBar.Separator jSeparator;
    
    private CategoryDialog categoryDialog;
    private RemoveCategoryDialog removeCategoryDialog;
    private RemoveRecipeDialog removeRecipeDialog;
    private RecipeDialog recipeDialog;
    private SettingsDialog settingsDialog;
    private PrintRecipeDialog printRecipeDialog;

    /**
     * Privátní konstruktor třídy Settings.
     */
    private MyToolbar() {
        initComponent();
    }
    
    /**
     * Metoda pro získání instance třídy TreeNode.
     * Použitý návrhový vzor - <b>Singleton</b>.
     * 
     * @return Instance třídy TreeNode
     */
    public static MyToolbar getInstance() {   
        return instance;
    }
    
    private void initComponent() {
        lblSpace = new JLabel();
        btnAddNew = new JButton();
        btnAddNewCategory = new JButton();
        btnEditSelected = new JButton();
        btnPrintSelected = new JButton();
        btnDeleteSelected = new JButton();
        btnSettings = new JButton();
        jSeparator = new JToolBar.Separator();

        
        this.setFloatable(false);
        this.setName("jToolBar");
        
        lblSpace.setName("lblSpace");
        this.add(lblSpace);

        btnAddNew.setIcon(Icons.ADD_NEW_RECIPE);
        btnAddNew.setFocusable(false);
        btnAddNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddNew.setName("btnAddNew");
        btnAddNew.setToolTipText(RESOURCE_BUNDLE.getString("MyToolbar.btnAddNew.tooltip"));
        btnAddNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddNew.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewActionPerformed(evt);
            }
        });
        this.add(btnAddNew);

        btnAddNewCategory.setIcon(Icons.ADD_NEW_FOLDER);
        btnAddNewCategory.setFocusable(false);
        btnAddNewCategory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddNewCategory.setName("btnAddNewCategory");
        btnAddNewCategory.setToolTipText(RESOURCE_BUNDLE.getString("MyToolbar.btnAddNewCategory.tooltip"));
        btnAddNewCategory.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddNewCategory.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewCategoryActionPerformed(evt);
            }
        });
        this.add(btnAddNewCategory);

        btnEditSelected.setIcon(Icons.EDIT_SELECTED);
        btnEditSelected.setEnabled(false);
        btnEditSelected.setFocusable(false);
        btnEditSelected.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditSelected.setName("btnEditSelected");
        btnEditSelected.setToolTipText(RESOURCE_BUNDLE.getString("MyToolbar.btnEditSelected.tooltip"));
        btnEditSelected.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditSelected.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSelectedActionPerformed(evt);
            }
        });
        this.add(btnEditSelected);

        btnPrintSelected.setIcon(Icons.PRINT_SELECTED);
        btnPrintSelected.setEnabled(false);
        btnPrintSelected.setFocusable(false);
        btnPrintSelected.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrintSelected.setName("btnPrintSelected");
        btnPrintSelected.setToolTipText(RESOURCE_BUNDLE.getString("MyToolbar.btnPrintSelected.tooltip"));
        btnPrintSelected.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrintSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnPrintSelectedActionPerformed(e);
            }
        });
        this.add(btnPrintSelected);

        btnDeleteSelected.setIcon(Icons.DELETE_SELECTED);
        btnDeleteSelected.setEnabled(false);
        btnDeleteSelected.setFocusable(false);
        btnDeleteSelected.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDeleteSelected.setName("btnDeleteSelected");
        btnDeleteSelected.setToolTipText(RESOURCE_BUNDLE.getString("MyToolbar.btnDeleteSelected.tooltip"));
        btnDeleteSelected.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDeleteSelected.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedActionPerformed(evt);
            }
        });
        this.add(btnDeleteSelected);

        jSeparator.setName("jSeparator");
        this.add(jSeparator);
        
        btnSettings.setIcon(Icons.SETTINGS);
        btnSettings.setEnabled(true);
        btnSettings.setFocusable(false);
        btnSettings.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSettings.setName("btnSettings");
        btnSettings.setToolTipText(RESOURCE_BUNDLE.getString("MyToolbar.btnSettings.tooltip"));
        btnSettings.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettings.setText(RESOURCE_BUNDLE.getString("MyToolbar.btnSettings.text"));
        btnSettings.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingsActionPerformed(evt);
            }
        });
        this.add(btnSettings);
    }
    
    public void enableIconEdit(boolean canActive) {
        this.btnEditSelected.setEnabled(canActive);
    }
    
    public void enableIconDelete(boolean canDelete) {
        this.btnDeleteSelected.setEnabled(canDelete);
    }
    
    public void enableIconPrint(boolean canPrint) {
        this.btnPrintSelected.setEnabled(canPrint);
    }
    
    public void setToolTipBtnEdit(String toolTip) {
        btnEditSelected.setToolTipText(toolTip);
    }
    
    public void setToolTipBtnDelete(String toolTip) {
        btnDeleteSelected.setToolTipText(toolTip);
    }
    
    public void setToolTipBtnPrint(String toolTip) {
        btnPrintSelected.setToolTipText(toolTip);
    }
    
    /**
     * Metoda zachytává událost při zmáčknutí tlačítka pro přidání nové kategoie
     * @param evt 
     */
    private void btnAddNewCategoryActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        categoryDialog = new CategoryDialog(new javax.swing.JFrame(), true,
                RESOURCE_BUNDLE.getString("MyToolbar.dialog.newCategory.title"), null);
        categoryDialog.setVisible(true);
        if(categoryDialog.getReturnStatus()==CategoryDialog.RET_OK) {
            MyTree.addNewCategory(categoryDialog.getNode());
        }
    }                                                 

    /**
     * Metoda zachytává událost při zmáčknutí tlačítka pro editaci receptu/kategorie
     * 
     * @param evt 
     */
    private void btnEditSelectedActionPerformed(java.awt.event.ActionEvent evt) {                                                
        DefaultMutableTreeNode node = MyTree.getClickedTreeNode();
        
        if(node.getUserObject() instanceof TreeNodes.Node) {
            categoryDialog = new CategoryDialog(new javax.swing.JFrame(), true, 
                    RESOURCE_BUNDLE.getString("MyToolbar.dialog.editCategory.title"), MyTree.getCurrentCategory());
            categoryDialog.setVisible(true);
            if(categoryDialog.getReturnStatus()==CategoryDialog.RET_OK) {
                MyTree.editCategory(categoryDialog.getNode());
            }
        } else {
            recipeDialog = new RecipeDialog(new javax.swing.JFrame(), true, 
                    RESOURCE_BUNDLE.getString("MyToolbar.dialog.editRecipe.title"), MyTree.getCurrentRecipe());
            recipeDialog.setVisible(true);
            /* uložení receptu se provede ve třídě RecipeDialog */
            if(recipeDialog.getReturnStatus()==RecipeDialog.RET_OK) {
                MyTree.editRecipe(recipeDialog.getRecipe());
                SearchPanel.reloadRecipePanel();
            }
        }
    }                                               

    /**
     * Metoda zachytává událost při zmáčknutí tlačítka pro přidání nového receptu
     * 
     * @param evt 
     */
    private void btnAddNewActionPerformed(java.awt.event.ActionEvent evt) { 
        CookBook.Recipe recipe = new CookBook.Recipe();
        recipeDialog = new RecipeDialog(new javax.swing.JFrame(), true, 
                RESOURCE_BUNDLE.getString("MyToolbar.dialog.newRecipe.title"), recipe);
        recipeDialog.setVisible(true);
        if(recipeDialog.getReturnStatus()==RecipeDialog.RET_OK) {
            MyTree.addNewRecipe(recipe);
        }
    }
    
    /**
     * Metoda zachytává událost při zmáčknutí tlačítka pro smazání receptu/kategorie
     * 
     * @param evt 
     */
    private void btnDeleteSelectedActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if(MyTree.getClickedTreeNode().getUserObject() instanceof TreeNodes.Node) {
            removeCategoryDialog = new RemoveCategoryDialog(
                    new javax.swing.JFrame(), true, RESOURCE_BUNDLE.getString("MyToolbar.dialog.deleteCategory.title"));
            removeCategoryDialog.setVisible(true);
            
            if(removeCategoryDialog.getReturnStatus()==RemoveCategoryDialog.RET_DELETE) {
                MyTree.removeOnlyCategory();
            } else if(removeCategoryDialog.getReturnStatus()==RemoveCategoryDialog.RET_DELETE_ALL) {
                MyTree.removeAllCategory();
            }
        } else if(MyTree.getClickedTreeNode().getUserObject() instanceof CookBook.Recipe) {
            removeRecipeDialog = new RemoveRecipeDialog(
                    new javax.swing.JFrame(), true, RESOURCE_BUNDLE.getString("MyToolbar.dialog.deleteRecipe.title"));
            removeRecipeDialog.setVisible(true);
            
            if(removeRecipeDialog.getReturnStatus()==RemoveRecipeDialog.RET_DELETE_RECIPE) {
                MyTree.removeRecipe();
            }
        }
    }
    
    /**
     * Metoda zachytává událost při zmáčknutí tlačítka pro zobrazební nastavení
     * aplikace.
     * @param evt 
     */
    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {                                          
        settingsDialog = new SettingsDialog(new javax.swing.JFrame(), true);
        settingsDialog.setVisible(true);
    }
    
    /**
     * Metoda zachycena při stisku tlačítka pro tisk receptu.
     * @param ae Zachycená událost.
     */
    private void btnPrintSelectedActionPerformed(ActionEvent ae) {
        printRecipeDialog = new PrintRecipeDialog(new JFrame(), true, MyTree.getCurrentRecipe());
        printRecipeDialog.setVisible(true);
    }
}
