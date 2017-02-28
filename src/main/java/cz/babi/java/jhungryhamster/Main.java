
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

package cz.babi.java.jhungryhamster;

import static cz.babi.java.jhungryhamster.utils.Common.RESOURCE_BUNDLE;

import cz.babi.java.jhungryhamster.data.DatabaseOperations;
import cz.babi.java.jhungryhamster.data.FileOperations;
import cz.babi.java.jhungryhamster.entity.Settings;
import cz.babi.java.jhungryhamster.gui.MainFrame;
import cz.babi.java.jhungryhamster.gui.OpenOrCreateDatabaseDialog;
import cz.babi.java.jhungryhamster.utils.Common;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.File;

/**
 * The main class of the application.
 */
public class Main extends SingleFrameApplication {
    
    private static Settings settings = Settings.getInstance();
    private static FileOperations fileOperations = FileOperations.getInstance();
    private static DatabaseOperations databaseOperations = DatabaseOperations.getInstance();

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Common.useSubstanceLAF(settings.getTheme());
                Main.this.show(new MainFrame(getApplication()));
            }
        });
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of Main.
     */
    public static Main getApplication() {
        return Application.getInstance(Main.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {

        /* proměnná, která v sobě nese hodnotu, zda-li je možné vytvářet/číst soubory na disku */
        boolean canContinue = true;
        
        /* proměnná, která v sobě nese hodnotu, zda-li se má zobrazit dialog pro vytvoření
         nebo výběr existující databáze */
        boolean needCreateOrOpenDB = false;
        
        /* proměnná, která v sobě nese hodnotu, zda-li je nutné vytvořit soubor pro nastavení aplikace */
        boolean needCreateSettingsFile = false;
        
        /* otestování zda-li existuje; je možné vytvořit adresář pro uživatelská data */
        if(!fileOperations.checkFilePath(FileOperations.getUserDataFolder())) {
            if(!fileOperations.createDirectory(FileOperations.getUserDataFolder())) {
                canContinue = false;
                JOptionPane.showMessageDialog(null,
                        RESOURCE_BUNDLE.getString("Main.userDataFolder.createDirectoryError.text"),
                        RESOURCE_BUNDLE.getString("Main.userDataFolder.createDirectoryError.title")
                        + FileOperations.getUserDataFolder().getPath() + "'",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if(canContinue) {
            /* otestování, zda-li existuje soubor s nastavením aplikace */
            if(fileOperations.checkFilePath(FileOperations.getUserSettingsFile())) {
                if(!fileOperations.readSettingsFromXml(FileOperations.getUserSettingsFile())) {
                    JOptionPane.showMessageDialog(null,
                            RESOURCE_BUNDLE.getString("Main.userDataFolder.readSettingsError.text"),
                            RESOURCE_BUNDLE.getString("Main.userDataFolder.readFileError.title")
                            + FileOperations.getUserSettingsFile().getPath() + "'",
                            JOptionPane.ERROR_MESSAGE);
                    if(fileOperations.deteleFile(FileOperations.getUserSettingsFile()))
                        needCreateSettingsFile = true;
                } else {
                    /* po načtení nastavení aplikace se vytvoří i správný název databáze receptů */
                    if(settings.isDatabaseInUserDir()) FileOperations.setUserDatabaseFile(
                            settings.getDatabaseName(), true);
                    else FileOperations.setUserDatabaseFile(settings.getDatabasePath(), false);
                    settings.setCanSaveSettings(true);
                }
            /* pokud neexistuje soubor s nastavením, bude vytvořen */
            } else {
                needCreateSettingsFile = true;
            }
            
            /* pokud neexistuje soubor s nastavením, bude vytvořen */
            if(needCreateSettingsFile) {
                if(!fileOperations.createFile(FileOperations.getUserSettingsFile())) {
                    JOptionPane.showMessageDialog(null,
                            RESOURCE_BUNDLE.getString("Main.userDataFolder.createSettingsFileError.text"),
                            RESOURCE_BUNDLE.getString("Main.userDataFolder.createFileError.title")
                            + FileOperations.getUserSettingsFile().getPath() + "'",
                            JOptionPane.WARNING_MESSAGE);
                    settings.setCanSaveSettings(false);
                } else {
                    /* po vytvoření souboru je nutné jej uzamknout proti zápisu */
                    fileOperations.setWritable(FileOperations.getUserSettingsFile(), false);
                    settings.setCanSaveSettings(true);
                }
            }
            
            /* pokud je nastaveno automatické načtení databáze pokusí se jí systém načíst */
            if(settings.isAutoLoadDatabase()) {
                /* otestování, zda-li existuje soubor s databází receptů načtený z astavení aplikace */
                if(fileOperations.checkFilePath(FileOperations.getUserDatabaseFile())) {
                    fileOperations.setWritable(FileOperations.getUserDatabaseFile(), true);
                    if(!databaseOperations.readDataFromDatabase(FileOperations.getUserDatabaseFile())) {
                        JOptionPane.showMessageDialog(null,
                                RESOURCE_BUNDLE.getString("Main.userDataFolder.readDatabaseErrorFirst.text") +
                                    "\n\n'" + FileOperations.getUserDatabaseFile().getPath() + "'\n\n" +
                                        RESOURCE_BUNDLE.getString("Main.userDataFolder.readDatabaseErrorSecond.text"),
                                RESOURCE_BUNDLE.getString("Main.userDataFolder.readFileError.title")
                                + FileOperations.getUserDatabaseFile().getPath() + "'",
                                JOptionPane.ERROR_MESSAGE);
                        needCreateOrOpenDB = true;
                    }
                /* pokud neexistuje soubor s databází receptů, zobrazí se okno pro zadání nového názvu */
                } else {
                    if(!settings.getDatabasePath().isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                RESOURCE_BUNDLE.getString("Main.userDataFolder.readDatabaseErrorFirst.text") +
                                "\n\n'" + FileOperations.getUserDatabaseFile().getPath() + "'\n\n" +
                                        RESOURCE_BUNDLE.getString("Main.userDataFolder.readDatabaseErrorSecond.text"),
                                RESOURCE_BUNDLE.getString("Main.userDataFolder.readDatabaseErrorTitle.title"),
                            JOptionPane.ERROR_MESSAGE);
                    }
                    
                    needCreateOrOpenDB = true;
                }
            } else {
                needCreateOrOpenDB = true;
            }
            
            /* není vybráno automatické načtení databáze - zobrazí se dialogové okno 
            pro vytvoření nové databáze a nebo otevření již existující databáze */
            if(needCreateOrOpenDB) {
                OpenOrCreateDatabaseDialog openOrCreateDatabaseDialog = 
                        new OpenOrCreateDatabaseDialog(new JFrame(), true);
                openOrCreateDatabaseDialog.setVisible(true);
                
                /* klikne-li uživatel na "zrušit" - aplikace se ukončí */
                if(openOrCreateDatabaseDialog.getReturnStatus()==OpenOrCreateDatabaseDialog.RET_CANCEL)
                    System.exit(0);
                else {
                    /* pokud je vybráno vytvoření nové databáze */
                    if(OpenOrCreateDatabaseDialog.rbtnNewDatabase.isSelected()) {
                        String databaseName = OpenOrCreateDatabaseDialog.txtfNewDatabase.getText();
                        
                        File newDatabaseFile = new File(FileOperations.getUserDir() + File.separator + 
                            databaseName + FileOperations.getDatabaseExtension());
                        if(!fileOperations.checkFilePath(newDatabaseFile)) {
                            FileOperations.setUserDatabaseFile(databaseName, true);

                            if(!databaseName.equals("")) settings.setDatabaseName(databaseName);
                            else settings.setDatabaseName("jHungryHamster");

                            settings.setDatabasePath(FileOperations.getUserDatabaseFile().toString());
                            settings.setDatabaseInUserDir(true);

                            if(!fileOperations.createFile(FileOperations.getUserDatabaseFile())) {
                                JOptionPane.showMessageDialog(null,
                                        RESOURCE_BUNDLE.getString("Main.userDataFolder.createDatabaseFileError.text"),
                                        RESOURCE_BUNDLE.getString("Main.userDataFolder.createFileError.title")
                                        + FileOperations.getUserDatabaseFile().getPath() + "'",
                                        JOptionPane.ERROR_MESSAGE);
                                System.exit(0);
                            } else {
                                databaseOperations.createNewDatabase(FileOperations.getUserDatabaseFile());
                                fileOperations.setWritable(FileOperations.getUserDatabaseFile(), true);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    RESOURCE_BUNDLE.getString("Main.userDataFolder.createDatabaseFileSameFileError.text"),
                                    RESOURCE_BUNDLE.getString("Main.userDataFolder.createFileError.title")
                                    + newDatabaseFile.getPath() + "'",
                                    JOptionPane.ERROR_MESSAGE);
                            System.exit(0);
                        }
                    } else {
                        /* pokud je vybráno otevření již existující databáze */
                        String databasePath = OpenOrCreateDatabaseDialog.txtfOpenDatabase.getText();
                         
                        /* kontrola správnosti databáze */
                        if(databaseOperations.checkDatabase(new File(databasePath))) {
                            
                            /* kontrola, zda-li se vybraná databáze nachází v uživatelské složce */
                            if(databasePath.startsWith(FileOperations.getCurrentDir() + 
                                    File.separator + FileOperations.getUserDir())) {
                                settings.setDatabaseInUserDir(true);
                            } else settings.setDatabaseInUserDir(false);
                            
                            String databaseName = databasePath.substring(
                                    databasePath.lastIndexOf(File.separator)+1, databasePath.lastIndexOf('.'));

                            /* pokud je databáze v uživatelské složce, nastaví se tak i databázový soubor
                              - bude obsahovat relativní cestu */
                            if(settings.isDatabaseInUserDir()) FileOperations.setUserDatabaseFile(databaseName, true);
                            else FileOperations.setUserDatabaseFile(databasePath, false);

                            settings.setDatabaseName(databaseName);
                            settings.setDatabasePath(FileOperations.getUserDatabaseFile().toString());

                            /* pokus o načtení dat z databáze */
                            if(!databaseOperations.readDataFromDatabase(FileOperations.getUserDatabaseFile())) {
                                JOptionPane.showMessageDialog(null,
                                        RESOURCE_BUNDLE.getString("Main.userDataFolder.readDatabaseErrorFirst.text") +
                                            "\n\n'" + FileOperations.getUserDatabaseFile().getPath() + "'\n\n" +
                                                RESOURCE_BUNDLE.getString("Main.userDataFolder.readDatabaseErrorSecond.text"),
                                        RESOURCE_BUNDLE.getString("Main.userDataFolder.readFileError.title")
                                        + FileOperations.getUserDatabaseFile().getPath() + "'",
                                        JOptionPane.ERROR_MESSAGE);
                                System.exit(0);
                            } else fileOperations.setWritable(FileOperations.getUserDatabaseFile(), true);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    RESOURCE_BUNDLE.getString("Main.userDataFolder.readDatabaseErrorFirst.text") +
                                            "\n\n'" + databasePath + "'\n\n" +
                                            RESOURCE_BUNDLE.getString("Main.userDataFolder.readDatabaseErrorSecond.text"),
                                    RESOURCE_BUNDLE.getString("Main.userDataFolder.readFileError.title")
                                    + databasePath + "'",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
        
        launch(Main.class, args);
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                /* uložení nastavení se provede pouze, když byl soubor s nastavením vytvořen */
                if(settings.isCanSaveSettings()) {
                    /* odemčení souboru pro zápis */
                    fileOperations.setWritable(FileOperations.getUserSettingsFile(), true);
                    
                    /* uložení nastavení do souboru */
                    fileOperations.saveSettingsToXml(FileOperations.getUserSettingsFile());
                    
                    /* uzamčení souboru proti zápisu */
                    fileOperations.setWritable(FileOperations.getUserSettingsFile(), false);
                }
                
                fileOperations.setWritable(FileOperations.getUserDatabaseFile(), false);
            }
        }));
    }
    
//    /**
//     * Metoda nastaví vzhled Substance.
//     * Nutno provést dvakrát, asi nějaká chyba v javě.
//     */
//    public static void useSubstanceLAF() {     
//        LookAndFeel lookAndFeel = Common.getLookAndFeel(settings.getTheme());
//        
//        try {
//            JFrame.setDefaultLookAndFeelDecorated(true);
//            JDialog.setDefaultLookAndFeelDecorated(true);
//            
//            UIManager.setLookAndFeel(lookAndFeel);
//            
//            UIManager.put(LafWidget.TEXT_EDIT_CONTEXT_MENU, Boolean.TRUE);
//            UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);
//        } catch(UnsupportedLookAndFeelException ulafe) {
//            LOGGER.error("Nepodporovaný vzhled.", ulafe);
//        }
//
//        try {
//            JFrame.setDefaultLookAndFeelDecorated(true);
//            JDialog.setDefaultLookAndFeelDecorated(true);
//            
//            UIManager.setLookAndFeel(lookAndFeel);
//            UIManager.put(LafWidget.TEXT_EDIT_CONTEXT_MENU, Boolean.TRUE);
//            UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);
//        } catch(UnsupportedLookAndFeelException ulafe) {
//            LOGGER.error("Nepodporovaný vzhled.", ulafe);
//        }
//    }
}
