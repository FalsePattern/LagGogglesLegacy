/*
 * Lag Goggles: Legacy
 *
 * Copyright (C) 2022 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice, this permission notice and the word "SNEED"
 * shall be included in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.laggoggles.client;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.util.ColorBlindMode;
import com.falsepattern.lib.config.Config;
@Config(modid = Tags.MODID, category = "client")
public class ClientConfig {

    @Config.Comment("Define the number of microseconds at which an object is marked with a deep red colour for WORLD lag.")
    @Config.LangKey("config.laggoggles.client.gradientworld")
    @Config.RangeInt(min = 0)
    public static int GRADIENT_MAXED_OUT_AT_MICROSECONDS = 25;

    @Config.Comment("Define the number of nanoseconds at which an object is marked with a deep red colour for FPS lag.")
    @Config.LangKey("config.laggoggles.client.gradientfps")
    @Config.RangeInt(min = 0)
    public static int GRADIENT_MAXED_OUT_AT_NANOSECONDS_FPS = 50000;

    @Config.Comment("What is the minimum amount of microseconds required before an object is tracked in the client?\n" +
                    "This is only for WORLD lag.\n" +
                    "This also affects the analyze results window")
    @Config.LangKey("config.laggoggles.client.minmicros")
    @Config.RangeInt(min = 0)
    public static int MINIMUM_AMOUNT_OF_MICROSECONDS_THRESHOLD = 1;

    @Config.Comment("If you're colorblind, change this to fit your needs.")
    @Config.LangKey("config.laggoggles.client.colors")
    public static ColorBlindMode COLORS = ColorBlindMode.GREEN_TO_RED;
}
