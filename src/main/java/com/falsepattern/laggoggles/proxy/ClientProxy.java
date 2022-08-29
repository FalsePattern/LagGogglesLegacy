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

package com.falsepattern.laggoggles.proxy;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.ClientConfig;
import com.falsepattern.laggoggles.client.gui.GuiProfile;
import com.falsepattern.laggoggles.client.gui.KeyHandler;
import com.falsepattern.laggoggles.client.gui.LagOverlayGui;
import com.falsepattern.laggoggles.packet.CPacketRequestServerData;
import com.falsepattern.lib.config.ConfigurationManager;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import static com.falsepattern.laggoggles.client.ServerDataPacketHandler.RECEIVED_RESULT;
import static com.falsepattern.laggoggles.profiler.ProfileManager.LAST_PROFILE_RESULT;

public class ClientProxy extends CommonProxy {

    @Override
    @SneakyThrows
    public void preinit(FMLPreInitializationEvent e) {
        super.preinit(e);
        ConfigurationManager.registerConfig(ClientConfig.class);
    }

    @Override
    public void postinit(FMLPostInitializationEvent e){
        super.postinit(e);
        ClientRegistry.registerKeyBinding(new KeyHandler("Profile GUI", Keyboard.KEY_INSERT, Tags.MODID, () -> {
            NETWORK_WRAPPER.sendToServer(new CPacketRequestServerData());
            Minecraft.getMinecraft().displayGuiScreen(new GuiProfile());
        }));

        FMLCommonHandler.instance().bus().register(new LoginHandler());
    }

    public static class LoginHandler {
        @SubscribeEvent
        public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent e){
            RECEIVED_RESULT = false;
            LagOverlayGui.destroy();
            LAST_PROFILE_RESULT.set(null);
            new ClientLoginAction().activate();
        }
    }

    public static class ClientLoginAction {

        int count = 0;

        @SubscribeEvent
        public void onTick(TickEvent.ClientTickEvent e){
            if(RECEIVED_RESULT){
                FMLCommonHandler.instance().bus().unregister(this);
                return;
            }
            if(e.phase != TickEvent.Phase.START){
                return;
            }
            if(count++ % 5 == 0){
                NETWORK_WRAPPER.sendToServer(new CPacketRequestServerData());
            }
        }

        public void activate(){
            FMLCommonHandler.instance().bus().register(this);
        }

    }
}
