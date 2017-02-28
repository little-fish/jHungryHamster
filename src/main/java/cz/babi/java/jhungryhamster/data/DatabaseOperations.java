
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

package cz.babi.java.jhungryhamster.data;

import cz.babi.java.jhungryhamster.entity.CookBook;
import cz.babi.java.jhungryhamster.entity.CookBook.Recipe;
import cz.babi.java.jhungryhamster.entity.TreeNodes;
import cz.babi.java.jhungryhamster.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Třída starajícíse o práci s databází.
 * 
 * @author babi
 */
public class DatabaseOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseOperations.class);
    
    private static DatabaseOperations instance = new DatabaseOperations();
    
    private final String TABLE_CATEGORY = "category";
    private final String TABLE_RECIPE = "recipe";
    
    private final String INDEX_RECIPE_ID = "index_recipe_recipeId";
    private final String INDEX_RECIPE_ID_CATEGORY = "index_recipe_categoryId";
    private final String INDEX_RECIPE_TITLE = "index_recipe_title";
    private final String INDEX_RECIPE_AUTHOR = "index_recipe_author";
    private final String INDEX_RECIPE_INGREDIENTS = "index_recipe_ingredients";
    private final String INDEX_RECIPE_METHOD = "index_recipe_method";
    private final String INDEX_CATEGORY_ID_CATEGORY = "index_category_id";
    private final String INDEX_CATEGORY_NAME = "index_category_name";
    private final String INDEX_CATEGORY_ID_PARENT = "index_category_idParent";
    
    private final String FIELD_CATEGORY_ID_CATEGORY = "id_category";
    private final String FIELD_CATEGORY_ID_PARENT = "id_parent";
    private final String FIELD_CATEGORY_NAME = "name";
    
    private final String FIELD_RECIPE_ID_RECIPE = "id_recipe";
    private final String FIELD_RECIPE_ID_CATEGORY = "id_category";
    private final String FIELD_RECIPE_TITLE = "title";
    private final String FIELD_RECIPE_AUTHOR = "author";
    private final String FIELD_RECIPE_RATING = "rating";
    private final String FIELD_RECIPE_ADDED = "added";
    private final String FIELD_RECIPE_MODIFIED = "modified";
    private final String FIELD_RECIPE_NOTE = "note";
    private final String FIELD_RECIPE_INGREDIENTS = "ingredients";
    private final String FIELD_RECIPE_METHOD = "method";
    private final String FIELD_RECIPE_IMAGE = "image";
    
    private final String SQL_TABLE_CATEGORY = "CREATE  TABLE " + TABLE_CATEGORY + " ("
                + FIELD_CATEGORY_ID_CATEGORY + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + FIELD_CATEGORY_ID_PARENT + " INTEGER NOT NULL , "
                + FIELD_CATEGORY_NAME + " TEXT NOT NULL )";
    
    private final String SQL_TABLE_RECIPE = "CREATE TABLE " + TABLE_RECIPE + " ("
                + FIELD_RECIPE_ID_RECIPE + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + FIELD_RECIPE_ID_CATEGORY + " INTEGER NOT NULL , "
                + FIELD_RECIPE_TITLE + " TEXT NOT NULL , "
                + FIELD_RECIPE_AUTHOR + " TEXT, "
                + FIELD_RECIPE_RATING + " INTEGER, "
                + FIELD_RECIPE_ADDED + " DATETIME NOT NULL , "
                + FIELD_RECIPE_MODIFIED + " DATETIME NOT NULL , "
                + FIELD_RECIPE_NOTE + " TEXT, "
                + FIELD_RECIPE_INGREDIENTS + " TEXT, "
                + FIELD_RECIPE_METHOD + " TEXT, "
                + FIELD_RECIPE_IMAGE + " BLOB"
                + ", FOREIGN KEY(" + FIELD_RECIPE_ID_CATEGORY + ") "
                    + "REFERENCES " + TABLE_CATEGORY + "(" + FIELD_CATEGORY_ID_CATEGORY + ")"
                + " )";
    
    private final String SQL_INDEX_RECIPE_ID = "CREATE INDEX "
            + INDEX_RECIPE_ID + " ON " + TABLE_RECIPE + " (" + FIELD_RECIPE_ID_RECIPE + ")";
    
    private final String SQL_INDEX_RECIPE_ID_CATEGOTY = "CREATE INDEX "
            + INDEX_RECIPE_ID_CATEGORY + " ON " + TABLE_RECIPE + " (" + FIELD_RECIPE_ID_CATEGORY + ")";
    
    private final String SQL_INDEX_RECIPE_TITLE = "CREATE INDEX "
            + INDEX_RECIPE_TITLE + " ON " + TABLE_RECIPE + " (" + FIELD_RECIPE_TITLE + ")";
    
    private final String SQL_INDEX_RECIPE_AUTHOR = "CREATE INDEX "
            + INDEX_RECIPE_AUTHOR + " ON " + TABLE_RECIPE + " (" + FIELD_RECIPE_AUTHOR + ")";
    
    private final String SQL_INDEX_RECIPE_INGREDIENTS = "CREATE INDEX "
            + INDEX_RECIPE_INGREDIENTS + " ON " + TABLE_RECIPE + " (" + FIELD_RECIPE_INGREDIENTS + ")";
    
    private final String SQL_INDEX_RECIPE_METHOD = "CREATE INDEX "
            + INDEX_RECIPE_METHOD + " ON " + TABLE_RECIPE + " (" + FIELD_RECIPE_METHOD + ")";
    
    private final String SQL_INDEX_CATEGORY_ID_CATEGORY = "CREATE INDEX "
            + INDEX_CATEGORY_ID_CATEGORY + " ON " + TABLE_CATEGORY + " (" + FIELD_CATEGORY_ID_CATEGORY + ")";
    
    private final String SQL_INDEX_CATEGORY_NAME = "CREATE INDEX "
            + INDEX_CATEGORY_NAME + " ON " + TABLE_CATEGORY + " (" + FIELD_CATEGORY_NAME + ")";
    
    private final String SQL_INDEX_CATEGORY_ID_PARENT = "CREATE INDEX "
            + INDEX_CATEGORY_ID_PARENT + " ON " + TABLE_CATEGORY + " (" + FIELD_CATEGORY_ID_PARENT + ")";
        
    /**
     * Privátní konstruktor třídy FileOperations.
     */
    private DatabaseOperations() {}
    
    /**
     * Metoda pro získání instance třídy DatabaseOperations.
     * Použitý návrhový vzor - <b>Singleton</b>.
     * 
     * @return Instance třídy DatabaseOperations
     */
    public static DatabaseOperations getInstance() {
        return instance;
    }
    
    /**
     * Metoda se pokusí zjistit, zda-li je otevíraná databáze v pořádku.
     * Zjednodušeně se pouze pokusí otevřít potřebné tabulky a kurzory.
     * @param databaseFile Databázový soubor, který se má zkontrolovat
     * @return zda-li je databáze v pořádku.
     */
    public boolean checkDatabase(File databaseFile) {
        
        boolean allDone = true;
        
        SqlJetDb database = null;
        try {
            database = SqlJetDb.open(databaseFile, true);
            
            ISqlJetTable tableCategory = database.getTable(TABLE_CATEGORY);
            ISqlJetTable tableRecipe = database.getTable(TABLE_RECIPE);
            
            database.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            
            /* vytvoření kurzoru na procházení řádky z databáze */
            ISqlJetCursor cursorCategory = tableCategory.order(INDEX_CATEGORY_ID_PARENT);
            ISqlJetCursor cursorRecipe = tableRecipe.order(INDEX_RECIPE_ID_CATEGORY);
            
            cursorCategory.close();
            cursorRecipe.close();
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while reading data from the database.", ex);
            allDone = false;
        } finally {
            if(database!=null) {
                try {
                    database.close();
                } catch (SqlJetException ex) {
                    LOGGER.error("Unexpected error while closing the database.", ex);
                    allDone = false;
                }
            }
        }
        
        return allDone;
    }
    
    /**
     * Metoda vytvoří novou databázi a naplní ji potřebnýma tabulkama.
     * @param userDatabaseFile Soubor, kde má být nová databáze vytvořena.
     */
    public void createNewDatabase(File userDatabaseFile) {
        SqlJetDb database = null;
        
        try {
            database = SqlJetDb.open(userDatabaseFile, true);
            
            // set DB option that have to be set before running any transactions: 
            database.getOptions().setAutovacuum(true);
            
            // set DB option that have to be set in a transaction: 
            database.runTransaction(new ISqlJetTransaction() {
                @Override
                public Object run(SqlJetDb db) throws SqlJetException {
                    db.getOptions().setUserVersion(1);
                    return true;
                }
            }, SqlJetTransactionMode.WRITE);
            
            database.beginTransaction(SqlJetTransactionMode.WRITE);
            
            database.createTable(SQL_TABLE_CATEGORY);
            database.createTable(SQL_TABLE_RECIPE);
            database.createIndex(SQL_INDEX_RECIPE_ID);
            database.createIndex(SQL_INDEX_RECIPE_ID_CATEGOTY);
            database.createIndex(SQL_INDEX_RECIPE_TITLE);
            database.createIndex(SQL_INDEX_RECIPE_AUTHOR);
            database.createIndex(SQL_INDEX_RECIPE_METHOD);
            database.createIndex(SQL_INDEX_RECIPE_INGREDIENTS);
            database.createIndex(SQL_INDEX_CATEGORY_ID_CATEGORY);
            database.createIndex(SQL_INDEX_CATEGORY_NAME);
            database.createIndex(SQL_INDEX_CATEGORY_ID_PARENT);
            
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while creating new database.", ex);
        } finally {
            if(database!=null) {
                try {
                    database.commit();
                    database.close();
                } catch (SqlJetException ex) {
                    LOGGER.error("Unexpected error while closing the database.", ex);
                }
            }
        }
    }
    
    /**
     * Metoda načte veškerý obsah z databáze (kategorie a poté recepty)
     * @param databaseFile Vstupní databázový soubor.
     * @return True/False zda-li vše proběhlo v pořádku.
     */
    public boolean readDataFromDatabase(File databaseFile) {
        
        boolean allDone = true;
        
        TreeNodes treeNodes = TreeNodes.getInstance();
        CookBook cookBook = CookBook.getInstance();
        
        if(treeNodes.getNodes()!=null) treeNodes.getNodes().clear();
        if(cookBook.getRecipes()!=null) cookBook.removeAllRecipes();
        
        SqlJetDb database = null;
        try {
            database = SqlJetDb.open(databaseFile, true);
            
            ISqlJetTable tableCategory = database.getTable(TABLE_CATEGORY);
            ISqlJetTable tableRecipe = database.getTable(TABLE_RECIPE);
            
            database.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            
            /* vytvoření kurzoru na procházení řádky z databáze */
            ISqlJetCursor cursorCategory = tableCategory.order(INDEX_CATEGORY_ID_PARENT);
            ISqlJetCursor cursorRecipe = tableRecipe.order(INDEX_RECIPE_ID_CATEGORY);
            
            try {
                if (!cursorCategory.eof()) {
                    do {
                        treeNodes.addNode(new TreeNodes.Node(
                                cursorCategory.getString(FIELD_CATEGORY_NAME),
                                (int)cursorCategory.getInteger(FIELD_CATEGORY_ID_CATEGORY),
                                (int)cursorCategory.getInteger(FIELD_CATEGORY_ID_PARENT)));
                    } while(cursorCategory.next());
                }
            } finally {
                cursorCategory.close();
            }
            
            try {
                if (!cursorRecipe.eof()) {
                    do {
                        cookBook.addRecipe(new CookBook.Recipe(
                                (int)cursorRecipe.getInteger(FIELD_RECIPE_ID_RECIPE),
                                (int)cursorRecipe.getInteger(FIELD_RECIPE_ID_CATEGORY),
                                cursorRecipe.getString(FIELD_RECIPE_TITLE),
                                cursorRecipe.getString(FIELD_RECIPE_AUTHOR),
                                Common.getRatingFromInt(Integer.valueOf(cursorRecipe.getString(FIELD_RECIPE_RATING))),
                                Common.convertStringToDate(cursorRecipe.getString(FIELD_RECIPE_ADDED), Common.DATEFORMAT_DATABASE),
                                Common.convertStringToDate(cursorRecipe.getString(FIELD_RECIPE_MODIFIED), Common.DATEFORMAT_DATABASE),
                                cursorRecipe.getString(FIELD_RECIPE_NOTE),
                                cursorRecipe.getString(FIELD_RECIPE_INGREDIENTS),
                                cursorRecipe.getString(FIELD_RECIPE_METHOD)));    
                    } while(cursorRecipe.next());
                }
            } finally {
                cursorRecipe.close();
            }
            
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while reading data from the database.", ex);
            allDone = false;
        } finally {
            if(database!=null) {
                try {
                    database.commit();
                    database.close();
                } catch (SqlJetException ex) {
                    LOGGER.error("Unexpected error while closing the database.", ex);
                    allDone = false;
                }
            }
        }
        
        return allDone;
    }
    
    /**
     * Metoda uloží do databáze novou kategorii.
     * @param databaseFile Databázový soubor, do kterého má být kategorie uložena.
     * @param category Jméno nové kategorie.
     * @param parentID ID předka.
     * @return ID vloženého řádku (kategorie).
     */
    public long insertNewCategoryIntoDatabase(File databaseFile, TreeNodes.Node category, int parentID) {
        
        long ret = -1;
        SqlJetDb database = null;
        
        try {
            database = SqlJetDb.open(databaseFile, true);
        
            database.beginTransaction(SqlJetTransactionMode.WRITE);
        
            ISqlJetTable table = database.getTable(TABLE_CATEGORY);
        
            ret = table.insert(parentID, category.getTitle());        
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while storing new category.", ex);
        } finally {
            try {
                if(database!=null) {
                    database.commit();
                    database.close();
                }
            } catch (SqlJetException ex) {
                LOGGER.error("Unexpected error while closing the database.", ex);
            }
        }
        
        return ret;
    }
    
    /**
     * Metoda uloží nový recept do dataze.
     * @param databaseFile Databázov soubor.
     * @param recipe Recept pro uložení.
     * @param parentId Id kategorie.
     * @return Id vloženého receptu.
     */
    public long insertNewRecipeIntoDatabase(File databaseFile, Recipe recipe, int parentId) {
        long ret = -1;
        
        Map<String, Object> recipeMap = new HashMap<String, Object>();
        recipeMap.put(FIELD_RECIPE_ID_CATEGORY, parentId);
        recipeMap.put(FIELD_RECIPE_TITLE, recipe.getTitle());
        recipeMap.put(FIELD_RECIPE_AUTHOR, recipe.getAuthor());
        recipeMap.put(FIELD_RECIPE_RATING, recipe.getRating().ordinal());
        recipeMap.put(FIELD_RECIPE_ADDED, Common.convertDateToString(
                Common.DATEFORMAT_DATABASE, recipe.getAdded()));
        recipeMap.put(FIELD_RECIPE_MODIFIED, Common.convertDateToString(
                Common.DATEFORMAT_DATABASE, recipe.getModified()));
        recipeMap.put(FIELD_RECIPE_NOTE, recipe.getNote());
        recipeMap.put(FIELD_RECIPE_INGREDIENTS, recipe.getIngredients());
        recipeMap.put(FIELD_RECIPE_METHOD, recipe.getMethods());
        if (recipe.getRecipeImage() != null) {
            recipeMap.put(FIELD_RECIPE_IMAGE, Common.getByteArrayFromBufferedImage(recipe.getRecipeImage()));
        }
        
        SqlJetDb database = null;
        
        try {
            database = SqlJetDb.open(databaseFile, true);
        
            database.beginTransaction(SqlJetTransactionMode.WRITE);
        
            ISqlJetTable table = database.getTable(TABLE_RECIPE);
        
            ret = table.insertByFieldNames(recipeMap);           
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while storing new recipe.", ex);
        } finally {
            try {
                if(database!=null) {
                    database.commit();
                    database.close();
                }
            } catch (SqlJetException ex) {
                LOGGER.error("Unexpected error while closing the database.", ex);
            }
        }
        
        return ret;
    }
    
    /**
     * Metoda načte z databáze Recept podle jeho ID a uloží do něj obrázek z databáze.
     * @param databaseFile Databázový soubor.
     * @param recipe Recept do kterého se obrázek vloží.
     * @return true/false, zda-li vše proběhlo v pořádku.
     */
    public boolean insertImageToRecipeById(File databaseFile, Recipe recipe) {
        boolean allDone = true;
        
        SqlJetDb database = null;
        
        try {
            database = SqlJetDb.open(databaseFile, true);
        
            database.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        
            ISqlJetTable table = database.getTable(TABLE_RECIPE);
        
            ISqlJetCursor cursorRecipe = table.lookup(INDEX_RECIPE_ID, recipe.getIdRecipe());
            
            if(!cursorRecipe.eof()) {
                recipe.setRecipeImage(Common.getBufferedImageFromInputStream(cursorRecipe.getBlobAsStream(FIELD_RECIPE_IMAGE)));  
            }
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while storing image of the recipe.", ex);
            allDone = false;
        } finally {
            try {
                if(database!=null) {
                    database.commit();
                    database.close();
                }
            } catch (SqlJetException ex) {
                LOGGER.error("Unexpected error while closing the database.", ex);
                allDone = false;
            }
        }
        
        return allDone;
    }
    
    /**
     * Metoda pro úpravu receptu.
     * @param databaseFile Databázový soubor.
     * @param recipe Upravený recept.
     * @return True/False, zda-li vše proběhlo bez problémů.
     */
    public boolean updateRecipe(File databaseFile, Recipe recipe) {
        
        boolean allDone = true;
        
        Map<String, Object> recipeMap = new HashMap<String, Object>();
        recipeMap.put(FIELD_RECIPE_ID_CATEGORY, recipe.getIdCategry());
        recipeMap.put(FIELD_RECIPE_TITLE, recipe.getTitle());
        recipeMap.put(FIELD_RECIPE_AUTHOR, recipe.getAuthor());
        recipeMap.put(FIELD_RECIPE_RATING, recipe.getRating().ordinal());
        recipeMap.put(FIELD_RECIPE_ADDED, Common.convertDateToString(
                Common.DATEFORMAT_DATABASE, recipe.getAdded()));
        recipeMap.put(FIELD_RECIPE_MODIFIED, Common.convertDateToString(
                Common.DATEFORMAT_DATABASE, recipe.getModified()));
        recipeMap.put(FIELD_RECIPE_NOTE, recipe.getNote());
        recipeMap.put(FIELD_RECIPE_INGREDIENTS, recipe.getIngredients());
        recipeMap.put(FIELD_RECIPE_METHOD, recipe.getMethods());
        if(recipe.getRecipeImage()!=null)
            recipeMap.put(FIELD_RECIPE_IMAGE, Common.getByteArrayFromBufferedImage(recipe.getRecipeImage()));
        else recipeMap.put(FIELD_RECIPE_IMAGE, null);
        
        SqlJetDb database = null;
        ISqlJetCursor cursorUpdateRecipe = null;
        
        try {
            database = SqlJetDb.open(databaseFile, true);
        
            database.beginTransaction(SqlJetTransactionMode.WRITE);
        
            ISqlJetTable table = database.getTable(TABLE_RECIPE);
        
            cursorUpdateRecipe = table.lookup(INDEX_RECIPE_ID, recipe.getIdRecipe());
            
            if(!cursorUpdateRecipe.eof()) {
                cursorUpdateRecipe.updateByFieldNames(recipeMap);
            }
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while updating the recipe.", ex);
            allDone = false;
        } finally {
            try {
                if(cursorUpdateRecipe!=null)
                    cursorUpdateRecipe.close();
                if(database!=null) {
                    database.commit();
                    database.close();
                }
            } catch (SqlJetException ex) {
                LOGGER.error("Unexpected error while closing the database.", ex);
                allDone = false;
            }
        }
        
        return allDone;
    }
    
    /**
     * Metoda pro úpravu kategorie.
     * @param databaseFile Databázový soubor.
     * @param category Upravená kategorie.
     * @return True/False, zda-li vše proběhlo bez problémů.
     */
    public boolean updateCategory(File databaseFile, TreeNodes.Node category) {
        
        boolean allDone = true;
        
        Map<String, Object> categoryMap = new HashMap<String, Object>();
        categoryMap.put(FIELD_CATEGORY_ID_PARENT, category.getIdParent());
        categoryMap.put(FIELD_CATEGORY_NAME, category.getTitle());
           
        SqlJetDb database = null;
        ISqlJetCursor cursorUpdateCategory = null;
        
        try {
            database = SqlJetDb.open(databaseFile, true);
        
            database.beginTransaction(SqlJetTransactionMode.WRITE);
        
            ISqlJetTable table = database.getTable(TABLE_CATEGORY);
        
            cursorUpdateCategory = table.lookup(INDEX_CATEGORY_ID_CATEGORY, category.getIdNode());
            
            if(!cursorUpdateCategory.eof()) {
                cursorUpdateCategory.updateByFieldNames(categoryMap);
            }
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while updating the category.", ex);
            allDone = false;
        } finally {
            try {
                if(cursorUpdateCategory!=null)
                    cursorUpdateCategory.close();
                if(database!=null) {
                    database.commit();
                    database.close();
                }
            } catch (SqlJetException ex) {
                LOGGER.error("Unexpected error while closing the database.", ex);
                allDone = false;
            }
        }
        
        return allDone;
    }
    
    /**
     * Metoda pro vymazání receptu z databáze.
     * @param databaseFile Databázový soubor.
     * @param recipeId Id receptu pro vymazání.
     * @return True/False, zda-li vše proběhlo v pořádku.
     */
    public boolean removeRecipeById(File databaseFile, int recipeId) {
        boolean allDone = false;
        
        SqlJetDb database = null;
        ISqlJetCursor cursorDeleteRecipe = null;
        
        try {
            database = SqlJetDb.open(databaseFile, true);
        
            database.beginTransaction(SqlJetTransactionMode.WRITE);
        
            ISqlJetTable table = database.getTable(TABLE_RECIPE);
        
            cursorDeleteRecipe = table.lookup(INDEX_RECIPE_ID, recipeId);
            
            if(!cursorDeleteRecipe.eof()) {
                cursorDeleteRecipe.delete();
                allDone = true;
            }
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while removing the recipe.", ex);
            allDone = false;
        } finally {
            try {
                if(cursorDeleteRecipe!=null)
                    cursorDeleteRecipe.close();
                if(database!=null) {
                    database.commit();
                    database.close();
                }
            } catch (SqlJetException ex) {
                LOGGER.error("Unexpected error while closing the database.", ex);
                allDone = false;
            }
        }
        
        return allDone;
    }
    
    /**
     * Metoda pro vymazání kategorie z databáze.
     * @param databaseFile Databázový soubor.
     * @param categoryId Id kategorie pro vymazání.
     * @return True/False, zda-li vše proběhlo v pořádku.
     */
    public boolean removeCategoryById(File databaseFile, int categoryId) {
        boolean allDone = false;
        
        SqlJetDb database = null;
        ISqlJetCursor cursorDeleteCategory = null;
        
        try {
            database = SqlJetDb.open(databaseFile, true);
        
            database.beginTransaction(SqlJetTransactionMode.WRITE);
        
            ISqlJetTable table = database.getTable(TABLE_CATEGORY);
        
            cursorDeleteCategory = table.lookup(INDEX_CATEGORY_ID_CATEGORY, categoryId);
            
            if(!cursorDeleteCategory.eof()) {
                cursorDeleteCategory.delete();
                allDone = true;
            }
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while removing the category.", ex);
            allDone = false;
        } finally {
            try {
                if(cursorDeleteCategory!=null)
                    cursorDeleteCategory.close();
                if(database!=null) {
                    database.commit();
                    database.close();
                }
            } catch (SqlJetException ex) {
                LOGGER.error("Unexpected error while closing the database.", ex);
                allDone = false;
            }
        }
        
        return allDone;
    }
    
    /**
     * Metoda načte z databáze Recept podle jeho ID.
     * @param databaseFile Databázový soubor.
     * @param recipeId Id hledaného receptu.
     * @return Nalezený recept. Jinak null.
     */
    public Recipe getRecipeById(File databaseFile, int recipeId) {
        Recipe ret = null;
        
        SqlJetDb database = null;
        
        try {
            database = SqlJetDb.open(databaseFile, true);
        
            database.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        
            ISqlJetTable table = database.getTable(TABLE_RECIPE);
        
            ISqlJetCursor cursorRecipe = table.lookup(INDEX_RECIPE_ID, recipeId);
            
            if(!cursorRecipe.eof()) {
                ret = new CookBook.Recipe(
                                (int)cursorRecipe.getInteger(FIELD_RECIPE_ID_RECIPE),
                                (int)cursorRecipe.getInteger(FIELD_RECIPE_ID_CATEGORY),
                                cursorRecipe.getString(FIELD_RECIPE_TITLE),
                                cursorRecipe.getString(FIELD_RECIPE_AUTHOR),
                                Common.getRatingFromInt(Integer.valueOf(cursorRecipe.getString(FIELD_RECIPE_RATING))),
                                Common.convertStringToDate(cursorRecipe.getString(FIELD_RECIPE_ADDED), Common.DATEFORMAT_DATABASE),
                                Common.convertStringToDate(cursorRecipe.getString(FIELD_RECIPE_MODIFIED), Common.DATEFORMAT_DATABASE),
                                cursorRecipe.getString(FIELD_RECIPE_NOTE),
                                cursorRecipe.getString(FIELD_RECIPE_INGREDIENTS),
                                cursorRecipe.getString(FIELD_RECIPE_METHOD),
                                Common.getBufferedImageFromInputStream(cursorRecipe.getBlobAsStream(FIELD_RECIPE_IMAGE)));  
            }
        } catch (SqlJetException ex) {
            LOGGER.error("Unexpected error while retrieving the recipe.", ex);
        } finally {
            try {
                if(database!=null) {
                    database.commit();
                    database.close();
                }
            } catch (SqlJetException ex) {
                LOGGER.error("Unexpected error while closing the database.", ex);
            }
        }
        
        return ret;
    }
}
