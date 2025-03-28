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

package com.falsepattern.laggoggles.client.gui.buttons;

import com.falsepattern.laggoggles.client.gui.GuiProfile;
import com.falsepattern.laggoggles.client.gui.LagOverlayGui;

import net.minecraft.client.gui.GuiButton;

import java.util.List;

import static com.falsepattern.laggoggles.client.gui.buttons.SplitButton.State.NORMAL;
import static com.falsepattern.laggoggles.client.gui.buttons.SplitButton.State.SPLIT;

public abstract class SplitButton<T extends GuiButton> extends GuiButton {

    State state = NORMAL;
    long lastClick = 0;
    enum State{
        NORMAL,
        SPLIT,
    }

    protected final T leftButton;
    protected final T rightButton;

    public SplitButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String leftButtonText, String rightButtonText, GuiButtonConstructor<T> subButtonConstructor) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        leftButton = subButtonConstructor.construct(id, x, y, width / 2 - 5, height, leftButtonText);
        rightButton = subButtonConstructor.construct(id, x + width / 2 + 5, y, width / 2 - 5, height, rightButtonText);
    }

    public interface GuiButtonConstructor<T extends GuiButton> {
        T construct(int id, int x, int y, int w, int h, String text);
    }


    public void click(GuiProfile parent, List<GuiButton> buttonList, int x, int y){
        if(lastClick + 50 > System.currentTimeMillis()){
            return;
        }
        lastClick = System.currentTimeMillis();
        updateButtons();
        if(state == NORMAL) {
            state = SPLIT;
            buttonList.remove(this);
            buttonList.add(leftButton);
            buttonList.add(rightButton);
        }else if(state == SPLIT){
            LagOverlayGui.hide();
            buttonList.add(this);
            buttonList.remove(leftButton);
            buttonList.remove(rightButton);
            if (rightButton.mousePressed(parent.mc, x, y)) {
                onRightButton(parent);
            } else if (leftButton.mousePressed(parent.mc, x, y)) {
                onLeftButton(parent);
            }
            state = NORMAL;
        }
    }

    public void updateButtons(){};

    public abstract void onRightButton(GuiProfile parent);
    public abstract void onLeftButton(GuiProfile parent);
}
