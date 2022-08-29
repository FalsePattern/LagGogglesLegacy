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

package com.falsepattern.laggoggles.api.event;

import com.falsepattern.laggoggles.profiler.ProfileResult;
import cpw.mods.fml.common.eventhandler.Event;

public class LagGogglesEvent extends Event {

    private final ProfileResult profileResult;

    /**
     * The base event. Use this if you need to catch any profile result.
     * @param result The profile result
     */
    public LagGogglesEvent(ProfileResult result){
        this.profileResult = result;
    }

    public ProfileResult getProfileResult() {
        return profileResult;
    }

    /**
     * When the client receives a result, this event is created.
     * It runs on connection thread, meaning that it
     * doesn't run on any of the minecraft threads. (Async)
     *
     * If you need to perform any action based on this result, make sure
     * that you do it in the Minecraft thread, and NOT this one.
     *
     * Fired on the FMLCommonHandler.instance().bus().
     */
    public static class ReceivedFromServer extends LagGogglesEvent{
        public ReceivedFromServer(ProfileResult result){
            super(result);
        }
    }

    /**
     * When the profiler is finished, this event is created.
     * It runs on the thread that created the profiler, meaning that it
     * doesn't run on any of the minecraft threads. (Async)
     *
     * If you need to perform any action based on this result, make sure
     * that you do it in the Minecraft thread, and NOT this one.
     *
     * Fired on the FMLCommonHandler.instance().bus().
     */
    public static class LocalResult extends LagGogglesEvent{
        public LocalResult(ProfileResult result){
            super(result);
        }
    }
}
