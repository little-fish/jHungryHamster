
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

import cz.babi.java.jhungryhamster.data.CategoryIndenter;
import cz.babi.java.jhungryhamster.data.SearchWordSeparator;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Třída definující nastavení aplikace.
 *
 * @author babi
 */
@XmlRootElement(name="settings")
public class Settings {
    
    private static Settings instance = new Settings();
    
    private static boolean autoLoadDatabase = true;
    private static boolean showRootElement = true;
    private static boolean canSaveSettings = false;
    private static boolean canSaveDatabase = false;
    private static boolean hideSearchDetails = false;
    private static boolean hideRecipeDetails = false;
    private static boolean searchInAuthor = false;
    private static boolean searchInTitle = true;
    private static boolean searchInIngredients = false;
    private static boolean searchInMethod = false;
    private static boolean searchInNote = false;
    private static String databaseName = "jHungryHamster";
    private static String databasePath = "";
    private static boolean databaseInUserDir = true;
    private static ImageResolution imageResolution = ImageResolution.MED_PLUS;
    
    private static boolean printTitle = true;
    private static boolean printAuthor = true;
    private static boolean printCategory = false;
    private static boolean printAdded = false;
    private static boolean printModified = false;
    private static boolean printNote = true;
    private static boolean printRating = true;
    private static boolean printIngredience = true;
    private static boolean printMethod = true;
    private static boolean printRecipeImage = false;
    private static boolean printHeader = false;
    private static String printHeaderText = "";
    private static boolean printFooter = false;
    private static String printFooterText = "";
    private static boolean backgroundPrint = false;
    private static boolean showPrint = true;
    private static boolean editPrintText = false;
    
    private static boolean showWholeTreeAfterSearch = true;
    private static boolean selectFirstMatch = true;
    private static CategoryIndenter categoryIndenter = CategoryIndenter.Asterisk;
    private static String userCategoryJoiner = "";
    private static SearchWordSeparator searchSplitter = SearchWordSeparator.Space;
    private static String userSearchSplitter = "";
    
    private static String theme = "Business Black Steel";
    
    /**
     * Privátní konstruktor třídy Settings.
     */
    private Settings() {}
    
    /**
     * Metoda pro získání instance třídy Settings.
     * Použitý návrhový vzor - <b>Singleton</b>.
     * 
     * @return Instance třídy Settings
     */
    public static Settings getInstance() {
        return instance;
    }

    /**
     * @return the databaseInUserDir
     */
    public boolean isDatabaseInUserDir() {
        return databaseInUserDir;
    }

    /**
     * @param aDatabaseInUserDir the databaseInUserDir to set
     */
    public void setDatabaseInUserDir(boolean aDatabaseInUserDir) {
        databaseInUserDir = aDatabaseInUserDir;
    }

    /**
     * @return the searchInNote
     */
    public boolean isSearchInNote() {
        return searchInNote;
    }

    /**
     * @param aSearchInNote the searchInNote to set
     */
    public void setSearchInNote(boolean aSearchInNote) {
        searchInNote = aSearchInNote;
    }

    /**
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * @param aTheme the theme to set
     */
    public void setTheme(String aTheme) {
        theme = aTheme;
    }
    
    /**
     * @return the searchSplitter
     */
    public SearchWordSeparator getSearchSplitter() {
        return searchSplitter;
    }

    /**
     * @return the userSearchSplitter
     */
    public String getUserSearchSplitter() {
        return userSearchSplitter;
    }

    /**
     * @param aSearchSplitter the searchSplitter to set
     */
    public void setSearchSplitter(SearchWordSeparator aSearchSplitter) {
        searchSplitter = aSearchSplitter;
    }

    /**
     * @param aUserSearchSplitter the userSearchSplitter to set
     */
    public void setUserSearchSplitter(String aUserSearchSplitter) {
        userSearchSplitter = aUserSearchSplitter;
    }

    /**
     * @return the userSearchSplitter
     */
    public String getUserCategoryJoiner() {
        return userCategoryJoiner;
    }

    /**
     * @param aUserSearchJoiner the userSearchSplitter to set
     */
    public void setUserCategoryJoiner(String aUserSearchJoiner) {
        userCategoryJoiner = aUserSearchJoiner;
    }
    
    /**
     * @return the showWholeTreeAfterSearch
     */
    public boolean isShowWholeTreeAfterSearch() {
        return showWholeTreeAfterSearch;
    }

    /**
     * @return the selectFirstMatch
     */
    public  boolean isSelectFirstMatch() {
        return selectFirstMatch;
    }

    /**
     * @return the categoryIndenter
     */
    public CategoryIndenter getCategoryIndenter() {
        return categoryIndenter;
    }

