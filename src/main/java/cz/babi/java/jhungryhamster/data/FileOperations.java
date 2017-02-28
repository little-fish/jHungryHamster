
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

import cz.babi.java.jhungryhamster.entity.Settings;
import cz.babi.java.jhungryhamster.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.xml.bind.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Třída pro běžnou práci s soubory (nastavení, databáze receptů)
 * 
 * @author babi
 */
public class FileOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileOperations.class);
    
    private static FileOperations instance = new FileOperations();
    
    private static Settings settings = Settings.getInstance();
    
    private static final String databaseExtension = ".sqlite";
    private static final String userDir = "user-data";
    private static final String currentDir = Common.getPathToRealCurrentDirectory();
//    private static final String currentDir = ClassLoader.getSystemClassLoader().getResource(".").getPath();
    private static final File userDataFolder = new File(getCurrentDir() + File.separator + getUserDir());
    private static final File userSettingsFile = new File(getCurrentDir() + File.separator +
            getUserDir() + File.separator + "settings.xml");
    private static File userDatabaseFile = null;
    
    /**
     * Privátní konstruktor třídy FileOperations.
     */
    private FileOperations() {}
    
    /**
     * Metoda pro získání instance třídy FileOperations.
     * Použitý návrhový vzor - <b>Singleton</b>.
     * 
     * @return Instance třídy FileOperations
     */
    public static FileOperations getInstance() {
        return instance;
    }

    /**
     * @return the currentDir
     */
    public static String getCurrentDir() {
        return currentDir;
    }

    /**
     * @return the databaseExtension
     */
    public static String getDatabaseExtension() {
        return databaseExtension;
    }
    
    /**
     * @return the userDir
     */
    public static String getUserDir() {
        return userDir;
    }
    
    /**
     * @return the userDataFolder
     */
    public static File getUserDataFolder() {
        return userDataFolder;
    }

    /**
     * @return the userSettings
     */
    public static File getUserSettingsFile() {
        return userSettingsFile;
    }

    /**
     * @return the userDatabase
     */
    public static File getUserDatabaseFile() {
        return userDatabaseFile;
    }
    
    /**
     * @param userDatabaseFile the userDatabase to set
     * @param putInUserDir Pokud se vytváří nová databáze, nebo pokud otevíraná
     * databáze je umístěna v uživatelské složce "user-data", tak se nastaví do
     * proměnné userDatabaseFile relativní cesta, jinak se do ní nastaví absolutní
     * cesta k databázi
     */
    public static void setUserDatabaseFile(String userDatabaseFile, boolean putInUserDir) {
        if(putInUserDir) FileOperations.userDatabaseFile = new File(getCurrentDir() + 
                File.separator + userDir + File.separator + 
                userDatabaseFile + databaseExtension);
        else FileOperations.userDatabaseFile = new File(userDatabaseFile);
    }
    
    /**
     * Metoda zjistí, zda-li vstupní soubor/složka existuje.
     * @param path vstupní soubor/složka, která se má zkontrolovat
     * @return existence vstupní složky/dokumentu
     */
    public boolean checkFilePath(File path) {
        if(path==null) return false;
        else return path.exists();
    }
    
    /**
     * Metoda vytvoří soubor.
     * @param file soubor, který má být vytvořen
     * @return existence vstupního soubour
     */
    public boolean createFile(File file) {
        boolean isCreated = false;
        
        try {
            isCreated = file.createNewFile();
        } catch (IOException ex) {
            LOGGER.error("Unexpected error while creating file '{}'.", file.getPath(), ex);
        }
        
        return isCreated;
    }
    
    /**
     * Metoda vytvoří adresář.
     * @param directory adresář, který má být vytvořen
     * @return existence vytvořeného adresáře
     */
    public boolean createDirectory(File directory) {
        return directory.mkdir();
    }
    
    /**
     * Metoda nastaví možnosti zápisu do souboru.
     * @param file soubor, který se má nastavit
     * @param canWriteToFile hodnota, která se má nastavit
     */
    public void setWritable(File file, boolean canWriteToFile) {
        file.setWritable(canWriteToFile);
    }
    
    /**
     * Metoda uloží uživatelské nastavení do xml souboru.
     * @param file soubor, do kterého má být nastavení uloženo
     * @return zda-li bylo nastavení uloženo
     */
    public boolean saveSettingsToXml(File file) {
        JAXBContext context = null;
        Marshaller marshaller = null;
        
        try {
            context = JAXBContext.newInstance(Settings.class);
        } catch(JAXBException jex) {
            LOGGER.error("Can not create JAXB instance of Settings class.", jex);
            return false;
        }
        
        try {
            marshaller = context.createMarshaller();
        } catch(JAXBException jex) {
            LOGGER.error("Can not create a marshaller.", jex);
            return false;
        }
        
        try {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch(PropertyException pex) {
            LOGGER.error("Can not set marshaller's property.", pex);
            return false;
        }
        
        try {
            marshaller.marshal(settings, file);
        } catch(JAXBException jex) {
            LOGGER.error("Can not marshall the settings.", jex);
            return false;
        }
        
        return true;
    }
    
    /**
     * Metoda pro načetení nastavení aplikace.
     * @param file vstupní soubor, ze kterého bude nastavení načteno
     * @return zda-li nastavení bylo načteno v pořádku
     */
    public boolean readSettingsFromXml(File file) {
        Settings newSettings;
        JAXBContext context = null;
        Unmarshaller unmarshaller = null;
        
        try {
            context = JAXBContext.newInstance(Settings.class);
        } catch (JAXBException jex) {
            LOGGER.error("Can not create JAXB instance of the Settings class.", jex);
            return false;
        }
            
        try {
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException jex) {
            LOGGER.error("Can not create a unmarshaller.", jex);
            return false;
        }
        
        try {
            newSettings = (Settings)unmarshaller.unmarshal(file);
        } catch (JAXBException jex) {
            LOGGER.error("Can not unmarshall the settings.", jex);
            return false;
        }

        /* pakliže není nastavení načetno jako null, přiřadí se načtené
         nastavení do instance nastavení aplikace. */
        if(newSettings!=null) settings = newSettings;
        
        return true;
    }
    
    /**
     * Metoda pro načtení obrázku ze vstupního souboru.
     * @param file vstupní soubor
     * @return načtený obrázek
     */
    public BufferedImage loadImageFromFile(File file) {
        BufferedImage bfImage = null;

        try {
            bfImage = ImageIO.read(file);
        } catch (IOException ioex) {
            LOGGER.error("Can not read the given file.", ioex);
        }
        
        return bfImage;
    }
    
    /**
     * Metoda vymaže vstupní soubor.
     * @param file Soubor pro vymazání.
     * @return zda-li bylo vymazání úspěšné.
     */
    public boolean deteleFile(File file) {
        return file.delete();
    }
    
    /**
     * Metoda přejmenuje soubor.
     * @param fileToRename Soubor na přejmenování
     * @param newName Nový název souboru
     * @param extension Přípona nového souboru
     * @return Nový přejmenovaný soubor
     */
    public File renameFile(File fileToRename, String newName, String extension) {
        File ret = null;
        File fileParent = fileToRename.getParentFile();
        File newFile = new File(fileParent, newName+extension);
        if(!checkFilePath(newFile)) {
            if(fileToRename.renameTo(newFile)) ret = newFile;
        }
        
        return ret;
    }
}
