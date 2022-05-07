package com.falsepattern.laggoggles.client.gui.buttons;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class OptionsButton extends GuiButton {

    public OptionsButton(int buttonId, int x, int y) {
        super(buttonId, x, y, 200, 20, I18n.format("gui.laggoggles.button.options.name"));
    }

}
