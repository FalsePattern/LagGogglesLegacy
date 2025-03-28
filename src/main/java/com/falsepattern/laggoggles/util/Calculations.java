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
import com.falsepattern.laggoggles.client.gui.GuiScanResultsWorld;
import com.falsepattern.laggoggles.packet.ObjectData;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.profiler.TimingManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static com.falsepattern.laggoggles.util.Graphical.mu;

@SideOnly(Side.CLIENT)
public class Calculations {

    public static final double NANOS_IN_A_TICK = 50000000;

    public static double heat(long nanos, ProfileResult result) {
        return Math.min((muPerTick(nanos, result) / ClientConfig.GRADIENT_MAXED_OUT_AT_MICROSECONDS) * 100, 100);
    }

    public static double heatThread(GuiScanResultsWorld.LagSource source, ProfileResult result){
        if(source.data.type != ObjectData.Type.EVENT_BUS_LISTENER){
            throw new IllegalArgumentException("Expected heat calculation for thread, not " + source.data.type);
        }
        TimingManager.EventTimings.ThreadType type = TimingManager.EventTimings.ThreadType.values()[source.data.<Integer>getValue(ObjectData.Entry.EVENT_BUS_THREAD_TYPE)];
        if(type == TimingManager.EventTimings.ThreadType.CLIENT) {
            return Math.min(((double) source.nanos / (double) result.getTotalTime()) * 100,100);
        }else if (type == TimingManager.EventTimings.ThreadType.ASYNC){
            return  0;
        }else if(type == TimingManager.EventTimings.ThreadType.SERVER){
            return Math.floor((source.nanos / result.getTickCount()) / NANOS_IN_A_TICK * 10000) / 100d;
        }else{
            throw new IllegalStateException("Terminator_NL forgot to add code here... Please submit an issue at github!");
        }
    }

    public static double heatNF(long nanos, ProfileResult result) {
        return Math.min(((double) nanos/(double) result.getTotalFrames() / (double) ClientConfig.GRADIENT_MAXED_OUT_AT_NANOSECONDS_FPS) * 100D, 100);
    }

    public static String NFString(long nanos, long frames) {
        long nf = nanos/frames;
        if(nf > 1000) {
            return nf/1000+"k ns/F";
        }else{
            return nf +" ns/F";
        }
    }

    public static String NFStringSimple(long nanos, long frames) {
        return nanos/frames + " ns/F";
    }

    public static String tickPercent(long nanos, ProfileResult result) {
        if(result == null || result.getTickCount() == 0){
            return "?";
        }
        return Math.floor((nanos / result.getTickCount()) / NANOS_IN_A_TICK * 10000) / 100d + "%";
    }

    public static String nfPercent(long nanos, ProfileResult result) {
        if(result == null || result.getTotalFrames() == 0){
            return "?";
        }
        return Math.floor((nanos / (double) result.getTotalTime()) * 10000D) / 100D + "%";
    }

    public static double muPerTick(long nanos, ProfileResult result) {
        if(result == null){
            return 0;
        }
        return (nanos / result.getTickCount()) / 1000;
    }

    public static double muPerTickCustomTotals(long nanos, long totalTicks) {
        return (nanos / totalTicks) / 1000;
    }

    public static String muPerTickString(long nanos, ProfileResult result) {
        if(result == null){
            return "?";
        }
        return Double.valueOf((nanos / result.getTickCount()) / 1000).intValue() + " " + mu + "s/t";
    }

}