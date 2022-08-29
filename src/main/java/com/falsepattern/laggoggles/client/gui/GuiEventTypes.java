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

package com.falsepattern.laggoggles.client.gui;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.ServerDataPacketHandler;
import com.falsepattern.laggoggles.packet.ObjectData;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.profiler.ScanType;
import com.falsepattern.laggoggles.profiler.TimingManager;
import com.falsepattern.laggoggles.util.Calculations;
import com.falsepattern.laggoggles.util.Graphical;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.lib.text.FormattedText;
import cpw.mods.fml.client.GuiScrollingList;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

import java.util.Collections;
import java.util.TreeSet;

public class GuiEventTypes extends GuiScrollingList {

    final private FontRenderer FONTRENDERER;
    private static final int slotHeight = 24;
    private int COLUMN_WIDTH_NANOS = 0;
    private TreeSet<GuiScanResultsWorld.LagSource> DATA = new TreeSet<>(Collections.reverseOrder());
    private final ProfileResult result;

    public GuiEventTypes(Minecraft client, int width, int height, int top, int bottom, int left, ProfileResult result) {
        super(client, width, height, top, bottom, left, slotHeight);
        FONTRENDERER = client.fontRenderer;
        this.result = result;

        for(GuiScanResultsWorld.LagSource src : result.getLagSources()){
            if(src.data.type == ObjectData.Type.EVENT_BUS_LISTENER){
                TimingManager.EventTimings.ThreadType type = TimingManager.EventTimings.ThreadType.values()[src.data.<Integer>getValue(ObjectData.Entry.EVENT_BUS_THREAD_TYPE)];
                if((result.getType() == ScanType.FPS && type == TimingManager.EventTimings.ThreadType.CLIENT) || (result.getType() == ScanType.WORLD && type != TimingManager.EventTimings.ThreadType.CLIENT)){


                    /* This removes the LagGoggles tooltip from the results, as it's only visible while profiling, it's clutter. */
                    if(src.data.<String>getValue(ObjectData.Entry.EVENT_BUS_LISTENER).contains(Tags.MODID)){
                        if(src.data.<String>getValue(ObjectData.Entry.EVENT_BUS_EVENT_CLASS_NAME).equals(Graphical.formatClassName(net.minecraftforge.client.event.RenderGameOverlayEvent.Post.class.toString()))) {
                            continue;
                        }
                    }

                    DATA.add(src);
                    COLUMN_WIDTH_NANOS = Math.max(COLUMN_WIDTH_NANOS, FONTRENDERER.getStringWidth(getMuStringFor(src)));
                }
            }
        }

    }


    @Override
    protected int getSize() {
        if((ServerDataPacketHandler.NON_OPS_CAN_SEE_EVENT_SUBSCRIBERS == false && result.getType() == ScanType.WORLD ) && ServerDataPacketHandler.PERMISSION.ordinal() < Perms.Permission.FULL.ordinal()){
            return 1;
        }else {
            return DATA.size();
        }
    }

