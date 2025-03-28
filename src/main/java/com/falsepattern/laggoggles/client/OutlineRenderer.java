/*
 * Copyright (c) 2023 FalsePattern, Ven
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 *
 */

package com.falsepattern.laggoggles.client;

import com.falsepattern.laggoggles.proxy.ClientProxy;
import com.falsepattern.lib.util.RenderUtil;
import lombok.val;
import lombok.var;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class OutlineRenderer {
    private static final Map<WorldRenderer, Long> UPDATED_RENDERERS = new WeakHashMap<>();

    private static final long INITIAL_RENDER_MS = 2000;
    private static final long EXTRA_RENDER_MS = 500;
    private static final long MAX_RENDER_MS = 6000;

    private static long lastRenderMs;

    public void register() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderWorldPost(RenderWorldEvent.Post event) {
        if (!ClientProxy.isRenderUpdates())
            return;

        val renderer = event.renderer;
        var remainingTimeMs = UPDATED_RENDERERS.get(renderer);
        if (remainingTimeMs == null) {
            remainingTimeMs = INITIAL_RENDER_MS;
        } else {
            remainingTimeMs += EXTRA_RENDER_MS;
            if (remainingTimeMs > MAX_RENDER_MS)
                remainingTimeMs = MAX_RENDER_MS;
        }

        UPDATED_RENDERERS.put(renderer, remainingTimeMs);
    }

    @SubscribeEvent
    public void onJoinWorldEvent(EntityJoinWorldEvent e) {
        if (!e.world.isRemote)
            return;
        if (!(e.entity instanceof EntityPlayerSP))
            return;

        UPDATED_RENDERERS.clear();
        lastRenderMs = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onDraw(RenderWorldLastEvent event) {
        if (!ClientProxy.isRenderUpdates())
            return;
        if (UPDATED_RENDERERS.isEmpty())
            return;

        val currentTimeMs = System.currentTimeMillis();
        val deltaTimeMs = currentTimeMs - lastRenderMs;

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        RenderUtil.setGLTranslationRelativeToPlayer();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        GL11.glBegin(GL11.GL_LINES);
        UPDATED_RENDERERS.replaceAll((renderer, remainingTimeMs) -> {
            drawChunk(renderer, remainingTimeMs);
            return remainingTimeMs - deltaTimeMs;
        });
        GL11.glEnd();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        GL11.glPopAttrib();

        UPDATED_RENDERERS.entrySet().removeIf(e -> e.getValue() <= 0);
        lastRenderMs = currentTimeMs;
    }

    private static void drawChunk(WorldRenderer renderer, long remainingTimeUs) {
        setColorGradient(remainingTimeUs);
        drawAABB(renderer);
    }

    private static void setColorGradient(long remainingTimeUs) {
        // Shift from red to orange between 6000ms and 4000ms
        if (remainingTimeUs > 4000) {
            val delta = (remainingTimeUs - 4000L) / 2000F;
            GL11.glColor4f(1F, 1 - delta, 0F, 1F);
            return;
        }

        // Shift from orange to green between 4000ms and 2000ms
        if (remainingTimeUs > 2000) {
            val delta = (remainingTimeUs - 2000L) / 2000F;
            GL11.glColor4f(delta, 1F, 0F, 1F);
            return;
        }

        // Fade out green based on alpha between 2000ms and 0ms
        val delta = remainingTimeUs / 2000F;
        GL11.glColor4f(0F, 1F, 0F, delta);
    }

    private static void drawAABB(WorldRenderer renderer) {
        // Pushes one block into the chunk
        val minX = renderer.posX + 1;
        val minY = renderer.posY + 1;
        val minZ = renderer.posZ + 1;

        val maxX = minX + 14;
        val maxY = minY + 14;
        val maxZ = minZ + 14;

        // Draw Bottom
        drawLine(minX, minY, minZ, maxX, minY, minZ);
        drawLine(maxX, minY, minZ, maxX, minY, maxZ);
        drawLine(maxX, minY, maxZ, minX, minY, maxZ);
        drawLine(minX, minY, maxZ, minX, minY, minZ);

        // Draw Top
        drawLine(minX, maxY, minZ, maxX, maxY, minZ);
        drawLine(maxX, maxY, minZ, maxX, maxY, maxZ);
        drawLine(maxX, maxY, maxZ, minX, maxY, maxZ);
        drawLine(minX, maxY, maxZ, minX, maxY, minZ);

        // Draw Vertical
        drawLine(minX, minY, minZ, minX, maxY, minZ);
        drawLine(maxX, minY, minZ, maxX, maxY, minZ);
        drawLine(maxX, minY, maxZ, maxX, maxY, maxZ);
        drawLine(minX, minY, maxZ, minX, maxY, maxZ);
    }

    private static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2) {
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x2, y2, z2);
    }
}
