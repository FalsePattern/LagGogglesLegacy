package com.falsepattern.laggoggles.client.gui;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

public class GuiInGameConfigFactory implements IModGuiFactory{
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return null;
    }

//    @Override
//    public boolean hasConfigGui() {
//        return true;
//    }
//
//    @Override
//    public GuiScreen createConfigGui(GuiScreen parentScreen) {
//        return new GuiInGameConfig(parentScreen);
//    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }
}