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

package com.falsepattern.laggoggles.client.gui;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.ClientConfig;
import com.falsepattern.laggoggles.client.ServerDataPacketHandler;
import com.falsepattern.laggoggles.client.gui.buttons.DonateButton;
import com.falsepattern.laggoggles.client.gui.buttons.DownloadButton;
import com.falsepattern.laggoggles.client.gui.buttons.OptionsButton;
import com.falsepattern.laggoggles.client.gui.buttons.ProfileButton;
import com.falsepattern.laggoggles.packet.CPacketRequestResult;
import com.falsepattern.laggoggles.packet.CPacketRequestScan;
import com.falsepattern.laggoggles.packet.SPacketMessage;
import com.falsepattern.laggoggles.profiler.ProfileManager;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.profiler.ScanType;
import com.falsepattern.laggoggles.proxy.ClientProxy;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.lib.compat.GuiLabel;
import com.falsepattern.lib.config.ConfigurationManager;
import lombok.SneakyThrows;
import lombok.val;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.client.config.GuiConfig;

import java.util.ArrayList;
import java.util.List;

public class GuiProfile extends GuiScreen {

    private static final int BUTTON_START_PROFILE_ID = 0;
    private static final int BUTTON_SHOW_TOGGLE      = 1;
    private static final int BUTTON_ANALYZE_RESULTS  = 2;
    private static final int LABEL_ID                = 3;
    private static final int BUTTON_DONATE           = 4;
    private static final int BUTTON_OPTIONS          = 5;
    private static final int BUTTON_DOWNLOAD         = 6;
    private static final int BUTTON_PLUS_5           = 7;
    private static final int BUTTON_MINUS_5          = 8;

    public static String PROFILING_PLAYER = null;
    public static long PROFILE_END_TIME = 0L;

    public static SPacketMessage MESSAGE = null;
    public static long MESSAGE_END_TIME = 0L;


    private ProfileButton startProfile;
    private DownloadButton downloadButton;
    private GuiButton optionsButton;
    private DonateButton donateButton;
    private boolean initialized = false;

    protected List<GuiLabel> labelList;

    public int seconds = Math.min(30, ServerDataPacketHandler.MAX_SECONDS);

    public GuiProfile(){
        super();
    }

    public static void update(){
        if(isThisGuiOpen() == false){
            return;
        }
        Minecraft.getMinecraft().displayGuiScreen(new GuiProfile());
    }

    public static void open(){
        Minecraft.getMinecraft().displayGuiScreen(new GuiProfile());
    }

    private static boolean isThisGuiOpen(){
        return Minecraft.getMinecraft().currentScreen != null && (Minecraft.getMinecraft().currentScreen instanceof GuiProfile == true);
    }

    @Override
    public void initGui(){
        super.initGui();

        buttonList = new ArrayList<>();
        labelList = new ArrayList<>();

        int centerX = width/2;
        int centerY = height/2;

        boolean profileLoaded = ProfileManager.LAST_PROFILE_RESULT.get() != null;
        int y = centerY - (130 / 2);
        GuiLabel scrollHint = new GuiLabel(fontRendererObj, LABEL_ID, centerX - 100, y, 200, 20, 0xFFFFFF);
        y += 30;
        startProfile = new ProfileButton(BUTTON_START_PROFILE_ID, centerX - 100, y, I18n.format("gui.laggoggles.text.profile.start", seconds));
        downloadButton = new DownloadButton(this, BUTTON_DOWNLOAD, centerX + 80, y);
        GuiButton minus5 = new GuiButton(BUTTON_MINUS_5, centerX - 125, y, "-5");
        GuiButton plus5 = new GuiButton(BUTTON_PLUS_5, centerX + 105, y, "+5");
        minus5.width = 20;
        plus5.width = 20;
        y += 25;
        GuiButton showToggle  = new GuiButton(BUTTON_SHOW_TOGGLE, centerX - 100, y, LagOverlayGui.isShowing() ? I18n.format("gui.laggoggles.text.profile.hide") : I18n.format("gui.laggoggles.text.profile.show"));
        y += 25;
        GuiButton analyzeResults  = new GuiButton(BUTTON_ANALYZE_RESULTS, centerX - 100, y, I18n.format("gui.laggoggles.text.profile.analyze"));
        y += 25;
        optionsButton = new OptionsButton(BUTTON_OPTIONS, centerX - 100, y);
        y += 25;
        donateButton = new DonateButton(BUTTON_DONATE, centerX - 100, y);


        showToggle.enabled = profileLoaded;
        analyzeResults.enabled = profileLoaded;
        this.buttonList.add(startProfile);
        this.buttonList.add(plus5);
        this.buttonList.add(minus5);
        this.buttonList.add(showToggle);
        this.buttonList.add(analyzeResults);
        this.buttonList.add(donateButton);
        this.buttonList.add(optionsButton);
        for (val line: I18n.format("gui.laggoggles.text.profile.scrollhint", '\n').split("\n")) {
            scrollHint.addLine(line);
        }
        labelList.add(scrollHint);
        this.buttonList.add(downloadButton);
        initialized = true;
        updateButton();
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);


