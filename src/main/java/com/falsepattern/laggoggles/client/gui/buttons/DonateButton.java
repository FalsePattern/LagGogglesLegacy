package com.falsepattern.laggoggles.client.gui.buttons;

import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.gui.FakeIIcon;
import com.falsepattern.laggoggles.client.gui.GuiProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.IIcon;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

public class DonateButton extends SplitButton<DonateButtonSmall> {

    private ResourceLocation DONATE_TEXTURE = new ResourceLocation(Tags.MODID, "donate.png");
    private static final IIcon icon = new FakeIIcon(14, 14);

    public DonateButton(int buttonId, int x, int y) {
        super(buttonId, x, y, 200, 20, I18n.format("gui.laggoggles.button.donate.name"),
                "Terminator_NL", "FalsePattern", DonateButtonSmall::new);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
        mc.getTextureManager().bindTexture(DONATE_TEXTURE);
        drawTexturedModelRectFromIcon(xPosition + 3, yPosition + 3, icon, 14, 14);
    }

    @Override
    public void onRightButton(GuiProfile parent) {
        rightButton.donate();
    }

    @Override
    public void onLeftButton(GuiProfile parent) {
        leftButton.donate();
    }
}
