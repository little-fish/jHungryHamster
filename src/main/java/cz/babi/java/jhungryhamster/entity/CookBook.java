
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.*;

/**
 * Třída zastupující entitu CookBook (Kuchařka).
 * Uchovává v sobě seznam všech Receptů.
 *
 * @author babi
 */
public class CookBook {
    
    /* sdílená instance */
    private static final CookBook instance = new CookBook();

    private static final Logger LOGGER = LoggerFactory.getLogger(CookBook.class);
    private List<Recipe> recipes = new ArrayList<>();
     
    /**
     * Privátní konstruktor třídy Settings.
     */
    private CookBook() {}
    
    /**
     * Metoda pro získání instance třídy CookBook.
     * Použitý návrhový vzor - <b>Singleton</b>.
     * 
     * @return Instance třídy CookBook
     */
    public static CookBook getInstance() {
        return instance;
    }
            
    /**
     * Metoda vrací všechny recepty.
     * 
     * @return List všech receptů
     */
    public List<Recipe> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }
    
    /**
     * Metoda vrací Recept podle vstupního indexu.
     * Vstupní index != ID receptu.
     * Indexem je myšlena pozice v kolekci.
     * 
     * @param index hledaného receptu
     * @return Recept
     */
    public Recipe getRecipe(int index) {
        return recipes.get(index);
    }
    
    /**
     * Metoda pro přidání nových receptů.
     * 
     * @param recipes kolekce receptů pro přidání
     */
    public void addRecipes(Collection<Recipe> recipes) {
        for(Recipe recipe : recipes) {
            this.recipes.add(recipe);
            
            LOGGER.debug("New recipe was added {}.", recipe.getTitle());
        }
        
        LOGGER.debug("{} new recipes was added.", recipes.size());
    }
    
    /**
     * Metoda pro přidání jednoho receptu.
     * 
     * @param recipe Recept pro přidání
     */
    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        
        LOGGER.debug("New recipe was added {}.", recipe.getTitle());
    }
    
    /**
     * Metoda pro odstranění několika receptů.
     * 
     * @param recipes kolekce receptů pro ostranění
     */
    public void removeRecipes(Collection<Recipe> recipes) {
        for(Recipe recipe : recipes) {
            this.recipes.remove(recipe);
            
            LOGGER.debug("The recipe has been removed {}.", recipe.getTitle());
        }
        
        LOGGER.debug("{} recipes have been removed.", recipes.size());
    }
    
    /**
     * Metoda pro odstranění daného receptu.
     * 
     * @param recipe Recept pro odstranění
     */
    public void removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
        
        LOGGER.debug("The recipe has been removed {}.", recipe.getTitle());
    }
    
    /**
     * Metoda pro odstranění všech receptů.
     */
    public void removeAllRecipes() {
        int size = recipes.size();
        this.recipes.clear();
        
        LOGGER.debug("{} recipes have been removed.", size);
    }
    
    /**
     * Třída zastupující entitu Recipe (Recept).
     *
     * @author babi
     */
    public static class Recipe implements Serializable {
        
        private int idRecipe;
        private int idCategory;
        private String title;
        private String author;
        private Rating rating;
        private Date added;
        private Date modified;
        private String note;
        private String ingredients;
        private String method;
        private BufferedImage recipeImage;

        /**
         * Konstruktor třídy Recipe.
         * 
         * @param idRecipe ID receptu
         * @param idCategory ID kategorie
         * @param title Název receptu
         * @param author Autor receptu
         * @param rating Hodnocení receptu
         * @param added Datum přidání receptu
         * @param modified Datum změndy receptu
         * @param note Poznámka k receptu
         * @param ingredients Ingredience k přípravě
         * @param method Postup práce
         * @param recipeImage Obrázek hotového jídla
         */
        public Recipe(int idRecipe, int idCategory, String title, String author, 
                Rating rating, Date added, Date modified, String note, 
                String ingredients, String method, BufferedImage recipeImage) {
            this.idRecipe = idRecipe;
            this.idCategory = idCategory;
            this.title = title;
            this.author = author;
            this.rating = rating;
            this.added = added;
            this.modified = modified;
            this.note = note;
            this.ingredients = ingredients;
            this.method = method;
            this.recipeImage = recipeImage;
        }
        
        /**
         * Konstruktor třídy Recipe.
         * 
         * @param idRecipe ID receptu
         * @param idCategory ID kategorie
         * @param title Název receptu
         * @param author Autor receptu
         * @param rating Hodnocení receptu
         * @param added Datum přidání receptu
         * @param modified Datum změndy receptu
         * @param note Poznámka k receptu
         * @param ingredients Ingredience k přípravě
         * @param method Postup práce
         */
        public Recipe(int idRecipe, int idCategory, String title, String author, 
                Rating rating, Date added, Date modified, String note, 
                String ingredients, String method) {
            this.idRecipe = idRecipe;
            this.idCategory = idCategory;
            this.title = title;
            this.author = author;
            this.rating = rating;
            this.added = added;
            this.modified = modified;
            this.note = note;
            this.ingredients = ingredients;
            this.method = method;
        }
        
        /**
         * Bezparametrický konstruktor třídy Recipe.
         */
        public Recipe() {}
        

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = StringUtils.defaultString(title);
        }

        /**
         * @return the author
         */
        public String getAuthor() {
            return author;
        }

        /**
         * @param author the author to set
         */
        public void setAuthor(String author) {
            this.author = StringUtils.defaultString(author);
        }

        /**
         * @return the rating
         */
        public Rating getRating() {
            return rating;
        }

        /**
         * @param rating the rating to set
         */
        public void setRating(Rating rating) {
            this.rating = rating;
        }

        /**
         * @return the added
         */
        public Date getAdded() {
            return added;
        }

        /**
         * @param added the added to set
         */
        public void setAdded(Date added) {
            this.added = added;
        }

        /**
         * @return the modified
         */
        public Date getModified() {
            return modified;
        }

        /**
         * @param modified the modified to set
         */
        public void setModified(Date modified) {
            this.modified = modified;
        }

        /**
         * @return the note
         */
        public String getNote() {
            return note;
        }

        /**
         * @param note the note to set
         */
        public void setNote(String note) {
            this.note = StringUtils.defaultString(note);
        }

        /**
         * @return the ingredients
         */
        public String getIngredients() {
            return ingredients;
        }

        /**
         * @param ingredients the ingredients to set
         */
        public void setIngredients(String ingredients) {
            this.ingredients = StringUtils.defaultString(ingredients);
        }

        /**
         * @return the methods
         */
        public String getMethods() {
            return method;
        }

        /**
         * @param methods the methods to set
         */
        public void setMethods(String methods) {
            this.method = StringUtils.defaultString(methods);
        }

        /**
         * @return the recipeImage
         */
        public BufferedImage getRecipeImage() {
            return recipeImage;
        }

        /**
         * @param recipeImage the recipeImage to set
         */
        public void setRecipeImage(BufferedImage recipeImage) {
            this.recipeImage = recipeImage;
        }

        @Override
        public String toString() {
            return this.getTitle();
        }

        /**
         * @return the idRecipe
         */
        public int getIdRecipe() {
            return idRecipe;
        }
        
        /**
         * @param idRecipe
         */
        public void setIdRecipe(int idRecipe) {
            this.idRecipe = idRecipe;
        }
        
        /**
         * @param idCategory
         */
        public void setIdCategory(int idCategory) {
            this.idCategory = idCategory;
        }

        /**
         * @return the idCategry
         */
        public int getIdCategry() {
            return idCategory;
        }
    }
}
