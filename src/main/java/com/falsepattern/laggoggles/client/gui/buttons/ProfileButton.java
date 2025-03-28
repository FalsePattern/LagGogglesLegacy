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

import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.api.Profiler;
import com.falsepattern.laggoggles.client.ServerDataPacketHandler;
import com.falsepattern.laggoggles.client.gui.GuiProfile;
import com.falsepattern.laggoggles.client.gui.LagOverlayGui;
import com.falsepattern.laggoggles.client.gui.QuickText;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.profiler.ScanType;
import com.falsepattern.laggoggles.util.Perms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class ProfileButton extends SplitButton<GuiButton> {

    public static Thread PROFILING_THREAD;
    private long frames = 0;
    public ProfileButton(int buttonId, int x, int y, String text) {
        super(buttonId, x, y, 170, 20, text,
                I18n.format("gui.laggoggles.button.profile.fps.name"),
                I18n.format("gui.laggoggles.button.profile.world.name"),
                GuiButton::new);
    }

    @Override
    public void onRightButton(GuiProfile parent) {
        parent.startProfile();
    }

    @Override
    public void updateButtons(){
        if(ServerDataPacketHandler.PERMISSION.ordinal() < Perms.Permission.START.ordinal()) {
            rightButton.enabled = false;
            rightButton.displayString = I18n.format("gui.laggoggles.button.profile.server.noperms");
        }
    }

    @Override
    public void onLeftButton(GuiProfile parent) {
        final int seconds = parent.seconds;
        if(PROFILING_THREAD == null || PROFILING_THREAD.isAlive() == false){
            PROFILING_THREAD = new Thread(new Runnable() {
                @Override
                public void run() {
                    Main.LOGGER.info("Clientside profiling started. (" + seconds + " seconds)");
                    QuickText text = new QuickText(I18n.format("gui.laggoggles.text.fpswarning"));
                    GuiProfile.PROFILING_PLAYER = Minecraft.getMinecraft().thePlayer.getPersistentID().toString();
                    GuiProfile.PROFILE_END_TIME = System.currentTimeMillis() + (seconds * 1000L);
                    GuiProfile.update();
                    text.show();
                    ProfileResult result = Profiler.runProfiler(seconds, ScanType.FPS, Minecraft.getMinecraft().thePlayer);
                    text.hide();
                    Main.LOGGER.info("Clientside profiling done.");
                    LagOverlayGui.create(result);
                    LagOverlayGui.show();
                    GuiProfile.update();
                }
            });
            PROFILING_THREAD.start();
        }
    }

}
