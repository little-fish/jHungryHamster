
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

/**
 * Výčet možných rozlišení obrázku.
 * @author babi
 */
public enum ImageResolution {
    ORIGINAL("originál", 0, 0),
    LOW("320 x 240", 320, 240),
    MED("640 x 480", 640, 480),
    MED_PLUS("800 x 600", 800, 600),
    HIGH("1024 x 768", 1024, 768),
    HIGH_PLUS("1280 x 1024", 1280, 1024);
    
    private String resolution;
    private int width;
    private int height;
    
    private ImageResolution(String resolution, int width, int height) {
        this.resolution = resolution;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return getResolution();
    }

    /**
     * @return the resolution
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param resolution the resolution to set
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
