package com.falsepattern.laggoggles.client.gui;


import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.ClientConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

import java.util.ArrayList;

public class GuiInGameConfig extends GuiConfig {

    public GuiInGameConfig(GuiScreen parent) {
        super(parent, new ClientConfigList(), Tags.MODID, false, false, Tags.MODID + " configuration", "Hover with the mouse over a variable to see a description");
    }

    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
    }

    public static class ClientConfigList extends ArrayList<IConfigElement>{

        public ClientConfigList() {
            //TODO
//            Configuration config = ClientConfig.ConfigurationHolder.getConfiguration();
//            this.addAll(new ConfigElement(config.getCategory("general")).getChildElements());
        }
    }
}