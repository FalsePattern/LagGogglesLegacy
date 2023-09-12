/*
 * Copyright (c) 2023 FalsePattern, Ven
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 *
 */

package com.falsepattern.laggoggles.client;

import com.falsepattern.laggoggles.proxy.ClientProxy;
import com.falsepattern.lib.compat.ChunkPos;
import lombok.val;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class OutlineRenderer {
    private static final Map<WorldRenderer, Long> rendererCache = new WeakHashMap<>();
    public void register() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderWorldPost(RenderWorldEvent.Post event) {
        boolean doRender = ClientProxy.isRenderUpdates();
        if (!doRender) {
            return;
        }
        rendererCache.put(event.renderer, System.currentTimeMillis());
    }
    @SubscribeEvent
    public void onJoinWorldEvent(EntityJoinWorldEvent e) {
        if (!e.world.isRemote) {
            return;
        }
        if (!(e.entity instanceof EntityPlayerSP)) {
            return;
        }
        rendererCache.clear();
    }
    @SubscribeEvent
    public void onDraw(RenderWorldLastEvent event) {
        boolean doRender = ClientProxy.isRenderUpdates();
        if (!doRender) {
            return;
        }
        val MINECRAFT = Minecraft.getMinecraft();
        float partialTicks = event.partialTicks;
        double pX = MINECRAFT.thePlayer.prevPosX + (MINECRAFT.thePlayer.posX - MINECRAFT.thePlayer.prevPosX) * partialTicks;
        double pY = MINECRAFT.thePlayer.prevPosY + (MINECRAFT.thePlayer.posY - MINECRAFT.thePlayer.prevPosY) * partialTicks;
        double pZ = MINECRAFT.thePlayer.prevPosZ + (MINECRAFT.thePlayer.posZ - MINECRAFT.thePlayer.prevPosZ) * partialTicks;


        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslated(-pX, -pY, -pZ);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        GL11.glBegin(GL11.GL_LINES);

        val iterator = rendererCache.entrySet().iterator();
        long time = System.currentTimeMillis();
        while (iterator.hasNext()) {
            val entry = iterator.next();
            val renderer = entry.getKey();
            val deltaTime = time - entry.getValue();
            if (deltaTime > 2000) {
                iterator.remove();
                rendererCache.remove(renderer);
                continue;
            }
            drawChunk(renderer, deltaTime / 2000.0);
        }

        GL11.glEnd();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void drawLine(double x1, double y1, double z1, double x2, double y2, double z2){
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x2, y2, z2);
    }

    private void drawChunk(WorldRenderer renderer, double deltaTime) {
        double a = 1 - deltaTime;

        GL11.glColor4d(0, 1, 0, a);

        int xStart = renderer.posX + 1;
        int yStart = renderer.posY + 1;
        int zStart = renderer.posZ + 1;
        int xEnd = xStart + 14;
        int yEnd = yStart + 14;
        int zEnd = zStart + 14;

        //Draw cube wireframe outline
        //Bottom
        drawLine(xStart, yStart, zStart, xEnd, yStart, zStart);
        drawLine(xEnd, yStart, zStart, xEnd, yStart, zEnd);
        drawLine(xEnd, yStart, zEnd, xStart, yStart, zEnd);
        drawLine(xStart, yStart, zEnd, xStart, yStart, zStart);
        //Top
        drawLine(xStart, yEnd, zStart, xEnd, yEnd, zStart);
        drawLine(xEnd, yEnd, zStart, xEnd, yEnd, zEnd);
        drawLine(xEnd, yEnd, zEnd, xStart, yEnd, zEnd);
        drawLine(xStart, yEnd, zEnd, xStart, yEnd, zStart);
        //Verticals
        drawLine(xStart, yStart, zStart, xStart, yEnd, zStart);
        drawLine(xEnd, yStart, zStart, xEnd, yEnd, zStart);
        drawLine(xEnd, yStart, zEnd, xEnd, yEnd, zEnd);
        drawLine(xStart, yStart, zEnd, xStart, yEnd, zEnd);
    }

}
