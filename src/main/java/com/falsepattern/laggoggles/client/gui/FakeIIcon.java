package com.falsepattern.laggoggles.client.gui;

import lombok.RequiredArgsConstructor;
import net.minecraft.util.IIcon;

@RequiredArgsConstructor
public final class FakeIIcon implements IIcon {
    private final int w;
    private final int h;

    @Override
    public int getIconWidth() {
        return w;
    }

    @Override
    public int getIconHeight() {
        return h;
    }

    @Override
    public float getMinU() {
        return 0;
    }

    @Override
    public float getMaxU() {
        return 1;
    }

    @Override
    public float getInterpolatedU(double p_94214_1_) {
        return lerp(getMinU(), getMaxU(), p_94214_1_);
    }

    @Override
    public float getMinV() {
        return 0;
    }

    @Override
    public float getMaxV() {
        return 1;
    }

    @Override
    public float getInterpolatedV(double p_94207_1_) {
        return lerp(getMinV(), getMaxV(), p_94207_1_);
    }

    private static float lerp(float a, float b, double lerp) {
        lerp = Math.min(Math.max(lerp, 0d), 16d) / 16d;
        return (float) (a * (1d - lerp) + b * (lerp));
    }

    @Override
    public String getIconName() {
        return "fake";
    }
}