        for (GuiLabel guiLabel : this.labelList) {
            guiLabel.drawLabel(this.mc, p_73863_1_, p_73863_2_);
        }
    }

    private Runnable buttonUpdateTask = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                if(isThisGuiOpen() == false){
                    return;
                }
                updateButton();
            } catch (InterruptedException ignored){}
        }
    };

    private void updateButton(){
        if(initialized == false){
            return;
        }
        if(getSecondsLeftForMessage() >= 0){
            startProfile.displayString = MESSAGE.message;
            startProfile.enabled = false;
            new Thread(buttonUpdateTask).start();
        }else if(getSecondsLeftForProfiler() >= 0){
            startProfile.displayString = I18n.format("gui.laggoggles.text.profile.running", PROFILING_PLAYER, getSecondsLeftForProfiler());
            startProfile.enabled = false;
            new Thread(buttonUpdateTask).start();
        }else{
            startProfile.enabled = true;
            startProfile.displayString = I18n.format("gui.laggoggles.text.profile.start", seconds);
        }
        downloadButton.enabled = ServerDataPacketHandler.PERMISSION.ordinal() >= Perms.Permission.GET.ordinal();
    }

    private static int getSecondsLeftForProfiler(){
        if(PROFILING_PLAYER != null) {
            return new Double(Math.ceil((PROFILE_END_TIME - System.currentTimeMillis()) / 1000)).intValue();
        }else{
            return -1;
        }
    }

    public static int getSecondsLeftForMessage(){
        return new Double(Math.ceil((MESSAGE_END_TIME - System.currentTimeMillis()) / 1000)).intValue();
    }

    @Override
    public void handleMouseInput() {

        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        if(initialized == false){
            return;
        }
        if(startProfile.mousePressed(mc, x, y) && startProfile.enabled){
            int wheel = Mouse.getDWheel();
            if(wheel != 0) {
                modifySeconds(wheel / 120); // 1 click is 120
            }
        }
        super.handleMouseInput();
    }

    private void modifySeconds(int steps) {
        seconds = seconds + steps * 5; // 1 step is 5 seconds
        seconds = Math.max(seconds, 5);
        boolean triedMore = seconds > ServerDataPacketHandler.MAX_SECONDS;
        seconds = Math.min(seconds, ServerDataPacketHandler.MAX_SECONDS);
        if(triedMore){
            startProfile.displayString = I18n.format("gui.laggoggles.text.profile.limited", seconds);
        }else {
            startProfile.displayString = I18n.format("gui.laggoggles.text.profile.start", seconds);
        }
    }

    public void startProfile(){
        CPacketRequestScan scan = new CPacketRequestScan();
        scan.length = seconds;
        startProfile.enabled = false;
        startProfile.displayString = I18n.format("gui.laggoggles.text.profile.sending");
        ClientProxy.NETWORK_WRAPPER.sendToServer(scan);
    }

    private void analyzeResults(){
        ProfileResult result = ProfileManager.LAST_PROFILE_RESULT.get();
        if(result != null) {
            if(result.getType() == ScanType.WORLD) {
                mc.displayGuiScreen(new GuiScanResultsWorld(result));
            }else if(result.getType() == ScanType.FPS){
                mc.displayGuiScreen(new GuiFPSResults(result));
            }
        }
    }

    @SneakyThrows
    @Override
    public void actionPerformed(GuiButton button){
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        switch (button.id){
            case BUTTON_START_PROFILE_ID:
                startProfile.click(this, buttonList, x, y);
                break;
            case BUTTON_SHOW_TOGGLE:
                if(LagOverlayGui.isShowing()) {
                    LagOverlayGui.hide();
                    Minecraft.getMinecraft().displayGuiScreen(null);
                }else{
                    LagOverlayGui.show();
                    Minecraft.getMinecraft().displayGuiScreen(null);
                }
                break;
            case BUTTON_ANALYZE_RESULTS:
                analyzeResults();
                break;
            case BUTTON_DONATE:
                donateButton.click(this, buttonList, x, y);
                break;
            case BUTTON_OPTIONS:
                mc.displayGuiScreen(new GuiConfig(this, ConfigurationManager.getConfigElements(ClientConfig.class), Tags.MOD_ID, false, false, Tags.MOD_NAME + " Configuration", "Hover with the mouse over a variable to see a description"));
                break;
            case BUTTON_DOWNLOAD:
                ClientProxy.NETWORK_WRAPPER.sendToServer(new CPacketRequestResult());
                break;
            case BUTTON_PLUS_5:
                modifySeconds(1);
                break;
            case BUTTON_MINUS_5:
                modifySeconds(-1);
        }
    }

    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
        //TODO: Load the config, and install changes
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }

}
