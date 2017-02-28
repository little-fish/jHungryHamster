
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

import java.io.Serializable;

/**
 * Výčet možných hodnocení.
 * @author babi
 */
public enum Rating implements Serializable {
    ZERO(0),
    ZERO_AND_HALF(1),
    ONE(2),
    ONE_AND_HALF(3),
    TWO(4),
    TWO_AND_HALF(5),
    THREE(6),
    THREE_AND_HALF(7),
    FOUR(8),
    FOUR_AND_HALF(9),
    FIVE(10);
    
    private int rating;
    
    private Rating(int rating) {
        this.rating = rating;
    }
}
