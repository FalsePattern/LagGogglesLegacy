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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinimapRenderer {
    private static final Map<ChunkPos, List<WorldRenderer>> rendererCache = new HashMap<>();
    public void register() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderWorldPost(RenderWorldEvent.Post event) {
        val chunkX = event.renderer.posX >> 4;
        val chunkZ = event.renderer.posZ >> 4;
        val chunk = new ChunkPos(chunkX, chunkZ);
        rendererCache.computeIfAbsent(chunk, (ignored) -> new ArrayList<>()).add(event.renderer);
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
    public void onDraw(RenderGameOverlayEvent.Pre event) {
        boolean doRender = ClientProxy.isRenderMinimap();
        val iter = rendererCache.entrySet().iterator();
        while (iter.hasNext()) {
            val entry = iter.next();
            val pos = entry.getKey();
            val renderers = entry.getValue();
            val rendererIter = renderers.iterator();
            while (rendererIter.hasNext()) {
                val renderer = rendererIter.next();
                if (!renderer.isInitialized || renderer.posX >> 4 != pos.x || renderer.posZ >> 4 != pos.z) {
                    rendererIter.remove();
                }
            }
            if (renderers.isEmpty()) {
                iter.remove();
            }
        }
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
        GL11.glScaled(2, 2, 1);
        GL11.glTranslated(32, 32, 0);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        GL11.glBegin(GL11.GL_QUADS);

        int playerChunkX = ((int) Math.floor(pX)) >> 4;
        int playerChunkZ = ((int) Math.floor(pZ)) >> 4;
        int range = 32;
        for(int x = playerChunkX - range; x < playerChunkX + range; x++) {
            for(int z = playerChunkZ - range; z < playerChunkZ + range; z++) {
                drawChunk(new ChunkPos(x,z), playerChunkX, playerChunkZ, doRender);
            }
        }

        GL11.glEnd();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void drawChunk(ChunkPos chunk, int pX, int pZ, boolean doRender){
        val cacheLine = rendererCache.get(chunk);
        boolean initialized = false;
        double r = 0;
        double g = 0;
        double b = 0;
        if (cacheLine != null) {
            g = 1;
            initialized = true;
        }
        if (!doRender) {
            return;
        }
        if (!initialized) {
            val chunkProviderClient = (ChunkProviderClient) Minecraft.getMinecraft().theWorld.getChunkProvider();
            val theChunk = chunkProviderClient.provideChunk(chunk.x, chunk.z);
            if (theChunk instanceof EmptyChunk) {
                r = 1;
            } else {
                b = 1;
                initialized = true;
            }
        }
        if (!initialized && Minecraft.getMinecraft().isIntegratedServerRunning()) {
            val chunkProviderIntegratedServer = Minecraft.getMinecraft().getIntegratedServer().worldServerForDimension(Minecraft.getMinecraft().thePlayer.dimension).getChunkProvider();
            if (chunkProviderIntegratedServer.chunkExists(chunk.x, chunk.z)) {
                val theChunk = chunkProviderIntegratedServer.provideChunk(chunk.x, chunk.z);
                if (!theChunk.isLightPopulated) {
                    b = 1;
                } else {
                    g = 1;
                }
            }
        }
        GL11.glColor4d(r, g, b,0.4);

        int xStart = chunk.x - pX;
        int zStart = chunk.z - pZ;
        int xEnd = xStart + 1;
        int zEnd = zStart + 1;

        GL11.glVertex3d(xStart, zStart ,  0 );
        GL11.glVertex3d(xStart, zEnd ,  0   );
        GL11.glVertex3d(xEnd,   zEnd ,  0   );
        GL11.glVertex3d(xEnd,   zStart ,  0 );


    }
}
