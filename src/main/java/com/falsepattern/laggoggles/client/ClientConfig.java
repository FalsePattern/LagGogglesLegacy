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

package com.falsepattern.laggoggles.client;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.util.ColorBlindMode;
import com.falsepattern.lib.config.Config;
import com.falsepattern.lib.config.ConfigurationManager;

@Config(modid = Tags.MOD_ID, category = "client")
public class ClientConfig {
    @Config.Comment("Define the number of microseconds at which an object is marked with a deep red colour for WORLD lag.")
    @Config.LangKey("config.laggoggles.client.gradientworld")
    @Config.DefaultInt(25)
    @Config.RangeInt(min = 0)
    public static int GRADIENT_MAXED_OUT_AT_MICROSECONDS;

    @Config.Comment("Define the number of nanoseconds at which an object is marked with a deep red colour for FPS lag.")
    @Config.LangKey("config.laggoggles.client.gradientfps")
    @Config.DefaultInt(50000)
    @Config.RangeInt(min = 0)
    public static int GRADIENT_MAXED_OUT_AT_NANOSECONDS_FPS;

    @Config.Comment("What is the minimum amount of microseconds required before an object is tracked in the client?\n" +
                    "This is only for WORLD lag.\n" +
                    "This also affects the analyze results window")
    @Config.LangKey("config.laggoggles.client.minmicros")
    @Config.DefaultInt(1)
    @Config.RangeInt(min = 0)
    public static int MINIMUM_AMOUNT_OF_MICROSECONDS_THRESHOLD;

    @Config.Comment("If you're colorblind, change this to fit your needs.")
    @Config.LangKey("config.laggoggles.client.colors")
    @Config.DefaultEnum("GREEN_TO_RED")
    public static ColorBlindMode COLORS;

    static {
        ConfigurationManager.selfInit();
    }

    //This is here to force the class to load
    public static void init() {

    }
}
