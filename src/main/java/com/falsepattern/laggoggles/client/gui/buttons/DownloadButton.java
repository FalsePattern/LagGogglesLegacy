package com.falsepattern.laggoggles.client.gui.buttons;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.gui.FakeIIcon;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.lib.text.FormattedText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

import static com.falsepattern.laggoggles.client.ServerDataPacketHandler.PERMISSION;
import static com.falsepattern.laggoggles.client.gui.GuiProfile.getSecondsLeftForMessage;

public class DownloadButton extends GuiButton{

    private ResourceLocation DOWNLOAD_TEXTURE = new ResourceLocation(Tags.MODID, "download.png");
    private static final IIcon icon = new FakeIIcon(14, 14);
    private final GuiScreen parent;

    public DownloadButton(GuiScreen parent, int buttonId, int x, int y) {
        super(buttonId, x, y, 20, 20, "");
        this.parent = parent;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
        mc.getTextureManager().bindTexture(DOWNLOAD_TEXTURE);
        drawTexturedModelRectFromIcon(xPosition + 3, yPosition + 3, icon, 14, 14);
        if (this.mousePressed(mc, mouseX, mouseY)) {
            ArrayList<String> hover = new ArrayList<>();
            hover.add(I18n.format("gui.laggoggles.button.download.hover", '\n'));
            if(PERMISSION != Perms.Permission.FULL) {
                hover.add("");
                hover.add(I18n.format("gui.laggoggles.button.download.hover.notop", '\n'));

                if(getSecondsLeftForMessage() >= 0){
                    hover.add("");
                    hover.add(EnumChatFormatting.GRAY + I18n.format("gui.laggoggles.button.download.hover.cooldown", '\n'));
                }
            }
            FormattedText.parse(String.join("\n", hover)).drawWithShadow(mc.fontRenderer, mouseX, mouseY);
        }
    }
}
