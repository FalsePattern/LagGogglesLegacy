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

package com.falsepattern.laggoggles.client.gui;

import com.falsepattern.laggoggles.packet.ObjectData;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.profiler.ScanType;
import com.falsepattern.laggoggles.util.Calculations;
import com.falsepattern.laggoggles.util.Graphical;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.GuiScrollingList;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GuiEntityTypes extends GuiScrollingList {

    private TreeMap<Long, String> DATA = new TreeMap<>();

    private final FontRenderer FONTRENDERER;
    private static final int slotHeight = 12;
    private int COLUMN_WIDTH_NANOS = 0;
    private int COLUMN_WIDTH_PERCENTAGES = 0;
    private ProfileResult result;

    public GuiEntityTypes(Minecraft client, int width, int height, int top, int bottom, int left, ProfileResult result) {
        super(client, width, height, top, bottom, left, slotHeight);
        FONTRENDERER = client.fontRenderer;
        this.result = result;
        ScanType type = result.getType();
        HashMap<String, Long> totals = new HashMap<>();
        for(GuiScanResultsWorld.LagSource src : result.getLagSources()){
            String className;
            if(type == ScanType.WORLD) {
                switch (src.data.type) {
                    case ENTITY:
                        className = src.data.getValue(ObjectData.Entry.ENTITY_CLASS_NAME);
                        break;
                    case BLOCK:
                    case TILE_ENTITY:
                        className = src.data.getValue(ObjectData.Entry.BLOCK_CLASS_NAME);
                        break;
                    default:
                        continue;
                }
            }else if(type == ScanType.FPS){
                switch (src.data.type) {
                    case GUI_ENTITY:
                        className = src.data.getValue(ObjectData.Entry.ENTITY_CLASS_NAME);
                        break;
                    case GUI_BLOCK:
                        className = src.data.getValue(ObjectData.Entry.BLOCK_CLASS_NAME);
                        break;
                    default:
                        continue;
                }
            }else{
                continue;
            }
            if(totals.containsKey(className) == false){
                totals.put(className, src.nanos);
            }else{
                totals.put(className, src.nanos + totals.get(className));
            }
        }

        if(type == ScanType.WORLD) {
            for(Map.Entry<String, Long> entry : totals.entrySet()){
                DATA.put(entry.getValue(), entry.getKey());
                COLUMN_WIDTH_NANOS = Math.max(COLUMN_WIDTH_NANOS, FONTRENDERER.getStringWidth(Calculations.muPerTickString(entry.getValue(), result)));
                COLUMN_WIDTH_PERCENTAGES = Math.max(COLUMN_WIDTH_PERCENTAGES, FONTRENDERER.getStringWidth(Calculations.tickPercent(entry.getValue(), result)));
            }
        }else if (type == ScanType.FPS){
            for(Map.Entry<String, Long> entry : totals.entrySet()){
                DATA.put(entry.getValue(), entry.getKey());
                COLUMN_WIDTH_NANOS = Math.max(COLUMN_WIDTH_NANOS, FONTRENDERER.getStringWidth(Calculations.NFStringSimple(entry.getValue(), result.getTotalFrames())));
                COLUMN_WIDTH_PERCENTAGES = Math.max(COLUMN_WIDTH_PERCENTAGES, FONTRENDERER.getStringWidth(Calculations.nfPercent(entry.getValue(), result)));
            }
        }
    }


    @Override
    protected int getSize() {
        return DATA.size();
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

    @Override
    protected void drawSlot(int slot, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if(slot == -1){
            return;
        }
        Long nanos = DATA.descendingKeySet().toArray(new Long[0])[slot];
        String name = DATA.get(nanos);

        double heat;
        if(result.getType() == ScanType.WORLD) {
            heat = Calculations.heat(nanos, result);
        }else{
            heat = Calculations.heatNF(nanos, result);
        }
        double[] RGB = Graphical.heatToColor(heat);
        int color = Graphical.RGBtoInt(RGB);
        int offSet;
        if(result.getType() == ScanType.WORLD){
            /* microseconds */
            drawStringToLeftOf(Calculations.muPerTickString(nanos, result),left + COLUMN_WIDTH_NANOS + 5, slotTop, color);

            /* Percent */
            drawString(Calculations.tickPercent(nanos, result), left + COLUMN_WIDTH_NANOS + 10, slotTop, color);


            offSet = FONTRENDERER.getStringWidth(Calculations.tickPercent(nanos, result));
        }else{
            /* nanoseconds */
            drawStringToLeftOf(Calculations.NFStringSimple(nanos, result.getTotalFrames()),left + COLUMN_WIDTH_NANOS + 5, slotTop, color);

            /* Percent */
            drawString(Calculations.nfPercent(nanos, result), left + COLUMN_WIDTH_NANOS + 10, slotTop, color);


            offSet = FONTRENDERER.getStringWidth(Calculations.nfPercent(nanos, result));
        }

        /* Name */
        drawString(name, left + COLUMN_WIDTH_NANOS + 15 + offSet, slotTop, 0x4C4C4C);
    }


    private void drawString(String text, int x, int y, int color) {
        FONTRENDERER.drawStringWithShadow(text, x, y, color);
    }

    private void drawStringToLeftOf(String text, int right, int y, int color) {
        FONTRENDERER.drawStringWithShadow(text, right-FONTRENDERER.getStringWidth(text), y, color);
    }

}
