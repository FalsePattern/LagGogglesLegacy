/*
 * LagGoggles: Legacy
 *
 * Copyright (C) 2022-2025 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, only version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.laggoggles.util;

import static com.falsepattern.laggoggles.util.Graphical.BLUE_CHANNEL;
import static com.falsepattern.laggoggles.util.Graphical.GREEN_CHANNEL;
import static com.falsepattern.laggoggles.util.Graphical.RED_CHANNEL;

public enum ColorBlindMode {

    GREEN_TO_RED(GREEN_CHANNEL, RED_CHANNEL, BLUE_CHANNEL),
    BLUE_TO_RED(BLUE_CHANNEL, RED_CHANNEL, GREEN_CHANNEL),
    GREEN_TO_BLUE(GREEN_CHANNEL, BLUE_CHANNEL, RED_CHANNEL);

    private final int bad;
    private final int good;
    private final int neutral;

    ColorBlindMode(int good, int bad, int neutral){
        this.bad = bad;
        this.good = good;
        this.neutral = neutral;
    }

    public double[] heatToColor(double heat){
        double[] rgb = new double[3];
        rgb[neutral] = 0;

        if(heat < 50){
            rgb[bad] = (heat / 50);
            rgb[good] = 1;
            return rgb;
        }else if(heat == 50){
            rgb[bad] = 1;
            rgb[good] = 1;
            return rgb;
        }else{
            rgb[bad] = 1;
            rgb[good] = 1 - ((heat-50) / 50);
            return rgb;
        }
    }

}
