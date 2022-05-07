package com.falsepattern.laggoggles.client.gui;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiFPSResults extends GuiScreen{

    private final ProfileResult result;
    private final FontRenderer FONTRENDERER;

    private GuiEntityTypes guiEntityTypes;
    private GuiSingleEntities guiSingleEntities;
    private GuiEventTypes guiEventTypes;

    public GuiFPSResults(ProfileResult result){
        super();
        this.result = result;
        FONTRENDERER = Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    public void initGui() {
        super.initGui();

        /*                                            width  , height              , top                   , bottom         , left      , screenWidth, screenHeight, ProfileResult*/
        guiSingleEntities = new GuiSingleEntities(mc, width/2, height - 25         , 45                    , height         ,  0      , result);
        guiEntityTypes    = new GuiEntityTypes(   mc, width/2, (height - 25)/2     , 45                    , (height - 25)/2,  width/2      , result);
        guiEventTypes     = new GuiEventTypes(    mc, width/2, (height - 25)/2 - 12, ((height - 25)/2) + 12, height         ,  width/2     , result);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        guiSingleEntities.drawScreen(mouseX, mouseY, partialTicks);
        guiEntityTypes.drawScreen(mouseX, mouseY, partialTicks);
        guiEventTypes.drawScreen(mouseX, mouseY, partialTicks);
        drawString(Tags.MODNAME + ": " + I18n.format("gui.laggoggles.text.fpsresults.titledescription"), 5, 5, 0xFFFFFF);
        drawString(I18n.format("gui.laggoggles.text.fpsresults.present"), 5, 15, 0xCCCCCC);
        drawString(I18n.format("gui.laggoggles.text.results.singleentities"), 5, 35, 0xFFFFFF);
        drawString(" (" + I18n.format("gui.laggoggles.text.results.teleport") + ")", 5 + FONTRENDERER.getStringWidth(I18n.format("gui.laggoggles.text.results.singleentities")), 35, 0x666666);
        drawString(I18n.format("gui.laggoggles.text.results.entitiesbytype"), width/2 + 5, 35, 0xFFFFFF);
        drawString(I18n.format("gui.laggoggles.text.results.eventsub"), width/2 + 5, ((height - 25)/2) + 2, 0xFFFFFF);
    }

    private void drawString(String text, int x, int y, int color) {
        FONTRENDERER.drawStringWithShadow(text, x, y, color);
    }
}
