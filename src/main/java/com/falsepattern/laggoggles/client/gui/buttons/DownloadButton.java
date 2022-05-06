package com.falsepattern.laggoggles.client.gui.buttons;

import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.lib.text.FormattedText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

import static com.falsepattern.laggoggles.client.ServerDataPacketHandler.PERMISSION;
import static com.falsepattern.laggoggles.client.gui.GuiProfile.getSecondsLeftForMessage;

public class DownloadButton extends GuiButton{

    private ResourceLocation DOWNLOAD_TEXTURE = new ResourceLocation(Main.MODID_LOWER, "download.png");
    private final GuiScreen parent;

    public DownloadButton(GuiScreen parent, int buttonId, int x, int y) {
        super(buttonId, x, y, 20, 20, "");
        this.parent = parent;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
        mc.getTextureManager().bindTexture(DOWNLOAD_TEXTURE);
        drawTexturedModalRect(xPosition + 3, yPosition + 3, 0, 0, 14, 14);
        if (this.mousePressed(mc, mouseX, mouseY)) {
            ArrayList<String> hover = new ArrayList<>();
            hover.add("Download the latest available");
            hover.add("world result from the server.");
            if(PERMISSION != Perms.Permission.FULL) {
                hover.add("");
                hover.add("Because you're not opped, the results");
                hover.add("will be trimmed to your surroundings");

                if(getSecondsLeftForMessage() >= 0){
                    hover.add("");
                    hover.add(EnumChatFormatting.GRAY + "Remember: There's a cooldown on this, you");
                    hover.add(EnumChatFormatting.GRAY + "may have to wait before you can use it again.");
                }
            }
            FormattedText.parse(String.join("\n", hover)).draw(mc.fontRenderer, mouseX, mouseY);
        }
    }
}