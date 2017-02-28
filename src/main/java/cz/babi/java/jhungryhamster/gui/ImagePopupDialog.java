
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

package cz.babi.java.jhungryhamster.gui;

import static cz.babi.java.jhungryhamster.utils.Common.RESOURCE_BUNDLE;

import cz.babi.java.jhungryhamster.data.Icons;
import cz.babi.java.jhungryhamster.entity.CookBook.Recipe;
import cz.babi.java.jhungryhamster.utils.Common;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Třída, která se stará o zobrazení obrázku receptu.
 * @author babi
 */
public class ImagePopupDialog extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    
    private JLabel lblZoomIn;
    private JLabel lblZoomOut;
    
    /* proměnné ve kterých je uložena maximální šířka a výška obrázku tak,
     aby nebyl vidět scrollbar */
    private int originalWidth, originalHeight;
    
    /* proměnné, ve kterých je uložena pozice vertikálního a horizontálního
     scrollbaru (použité v zachycení události posunu ver., nebo hor. scrollbarem) */
    private int oldVPos = 0, oldHPos = 0;
    
    /* proměnná se použije kdy se bude chtít táhnout myši a posouvat obrázek, 
     nastaví se při prvním kliknu na hodnotu aktuální polohy kurzoru a při 
     pohybu se bude jen dopočítávat rozdíl tohoto bodu a aktuální polohy při 
     tažení myši */
    private Point clickedPoint;
    
    private final Recipe recipe;

    /** Creates new form ImagePopupDialog */
    public ImagePopupDialog(JFrame parent, boolean modal, Recipe recipe) {
        super(parent, modal);
        
        this.recipe = recipe;
        
        initComponents();
        
        this.setTitle(RESOURCE_BUNDLE.getString("ImagePopupDialog.title") + recipe.getTitle());
        this.setLocationRelativeTo(null);
        
        // Close the dialog when Esc is pressed
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                    doClose(RET_CANCEL);
                }
            }
        });
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }
    
    /**
     * Nutno zamezit automatické změně velikosti okna když je obrázek příliš velký
     */
    private void initMyComponent() {
        double frameWidth, frameHeight;
        boolean needResizeImage = true;
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
        /* pokud je obrázek příliš široký, nastaví se max. šířka okna na 75%
         * celkové velikosti obrazovky */
        if(recipe.getRecipeImage().getWidth()>screenSize.width/1.333)
            frameWidth = screenSize.width/1.333;
        else {
            needResizeImage = false;
            frameWidth = recipe.getRecipeImage().getWidth();
        }
        
        /* pokud je obrázek příliš vysoký, nastaví se max. výška okna na 75%
         * celkové velikosti obrazovky */
        if(recipe.getRecipeImage().getHeight()>screenSize.height/1.333)
            frameHeight = screenSize.height/1.333;
        else {
            needResizeImage = false;
            frameHeight = recipe.getRecipeImage().getHeight();
        }
        
        /* pokud je obrázek příliš velký, je nutné jen zmenšit, aby se mohl celý
         * zobrazit bez nutnosti scrollbaru;
         * přidají se také tlačítka pro zvětšení a zmenšení obrázku */
        if(needResizeImage) {
            lblZoomIn = new javax.swing.JLabel(Icons.ZOOM_IN);
            lblZoomIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblZoomIn.setToolTipText(RESOURCE_BUNDLE.getString("ImagePopupDialog.lblZoomIn.tooltip"));
            lblZoomIn.setBounds((int)frameWidth-63, 15, 24, 24);
            lblZoomIn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if(lblZoomIn.isEnabled()) lblZoomInMouseClicked(e);
                }
            });
            
            lblZoomOut = new javax.swing.JLabel(Icons.ZOOM_OUT);
            lblZoomOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblZoomOut.setToolTipText(RESOURCE_BUNDLE.getString("ImagePopupDialog.lblZoomOut.tooltip"));
            lblZoomOut.setBounds((int)frameWidth-97, 15, 24, 24);
            lblZoomOut.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if(lblZoomOut.isEnabled()) lblZoomOutMouseClicked(e);
                }
            });
            lblZoomOut.setEnabled(false);
            
            originalWidth = (int)frameWidth;
            originalHeight = (int)frameHeight;
            
            pnlBackground.setUserBackground(
                    Common.getScaledImage((int)frameWidth, (int)frameHeight, recipe.getRecipeImage()));
            
            pnlBackground.setLayout(null);

            pnlBackground.add(lblZoomOut);
            pnlBackground.add(lblZoomIn);
            
            pnlBackground.repaint();
        }
        
        pnlBackground.setPreferredSize(new Dimension((int)frameWidth, (int)frameHeight));

        jspBackground.setSize((int)frameWidth, (int)frameHeight);
        
        AdjustmentListener adjustmentListener = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int vPos = jspBackground.getVerticalScrollBar().getValue();
                int hPos = jspBackground.getHorizontalScrollBar().getValue();
  
                if(e.getSource().equals(jspBackground.getVerticalScrollBar()) && vPos != oldVPos) {                    
                    lblZoomIn.setBounds(lblZoomIn.getLocation().x, lblZoomIn.getLocation().y+(vPos-oldVPos), 24, 24);
                    lblZoomOut.setBounds(lblZoomOut.getLocation().x, lblZoomOut.getLocation().y+(vPos-oldVPos), 24, 24);
                    
                    oldVPos = vPos;  
                }  
                
                if(e.getSource().equals(jspBackground.getHorizontalScrollBar()) && hPos != oldHPos) {  
                    lblZoomIn.setBounds(lblZoomIn.getLocation().x+(hPos-oldHPos), lblZoomIn.getLocation().y, 24, 24);
                    lblZoomOut.setBounds(lblZoomOut.getLocation().x+(hPos-oldHPos), lblZoomOut.getLocation().y, 24, 24);
                    
                    oldHPos = hPos;  
                } 
            }
        };
        
        jspBackground.setAutoscrolls(true);
        
        jspBackground.getHorizontalScrollBar().addAdjustmentListener(adjustmentListener);
        jspBackground.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
        
        /* oba následující listenery jsou zatím zbytečné, nějak se to seká a 
         v momentě vytváření aplikace jsem to už neřešil */
