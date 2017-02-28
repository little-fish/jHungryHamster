
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

import javax.swing.*;
import java.util.HashMap;

/**
 * Class with static references to frequently used icons.
 *
 * @author ripper, babi
 */
public class Icons {
    private static final String RES = "/res/img/";
    private static final HashMap<String, ImageIcon> cache = new HashMap<>();
    
    public static final ImageIcon ARROW_DOWN = get("control-down-20.png");
    public static final ImageIcon ARROW_UP = get("control-up-20.png");
    public static final ImageIcon ADD_NEW_RECIPE = get("recipe-new-32.png");
    public static final ImageIcon ADD_NEW_FOLDER = get("add-folder-32.png");
    public static final ImageIcon EDIT_SELECTED = get("edit-32.png");
    public static final ImageIcon DELETE_SELECTED = get("delete-32.png");
    public static final ImageIcon DELETE_RECIPE_IMAGE = get("delete-17.png");
    public static final ImageIcon PRINT_SELECTED = get("print-32.png");
    public static final ImageIcon ZOOM_IN = get("zoom-in-24-color.png");
    public static final ImageIcon ZOOM_OUT = get("zoom-out-24-color.png");
    public static final ImageIcon LOGOUT = get("exit-20.png");
    public static final ImageIcon TASK_IDLE = get("task-idle.png");
    public static final ImageIcon TASK_BUSY = get("task-busy.gif");
    public static final ImageIcon STAR_FULL = get("star-full-20.png");
    public static final ImageIcon STAR_HALF = get("star-half-20.png");
    public static final ImageIcon STAR_EMPTY = get("star-empty-20.png");
    public static final ImageIcon TREE_CATEGORY = get("folder-16.png");
    public static final ImageIcon TREE_RECIPE = get("breakfast-16.png");
    public static final ImageIcon EDIT_DATABASE = get("edit-16.png");
    public static final ImageIcon SETTINGS = get("settings-32.png");
    public static final ImageIcon SETTINGS_MENU = get("settings-20.png");
    public static final ImageIcon RECIP_NO_IMAGE = get("breakfast-128.png");
    public static final ImageIcon TREE_ROOT = get("root-16.png");
    public static final ImageIcon DATABASE_INFO_MENU = get("database-info-20.png");
    public static final ImageIcon SUPPORT_MENU = get("support-20.png");
    public static final ImageIcon COPPYRIGHT = get("copyright-13.png");
    public static final ImageIcon PAY_PAL_DONATE = get("paypal-donate-150.png");
    public static final ImageIcon HAMSTER_LOGO = get("hamster-logo-250.png");
    public static final ImageIcon STATUS_DATABASE = get("database-16.png");
    public static final ImageIcon STATUS_DATABASE_NEW = get("database-new-16.png");
    public static final ImageIcon STATUS_DATABASE_DELETE = get("database-delete-16.png");
    public static final ImageIcon STATUS_DATABASE_OPEN = get("database-open-16.png");
    public static final ImageIcon STATUS_BULB = get("bulb-16.png");
    public static final ImageIcon STATUS_CATEGORY_DELETE = get("category-delete-16.png");
    public static final ImageIcon STATUS_RECIPE_DELETE = get("recipe-delete-16.png");
    public static final ImageIcon STATUS_CATEGORY_NEW = get("category-new-16.png");
    public static final ImageIcon STATUS_RECIPE_NEW = get("recipe-new-16.png");
    public static final ImageIcon STATUS_CATEGORY_EDIT = get("category-edit-16.png");
    public static final ImageIcon STATUS_RECIPE_EDIT = get("recipe-edit-16.png");
    public static final ImageIcon STATUS_SEARCH = get("search-16.png");
    public static final ImageIcon STATUS_WARNING = get("warning-16.png");
    
    /** 
     * Return an ImageIcon of a requested name loaded from default resource directory.
     * Icons are cached for faster access.
     * 
     * @return Icon
     */
    public static ImageIcon get(String name) {
        ImageIcon icon = cache.get(name);
        if(icon==null) {
            icon = new ImageIcon(Icons.class.getResource(RES + name));
            cache.put(name, icon);
        }
        
        return icon;
    }
}
