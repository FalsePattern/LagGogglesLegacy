/*
 * Lag Goggles: Legacy
 *
 * Copyright (C) 2022 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice, this permission notice and the word "SNEED"
 * shall be included in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.laggoggles.client.gui.buttons;

import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.gui.FakeIIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

public class DonateButtonSmall extends GuiButton {

    private static final URI DONATE_URL_TERMINATOR_NL;
    private static final URI DONATE_URL_FALSEPATTERN;
    static {
        try {
            DONATE_URL_TERMINATOR_NL = new URI("https://www.paypal.com/cgi-bin/webscr?return=https://minecraft.curseforge.com/projects/laggoggles?gameCategorySlug=mc-mods&projectID=283525&cn=Add+special+instructions+to+the+addon+author()&business=leon.philips12%40gmail.com&bn=PP-DonationsBF:btn_donateCC_LG.gif:NonHosted&cancel_return=https://minecraft.curseforge.com/projects/laggoggles?gameCategorySlug=mc-mods&projectID=283525&lc=US&item_name=LagGoggles+(from+curseforge.com)&cmd=_donations&rm=1&no_shipping=1&currency_code=USD");
            DONATE_URL_FALSEPATTERN = new URI("https://ko-fi.com/falsepattern");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static final IIcon icon = new FakeIIcon(14, 14);
    private final URI uri;
    public DonateButtonSmall(int id, int x, int y, int w, int h, String text) {
        super(id, x, y, w, h, text);
        if (text.equals("FalsePattern")) {
            uri = DONATE_URL_FALSEPATTERN;
        } else {
            uri = DONATE_URL_TERMINATOR_NL;
        }
    }

    public void donate() {
        Main.LOGGER.info("Attempting to open link in browser: " + uri);
        try {
            if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            }else {
                Main.LOGGER.info("Attempting xdg-open...");
                Runtime.getRuntime().exec("xdg-open " + uri);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
