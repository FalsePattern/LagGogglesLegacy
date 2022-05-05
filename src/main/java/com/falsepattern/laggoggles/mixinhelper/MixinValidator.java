package com.falsepattern.laggoggles.mixinhelper;

import com.falsepattern.laggoggles.Main;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MixinValidator {

    public static void validate(){
        HashMap<String, String> FAILED_OR_UNLOADED_MIXINS = new HashMap<>(MixinConfigPlugin.MIXINS_TO_LOAD);
        for(String target : new HashSet<>(FAILED_OR_UNLOADED_MIXINS.values())){
            try {
                Main.LOGGER.info("Loading mixin target class: " + target);
                Class.forName(target);
            } catch (Exception e) {
                Main.LOGGER.warn("Failed to load class: " + target + ". This is required to apply mixins!");
                e.printStackTrace();
            }
        }
        if(MixinConfigPlugin.MIXINS_TO_LOAD.size() > 0){
            Main.LOGGER.fatal("Not all required mixins have been applied!");
            Main.LOGGER.fatal("To prevent you from wasting your time, the process has ended.");
            Main.LOGGER.fatal("");
            Main.LOGGER.fatal("Required mixins that have not been applied:");
            for(Map.Entry<String, String> entry : MixinConfigPlugin.MIXINS_TO_LOAD.entrySet()){
                Main.LOGGER.fatal("- " + entry.getKey() + " targeting: " + entry.getValue());
            }
            Main.LOGGER.fatal("");
            Main.LOGGER.fatal("This means that LagGoggles will not function properly.");
            Main.LOGGER.fatal("Make sure your versions are correct for Forge as well as SpongeForge.");
            Main.LOGGER.fatal("");

            ClassLoader thisLoader = MixinValidator.class.getClassLoader();
            if (thisLoader instanceof LaunchClassLoader == false){
                Main.LOGGER.fatal("MixinValidator.class was NOT loaded by the expected class loader. Loader: " + thisLoader);
                return;
            }
            LaunchClassLoader loader = (LaunchClassLoader) thisLoader;

            for(String target : new HashSet<>(FAILED_OR_UNLOADED_MIXINS.values())){
                try {

                } catch (Exception e) {
                }
            }

            FMLCommonHandler.instance().exitJava(1, false);
        }else{
            Main.LOGGER.info("All mixins have been applied. If they were not overridden by another mod, everything should work.");
        }
    }
}
