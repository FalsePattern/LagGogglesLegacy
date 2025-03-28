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
