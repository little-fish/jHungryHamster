
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

package cz.babi.java.jhungryhamster.entity;

import static cz.babi.java.jhungryhamster.utils.Common.RESOURCE_BUNDLE;

import cz.babi.java.jhungryhamster.data.CategoryIndenter;
import cz.babi.java.jhungryhamster.data.DatabaseOperations;
import cz.babi.java.jhungryhamster.data.FileOperations;
import cz.babi.java.jhungryhamster.data.Icons;
import cz.babi.java.jhungryhamster.data.SearchWordSeparator;
import cz.babi.java.jhungryhamster.entity.CookBook.Recipe;
import cz.babi.java.jhungryhamster.entity.TreeNodes.Node;
import cz.babi.java.jhungryhamster.gui.CategoryDialog;
import cz.babi.java.jhungryhamster.gui.MainFrame;
import cz.babi.java.jhungryhamster.gui.MyToolbar;
import cz.babi.java.jhungryhamster.gui.RecipeDialog;
import cz.babi.java.jhungryhamster.gui.SearchPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Třída definující obsah stromu s recepty a práci s nimi.
 *
 * @author babi
 */
public class MyTree extends JTree implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTree.class);

    private static java.text.Collator collator;

    private static MyContainer myContainer = MyContainer.getInstance();
    private static Settings settings = Settings.getInstance();
    private static CookBookFilteredTreeModel model;
    private static CookBookFilteredTreeModel tempModel;
    private static MyTree tree = new MyTree();
    private static MyToolbar instanceMyToolbar = MyToolbar.getInstance();
    private static DatabaseOperations databaseOperations = DatabaseOperations.getInstance();
    /**
     * Tyto tři proměnné se nastavují jakmile uživatel klikne na nějakou položku
     * ve stromu. Pracuje se s němi při zobrazování, editování, mazání,
     * tisknutí, atd.
     */
    private static Node currentCategory = null;
    private static Recipe currentRecipe = null;
    private static DefaultMutableTreeNode clickedTreeNode = null;

    /**
     * Privátní konstruktor třídy MyTree. Nastaví se v něm model stromu. Pakliže
     * není databáze načtena, nastaví se defaultní model, jinak se nastaví
     * načtený model.
     */
    private MyTree() {
        initMyTree(true);
    }

    /**
     * Metoda pro získání instance stromu.
     *
     * @return instance třídy MyTree
     */
    public static MyTree getInstance() {
        return tree;
    }
    
    /**
     * Metoda inicializuje strom receptů.
     * @param firstRun První spuštění.
     */
    public void initMyTree(boolean firstRun) {
        collator = java.text.Collator.getInstance();
        collator.setStrength(java.text.Collator.TERTIARY);
        
        model = new CookBookFilteredTreeModel(
                new DefaultMutableTreeNode(settings.getDatabaseName()));
        tempModel = myContainer.getCookBookFilteredTreeModel();

        if(firstRun) {
            if (tempModel != null) {
                model = tempModel;
            } else {
                myContainer.setCookBookFilteredTreeModel(model);
            }
        } else myContainer.setCookBookFilteredTreeModel(model);

        setModel(model);
        setCellRenderer(new MyRenderer());
    }
    
    public static void renameRootNode(String newName) {
        model.renameRoot(newName);
    }
    
    /**
     * Metoda vrací mapu všech kategorií (podle jejich ID)
     * @return mapa kategorií.
     */
    public static HashMap<Integer, DefaultMutableTreeNode> getCategoryMap() {
        return model.getCategoryMap();
    }
    
    /**
     * Metoda vrací mapu všech receptů (podle jejich ID)
     * @return mapa receptů.
     */
    public static HashMap<Integer, DefaultMutableTreeNode> getRecipeMap() {
        return model.getRecipeMap();
    }

    /**
     * Metoda pro nastavení filtru pro vyhledávání v receptech.
     *
     * @param filter textový řetězec pro vyhledávání
     * @param showStatusMessage má-li se zobrazit status zpráva
     */
    public static DefaultMutableTreeNode setFilterToModel(String filter, boolean showStatusMessage) {
        //TODO: udělat vyhledávání ve vlákně, aby nedocházelo ke zamrzání gui
        Recipe firstMatch = model.setFilter(filter, showStatusMessage);
        
        /* pokud byl nalezen nějaký recept odpovídající filtru, tak se označí
         první výskyt*/
        if(firstMatch==null) {
            if(settings.isShowRootElement()) tree.expandRow(0);
            else model.reload();
        } else if(firstMatch.equals((DefaultMutableTreeNode)model.getRoot())) {
            if(settings.isShowRootElement()) tree.expandRow(0);
            else model.reload();
        } else if(firstMatch!=null) {
            if(settings.isSelectFirstMatch()) {
                DefaultMutableTreeNode firstLeaf = ((DefaultMutableTreeNode)tree.getModel().getRoot()).getFirstLeaf();
                if(!firstLeaf.isRoot())
                    tree.setSelectionPath(new TreePath(firstLeaf.getPath()));
            }
            
            if(settings.isShowWholeTreeAfterSearch()) {
                for (int i = 0; i < tree.getRowCount(); i++) {
                    tree.expandRow(i);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Metoda vrací pole kategorií pro naplnění jComboBoxů pro vytváření a 
     * editaci receptů a kategorií.
     * @return pole kategorií.
     */
    public static Node[] getAllSortedCategories() {
        return model.getSortedCategories();
    }
    
    /**
     * Metoda se volá pro znovuvytvření pole kategorií.
     */
    public static void createSortedCategories() {
        model.createSortedCategories();
    }

    /**
     * Metoda se volá například při změně receptu, nebo kategorie.
     */
    public void reloadVariable() {
        model.reloadVariable();
    }
    
    /**
     * Metoda pro seřazení části stromu.
     * @param node Rodič, jehož potomci budou seřazeni.
     */
    public static void sortTree(DefaultMutableTreeNode node) {
        MainFrame.getStatusPanel().setAnimationIcon(Icons.TASK_BUSY);
        setFilterToModel(SearchPanel.getFilter(), false);
        MainFrame.getStatusPanel().setAnimationIcon(Icons.TASK_IDLE);
    }

    /**
     * Metoda pro přidání nového receptu. Tato metoda volá metodu pro přidání
     * nového objektu do stromu a předává jí jako parametr instanci třídy
     * Recipe.
     *
     * @param recipe recept přo přidání
     */
    public static void addNewRecipe(Recipe recipe) {
        int recipeId = (int) databaseOperations.insertNewRecipeIntoDatabase(
                FileOperations.getUserDatabaseFile(),
                recipe,
                (currentCategory == null) ? 0 : currentCategory.getIdNode());
        recipe.setIdRecipe(recipeId);
        
//        recipe.setIdCategory((currentCategory == null) ? 0 : currentCategory.getIdNode());
        
        DefaultMutableTreeNode addedTreeNode = model.addObjectFromDatabase(recipe);
        
        /* po přidání nového receptu je nutné seřadit všechny uzly v dané kategorii;
         nemusí se řadit celá databáze, ale pouze nadřazený uzel */
        if(getClickedTreeNode()==null)
            sortTree((DefaultMutableTreeNode)model.getRoot());
        else if(getClickedTreeNode().getUserObject() instanceof Node) {
            sortTree(getClickedTreeNode());
        } else if(getClickedTreeNode().getUserObject() instanceof Recipe) {
            if(getCurrentCategory()==null) sortTree((DefaultMutableTreeNode)model.getRoot());
            else sortTree((DefaultMutableTreeNode)getClickedTreeNode().getParent());
        } else if(getClickedTreeNode().isRoot())
            sortTree((DefaultMutableTreeNode)model.getRoot());
        
        
        //TODO: selection not working
        TreePath addedTreePath = new TreePath(addedTreeNode.getPath());
//        tree.expandPath(addedTreePath.getParentPath());
        tree.scrollPathToVisible(addedTreePath);
        tree.setSelectionPath(addedTreePath);

        MainFrame.getStatusPanel().setStatusMessage("Přídán nový recept '" + recipe.getTitle() + "'", 
                new Date(), Icons.STATUS_RECIPE_NEW);
    }

    /**
     * Metoda pro přidání nové kategorie. Tato metoda volá metodu pro přidání
     * nového objektu do stromu a předává jí jako parametr instanci třídy Node.
     *
     * @param category název nové kategorie
     */
    public static void addNewCategory(Node category) {
        int categoryId = (int) databaseOperations.insertNewCategoryIntoDatabase(
                FileOperations.getUserDatabaseFile(),
                category,
                category.getIdParent());
        category.setIdNode(categoryId);
        
        final DefaultMutableTreeNode addedTreeNode = model.addObjectFromDatabase(category);
        
        /* po přidání nové kategorie je nutné seřadit všechny uzly;
         nemusí se řadit všechny úzly, ale pouze nadřazený uzel */
        if(getClickedTreeNode()==null)
            sortTree((DefaultMutableTreeNode)model.getRoot());
        else if(getClickedTreeNode().getUserObject() instanceof Node) {
            sortTree(getClickedTreeNode());
        } else if(getClickedTreeNode().getUserObject() instanceof Recipe) {
            if(getCurrentCategory()==null) sortTree((DefaultMutableTreeNode)model.getRoot());
            else sortTree((DefaultMutableTreeNode)getClickedTreeNode().getParent());
        } else if(getClickedTreeNode().isRoot())
            sortTree((DefaultMutableTreeNode)model.getRoot());
        
        //TODO: selection not working
        tree.setExpandsSelectedPaths(true);
        TreePath addedTreePath = new TreePath(model.getPathToRoot(addedTreeNode));
        tree.expandPath(addedTreePath);
        
        tree.scrollPathToVisible(addedTreePath);
        tree.setSelectionPath(new TreePath(model.getPathToRoot(addedTreeNode)));
        
        
        model.createSortedCategories();
        
        MainFrame.getStatusPanel().setStatusMessage("Přídána nová kategorie '" + category.getTitle() + "'", 
                new Date(), Icons.STATUS_CATEGORY_NEW);
    }

    /**
     * Metoda se volá pro vložení nového objektu z databáze. Buď instance třídy
     * Node (kategorie), a nebo Recipe (recept).
     *
     * @param child Nový objekt z databáze.
     */
    public static void addObjectFromDatabase(Object child) {
        model.addObjectFromDatabase(child);
    }

    /**
     * Metoda pro editování vybrané kategorie. Tato metoda volá metodu pro
     * editování zvolené kategorie a předává jí jako parametr upravenou
     * kategorii.
     *
     * @param category Upravená kategorie.
     */
    public static void editCategory(Node category) {
        DefaultMutableTreeNode editedNode = model.editNodeObject(category);
        
        TreePath addedTreePath = new TreePath(editedNode.getPath());
        tree.scrollPathToVisible(addedTreePath);
        tree.setSelectionPath(addedTreePath);
        
        model.reloadVariable();
        
        /* pokud se změnil název receptu či kategorie, musí dojít k seřezení */
        if(CategoryDialog.changeTitle || CategoryDialog.changeCategory) {
            if(!SearchPanel.getFilter().equals("")) {
                tree.setFilterToModel(SearchPanel.getFilter(), true);
            } else {
            /* po přidání nového receptu je nutné seřadit všechny uzly v dané kategorii;
            nemusí se řadit celá databáze, ale pouze nadřazený uzel */
            if(getClickedTreeNode()==null) {
                sortTree((DefaultMutableTreeNode)model.getRoot());
            } else if(getClickedTreeNode().getUserObject() instanceof Node) {
                if(getClickedTreeNode().getParent()==null) sortTree((DefaultMutableTreeNode)model.getRoot());
                else sortTree((DefaultMutableTreeNode)getClickedTreeNode().getParent());
            } else if(getClickedTreeNode().isRoot())
                sortTree((DefaultMutableTreeNode)model.getRoot());
            
            model.createSortedCategories();
            
            addedTreePath = new TreePath(editedNode.getPath());
            tree.scrollPathToVisible(addedTreePath);
            tree.setSelectionPath(addedTreePath);

            model.reloadVariable();
            }
        }
        
        MainFrame.getStatusPanel().setStatusMessage("Editována kategorie '" + category.getTitle() + "'", 
                new Date(), Icons.STATUS_CATEGORY_EDIT);
    }

    /**
     * Metoda pro editování vybraného receptu. Tato metoda volá metodu pro
     * editování zvoleného receptu a předává jí jako parametr upravený recept.
     *
     * @param recipe Upravený recept.
     */
    public static void editRecipe(Recipe recipe) {
        DefaultMutableTreeNode editedNode = model.editRecipeObject(recipe);
        
        TreePath addedTreePath = new TreePath(editedNode.getPath());
        tree.scrollPathToVisible(addedTreePath);
        tree.setSelectionPath(addedTreePath);
        model.reloadVariable();

        /* pokud se změnil název receptu či kategorie, musí dojít k seřezení */
        if(RecipeDialog.changeTitle || RecipeDialog.changeCategory) {
            if(!SearchPanel.getFilter().equals("")) {
                tree.setFilterToModel(SearchPanel.getFilter(), true);
            } else {
                /* po editaci receptu je nutné seřadit všechny uzly v dané kategorii;
                nemusí se řadit celá databáze, ale pouze nadřazený uzel */
                if(getClickedTreeNode()==null)
                    sortTree((DefaultMutableTreeNode)model.getRoot());
                else if(getClickedTreeNode().getUserObject() instanceof Node) {
                    sortTree(getClickedTreeNode());
                } else if(getClickedTreeNode().getUserObject() instanceof Recipe) {
                    if(getCurrentCategory()==null) sortTree((DefaultMutableTreeNode)model.getRoot());
                    else sortTree((DefaultMutableTreeNode)getClickedTreeNode().getParent());
                } else if(getClickedTreeNode().isRoot())
                    sortTree((DefaultMutableTreeNode)model.getRoot());
                
                addedTreePath = new TreePath(editedNode.getPath());
                tree.scrollPathToVisible(addedTreePath);
                tree.setSelectionPath(addedTreePath);
                model.reloadVariable();
            }
        }
        
        MainFrame.getStatusPanel().setStatusMessage("Editován recept '" + recipe.getTitle() + "'", 
                new Date(), Icons.STATUS_RECIPE_EDIT);
    }

    /**
     * Metoda volá metodu pro vymazání zvoleného receptu.
     */
    public static void removeRecipe() {
        String removedRecipe = getCurrentRecipe().getTitle();
        
        model.removeRecipe();
        
        MainFrame.getStatusPanel().setStatusMessage("Smazán recept '" + removedRecipe + "'", 
                new Date(), Icons.STATUS_RECIPE_DELETE);
    }

    /**
     * Tato metoda volá metodu pro vymazání zvolené kategorie.
     */
    public static void removeOnlyCategory() {
        String removedCategory = getCurrentCategory().getTitle();
        
        model.removeOnlyCategory();
        
        /* po přidání nového receptu je nutné seřadit všechny uzly v dané kategorii;
         nemusí se řadit celá databáze, ale pouze nadřazený uzel */
        if(getClickedTreeNode()==null)
            sortTree((DefaultMutableTreeNode)model.getRoot());
        else if(getClickedTreeNode().getUserObject() instanceof Node) {
            if(getClickedTreeNode().getParent()==null) sortTree((DefaultMutableTreeNode)model.getRoot());
            else sortTree((DefaultMutableTreeNode)getClickedTreeNode().getParent());
        } else if(getClickedTreeNode().getUserObject() instanceof Recipe) {
            if(getCurrentCategory()==null) sortTree((DefaultMutableTreeNode)model.getRoot());
            else sortTree((DefaultMutableTreeNode)getClickedTreeNode().getParent());
        } else if(getClickedTreeNode().isRoot())
            sortTree((DefaultMutableTreeNode)model.getRoot());
        
        model.createSortedCategories();
        
        MainFrame.getStatusPanel().setStatusMessage("Smazána kategorie '" + removedCategory + "'", 
                new Date(), Icons.STATUS_CATEGORY_DELETE);
    }

    /**
     * Tato metoda volá metodu pro vymazání zvolené kategorie a jejich částí.
     */
    public static void removeAllCategory() {
        model.removeAllCategory();
        
        model.createSortedCategories();
        
        MainFrame.getStatusPanel().setStatusMessage("Smazáno více kategorií", 
                new Date(), Icons.STATUS_CATEGORY_DELETE);
    }

    /**
     * @return the currentCategory
     */
    public static Node getCurrentCategory() {
        return currentCategory;
    }

    /**
     * @return the currentRecipe
     */
    public static Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    /**
     * @return the clickedTreeNode
     */
    public static DefaultMutableTreeNode getClickedTreeNode() {
        return clickedTreeNode;
    }

    /**
     * @param currentCategory the currentNode to set
     */
    public static void setCurrentCategory(Node currentCategory) {
        MyTree.currentCategory = currentCategory;
    }

    /**
     * @param currentRecipe the currentRecipe to set
     */
    public static void setCurrentRecipe(Recipe currentRecipe) {
        MyTree.currentRecipe = currentRecipe;
    }

    /**
     * @param clickedTreeNode the clickedTreeNode to set
     */
    public static void setClickedTreeNode(DefaultMutableTreeNode clickedTreeNode) {
        MyTree.clickedTreeNode = clickedTreeNode;
    }

    /**
     * Třída definující práci s modelem stromu. Nutné vytvořit pro vyhledávání
     * ve stromu.
     */
    public static class CookBookFilteredTreeModel extends DefaultTreeModel {

        private static final long serialVersionUID = 5632874215454265848L;

        private static final Logger LOGGER = LoggerFactory.getLogger(CookBookFilteredTreeModel.class);
        
        private HashMap<Integer, DefaultMutableTreeNode> categoryMap = new HashMap<Integer, DefaultMutableTreeNode>();        
        private HashMap<Integer, DefaultMutableTreeNode> recipeMap = new HashMap<Integer, DefaultMutableTreeNode>();
        private ArrayList<DefaultMutableTreeNode> needReorganizeOrRemoveList = new ArrayList<DefaultMutableTreeNode>();
        
        private Node[] sortedCategories = null;
        
        HashMap<Integer, DefaultMutableTreeNode> tempCategoryMap = new HashMap<Integer, DefaultMutableTreeNode>();        
        HashMap<Integer, DefaultMutableTreeNode> tempRecipeMap = new HashMap<Integer, DefaultMutableTreeNode>();
        
        /*
         * filtr pro vyhledávání ve stromu receptů
         */
        private String filter = "";

        private CookBookFilteredTreeModel(TreeNode node) {
            super(node);
        }

        private CookBookFilteredTreeModel(TreeNode node, boolean asksAllowsChildren) {
            super(node, asksAllowsChildren);
        }

        public HashMap<Integer, DefaultMutableTreeNode> getCategoryMap() {
            return categoryMap;
        }

        public HashMap<Integer, DefaultMutableTreeNode> getRecipeMap() {
            return recipeMap;
        }

        public Node[] getSortedCategories() {
            return sortedCategories;
        }
        
        /**
         * Metoda vytvoří pole kategorií přesně v pořadí, jakém se nacházejí ve stromu
         * a uloží jej do třídní proměnné.
         */
        public void createSortedCategories() {
            ArrayList<Node> sortedCategoriesList = new ArrayList<>();
            
            /* nutno přidat roota */
            sortedCategoriesList.add(new Node(root.toString(), 0, -1));
            
            if(!getCategoryMap().isEmpty()) {
                ArrayList<DefaultMutableTreeNode> sortedMutableCategories = new ArrayList<>
                        (getCategoryMap().values());           

                Comparator categoryComparator = new Comparator () {
                    @Override
                    public int compare(Object o1, Object o2) {

                        DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)o1;
                        DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)o2;

                        Node category1 = (Node)node1.getUserObject();
                        Node category2 = (Node)node2.getUserObject();

                        if(category1.getIdParent()-category2.getIdParent()==0) {
                            return category1.getTitle().toLowerCase().compareTo(
                                    category2.getTitle().toLowerCase());
                        } else return category1.getIdParent() - category2.getIdParent();
                    }

                    @Override
                    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
                    public boolean equals(Object obj)    {
                        return false;
                    }

                    @Override
                    public int hashCode() {
                        int hash = 7;
                        return hash;
                    }
                };

                Collections.sort(sortedMutableCategories, categoryComparator);

                DefaultMutableTreeNode temp = sortedMutableCategories.get(0);
                Node tempNode = (Node)temp.getUserObject();
                int idParent = tempNode.getIdParent();
                String prefix;
                String settingsPrefix = "";
                
                if(settings.getCategoryIndenter().equals(CategoryIndenter.Custom))
                    settingsPrefix = settings.getUserCategoryJoiner() + " ";
                else if(settings.getCategoryIndenter().equals(CategoryIndenter.None)) ;
                else settingsPrefix = settings.getCategoryIndenter().getKey().trim() + " ";

                for(int i=0, j=0; i<sortedMutableCategories.size(); i++) {
                    DefaultMutableTreeNode mutableTreeNode = sortedMutableCategories.get(i);
                    Node node = (Node)mutableTreeNode.getUserObject();
                    prefix = "";

                    for(int y = 0; y<mutableTreeNode.getLevel(); y++) {
                        prefix = prefix + settingsPrefix;
                    }

                    if(idParent==node.getIdParent()) j++;
                    else {
                        idParent = node.getIdParent();
                        for(int y=0; y<sortedCategoriesList.size(); y++) {
                            if(sortedCategoriesList.get(y).getIdNode()==idParent) {
                                j = y+1;
                            }
                        }
                    }
                    
                    sortedCategoriesList.add(j, new Node(prefix+node.getTitle(), node.getIdNode(), node.getIdParent()));
                }
            }
            
            sortedCategories = new Node[sortedCategoriesList.size()];
            for (int i = 0; i < sortedCategoriesList.size(); i++) {
                sortedCategories[i] = sortedCategoriesList.get(i);
            }
        }


        
        /**
         * Metoda vyfiltruje požadované položky.
         * @param filter Filtr podle kterého se budou jednotlivé recepty filtrovat.
         * @param showStatusMessage má-li se zobrazit status zpráva
         * @return První nalezený recept dle vstupního filtru.
         */
        public Recipe setFilter(String filter, boolean showStatusMessage) {

            Recipe firstMatch = null;

            /* pokud je filtr prázdný, je nutné zrekonstruovat celý strom se všemi recepty */
            if(filter==null || filter.toLowerCase().equals(this.filter.toLowerCase())) {
                fillTreeAfterFiltering(categoryMap, recipeMap);
                
                if(showStatusMessage) MainFrame.getStatusPanel().setStatusMessage(RESOURCE_BUNDLE.getString("Filter.emptyFilter"),
                        new Date(), Icons.STATUS_SEARCH);
                
                return null;
            }
            
            LOGGER.debug("start filtering");

            tempCategoryMap.clear();
            tempRecipeMap.clear();

            String splitter = "";
            switch (settings.getSearchSplitter()) {
                case Space:
                    splitter = Pattern.quote(" ");
                    break;
                case Custom:
                    splitter = Pattern.quote(settings.getUserSearchSplitter());
                    break;
                case Dot:
                    splitter = Pattern.quote(SearchWordSeparator.Dot.getKey().trim());
                    break;
                case Comma:
                    splitter = Pattern.quote(SearchWordSeparator.Comma.getKey().trim());
                    break;
                default:
                    splitter = Pattern.quote(settings.getSearchSplitter().getKey().trim());
                    break;
            }

            String[] filterArray = filter.toLowerCase().split(String.valueOf(splitter));

            Iterator<DefaultMutableTreeNode> recipeIterator = recipeMap.values().iterator();

            /* procházíme veškeré recepty a zjišťujeme, zda-li jeho položky
             odpovídají filtru */
            while(recipeIterator.hasNext()) {
                DefaultMutableTreeNode currentTreeNode = recipeIterator.next();
                Recipe r = (Recipe)currentTreeNode.getUserObject();

                boolean needContinue = true;

                int idCategory = 0;
                DefaultMutableTreeNode currentCategory = null;

                if(settings.isSearchInTitle()) {
                    /* je-li shoda názvu receptu s filtrem, přidáme jej do
                     * nalezených receptů */
                    for(int i=0; i<filterArray.length; i++) {
                        if(r.getTitle().toLowerCase().indexOf(filterArray[i])!=-1) {
                            tempRecipeMap.put(r.getIdRecipe(), currentTreeNode);

                                /* po nalezení receptu, je nutné projít veškeré jeho
                                kategorie, do kterých je vnořen a tyto kategorie
                                přidat do seznamu kategorií, který se nakonec
                                zobrazí ve stromu */
                                idCategory = r.getIdCategry();
                                currentCategory = categoryMap.get(r.getIdCategry());

                                while(idCategory!=0) {
                                    tempCategoryMap.put(idCategory, currentCategory);
                                    Node n = (Node)currentCategory.getUserObject();

                                    currentCategory = categoryMap.get(n.getIdParent());
                                    idCategory = n.getIdParent();
                                }

                            /* pakliže už tento recept odpovídá filtru, není nutné
                            porovnávat další jeho hodnoty a můžeme přejít na další
                            recept */
                            needContinue = false;
                            break;
                        }
                    }
                }

                if(needContinue) {
                    if(settings.isSearchInAuthor()) {
                        for(int i=0; i<filterArray.length; i++) {
                            if(r.getAuthor().toLowerCase().indexOf(filterArray[i])!=-1) {
                                tempRecipeMap.put(r.getIdRecipe(), currentTreeNode);

                                idCategory = r.getIdCategry();
                                currentCategory = categoryMap.get(r.getIdCategry());

                                while(idCategory!=0) {
                                    tempCategoryMap.put(idCategory, currentCategory);
                                    Node n = (Node)currentCategory.getUserObject();

                                    currentCategory = categoryMap.get(n.getIdParent());
                                    idCategory = n.getIdParent();
                                }

                                needContinue = false;
                                break;
                            }
                        }
                    }

                    if(needContinue) {
                        if(settings.isSearchInIngredients()) {
                            for(int i=0; i<filterArray.length; i++) {
                                if(r.getIngredients().toLowerCase().indexOf(filterArray[i])!=-1) {
                                    tempRecipeMap.put(r.getIdRecipe(), currentTreeNode);
    //                                tempCategoryMap.put(r.getIdCategry(), categoryMap.get(r.getIdCategry()));

                                    idCategory = r.getIdCategry();
                                    currentCategory = categoryMap.get(r.getIdCategry());

                                    while(idCategory!=0) {
                                        tempCategoryMap.put(idCategory, currentCategory);
                                        Node n = (Node)currentCategory.getUserObject();

                                        currentCategory = categoryMap.get(n.getIdParent());
                                        idCategory = n.getIdParent();
                                    }

                                    needContinue = false;
                                    break;
                                }
                            }
                        }

                        if(needContinue) {
                            if(settings.isSearchInMethod()) {
                                for(int i=0; i<filterArray.length; i++) {
                                    if(r.getMethods().toLowerCase().indexOf(filterArray[i])!=-1) {
                                        tempRecipeMap.put(r.getIdRecipe(), currentTreeNode);
    //                                    tempCategoryMap.put(r.getIdCategry(), categoryMap.get(r.getIdCategry()));

                                        idCategory = r.getIdCategry();
                                        currentCategory = categoryMap.get(r.getIdCategry());

                                        while(idCategory!=0) {
                                            tempCategoryMap.put(idCategory, currentCategory);
                                            Node n = (Node)currentCategory.getUserObject();

                                            currentCategory = categoryMap.get(n.getIdParent());
                                            idCategory = n.getIdParent();
                                        }

                                        break;
                                    }
                                }
                            }
                        }

                        if(needContinue) {
                            if(settings.isSearchInNote()) {
                                for(int i=0; i<filterArray.length; i++) {
                                    if(r.getNote().toLowerCase().indexOf(filterArray[i])!=-1) {
                                        tempRecipeMap.put(r.getIdRecipe(), currentTreeNode);
    //                                    tempCategoryMap.put(r.getIdCategry(), categoryMap.get(r.getIdCategry()));

                                        idCategory = r.getIdCategry();
                                        currentCategory = categoryMap.get(r.getIdCategry());

                                        while(idCategory!=0) {
                                            tempCategoryMap.put(idCategory, currentCategory);
                                            Node n = (Node)currentCategory.getUserObject();

                                            currentCategory = categoryMap.get(n.getIdParent());
                                            idCategory = n.getIdParent();
                                        }

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(showStatusMessage) MainFrame.getStatusPanel().setStatusMessage(RESOURCE_BUNDLE.getString("Filter.recipesFound") + ": " + tempRecipeMap.size(),
                        new Date(), Icons.STATUS_SEARCH);

            /* pokud je alespoň jeden nalezený recept, naplní se strom těmito recepty */
            if(tempRecipeMap.size()>0)
                firstMatch = fillTreeAfterFiltering(tempCategoryMap, tempRecipeMap);
            else {
                /* nutno vzmazat cely strom když nebyl nalezen záznam */
                Enumeration<DefaultMutableTreeNode> enumeration = root.children();
                while(enumeration.hasMoreElements()) {
                    DefaultMutableTreeNode n = enumeration.nextElement();
                    enumeration = root.children();
                    removeNodeFromParent(n);
                }
            }

            LOGGER.debug("stop filterring");

            return firstMatch;
        }
        
        
        
//        private Map<Integer, DefaultMutableTreeNode> sortByValue(Map<Integer, DefaultMutableTreeNode> map) {
//           
//            List<Map.Entry<Integer, DefaultMutableTreeNode>> list = new LinkedList<>(map.entrySet());
//            Collections.sort(list, new Comparator<Map.Entry<Integer, DefaultMutableTreeNode>>() {
//                @Override
//                public int compare(Map.Entry<Integer, DefaultMutableTreeNode> o1, Map.Entry<Integer, DefaultMutableTreeNode> o2) {
//                    Node n1 = (Node)o1.getValue();
//                    Node n2 = (Node)o2.getValue();
//
//                    return (n1.getTitle().compareTo(n2.getTitle()));
//                }
//            });
//
//            Map<Integer, DefaultMutableTreeNode> result = new LinkedHashMap<>();
//            for (Map.Entry<Integer, DefaultMutableTreeNode> entry : list) {
//                result.put(entry.getKey(), entry.getValue());
//            }
//            return result;
//            List list = new ArrayList(map.entrySet());
//            Collections.sort(list, new Comparator<DefaultMutableTreeNode>() {
//
//                @Override
//                public int compare(DefaultMutableTreeNode o1, DefaultMutableTreeNode o2) {
//                    Node n1 = (Node)o1;
//                    Node n2 = (Node)o2;
//
//                    return (n1.getTitle().compareTo(n2.getTitle()));
//                }
//     });
//
//    Map result = new LinkedHashMap();
//    for (Iterator it = list.iterator(); it.hasNext();) {
//        Map.Entry entry = (Map.Entry)it.next();
//        result.put(entry.getKey(), entry.getValue());
//    }
//    return result;
//        }
        
        /**
         * Metoda naplní strom vyfiltrovanými záznamy.
         * @param categories Kategorie k naplnění.
         * @param recipes Recepty k naplnění.
         * @return První nalezený recept. Nutné pro to, aby se ve stromu označil.
         */
        private Recipe fillTreeAfterFiltering(HashMap<Integer, DefaultMutableTreeNode> categories,
                HashMap<Integer, DefaultMutableTreeNode> recipes) {
            
            Recipe firstMatch = null;
            
            /* nutno vzmazat cely strom před přidáním vyfiltrovaných záznamů */
            Enumeration<DefaultMutableTreeNode> enumeration = root.children();
            while(enumeration.hasMoreElements()) {
                DefaultMutableTreeNode n = enumeration.nextElement();
                enumeration = root.children();
                removeNodeFromParent(n);
            }
            
            HashMap<Integer, Node> tempCategories = new HashMap<>();
            
//            categories = sortByValue(categories);
//            Map<Integer, DefaultMutableTreeNode> cat = sortByValue(categories);
            
            /* nejprve přidáme veškeré kategorie, a je opět nutné kategorie 
             přidávat od nejmenšího ID předka, tedy prvně ty s ID=0 a pak ostatní*/
            ArrayList<Node> tempNodes = new ArrayList<>();
            for(DefaultMutableTreeNode node : categories.values()) {
                tempNodes.add((Node)node.getUserObject());
            }
            
            tempNodes.sort(new Comparator<Node>() {

                @Override
                public int compare(Node o1, Node o2) {
                    return collator.compare(o1.getTitle(), o2.getTitle());
//                    return (o1.getTitle().compareTo(o2.getTitle()));
                }
            });
        
            while(!tempNodes.isEmpty()) {
                for(Iterator<Node> iterator = tempNodes.iterator(); iterator.hasNext(); ) {
                    Node node = iterator.next();
                    if(node.getIdParent()==0) {
                        MyTree.addObjectFromDatabase(node);
                        tempCategories.put(node.getIdNode(), node);
                        iterator.remove();
                        tempNodes.remove(node);
                    } else {
                        if(tempCategories.get(node.getIdParent())!=null) {
                            MyTree.addObjectFromDatabase(node);
                            tempCategories.put(node.getIdNode(), node);
                            iterator.remove();
                            tempNodes.remove(node);
                        }
                    }
                }
            }

            /* poté přidáme veškeré nalezené recepty */
            ArrayList<Recipe> tempRecipes = new ArrayList<>();
            for(DefaultMutableTreeNode recipe : recipes.values()) {
                tempRecipes.add((Recipe)recipe.getUserObject());
            }
            
            tempRecipes.sort(new Comparator<Recipe>() {

                @Override
                public int compare(Recipe o1, Recipe o2) {
                    return collator.compare(o1.getTitle(), o2.getTitle());
                }
            });
            
            for(Recipe recipe : tempRecipes) {
                if(firstMatch==null) firstMatch = recipe;
                addObjectFromDatabase(recipe);
            }
            
//            Iterator<DefaultMutableTreeNode> ir = recipes.values().iterator();
//            while(ir.hasNext()) {
//                DefaultMutableTreeNode treeNode = ir.next();
//                
//                /* první nalezený záznam je nutné uchovat, aby se ve stromu označil */
//                Recipe r = (Recipe)treeNode.getUserObject();
//                if(firstMatch==null) firstMatch = r;
//                
//                addObjectFromDatabase(r);
//            }
            
            return firstMatch;
        }
          
        /**
         * Metoda přidá nový objekt do seznamu.
         *
         * @param child Nový objekt pro přidání (Node, nebo Recipe).
         * @return Přidaný objekt.
         */
        public DefaultMutableTreeNode addObjectFromDatabase(Object child) {
            int childParentId = 0;

            if (child instanceof Node) {
                Node n = (Node) child;
                childParentId = n.getIdParent();
            } else if (child instanceof Recipe) {
                Recipe r = (Recipe) child;
                childParentId = r.getIdCategry();
            }

            DefaultMutableTreeNode parrentDefaultMutableTreeNode = categoryMap.get(childParentId);

            if (parrentDefaultMutableTreeNode != null) {
                return addObject(parrentDefaultMutableTreeNode, child, true);
            } else {
                return addObject((DefaultMutableTreeNode) model.getRoot(), child, true);
            }
        }
        
        /**
         * Add child to the currently selected node.
         */
        public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                Object child, boolean shouldBeVisible) {

            DefaultMutableTreeNode childNode =
                    new DefaultMutableTreeNode(child);

            if (parent == null) {
                parent = (DefaultMutableTreeNode) this.getRoot();
            }

            if (parent.getUserObject() instanceof Recipe) {
                parent = (DefaultMutableTreeNode) parent.getParent();
            }
            
            //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
            insertNodeInto(childNode, parent, parent.getChildCount());

            //Make sure the user can see the lovely new node.
//            if (shouldBeVisible) {
//                tree.scrollPathToVisible(new TreePath(childNode.getPath()));
//            }

//            this.reload(childNode);

            if (child instanceof Node) {
                Node n = (Node) child;
                categoryMap.put(n.getIdNode(), childNode);
            } else if (child instanceof Recipe) {
                Recipe r = (Recipe) child;
                recipeMap.put(r.getIdRecipe(), childNode);
            }

            return childNode;
        }

//        /**
//         * Metoda seřadí uzly ve stromu. DEPRECATED!!!!
//         * @param root Uzel, jehož potomci mají být seřazeni.
//         */
//        public void sortTree(DefaultMutableTreeNode root) {
//            LOGGER.debug("Začátek řazení potomků v uzlu: " + root);
//            if(!root.isLeaf()) {
//                for(int i=0; i<root.getChildCount(); i++) {
//                    for(int j=i+1; j<root.getChildCount(); j++) {
//                        DefaultMutableTreeNode nodeFirst = (DefaultMutableTreeNode)root.getChildAt(i);
//                        DefaultMutableTreeNode nodeSecond = (DefaultMutableTreeNode)root.getChildAt(j);
//                        
//                        String nameFirst = null;
//                        String nameSecond = null;
//
//                        /* pokud jsou oba dva uzly kategorie jen se porovnají názvy */
//                        if(nodeFirst.getUserObject() instanceof Node &&
//                                nodeSecond.getUserObject() instanceof Node) {
//                            Node n1 =(Node)nodeFirst.getUserObject();
//                            nameFirst = n1.getTitle();
//                            Node n2 =(Node)nodeSecond.getUserObject();
//                            nameSecond = n2.getTitle();
//                            
//                            if(nameFirst.compareToIgnoreCase(nameSecond)>0) {
//                                root.insert(nodeSecond, i);
////                                model.reload(nodeSecond);
//                            }
//                        } else
//                            /* pokud jsou oba uzly recepty, jen se porovnají názvy */
//                            if(nodeFirst.getUserObject() instanceof Recipe &&
//                                nodeSecond.getUserObject() instanceof Recipe) {
//                            
//                            Recipe r1 =(Recipe)nodeFirst.getUserObject();
//                            nameFirst = r1.getTitle();
//                            Recipe r2 =(Recipe)nodeSecond.getUserObject();
//                            nameSecond = r2.getTitle();
//                            
//                            if(nameFirst.compareToIgnoreCase(nameSecond)>0) {
//                                root.insert(nodeSecond, i);
////                                model.reload(nodeSecond);
//                            }
//                        } else
//                            /* pokud je první uzel recept a druhy kategorie, přehodí se */  
//                            if(nodeFirst.getUserObject() instanceof Recipe &&
//                                nodeSecond.getUserObject() instanceof Node) {
//                            root.insert(nodeSecond, i);
////                            model.reload(nodeSecond);
//                        }
//
//                        /* pokud má první uzel potomky, rekurzivně se zavolá jejich seřazení */
//                        if(!nodeFirst.isLeaf()) sortTree(nodeFirst);
//                        if(!nodeSecond.isLeaf()) sortTree(nodeSecond);
//                    }
//                }
//            }
//            
//            model.reload(root);
//            LOGGER.debug("Konec řazení potomků v uzlu: " + root);
//        }

        /**
         * Metoda změní název zvolené kategorie.
         *
         * @param editedCategory změněná kategorie
         * @return Editovaný objekt.
         */
        public DefaultMutableTreeNode editNodeObject(Node editedCategory) {
            DefaultMutableTreeNode currentNode = null;

            TreePath currentSelection = tree.getSelectionPath();

            if(currentSelection != null) {
                /* nutné pro obnovení názvu receptu v seznamu */
                currentNode = (DefaultMutableTreeNode)(currentSelection.getLastPathComponent());
                
                /* pakliže editovanou kategorii vkládáme do kategorie, která je součástí
                 právě editované kategorie, je nutné, aby prvním potomkům právě editované
                 kategorie bylo správně nastavené id předka. */
                if(checkSameBranch(currentNode, categoryMap.get(editedCategory.getIdParent()))) {
                    if(databaseOperations.updateCategory(FileOperations.getUserDatabaseFile(), editedCategory)) {
                        int idParentToSet = 0;
                        DefaultMutableTreeNode parentMutableTreeNode = (DefaultMutableTreeNode)
                                currentNode.getParent();

                        /* pokud není předek root, nastaví se do proměnné idParent id předka,
                        jinak zůstane idParent roven 0, což je id ROOTU */
                        if(!parentMutableTreeNode.isRoot()) {
                            Node parentCategory = (Node)parentMutableTreeNode.getUserObject();
                            idParentToSet = parentCategory.getIdNode();
                        }
                        DefaultMutableTreeNode newParent = categoryMap.get(editedCategory.getIdParent());
                        Node newParentNode = (Node)newParent.getUserObject();

                        newParentNode.setIdParent(idParentToSet);

                        if(databaseOperations.updateCategory(FileOperations.getUserDatabaseFile(), newParentNode)) {

                            needReorganizeOrRemoveList.clear();

                            /* přidání aktuální kliknuté kategorie do seznamu pro smazání, bez
                            * ohledu na potomky */
                            needReorganizeOrRemoveList.add(getClickedTreeNode());
                            categoryMap.remove(editedCategory.getIdNode());

                            /* pokud není kliknutý uzel list, je nutné projít veškeré jeho
                            * potomky a taktéž je přidat do seznamu pro přeuspořádání */
                            if (!getClickedTreeNode().isLeaf()) {
                                Enumeration<DefaultMutableTreeNode> children = getClickedTreeNode().children();

                                /* procházíme všechny potomky kliknutého úzlu */
                                while (children.hasMoreElements()) {
                                    DefaultMutableTreeNode child = children.nextElement();

                                    needReorganizeOrRemoveList.add(child);
                                    if(child.getUserObject() instanceof Node) {
                                        Node n = (Node)child.getUserObject();
                                        categoryMap.remove(n.getIdNode());
                                    }

                                    /* pokud je potomek kategorie a zároveň není list (má
                                    * potomky), zavolá se metoda pro kontrolu potomků */
                                    if (child.getUserObject() instanceof Node && !child.isLeaf()) {
                                        checkExistAnotherChildren(true, child.children());
                                    }
                                }
                            }
                            
                            ArrayList<DefaultMutableTreeNode> tempNodes = new ArrayList<DefaultMutableTreeNode>(needReorganizeOrRemoveList);
                            
                            int categoryCount = 0;
                            for(DefaultMutableTreeNode defaultNode : tempNodes) {
                                if(defaultNode.getUserObject() instanceof Node)
                                    categoryCount++;
                            }
                            
                            while(categoryCount!=0) {
                                for(Iterator<DefaultMutableTreeNode> iterator = 
                                        tempNodes.iterator(); iterator.hasNext(); ) {
                                    DefaultMutableTreeNode defaultTreeNode = iterator.next();
                                    
                                    if(defaultTreeNode.getUserObject() instanceof Node) {
                                        Node node = (Node)defaultTreeNode.getUserObject();

                                        if(node.getIdParent()==0) {
                                            MyTree.addObjectFromDatabase(node);
                                            LOGGER.debug("Do stromu byla přidána kategorie: " + node);
                                            iterator.remove();
                                            categoryCount--;
                                        } else {
                                            if(MyTree.getCategoryMap().get(node.getIdParent())!=null) {
                                                MyTree.addObjectFromDatabase(node);
                                                LOGGER.debug("Do stromu byla přidána kategorie: " + node);
                                                iterator.remove();
                                                categoryCount--;
                                            }
                                        }
                                    }
                                }
                            }

                            for(DefaultMutableTreeNode node : needReorganizeOrRemoveList) {
                                if(node.getUserObject() instanceof Recipe) {
                                    Recipe r = (Recipe)node.getUserObject();
                                    addObjectFromDatabase(r);
                                }
                            }

                            removeNodeFromParent(currentNode);

                            Node editetNode = (Node)needReorganizeOrRemoveList.get(0).getUserObject();

                            this.reload(categoryMap.get(editetNode.getIdNode()));

                            return categoryMap.get(editetNode.getIdNode());                        
                        }
                    }
                } else {
                    if(databaseOperations.updateCategory(FileOperations.getUserDatabaseFile(), editedCategory)) {

                        Node editetNode = (Node)currentNode.getUserObject();

                        if(CategoryDialog.changeCategory) {
                            needReorganizeOrRemoveList.clear();

                            /* přidání aktuální kliknuté kategorie do seznamu pro smazání, bez
                            * ohledu na potomky */
                            needReorganizeOrRemoveList.add(getClickedTreeNode());

                            /* pokud není kliknutý uzel list, je nutné projít veškeré jeho
                            * potomky a taktéž je přidat do seznamu pro přeuspořádání */
                            if (!getClickedTreeNode().isLeaf()) {
                                Enumeration<DefaultMutableTreeNode> children = getClickedTreeNode().children();

                                /* procházíme všechny potomky kliknutého úzlu */
                                while (children.hasMoreElements()) {
                                    DefaultMutableTreeNode child = children.nextElement();

                                    needReorganizeOrRemoveList.add(child);

                                    /* pokud je potomek kategorie a zároveň není list (má
                                    * potomky), zavolá se metoda pro kontrolu potomků */
                                    if (child.getUserObject() instanceof Node && !child.isLeaf()) {
                                        checkExistAnotherChildren(false, child.children());
                                    }
                                }
                            }

                            for(DefaultMutableTreeNode node : needReorganizeOrRemoveList) {
                                if(node.getUserObject() instanceof Node) {
                                    Node n = (Node)node.getUserObject();
                                    addObjectFromDatabase(n);
                                }
                            }

                            for(DefaultMutableTreeNode node : needReorganizeOrRemoveList) {
                                if(node.getUserObject() instanceof Recipe) {
                                    Recipe r = (Recipe)node.getUserObject();
                                    addObjectFromDatabase(r);
                                }
                            }

                            removeNodeFromParent(currentNode);

                            editetNode = (Node)needReorganizeOrRemoveList.get(0).getUserObject();
                        }

                        this.reload(categoryMap.get(editetNode.getIdNode()));

                        return categoryMap.get(editetNode.getIdNode());
                    }
                }
            }
            
            return null;
        }
        
        /**
         * Metoda se volá rekurzivně pro zjištění zda-li chceme vložit kategorii 
         * do kategorie, která je potomkem původní kategorie.
         * @param sameBranchList Seznam všech indexů do kterého se budou vkládat
         * nové indexy pozic.
         * @param currentNode Uzel, ve kterém se bude hledat nová rodičovská kategorie.
         * @param newParentNode Rodičovská kategorie-kategorie do které chceme 
         * editovanou kategorii vložit.
         */
        private void checkAnotherSameBranch(ArrayList<Integer> sameBranchList, 
                DefaultMutableTreeNode currentNode, DefaultMutableTreeNode newParentNode) {
            
            Enumeration<DefaultMutableTreeNode> children = currentNode.children();
            while(children.hasMoreElements()) {
                DefaultMutableTreeNode child = children.nextElement();
                
                sameBranchList.add(currentNode.getIndex(newParentNode));
                if(!child.isLeaf()) checkAnotherSameBranch(sameBranchList, child, newParentNode);
            }
        }
        
        /**
         * Metoda zjistí, zda-li chceme vložit kategorii do kategorie, která je 
         * potomkem původní kategorie.
         * @param currentNode Editovaná kategorie.
         * @param newParentNode Kategorie, do které chceme editovanou kategorii vložit.
         * @return Je kategorie, do které chceme editovanou kategorii vložit 
         * její potomek?
         */
        private boolean checkSameBranch(DefaultMutableTreeNode currentNode, DefaultMutableTreeNode newParentNode) {
            /* je-li newParentNode roven null, znamená to, že parent je kořen stromu (root) */
            if(newParentNode==null) newParentNode = (DefaultMutableTreeNode)root;
            
            ArrayList<Integer> sameBranchList = new ArrayList<Integer>();
            
            /* je-li současný uzel kořen, vracíme okamžitě false */
            if(currentNode.isLeaf()) return false;
            else {
                /* jinak procházíme všechny potomky  */
                Enumeration<DefaultMutableTreeNode> children = currentNode.children();
                while(children.hasMoreElements()) {
                    DefaultMutableTreeNode child = children.nextElement();
                    
                    /* vždy přidáme index hledaného úzlu do seznamu */
                    sameBranchList.add(currentNode.getIndex(newParentNode));
                    
                    /* pokud potomek není uzel, musíme projít i jeho potomky */
                    if(!child.isLeaf()) checkAnotherSameBranch(sameBranchList, child, newParentNode);
                }
            }
            
            /* procházíme všechny indexy a pakliže je nějaký jiný než -1
             vracíme true - tedy editovanou kategorii chceme vložit do kategorie,
             která je podkategorií editované kategorie :D */
            for(int i : sameBranchList) {
//                System.out.println("lastIndexOf: " +i);
                if(i!=-1) return true;
            }
            
            return false;
        }

        /**
         * Metoda změní vybraný recept.
         *
         * @param recipe změněný recept.
         * @return editovaný objekt.
         */
        public DefaultMutableTreeNode editRecipeObject(Recipe recipe) {
            DefaultMutableTreeNode currentNode = null;

            TreePath currentSelection = tree.getSelectionPath();

            if (currentSelection != null) {
                if (databaseOperations.updateRecipe(FileOperations.getUserDatabaseFile(), recipe)) {
                    /* nutné pro obnovení názvu receptu v seznamu */
                    currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
                    if(RecipeDialog.changeCategory) {
//                        currentNode.setParent(categoryMap.get(recipe.getIdCategry()));
                        currentNode.removeFromParent();
                        recipeMap.remove(recipe.getIdRecipe());
                        addObjectFromDatabase(recipe);
                    }

                    this.reload(currentNode);
                    this.reload(recipeMap.get(recipe.getIdRecipe()));
                    
                    return recipeMap.get(recipe.getIdRecipe());
//                    return currentNode;
                }
            }
            
            return null;
        }

        /**
         * Metoda vymaze ze stromu daný recept.
         */
        public void removeRecipe() {
            
            if (databaseOperations.removeRecipeById(
                    FileOperations.getUserDatabaseFile(), getCurrentRecipe().getIdRecipe())) {
                recipeMap.remove(getCurrentRecipe().getIdRecipe());
                removeNodeFromParent(clickedTreeNode);
                reloadVariable();
            }
        }

        /**
         * Metoda vymaze ze stromu danou kategorii bez jejich podkategorií a
         * receptů, ty budou přesunuty do nadřazené kategorie.
         */
        public void removeOnlyCategory() {
            DefaultMutableTreeNode parentdeDefaultMutableTreeNode =
                    (DefaultMutableTreeNode) getClickedTreeNode().getParent();
            int parentId = 0; // parent je ROOT
            boolean allDone = false;
            needReorganizeOrRemoveList.clear();

            /* pokud parent není root, nastaví se parentovo ID do proměnné */
            if (!parentdeDefaultMutableTreeNode.isRoot()) {
                Node parentNode = (Node) parentdeDefaultMutableTreeNode.getUserObject();
                parentId = parentNode.getIdNode();
            }

            /* pokud je kliknutý uzel list, pokračuje se dále */
            if (getClickedTreeNode().isLeaf()) {
                allDone = true;
            } else {
                Enumeration<DefaultMutableTreeNode> children = getClickedTreeNode().children();

                /* procházíme všechny potomky kliknutého úzlu */
                while (children.hasMoreElements()) {
                    DefaultMutableTreeNode treeNode = children.nextElement();

                    if (treeNode.getUserObject() instanceof Node) {
                        
                        /* jelikož jsme v prvním potomkovi kliknutého úzlu, je
                         nutné nastavit tomuto potomkovi idPředka na idPředka
                         kliknutého úzlu a provést tuto změnu v databázi */
                        Node nodeToUpdate = (Node) treeNode.getUserObject();
                        nodeToUpdate.setIdParent(parentId);
                        allDone = databaseOperations.updateCategory(FileOperations.getUserDatabaseFile(), nodeToUpdate);
                        
                        /* pakliže potomek není list - jsou v něm ještě další úzly,
                         je nutné je přidat do seznamu úzlů, které mají být přeorganizovány */
                        if (!treeNode.isLeaf()) {
                            needReorganizeOrRemoveList.add(treeNode);
                            checkExistAnotherChildren(false, treeNode.children());
                        } else {
                            needReorganizeOrRemoveList.add(treeNode);
                        }
                    } else if (treeNode.getUserObject() instanceof Recipe) {
                        
                        /* recept nemůže mít potomka, proto pouze změníme jeho idPředka
                         a přidáme do seznamu úzlů, které se mají přeorganizovat */
                        Recipe recipeToUpdate = (Recipe) treeNode.getUserObject();
                        recipeToUpdate.setIdCategory(parentId);
                        allDone = databaseOperations.updateRecipe(FileOperations.getUserDatabaseFile(), recipeToUpdate);
                        needReorganizeOrRemoveList.add(treeNode);
                    }
                }
            }

            /* prošly-li databázové operace v pořádku, pokračujeme vymazáním kliknutého
             úzlu z databáze */
            if (allDone) {
                allDone = databaseOperations.removeCategoryById(
                        FileOperations.getUserDatabaseFile(), getCurrentCategory().getIdNode());

                /* pokud vymazání kliknutého úzlu proběhlo korektně vymažeme úzel
                 ze stromu a provedeme přeorganizování úzlů, které si to žádají */
                if (allDone) {
                    int idToDelete = ((Node)getClickedTreeNode().getUserObject()).getIdNode();
                    removeNodeFromParent(getClickedTreeNode());
                    reloadVariable();

                    /* nejprve nutno přeorganizovat kategorie */
                    for (DefaultMutableTreeNode treeNode : needReorganizeOrRemoveList) {
                        if (treeNode.getUserObject() instanceof Node) {
                            Node n = (Node) treeNode.getUserObject();
                            categoryMap.remove(n.getIdNode());
                            addObjectFromDatabase(treeNode.getUserObject());
                        }
                    }

                    /* poté přeorganizujeme recepty */
                    for (DefaultMutableTreeNode treeNode : needReorganizeOrRemoveList) {
                        if (treeNode.getUserObject() instanceof Recipe) {
                            Recipe r = (Recipe) treeNode.getUserObject();
                            recipeMap.remove(r.getIdRecipe());
                            addObjectFromDatabase(treeNode.getUserObject());
                        }
                    }
                    
                    categoryMap.remove(idToDelete);
                }
            }
        }

        /**
         * Metoda, která se volá rekurzivně pakliže má uzel další potomky. Tito
         * potomky je také nutné přeorganizovat.
         *
         * @param children Výčet potomků, kteří se budou prohledávat a testovat,
         * zda-li mají další potomky.
         */
        public void checkExistAnotherChildren(boolean needDeleteNodeFromMap, Enumeration<DefaultMutableTreeNode> children) {
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode child = children.nextElement();

                needReorganizeOrRemoveList.add(child);
                
                if(needDeleteNodeFromMap) {
                    if(child.getUserObject() instanceof Node) {
                        Node n = (Node)child.getUserObject();
                        categoryMap.remove(n.getIdNode());
                    }
                }

                /*
                 * pokud je potomek kategorie a zároveň není list (má potomky),
                 * zavolá se REKURZIVNĚ metoda pro kontrolu potomků
                 */
                if (child.getUserObject() instanceof Node && !child.isLeaf()) {
                    checkExistAnotherChildren(needDeleteNodeFromMap, child.children());
                }
            }
        }

        /**
         * Metoda vymaze ze stromu danou kategorii včetně všech podkategorií a
         * receptů.
         */
        public void removeAllCategory() {
            needReorganizeOrRemoveList.clear();

            /* přidání aktuální kliknuté kategorie do seznamu pro smazání, bez
             * ohledu na potomky */
            needReorganizeOrRemoveList.add(getClickedTreeNode());

            /* pokud není kliknutý uzel list, je nutné projít veškeré jeho
             * potomky a taktéž je přidat do seznamu pro vymazání */
            if (!getClickedTreeNode().isLeaf()) {
                Enumeration<DefaultMutableTreeNode> children = getClickedTreeNode().children();

                /* procházíme všechny potomky kliknutého úzlu */
                while (children.hasMoreElements()) {
                    DefaultMutableTreeNode child = children.nextElement();

                    needReorganizeOrRemoveList.add(child);

                    /* pokud je potomek kategorie a zároveň není list (má
                     * potomky), zavolá se metoda pro kontrolu potomků */
                    if (child.getUserObject() instanceof Node && !child.isLeaf()) {
                        checkExistAnotherChildren(false, child.children());
                    }
                }
            }

            /*
             * nejprve vymažeme recepty
             */
            for (DefaultMutableTreeNode treeNode : needReorganizeOrRemoveList) {
                if (treeNode.getUserObject() instanceof Recipe) {
                    Recipe r = (Recipe) treeNode.getUserObject();
                    databaseOperations.removeRecipeById(
                            FileOperations.getUserDatabaseFile(),
                            r.getIdRecipe());
                    recipeMap.remove(r.getIdRecipe());
                }
            }

            /*
             * poté je nutné vymazat kategorie
             */
            for (DefaultMutableTreeNode treeNode : needReorganizeOrRemoveList) {
                if (treeNode.getUserObject() instanceof Node) {
                    Node n = (Node) treeNode.getUserObject();
                    databaseOperations.removeCategoryById(
                            FileOperations.getUserDatabaseFile(),
                            n.getIdNode());
                    categoryMap.remove(n.getIdNode());
                }
            }

            removeNodeFromParent(getClickedTreeNode());
            reloadVariable();
        }

        /**
         * Metoda se volá po kliknutí na nějaký uzel ve stromu. Na základě
         * aktuálně kliknutého uzlu metoda zvolí správnou kategorii a recept a
         * nastaví je do určených proměnných.
         */
        public void reloadVariable() {
            DefaultMutableTreeNode currentNode = null;
            DefaultMutableTreeNode parentNode = null;

            TreePath currentSelection = tree.getSelectionPath();

            if (currentSelection != null) {
                currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
                setClickedTreeNode(currentNode);
                if (currentNode.isRoot()) {
                    parentNode = currentNode;
                    instanceMyToolbar.enableIconDelete(false);
                    instanceMyToolbar.enableIconEdit(false);
                    instanceMyToolbar.enableIconPrint(false);
                    
                    instanceMyToolbar.setToolTipBtnDelete(RESOURCE_BUNDLE.getString("Toolbar.btnDelete.tooltip"));
                    instanceMyToolbar.setToolTipBtnEdit(RESOURCE_BUNDLE.getString("Toolbar.btnEdit.tooltip"));
                    instanceMyToolbar.setToolTipBtnPrint(RESOURCE_BUNDLE.getString("Toolbar.btnPrint.tooltip"));
                } else {
                    parentNode = (DefaultMutableTreeNode) (currentSelection.getParentPath().getLastPathComponent());
                    instanceMyToolbar.enableIconDelete(true);
                    instanceMyToolbar.enableIconEdit(true);
                    instanceMyToolbar.enableIconPrint(false);
                }

                if (currentNode.getUserObject() instanceof Node) {
                    setCurrentCategory((Node) currentNode.getUserObject());
                    instanceMyToolbar.setToolTipBtnPrint(RESOURCE_BUNDLE.getString("Toolbar.btnPrint.tooltip"));
                    instanceMyToolbar.setToolTipBtnDelete(RESOURCE_BUNDLE.getString("Toolbar.btnDeleteCategory.tooltip"));
                    instanceMyToolbar.setToolTipBtnEdit(RESOURCE_BUNDLE.getString("Toolbar.btnEditCategory.tooltip"));
                } else if (currentNode.getUserObject() instanceof Recipe) {
                    instanceMyToolbar.enableIconPrint(true);
                    instanceMyToolbar.setToolTipBtnPrint(RESOURCE_BUNDLE.getString("Toolbar.btnPrintRecipe.tooltip"));
                    instanceMyToolbar.setToolTipBtnDelete(RESOURCE_BUNDLE.getString("Toolbar.btnDeleteRecipe.tooltip"));
                    instanceMyToolbar.setToolTipBtnEdit(RESOURCE_BUNDLE.getString("Toolbar.btnEditRecipe.tooltip"));
                                        
                    Recipe r = (Recipe) currentNode.getUserObject();
                    if(r.getRecipeImage()==null) {
                        LOGGER.debug("Začátek získávání obrázku z databáze pro recept '" + r.getTitle() + "'.");
                        MainFrame.getStatusPanel().setAnimationIcon(Icons.TASK_BUSY);
                        databaseOperations.insertImageToRecipeById(FileOperations.getUserDatabaseFile(), r);
                        LOGGER.debug("Konec získávání obrázku z databáze pro recept '" + r.getTitle() + "'.");
                        MainFrame.getStatusPanel().setAnimationIcon(Icons.TASK_IDLE);
                    }
                    setCurrentRecipe(r);
                    if (r.getIdCategry() != 0) {
                        setCurrentCategory((Node) categoryMap.get(r.getIdCategry()).getUserObject());
                    } else {
                        setCurrentCategory(null);
                    }

                    if (!parentNode.isRoot()) {
                        setCurrentCategory((Node) parentNode.getUserObject());
                    }
                } else if (currentNode.isRoot()) {
                    setCurrentCategory(new Node(currentNode.getUserObject().toString(), 0, 0));
                }
            } else {
                setClickedTreeNode(null);
                setCurrentRecipe(null);
                setCurrentCategory(null);

                instanceMyToolbar.enableIconDelete(false);
                instanceMyToolbar.enableIconEdit(false);
                instanceMyToolbar.enableIconPrint(false);
            }

//            System.out.println("currentRecipe " + currentRecipe);
//            System.out.println("currentCategory " + currentCategory);
//            System.out.println("clickedNode " + clickedTreeNode);
        }
        
        /**
         * Metoda změní název databáze ve stromu receptů.
         * @param newName Nové jméno databáze
         */
        public void renameRoot(String newName) {
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)getRoot();
            rootNode.setUserObject(newName);
            this.reload();
        }

//        /**
//         * Metoda zjistí veškeré kategorie i s podkategoriema.
//         * Použití pro editaci receptu/kategorie pro změnu kategorie.
//         * @return pole kategorií. Pakliže nějaká kategorie má podkategorie,
//         * je k názvu přidán prefix "- ".
//         */
//        private Node[] getAllCategories() {
//            ArrayList<Node> alNodes = new ArrayList<Node>();
//
//            Enumeration<DefaultMutableTreeNode> children = root.children();
//
//            /* nutno přidát roota */
//            alNodes.add(new Node(root.toString(), 0, -1));
//            
//            while (children.hasMoreElements()) {
//                DefaultMutableTreeNode child = children.nextElement();
//
//                if (child.getUserObject() instanceof Node) {
//                    Node n = (Node)child.getUserObject();
//                    String prefix = "- ";
//                        alNodes.add(new Node(prefix+n.getTitle(), n.getIdNode(), n.getIdParent()));
//
//                    if(!child.isLeaf())
//                        checkExistAnotherCatrgories(child.children(), alNodes, prefix+"- ");
//                }
//            }
//
//            Node[] retNodes = new Node[alNodes.size()];
//            for (int i = 0; i < alNodes.size(); i++) {
//                retNodes[i] = alNodes.get(i);
//            }
//
//            return retNodes;
//        }
//        
//        /**
//         * Metoda zjistí, zda-li předek (kategorie) má další potomky (kategorie) 
//         * a přidá je do seznamu kategorií
//         * @param children
//         * @param alNodes
//         * @param prefix 
//         */
//        private void checkExistAnotherCatrgories(Enumeration<DefaultMutableTreeNode> children, 
//                ArrayList<Node> alNodes, String prefix) {
//            
//            while (children.hasMoreElements()) {
//                DefaultMutableTreeNode child = children.nextElement();
//
//                if (child.getUserObject() instanceof Node) {
//                    Node n = (Node)child.getUserObject();
//                        alNodes.add(new Node(prefix+n.getTitle(), n.getIdNode(), n.getIdParent()));
//                    
//                    if(!child.isLeaf())
//                        checkExistAnotherCatrgories(child.children(), alNodes, prefix+"- ");
//                }
//            }
//        }
    }

    /**
     * Třída, díky které je možné nastavit ikony uzlů ve stromu.
     */
    private class MyRenderer extends DefaultTreeCellRenderer {

        ImageIcon categoryIcon;
        ImageIcon recipeIcon;
        ImageIcon rootIcon;

        public MyRenderer() {
            categoryIcon = Icons.TREE_CATEGORY;
            recipeIcon = Icons.TREE_RECIPE;
            rootIcon = Icons.TREE_ROOT;
        }

        @Override
        public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {

            super.getTreeCellRendererComponent(
                    tree, value, sel,
                    expanded, leaf, row,
                    hasFocus);

            if(isRoot(value)) setIcon(rootIcon);
            else if (isCategory(value)) {
                setIcon(categoryIcon);
//                setToolTipText("This book is in the Tutorial series.");
            } else {
                setIcon(recipeIcon);
            }

            return this;
        }

        /**
         * Metoda zjistí, zda-li je uzel stromu kategorie, nebo recept.
         * @param value uzel stromu
         * @return zda-li je uzel recept
         */
        protected boolean isCategory(Object value) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)value;

            if (node.getUserObject() instanceof Node || node.isRoot()) {
                return true;
            } else {
                return false;
            }
        }
        
        /**
         * Metoda zjistí, zda-li je uzel stromu kořen.
         * @param value uzel stromu.
         * @return zda-li je kořen stromu
         */
        protected boolean isRoot(Object value) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)value;
            
            return node.isRoot();
        }
    }
}
