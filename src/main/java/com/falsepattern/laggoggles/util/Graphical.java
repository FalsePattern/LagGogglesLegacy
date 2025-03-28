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

import com.falsepattern.laggoggles.client.ClientConfig;

public class Graphical {

    public static final String mu = "\u00B5";

    public static String formatClassName(String in){
        return in.startsWith("class ") ? in.substring(6) : in;
    }

    public static final int RED_CHANNEL   = 0;
    public static final int GREEN_CHANNEL = 1;
    public static final int BLUE_CHANNEL  = 2;

    public static double[] heatToColor(double heat){
        return ClientConfig.COLORS.heatToColor(heat);
    }

    public static int RGBtoInt(double[] rgb){
        int R = (int) (rgb[0] * 255);
        R = (R << 8) + (int) (rgb[1] * 255);
        return (R << 8) + (int) (rgb[2] * 255);
    }
}
