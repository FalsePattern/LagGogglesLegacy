/*
 * LagGoggles: Legacy
 *
 * Copyright (C) 2022-2025 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, only version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.laggoggles.client.gui.buttons;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.gui.FakeIIcon;
import com.falsepattern.laggoggles.client.gui.GuiProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class DonateButton extends SplitButton<DonateButtonSmall> {

    private ResourceLocation DONATE_TEXTURE = new ResourceLocation(Tags.MOD_ID, "donate.png");
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
