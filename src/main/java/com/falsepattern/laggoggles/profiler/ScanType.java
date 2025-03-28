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

package com.falsepattern.laggoggles.profiler;

import com.falsepattern.laggoggles.Tags;

public enum ScanType {
    WORLD(Tags.MOD_NAME + ": World scan results"),
    FPS(Tags.MOD_NAME + ": FPS scan results"),
    EMPTY("Empty profile results.");

    private final String text;

    ScanType(String text){
        this.text = text;
    }

    public String getText(ProfileResult result){
        return text;
    }
}
