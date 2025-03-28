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

package com.falsepattern.laggoggles.api;

import com.falsepattern.laggoggles.profiler.ProfileManager;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.profiler.ScanType;

import net.minecraft.command.ICommandSender;

import javax.annotation.Nullable;

import static com.falsepattern.laggoggles.profiler.ProfileManager.LAST_PROFILE_RESULT;
import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;

@SuppressWarnings({"WeakerAccess","unused"})
public class Profiler {

    /**
     * Checks if the profiler is already running.
     * if this value returns true, you shouldn't start profiling.
     */
    public static boolean isProfiling(){
        return PROFILE_ENABLED.get();
    }

    /**
     * Checks if you can start the profiler.
     * In future updates, complexity may increase.
     *
     * This method will update accordingly.
     */
    public static boolean canProfile(){
        return PROFILE_ENABLED.get() == false;
    }

    /**
     * Starts the profiler, and runs it in THIS THREAD!.
     * This is a blocking method, and should NEVER EVER EVER
     * be ran on a minecraft thread. EVER!!!!
     *
     * @param seconds how many seconds to profile
     * @param type the profiling type, either WORLD or FPS
     * @return the result, after the profiler is done.
     * @throws IllegalStateException if the profiler is already running, you should use {@link #canProfile()} before doing this
     */
    public static ProfileResult runProfiler(int seconds, ScanType type, ICommandSender sender) throws IllegalStateException{
        return ProfileManager.runProfiler(seconds, type, sender);
    }

    /**
     * Gets the latest scan result from the profiler. This can be any scan, of any length started by anyone.
     *
     * @return the last scan result, or null, if no scan is performed yet
     */
    public static @Nullable ProfileResult getLatestResult(){
        return LAST_PROFILE_RESULT.get();
    }

}
