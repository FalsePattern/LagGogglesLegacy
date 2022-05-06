package com.falsepattern.laggoggles.profiler;

import com.falsepattern.laggoggles.Tags;

public enum ScanType {
    WORLD(Tags.MODNAME + ": World scan results"),
    FPS(Tags.MODNAME + ": FPS scan results"),
    EMPTY("Empty profile results.");

    private final String text;

    ScanType(String text){
        this.text = text;
    }

    public String getText(ProfileResult result){
        return text;
    }
}
