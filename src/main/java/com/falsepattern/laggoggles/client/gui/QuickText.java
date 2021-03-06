package com.falsepattern.laggoggles.client.gui;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

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
