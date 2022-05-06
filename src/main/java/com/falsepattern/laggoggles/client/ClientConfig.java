package com.falsepattern.laggoggles.client;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.util.ColorBlindMode;
import com.falsepattern.lib.config.Config;
@Config(modid = Tags.MODID, category = "client")
public class ClientConfig {

    @Config.Comment("Define the number of microseconds at which an object is marked with a deep red colour for WORLD lag.")
    public static int GRADIENT_MAXED_OUT_AT_MICROSECONDS = 25;

    @Config.Comment("Define the number of microseconds at which an object is marked with a deep red colour for FPS lag.")
    public static int GRADIENT_MAXED_OUT_AT_NANOSECONDS_FPS = 50000;

    @Config.Comment("What is the minimum amount of microseconds required before an object is tracked in the client?\n" +
                    "This is only for WORLD lag.\n" +
                    "This also affects the analyze results window")
    public static int MINIMUM_AMOUNT_OF_MICROSECONDS_THRESHOLD = 1;

    @Config.Comment("If you're colorblind, change this to fit your needs.\n" +
                    "Available options:\n" +
                    "- GREEN_TO_RED\n" +
                    "- BLUE_TO_RED\n" +
                    "- GREEN_TO_BLUE")
    public static ColorBlindMode COLORS = ColorBlindMode.GREEN_TO_RED;
}
