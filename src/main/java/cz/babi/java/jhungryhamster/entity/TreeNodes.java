
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Třída zastupující entitu TreeNodes, neboli jednotlivé uzly stromu.
 * Uchovává v sobě seznam seznam všech uzlů.
 * 
 * @author babi
 */
public class TreeNodes {
    
    /* sdílená instance */
    private static final TreeNodes instance = new TreeNodes();

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeNodes.class);
    private ArrayList<Node> nodes = new ArrayList<Node> ();
    
    /**
     * Privátní konstruktor třídy Settings.
     */
    private TreeNodes() {}
    
    /**
     * Metoda pro získání instance třídy TreeNode.
     * Použitý návrhový vzor - <b>Singleton</b>.
     * 
     * @return Instance třídy TreeNode
     */
    public static TreeNodes getInstance() {
        return instance;
    }
    
    /**
     * Metoda vrací všechny uzly.
     * 
     * @return List všech uyly
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }
    
    /**
     * Metoda vrací Uzel podle vstupního indexu.
     * Vstupní index != ID uzlu.
     * Indexem je myšlena pozice v listu.
     * 
     * @param index hledaného uzlu
     * @return Uzel
     */
    public Node getNode(int index) {
        return nodes.get(index);
    }
    
    /**
     * Metoda pro přidání jednoho uzlu.
     * 
     * @param node Uzel pro přidání
     */
    public void addNode(Node node) {
        this.nodes.add(node);
        
        LOGGER.debug("New category {} has been created.", node.getTitle());
    }
    
    /**
     * Metoda pro odstranění několika uzlů.
     * 
     * @param nodes kolekce uzlů pro ostranění
     */
    public void removeNodes(Collection<Node> nodes) {
        for(Node node : nodes) {
            this.nodes.remove(node);
            
            LOGGER.debug("Category {} has been removed.", node.getTitle());
        }
        
        LOGGER.debug("{} categories have been removed.", nodes.size());
    }
    
    /**
     * Metoda pro odstranění všech uzlů.
     */
    public void removeAllNodes() {
        int size = nodes.size();
        this.nodes.clear();
        
        LOGGER.debug("{} categories have been removed.", size);
    }
    
    /**
     * Třída zastupující entitu Node.
     * Jedná se o jednotlivé uzly ve stromu.
     *
     * @author babi
     */
    public static class Node implements Serializable {
        
        private String title;
        private int idNode;
        private int idParent;
        
        /**info
         * Konstruktor třídy Node.
         * 
         * @param title 
         */
        public Node(String title, int idNode, int idParent) {
            this.title = title;
            this.idNode = idNode;
            this.idParent = idParent;
        }

        /**
         * Konstruktor třídy Node.
         * 
         * @param title 
         */
        public Node(String title) {
            this.title = title;
        }

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
         * @return the idNode
         */
        public int getIdNode() {
            return idNode;
        }

        /**
         * @return the idParent
         */
        public int getIdParent() {
            return idParent;
        }
        
        @Override
        public String toString() {
            return this.getTitle();
        }

        /**
         * @param idNode the idNode to set
         */
        public void setIdNode(int idNode) {
            this.idNode = idNode;
        }

        /**
         * @param idParent the idParent to set
         */
        public void setIdParent(int idParent) {
            this.idParent = idParent;
        }
    }
}
