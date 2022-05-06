package com.falsepattern.laggoggles.proxy;

import com.falsepattern.laggoggles.server.ServerConfig;
import com.falsepattern.lib.config.ConfigurationManager;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lombok.SneakyThrows;

public class ServerProxy extends CommonProxy {

    @Override
    @SneakyThrows
    public void preinit(FMLPreInitializationEvent e) {
        super.preinit(e);
        ConfigurationManager.registerConfig(ServerConfig.class);
    }
}
