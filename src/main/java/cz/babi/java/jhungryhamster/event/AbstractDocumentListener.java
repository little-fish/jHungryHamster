
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

package cz.babi.java.jhungryhamster.event;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** 
 * Abstract class for DocumentListener. For all DocumentListener's mandatory methods
 * executes method onUpdate(DocumentEvent e).
 *
 * @author ripper
 */
public abstract class AbstractDocumentListener implements DocumentListener {

    @Override
    public void insertUpdate(DocumentEvent e) {
        onUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        onUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        onUpdate(e);
    }

    /** Method executed on all document updates */
    public abstract void onUpdate(DocumentEvent e);
}