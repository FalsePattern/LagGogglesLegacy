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

package com.falsepattern.laggoggles.server;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.lib.config.Config;
import com.falsepattern.lib.config.ConfigurationManager;

@Config(modid = Tags.MODID, category = "server")
public class ServerConfig {

    @Config.Comment("What's the permission level available to non-operators (Normal players)?\n" +
                    "Please note that this ONLY works on dedicated servers. If you're playing singleplayer or LAN, the FULL permission is used.\n" +
                    "Available permissions in ascending order are:\n" +
                    "   'NONE'  No permissions are granted, all functionality is denied.\n" +
                    "   'GET'   Allow getting the latest scan result, this will be stripped down to the player's surroundings\n" +
                    "   'START' Allow starting the profiler\n" +
                    "   'FULL'  All permissions are granted, teleporting to entities, blocks")
    public static Perms.Permission NON_OP_PERMISSION_LEVEL = Perms.Permission.START;

    @Config.Comment("Allow normal users to see event subscribers?")
    public static boolean ALLOW_NON_OPS_TO_SEE_EVENT_SUBSCRIBERS = false;

    @Config.Comment("If normal users can start the profiler, what is the maximum time in seconds?")
    public static int NON_OPS_MAX_PROFILE_TIME = 20;

    @Config.Comment("If normal users can start the profiler, what is the cool-down between requests in seconds?")
    public static int NON_OPS_PROFILE_COOL_DOWN_SECONDS = 120;

    @Config.Comment("What is the maximum HORIZONTAL range in blocks normal users can get results for?")
    public static float NON_OPS_MAX_HORIZONTAL_RANGE = 50;

    @Config.Comment("What is the maximum VERTICAL range in blocks normal users can get results for?")
    public static float NON_OPS_MAX_VERTICAL_RANGE = 20;

    @Config.Comment("From where should we range-limit blocks vertically for normal users?\n" +
                    "This will override the MAX_VERTICAL_RANGE when the block is above this Y level")
    public static int NON_OPS_WHITELIST_HEIGHT_ABOVE = 64;

    @Config.Comment("How often can normal users request the latest scan result in seconds?")
    public static int NON_OPS_REQUEST_LAST_SCAN_DATA_TIMEOUT = 30;

    static {
        ConfigurationManager.selfInit();
    }

    //This is here to force the class to load
    public static void init() {

    }
}
