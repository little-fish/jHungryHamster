
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

package cz.babi.java.jhungryhamster.utils;

import cz.babi.java.jhungryhamster.entity.Rating;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Třída pro běžnou práci.
 * @author babi
 */
public class Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(Common.class);

    public static final DateFormat DATEFORMAT_RECIPE = new SimpleDateFormat("dd.MMMM yyyy, HH:mm");
    public static final DateFormat DATEFORMAT_DATABASE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat DATEFORMAT_STATUS = new SimpleDateFormat("HH:mm:ss");

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("res/l10n/strings");
    
    /**
     * Metoda převede datum z formátu typu <b>Date</b> do formátu typu 
     * <b>String</b>.
     * 
     * @param df Proměnná typu DateFormat, podle které se bude datum formátovat.
     * @param inputDate Vstupní hodnota data ve formátu typu <b>Date</b>.
     * @return Výstupní hodnota data ve formátu typu <b>String</b>.
     */
    public static String convertDateToString(DateFormat df, Date inputDate) {
        return new StringBuilder(df.format(inputDate)).toString();
    }
    
   /**
     * Metoda převede datum z formátu typu <b>String</b> do formátu typu 
     * <b>Date</b>.
     * 
     * @param inputDate Vstupní hodnota data ve formátu typu <b>String</b>.
     * @param df Proměnná typu DateFormat, podle které se bude datum formátovat.
     * @return Výstupní hodnota data ve formátu typu <b>Date</b>. Nastane-li 
     * chyba během parsování, vrací se hodnota <b>null</b>.
     */
    public static Date convertStringToDate(String inputDate, DateFormat df) {
        Date date = null;
        
        try {
            date = df.parse(inputDate);
        } catch (ParseException ex) {
            LOGGER.error("Can not parse given string {} to date", inputDate, ex);
        }
        
        return date;
    }
    
    /**
     * Metoda pro zmenšení/zvětšení obrázku. Změna velikosti se projeví na kvalitě 
     * výsledního obrázku, proto se tato metoda použije pro místa, kde není dbán 
     * důraz na kvalitu.
     * Pro kvalitní změnu velikosti se použije metoda 
     * getScaledImageForPrint(int, int, BufferedImage).
     * @param expectedWidth očekávaná šířka transformovaného obrázku
     * @param expectedHeight očekávaná výška transformovaného obrázku
     * @param bufferedImage vstupní obrázek pro transformaci
     * @return nový zvětšený/zmenšený obrázek
     */
    public static BufferedImage getScaledImage(int expectedWidth, int expectedHeight, 
            BufferedImage bufferedImage) {
        
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        
        /* pokud je očekávaná velikost obrázku větší než ta nynější, tak
         * vracím původní obrázek */
        if(imageWidth<expectedWidth && imageHeight<expectedHeight) {
            return bufferedImage;
        }

        double xScale = (double)expectedWidth/imageWidth;
        double yScale = (double)expectedHeight/imageHeight;
        
        /* scaling options: */
        /* scale to fit - image just fits in label */
        double scale = Math.min(xScale, yScale);
        /* scale to fill - image just fills label */
        //double scale = Math.max(xScale, yScale);
        
        int width  = (int)(scale*imageWidth);
        int height = (int)(scale*imageHeight);
        
//        int x = (expectedWidth - width)/2;
//        int y = (expectedHeight - height)/2;
        
        BufferedImage newImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = newImage.createGraphics();
        
        /* fill background for scale to fit */  
        g2.setBackground(UIManager.getColor("Panel.background"));
//        g2.clearRect(0, 0, expectedWidth, expectedHeight);
        g2.clearRect(0, 0, width, height);
        
//        g2.drawImage(image, x, y, width, height, null);
        g2.drawImage(bufferedImage, 0, 0, width, height, null);
        g2.dispose();
        
        return newImage;
    }
    
    /**
     * Metoda, která kvalitně změní velikost obrázku. Použije se zejména pro tisk.
     * @param expectedWidth Očekávaná šířka obrázku.
     * @param expectedHeight Očekávaná výška obrázku.
     * @param bufferedImage Obrázek, který má být změněn.
     * @return Změněný obrázek.
     */
    public static BufferedImage getScaledImageForPrint(int expectedWidth, int expectedHeight, 
            BufferedImage bufferedImage) {
        
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        
        /* pokud je očekávaná velikost obrázku větší než ta nynější, tak
         * vracím původní obrázek */
        if(imageWidth<expectedWidth && imageHeight<expectedHeight) {
            return bufferedImage;
        }
        
        Image tempImage = bufferedImage.getScaledInstance(expectedWidth, 
                expectedHeight, Image.SCALE_SMOOTH);
        
        double xScale = (double)expectedWidth/imageWidth;
        double yScale = (double)expectedHeight/imageHeight;
        
        /* scaling options: */
        /* scale to fit - image just fits in label */
        double scale = Math.min(xScale, yScale);
        /* scale to fill - image just fills label */
        //double scale = Math.max(xScale, yScale);
        
        int width  = (int)(scale*imageWidth);
        int height = (int)(scale*imageHeight);
        
        BufferedImage ret = new BufferedImage(width, 
                height, BufferedImage.TYPE_INT_RGB);
        
        java.awt.Graphics g = ret.getGraphics();
        g.drawImage(tempImage, 0, 0, width, height, null);
        g.dispose();
        
        return ret;
    }
    
    /**
     * Metoda vratí hodnotu Rating dle vstupního čísla
     * @param rating vstupní číslo
     * @return hodnota Rating, (nikdy ne null).
     */
    public static Rating getRatingFromInt(int rating) {
        switch(rating) {
            case 0: return Rating.ZERO;
            case 1: return Rating.ZERO_AND_HALF;
            case 2: return Rating.ONE;
            case 3: return Rating.ONE_AND_HALF;
            case 4: return Rating.TWO;
            case 5: return Rating.TWO_AND_HALF;
            case 6: return Rating.THREE;
            case 7: return Rating.THREE_AND_HALF;
            case 8: return Rating.FOUR;
            case 9: return Rating.FOUR_AND_HALF;
            case 10: return Rating.FIVE;
        }
        
        return null;
    }
    
    /**
     * Metoda pro získání obrázku ze vstupního proudu.
     * @param inputStream Vstupní proud.
     * @return Obrázek.
     */
    public static BufferedImage getBufferedImageFromInputStream(InputStream inputStream) {
        
        if(inputStream==null) return null;
        
        BufferedImage ret = null;
        
        try {
            ret = ImageIO.read(inputStream);
        } catch (IOException ioe) {
            LOGGER.error("Can not read from given InputStream.", ioe);
        }
        
        return ret;
    }
    
    /**
     * Metoda pro získání pole bytů ze vstupního obrázku.
     * @param inputImage Vstupní obrázek.
     * @return Pole bytů.
     */
    public static byte[] getByteArrayFromBufferedImage(BufferedImage inputImage) {
        
        byte[] ret = null;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(inputImage, "jpg", baos);
            baos.flush();
            ret = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
            LOGGER.error("Some weird exception.", ex);
        }
        
        return ret;
    }
    
    /**
     * Metoda vrací procentuální hodnotu hodnocení.
     * @param rating Vstupní hodnocení.
     * @return Procentuální hodnota vstupního hodnocení.
     */
    public static String getPercentFromRating(Rating rating) {
        switch(rating) {
            case ZERO: return "0%";
            case ZERO_AND_HALF: return "10%";
            case ONE: return "20%";
            case ONE_AND_HALF: return "30%";
            case TWO: return "40%";
            case TWO_AND_HALF: return "50%";
            case THREE: return "60%";
            case THREE_AND_HALF: return "70%";
            case FOUR: return "80%";
            case FOUR_AND_HALF: return "90%";
            case FIVE: return "100%";
        }
        
        return null;
    }
    
    /**
     * Metoda vrací cestu, kde je umístěn spouštěcí jar soubor.
     * Nutno vytvořit instanci třídy File z proměnné path. Nutno zajistit správný
     * oddělovač souborů.
     * @return Cesta ke spouštěcímu jar souboru.
     */
    public static String getPathToRealCurrentDirectory() {
        String ret = "";
        try {
            String pathToJar = Common.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            File fileToJar = new File(pathToJar);
            ret =  URLDecoder.decode(fileToJar.getAbsolutePath().substring(0, 
                    fileToJar.getAbsolutePath().lastIndexOf(File.separator)), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("", ex);
        }
        
        return ret;
    }
    
    /**
     * Metoda vrací instanci LookAndFeel podle názvu motivu.
     * @param className Název motivu.
     * @return LookAndFeel.
     */
    public static LookAndFeel getLookAndFeel(String className) {
        switch(className) {
            case "Autumn": return new SubstanceAutumnLookAndFeel();
            case "Business": return new SubstanceBusinessLookAndFeel();
            case "Business Black Steel": return new SubstanceBusinessBlackSteelLookAndFeel();
            case "Business Blue Steel": return new SubstanceBusinessBlueSteelLookAndFeel();
            case "Cerulean": return new SubstanceCeruleanLookAndFeel();
            case "Challenger Deep": return new SubstanceChallengerDeepLookAndFeel();
            case "Creme": return new SubstanceCremeLookAndFeel();
            case "Creme Coffee": return new SubstanceCremeCoffeeLookAndFeel();
            case "Dust": return new SubstanceDustLookAndFeel();
            case "Dust Coffee": return new SubstanceDustCoffeeLookAndFeel();
            case "Emerald Dusk": return new SubstanceEmeraldDuskLookAndFeel();
            case "Gemini": return new SubstanceGeminiLookAndFeel();
            case "Graphite": return new SubstanceGraphiteLookAndFeel();
            case "Graphite Aqua": return new SubstanceGraphiteAquaLookAndFeel();
            case "Graphite Glass": return new SubstanceGraphiteGlassLookAndFeel();
            case "Magellan": return new SubstanceMagellanLookAndFeel();
            case "Mariner": return new SubstanceMarinerLookAndFeel();
            case "Mist Aqua": return new SubstanceMistAquaLookAndFeel();
            case "Mist Silver": return new SubstanceMistSilverLookAndFeel();
            case "Moderate": return new SubstanceModerateLookAndFeel();
            case "Nebula": return new SubstanceNebulaLookAndFeel();
            case "Nebula Brick Wall": return new SubstanceNebulaBrickWallLookAndFeel();
            case "Office Black 2007": return new SubstanceOfficeBlack2007LookAndFeel();
            case "Office Blue 2007": return new SubstanceOfficeBlue2007LookAndFeel();
            case "Office Silver 2007": return new SubstanceOfficeSilver2007LookAndFeel();
            case "Raven": return new SubstanceRavenLookAndFeel();
            case "Sahara": return new SubstanceSaharaLookAndFeel();
            case "Twilight": return new SubstanceTwilightLookAndFeel();
            default: return new SubstanceBusinessBlackSteelLookAndFeel();
        }
    }
    
    /**
     * Metoda nastaví vzhled Substance.
     * Nutno provést dvakrát, asi nějaká chyba v javě.
     */
    public static void useSubstanceLAF(String theme) {     
        LookAndFeel lookAndFeel = Common.getLookAndFeel(theme);
        
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            
            UIManager.setLookAndFeel(lookAndFeel);
            
            UIManager.put(LafWidget.TEXT_EDIT_CONTEXT_MENU, Boolean.TRUE);
            UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);
        } catch(UnsupportedLookAndFeelException ulafe) {
            LOGGER.error("Unsupported look and feel.", ulafe);
        }
        
//        SwingUtilities.updateComponentTreeUI();
    }
}
