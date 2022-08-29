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
import com.falsepattern.laggoggles.packet.ObjectData;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.util.TreeMap;

public class GuiScanResultsWorld extends GuiScreen {

    private final FontRenderer FONTRENDERER;
    public final TreeMap<Integer, LagSource> DATA_ID_TO_SOURCE = new TreeMap<>();
    public final TreeMap<LagSource, Integer> DATA_SOURCE_TO_ID = new TreeMap<>();

    private GuiSingleEntities guiSingleEntities;
    private GuiEntityTypes guiEntityTypes;
    private GuiEventTypes guiEventTypes;

    private ProfileResult result;

    public GuiScanResultsWorld(ProfileResult result){
        super();
        FONTRENDERER = Minecraft.getMinecraft().fontRenderer;
        this.result = result;
    }

    @Override
    public void initGui() {
        super.initGui();

        /*                                            width  , height              , top                   , bottom         , left      , screenWidth, screenHeight, ProfileResult*/
        guiSingleEntities = new GuiSingleEntities(mc, width/2, height - 25         , 45                    , height         ,  0, result);
        guiEntityTypes    = new GuiEntityTypes(   mc, width/2, (height - 25)/2     , 45                    , (height - 25)/2,  width/2, result);
        guiEventTypes     = new GuiEventTypes(    mc, width/2, (height - 25)/2 - 12, ((height - 25)/2) + 12, height         ,  width/2, result);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        guiSingleEntities.drawScreen(mouseX, mouseY, partialTicks);
        guiEntityTypes.drawScreen(mouseX, mouseY, partialTicks);
        guiEventTypes.drawScreen(mouseX, mouseY, partialTicks);
        drawString(Tags.MODNAME + ": " + I18n.format("gui.laggoggles.text.worldresults.titledescription"), 5, 5, 0xFFFFFF);
        drawString(I18n.format("gui.laggoggles.text.worldresults.present"), 5, 15, 0xCCCCCC);
        drawString(I18n.format("gui.laggoggles.text.results.singleentities"), 5, 35, 0xFFFFFF);
        drawString(" (" + I18n.format("gui.laggoggles.text.results.teleport") + ")", 5 + FONTRENDERER.getStringWidth(I18n.format("gui.laggoggles.text.results.singleentities")), 35, 0x666666);
        drawString(I18n.format("gui.laggoggles.text.results.entitiesbytype"), width/2 + 5, 35, 0xFFFFFF);
        drawString(I18n.format("gui.laggoggles.text.results.eventsub"), width/2 + 5, ((height - 25)/2) + 2, 0xFFFFFF);
    }


    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }

    private void drawString(String text, int x, int y, int color) {
        FONTRENDERER.drawStringWithShadow(text,  x, y, color);
    }


    /* LAGSOURCE */
    public static class LagSource implements Comparable<LagSource>{

        public final long nanos;
        public final ObjectData data;

        public LagSource(long nanos, ObjectData e){
            this.nanos = nanos;
            data = e;
        }

        @Override
        public int compareTo(LagSource other) {
            boolean thisIsBigger = this.nanos > other.nanos;
            if(thisIsBigger) {
                return -1;
            }
            boolean thisIsSmaller= this.nanos < other.nanos;
            if(thisIsSmaller){
                return 1;
            }else{
                return 0;
            }
        }
    }
}
