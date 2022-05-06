package com.falsepattern.laggoggles;

import com.falsepattern.laggoggles.client.ClientProxy;
import com.falsepattern.laggoggles.mixinhelper.MixinValidator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MODID,
     name = Tags.MODNAME,
     version = Tags.VERSION,
     acceptedMinecraftVersions = "[1.7.10]",
     acceptableRemoteVersions = "*",
     guiFactory = Tags.GROUPNAME + ".client.gui.GuiInGameConfigFactory")
@IFMLLoadingPlugin.SortingIndex(1001)
public class Main {
    public static Logger LOGGER;

    @SidedProxy(
            serverSide = Tags.GROUPNAME + ".CommonProxy",
            clientSide = Tags.GROUPNAME + ".client.ClientProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preinit(FMLPreInitializationEvent e){
        LOGGER = e.getModLog();
        proxy.preinit(e);
        MixinValidator.validate();
        Main.LOGGER.info("Registered sided proxy for: " + (proxy instanceof ClientProxy ? "Client" : "Dedicated server"));
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent e) {
        proxy.postinit(e);
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent e){
        proxy.serverStartingEvent(e);
    }

}