//        jspBackground.getViewport().getView().addMouseMotionListener(new MouseAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                super.mouseDragged(e);
//                jspBackgroundMouseDragged(e);
//            }
//        });
//        
//        jspBackground.getViewport().getView().addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
//                clickedPoint = e.getPoint();
//            }
//        });
    }
    
    /**
     * Metoda se volá při zachycení události při kliknutí na ikonku zvětšení obrázku.
     * @param me Zachycená událost
     */
    private void lblZoomInMouseClicked(MouseEvent me) {
        boolean needOriginalSize = false;
        int newWidthToSet, newHeightToSet;

        int newExpectedWidth = pnlBackground.getUsetBackground().getWidth() +
                (int)(pnlBackground.getUsetBackground().getWidth()*0.05);
        int newExpectedHeight = pnlBackground.getUsetBackground().getHeight() +
                (int)(pnlBackground.getUsetBackground().getHeight()*0.05);

        if(newExpectedWidth>recipe.getRecipeImage().getWidth()) needOriginalSize = true;
        if(newExpectedHeight>recipe.getRecipeImage().getHeight()) needOriginalSize = true;

        if(needOriginalSize) {
            newWidthToSet = recipe.getRecipeImage().getWidth();
            newHeightToSet = recipe.getRecipeImage().getHeight();
            lblZoomIn.setEnabled(false);
        } else {
            newWidthToSet = newExpectedWidth;
            newHeightToSet = newExpectedHeight;
            lblZoomOut.setEnabled(true);
        }
        
        pnlBackground.setUserBackground(Common.getScaledImage(
                    newWidthToSet, newHeightToSet, recipe.getRecipeImage()));
        
        pnlBackground.setPreferredSize(new Dimension(newWidthToSet, newHeightToSet));
        pnlBackground.setSize(new Dimension(newWidthToSet, newHeightToSet));
    }
    
    /**
     * Metoda se volá při zachycení události při kliknutí na ikonku zmenšení obrázku.
     * @param me Zachycená událost
     */
    private void lblZoomOutMouseClicked(MouseEvent me) {
        boolean needOriginalSize = false;
        int newWidthToSet, newHeightToSet;

        int newExpectedWidth = pnlBackground.getUsetBackground().getWidth() -
                (int)(pnlBackground.getUsetBackground().getWidth()*0.05);
        int newExpectedHeight = pnlBackground.getUsetBackground().getHeight() -
                (int)(pnlBackground.getUsetBackground().getHeight()*0.05);

        if(newExpectedWidth<=originalWidth) needOriginalSize = true;
        if(newExpectedHeight<=originalHeight) needOriginalSize = true;

        if(needOriginalSize) {
            newWidthToSet = originalWidth;
            newHeightToSet = originalHeight;
            lblZoomOut.setEnabled(false);
        } else {
            newWidthToSet = newExpectedWidth;
            newHeightToSet = newExpectedHeight;
            lblZoomIn.setEnabled(true);
        }
        
        pnlBackground.setUserBackground(Common.getScaledImage(
                    newWidthToSet, newHeightToSet, recipe.getRecipeImage()));
        
        pnlBackground.setPreferredSize(new Dimension(newWidthToSet, newHeightToSet));
        pnlBackground.setSize(new Dimension(newWidthToSet, newHeightToSet));
    }
    
    /**
     * Metoda se volá při zachycení události táhnutí.
     * @param me Zachycená událost.
     */
    private void jspBackgroundMouseDragged(MouseEvent me) {
        boolean canMove = true;

        int newVerticalPosition = jspBackground.getViewport().getViewPosition().x+(clickedPoint.x-me.getPoint().x);
        int newHorizontalPosition = jspBackground.getViewport().getViewPosition().y+(clickedPoint.y-me.getPoint().y);
        
        if(newVerticalPosition<0) canMove = false;
        if(newVerticalPosition>(pnlBackground.getUsetBackground().getHeight()-originalHeight+16)) canMove = false;
        
        if(newHorizontalPosition<0) canMove = false;
        if(newHorizontalPosition>(pnlBackground.getUsetBackground().getWidth()-originalWidth+16)) canMove = false;
        
        if(canMove) jspBackground.getViewport().setViewPosition(new Point(
                newVerticalPosition,
                newHorizontalPosition));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jspBackground = new javax.swing.JScrollPane();
        pnlBackground = new MyPanel(recipe.getRecipeImage());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jspBackground.setBorder(null);
        jspBackground.setName("jspBackground");

        pnlBackground.setBorder(null);
        pnlBackground.setToolTipText("Obrázek receptu");
        pnlBackground.setName("pnlBackground");
	pnlBackground.setDoubleBuffered(true);

        jspBackground.setViewportView(pnlBackground);
        
	initMyComponent();

	this.add(jspBackground);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jspBackground;
    private MyPanel pnlBackground;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
    
    /**
     * Třída byla vytvořena aby šlo pohodlně vykreslit obrázek na pozadí JPanelu.
     */
    class MyPanel extends JPanel {
        
        private BufferedImage background;
        
        public MyPanel(BufferedImage background) {
            super();
            this.background = background;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, this);
        }
        
        /**
         * Nastaví obrázek na pozadí.
         * @param background Obrázek která má být nastaven jako pozadí.
         */
        public void setUserBackground(BufferedImage background) {
            this.background = background;
            repaint();
        }
        
        /**
         * Vrátí obrázek, který je nastaven jako pozadí panelu.
         * @return Pozadí panelu.
         */
        public BufferedImage getUsetBackground() {
            return this.background;
        }
    }
}
