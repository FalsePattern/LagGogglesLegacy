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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class QuickText {

    private final FontRenderer renderer;
    private final String text;

    public QuickText(String text){
        this.renderer = Minecraft.getMinecraft().fontRenderer;
        this.text = text;
    }

    @SubscribeEvent
    public void onDraw(RenderGameOverlayEvent.Post event){
        renderer.drawStringWithShadow(text, event.resolution.getScaledWidth()/2 - renderer.getStringWidth(text) / 2, 5, 0xFFFFFF);
    }

    public void show(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void hide(){
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
