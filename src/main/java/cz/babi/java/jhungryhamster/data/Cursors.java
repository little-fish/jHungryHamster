
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
import java.awt.*;
import java.util.HashMap;

/**
 * Class with static references to frequently used cursors.
 *
 * @author ripper, babi
 */
public class Cursors {
    
    private static final String RES = "/res/img/";
    private static final HashMap<String, Cursor> cache = new HashMap<String, Cursor>();
    private static final Toolkit toolKit = Toolkit.getDefaultToolkit();
    
    public static final Cursor ZOOM_IN = get("zoom-in-20.png", "zoom-in");
    
    /** 
     * Return an Cursor of a requested name loaded from default resource directory.
     * Cursors are cached for faster access.
     * 
     * @return Cursor
     */
    public static Cursor get(String name, String cursorName) {
        Cursor cursor = cache.get(name);
        if(cursor==null) {
            ImageIcon imageIcon = new ImageIcon(Cursors.class.getResource(RES + name));
            Image image = imageIcon.getImage();
            cursor = toolKit.createCustomCursor(image, new Point(0, 0), cursorName);
            cache.put(cursorName, cursor);
        }
        
        return cursor;
    }
}