    /**
     * @param aShowWholeTreeAfterSearch the showWholeTreeAfterSearch to set
     */
    public void setShowWholeTreeAfterSearch(boolean aShowWholeTreeAfterSearch) {
        showWholeTreeAfterSearch = aShowWholeTreeAfterSearch;
    }

    /**
     * @param aSelectFirstMatch the selectFirstMatch to set
     */
    public void setSelectFirstMatch(boolean aSelectFirstMatch) {
        selectFirstMatch = aSelectFirstMatch;
    }

    /**
     * @param categoryIndenter the categoryIndenter to set
     */
    public void setCategoryIndenter(CategoryIndenter categoryIndenter) {
        this.categoryIndenter = categoryIndenter;
    }

    /**
     * @return the databasePath
     */
    public String getDatabasePath() {
        return databasePath;
    }

    /**
     * @param databasePath the databasePath to set
     */
    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }
    
    /**
     * @return the printHeaderText
     */
    public String getPrintHeaderText() {
        return printHeaderText;
    }

    /**
     * @return the printFooterText
     */
    public String getPrintFooterText() {
        return printFooterText;
    }

    /**
     * @param aPrintHeaderText the printHeaderText to set
     */
    public void setPrintHeaderText(String aPrintHeaderText) {
        printHeaderText = aPrintHeaderText;
    }

    /**
     * @param aPrintFooterText the printFooterText to set
     */
    public void setPrintFooterText(String aPrintFooterText) {
        printFooterText = aPrintFooterText;
    }

    /**
     * @return the printTitle
     */
    public boolean isPrintTitle() {
        return printTitle;
    }

    /**
     * @return the printAuthor
     */
    public boolean isPrintAuthor() {
        return printAuthor;
    }

    /**
     * @return the printCategory
     */
    public boolean isPrintCategory() {
        return printCategory;
    }

    /**
     * @return the printAdded
     */
    public boolean isPrintAdded() {
        return printAdded;
    }

    /**
     * @return the printModified
     */
    public boolean isPrintModified() {
        return printModified;
    }

    /**
     * @return the printNote
     */
    public boolean isPrintNote() {
        return printNote;
    }

    /**
     * @return the printRating
     */
    public boolean isPrintRating() {
        return printRating;
    }

    /**
     * @return the printIngredience
     */
    public boolean isPrintIngredience() {
        return printIngredience;
    }

    /**
     * @return the printMethod
     */
    public boolean isPrintMethod() {
        return printMethod;
    }

    /**
     * @return the printRecipeImage
     */
    public boolean isPrintRecipeImage() {
        return printRecipeImage;
    }

    /**
     * @return the printHeader
     */
    public boolean isPrintHeader() {
        return printHeader;
    }

    /**
     * @return the printFooter
     */
    public boolean isPrintFooter() {
        return printFooter;
    }

    /**
     * @return the backgroundPrint
     */
    public boolean isBackgroundPrint() {
        return backgroundPrint;
    }

    /**
     * @return the showPrint
     */
    public boolean isShowPrint() {
        return showPrint;
    }

    /**
     * @return the editPrintText
     */
    public boolean isEditPrintText() {
        return editPrintText;
    }

    /**
     * @param aPrintTitle the printTitle to set
     */
    public void setPrintTitle(boolean aPrintTitle) {
        printTitle = aPrintTitle;
    }

    /**
     * @param aPrintAuthor the printAuthor to set
     */
    public void setPrintAuthor(boolean aPrintAuthor) {
        printAuthor = aPrintAuthor;
    }

    /**
     * @param aPrintCategory the printCategory to set
     */
    public void setPrintCategory(boolean aPrintCategory) {
        printCategory = aPrintCategory;
    }

    /**
     * @param aPrintAdded the printAdded to set
     */
    public void setPrintAdded(boolean aPrintAdded) {
        printAdded = aPrintAdded;
    }

    /**
     * @param aPrintModified the printModified to set
     */
    public void setPrintModified(boolean aPrintModified) {
        printModified = aPrintModified;
    }

    /**
     * @param aPrintNote the printNote to set
     */
    public void setPrintNote(boolean aPrintNote) {
        printNote = aPrintNote;
    }

    /**
     * @param aPrintRating the printRating to set
     */
    public void setPrintRating(boolean aPrintRating) {
        printRating = aPrintRating;
    }

    /**
     * @param aPrintIngredience the printIngredience to set
     */
    public void setPrintIngredience(boolean aPrintIngredience) {
        printIngredience = aPrintIngredience;
    }

    /**
     * @param aPrintMethod the printMethod to set
     */
    public void setPrintMethod(boolean aPrintMethod) {
        printMethod = aPrintMethod;
    }

    /**
     * @param aPrintRecipeImage the printRecipeImage to set
     */
    public void setPrintRecipeImage(boolean aPrintRecipeImage) {
        printRecipeImage = aPrintRecipeImage;
    }

    /**
     * @param aPrintHeader the printHeader to set
     */
    public void setPrintHeader(boolean aPrintHeader) {
        printHeader = aPrintHeader;
    }

    /**
     * @param aPrintFooter the printFooter to set
     */
    public void setPrintFooter(boolean aPrintFooter) {
        printFooter = aPrintFooter;
    }

    /**
     * @param aBackgroundPrint the backgroundPrint to set
     */
    public void setBackgroundPrint(boolean aBackgroundPrint) {
        backgroundPrint = aBackgroundPrint;
    }

    /**
     * @param aShowPrint the showPrint to set
     */
    public void setShowPrint(boolean aShowPrint) {
        showPrint = aShowPrint;
    }

    /**
     * @param aEditPrintText the editPrintText to set
     */
    public void setEditPrintText(boolean aEditPrintText) {
        editPrintText = aEditPrintText;
    }

    /**
     * @return the searchInAuthor
     */
    public boolean isSearchInAuthor() {
        return searchInAuthor;
    }

    /**
     * @return the searchInTitle
     */
    public boolean isSearchInTitle() {
        return searchInTitle;
    }

    /**
     * @return the searchInIngredients
     */
    public boolean isSearchInIngredients() {
        return searchInIngredients;
    }

    /**
     * @return the searchInMethod
     */
    public boolean isSearchInMethod() {
        return searchInMethod;
    }

    /**
     * @param aSearchInAuthor the searchInAuthor to set
     */
    public void setSearchInAuthor(boolean aSearchInAuthor) {
        searchInAuthor = aSearchInAuthor;
    }

    /**
     * @param aSearchInTitle the searchInTitle to set
     */
    public void setSearchInTitle(boolean aSearchInTitle) {
        searchInTitle = aSearchInTitle;
    }

    /**
     * @param aSearchInIngredients the searchInIngredients to set
     */
    public void setSearchInIngredients(boolean aSearchInIngredients) {
        searchInIngredients = aSearchInIngredients;
    }

    /**
     * @param aSearchInMethod the searchInMethod to set
     */
    public void setSearchInMethod(boolean aSearchInMethod) {
        searchInMethod = aSearchInMethod;
    }

    /**
     * @return the hideSearchDetails
     */
    public boolean isHideSearchDetails() {
        return hideSearchDetails;
    }

    /**
     * @return the hideRecipeDetails
     */
    public boolean isHideRecipeDetails() {
        return hideRecipeDetails;
    }

    /**
     * @param aHideSearchDetails the hideSearchDetails to set
     */
    public void setHideSearchDetails(boolean aHideSearchDetails) {
        hideSearchDetails = aHideSearchDetails;
    }

    /**
     * @param aHideRecipeDetails the hideRecipeDetails to set
     */
    public void setHideRecipeDetails(boolean aHideRecipeDetails) {
        hideRecipeDetails = aHideRecipeDetails;
    }

    /**
     * @return the imageResolution
     */
    public ImageResolution getImageResolution() {
        return imageResolution;
    }

    /**
     * @param aImageResolution the imageResolution to set
     */
    public void setImageResolution(ImageResolution aImageResolution) {
        imageResolution = aImageResolution;
    }
    
    /**
     * @return the autoLoadDatabase
     */
    public boolean isAutoLoadDatabase() {
        return autoLoadDatabase;
    }
    
    /**
     * @return the showRootElement
     */
    public boolean isShowRootElement() {
        return showRootElement;
    }
    
    /**
     * @return the canSaveSettings
     */
    public boolean isCanSaveSettings() {
        return canSaveSettings;
    }

    /**
     * @return the canSaveDatabase
     */
    public boolean isCanSaveDatabase() {
        return canSaveDatabase;
    }
    
    /**
     * @return the databaseName
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * @param autoLoadDatabase the autoLoadDatabase to set
     */
    public void setAutoLoadDatabase(boolean autoLoadDatabase) {
        this.autoLoadDatabase = autoLoadDatabase;
    }
    
    /**
     * @param showRootElement the showRootElement to set
     */
    public void setShowRootElement(boolean showRootElement) {
        this.showRootElement = showRootElement;
    }
    
    /**
     * @param canSaveSettings the canSaveSettings to set
     */
    public void setCanSaveSettings(boolean canSaveSettings) {
        this.canSaveSettings = canSaveSettings;
    }

    /**
     * @param canSaveDatabase the canSaveDatabase to set
     */
    public void setCanSaveDatabase(boolean canSaveDatabase) {
        this.canSaveDatabase = canSaveDatabase;
    }
    
    /**
     * @param databaseName the databaseName to set
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