    @Override
    protected void elementClicked(int slot, boolean doubleClick) {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {


    }

    public void displayCantSeeResults(int slotTop){
        val lines = I18n.format("gui.laggoggles.text.cantseeresults", '\n').split("\n");
        for (int i = 0; i < lines.length; i++) {
            drawString(lines[i], left + 10, slotTop + i * 12, 0x4C4C4C);
        }
    }

    @Override
    protected void drawSlot(int slot, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if((ServerDataPacketHandler.NON_OPS_CAN_SEE_EVENT_SUBSCRIBERS == false && result.getType() == ScanType.WORLD ) && ServerDataPacketHandler.PERMISSION.ordinal() < Perms.Permission.FULL.ordinal()){
            displayCantSeeResults(slotTop);
            return;
        }
        if(slot > DATA.size()){
            return;
        }
        GuiScanResultsWorld.LagSource lagSource = DATA.descendingSet().toArray(new GuiScanResultsWorld.LagSource[0])[slot];
        int threadColor = 0x00FF00;
        String threadType = "(" + I18n.format("gui.laggoggles.text.threadtype.async") + ")";
        switch (TimingManager.EventTimings.ThreadType.values()[lagSource.data.<Integer>getValue(ObjectData.Entry.EVENT_BUS_THREAD_TYPE)]){
            case CLIENT:
                threadType = "(" + I18n.format("gui.laggoggles.text.threadtype.gui") + ")";
                threadColor = 0xFF0000;
                break;
            case SERVER:
                threadType = "(" + I18n.format("gui.laggoggles.text.threadtype.server") + ")";
                threadColor = 0xFF0000;
                break;
        }
        double heat = Calculations.heatThread(lagSource, result);
        double[] RGB = Graphical.heatToColor(heat);
        int color = Graphical.RGBtoInt(RGB);

        /* times */
        drawStringToLeftOf(getMuStringFor(lagSource),left + COLUMN_WIDTH_NANOS + 5, slotTop, color);

        /* Percent */
        String percentString = getPercentStringFor(lagSource);
        drawString(percentString, left + COLUMN_WIDTH_NANOS + 10, slotTop, color);
        int percentOffSet = FONTRENDERER.getStringWidth(percentString);
        int offSet = percentOffSet;

        /* Name and blocking */
        String listener = lagSource.data.getValue(ObjectData.Entry.EVENT_BUS_LISTENER);
        drawString(listener, left + COLUMN_WIDTH_NANOS + 10 + offSet + 5, slotTop, 0x4C4C4C);

        offSet = offSet + FONTRENDERER.getStringWidth(listener);
        drawString(threadType, left + COLUMN_WIDTH_NANOS + 10 + offSet + 10, slotTop , threadColor);

        /* Event class */
        drawString(lagSource.data.getValue(ObjectData.Entry.EVENT_BUS_EVENT_CLASS_NAME)   , left + COLUMN_WIDTH_NANOS + 10 + percentOffSet + 5, slotTop + 12, 0x4C4C4C);
    }

    private String getMuStringFor(GuiScanResultsWorld.LagSource source){
        TimingManager.EventTimings.ThreadType type = TimingManager.EventTimings.ThreadType.values()[source.data.<Integer>getValue(ObjectData.Entry.EVENT_BUS_THREAD_TYPE)];
        if(type == TimingManager.EventTimings.ThreadType.CLIENT) {
            return Calculations.NFStringSimple(source.nanos, result.getTotalFrames());
        }else if (type == TimingManager.EventTimings.ThreadType.ASYNC){
            return  "No impact";
        }else if(type == TimingManager.EventTimings.ThreadType.SERVER){
            return Calculations.muPerTickString(source.nanos, result);
        }else{
            throw new IllegalStateException("Terminator_NL forgot to add code here... Please submit an issue at github!");
        }
    }

    private String getPercentStringFor(GuiScanResultsWorld.LagSource source){
        TimingManager.EventTimings.ThreadType type = TimingManager.EventTimings.ThreadType.values()[source.data.<Integer>getValue(ObjectData.Entry.EVENT_BUS_THREAD_TYPE)];
        if(type == TimingManager.EventTimings.ThreadType.CLIENT) {
            return Calculations.nfPercent(source.nanos, result);
        }else if (type == TimingManager.EventTimings.ThreadType.ASYNC){
            return  "";
        }else if(type == TimingManager.EventTimings.ThreadType.SERVER){
            return Calculations.tickPercent(source.nanos, result);
        }else{
            throw new IllegalStateException("Terminator_NL forgot to add code here... Please submit an issue at github!");
        }
    }

    private void drawString(String text, int x, int y, int color) {
        FONTRENDERER.drawStringWithShadow(text, x, y, color);
    }

    private void drawStringToLeftOf(String text, int right, int y, int color) {
        FONTRENDERER.drawStringWithShadow(text, right-FONTRENDERER.getStringWidth(text), y, color);
    }

}
