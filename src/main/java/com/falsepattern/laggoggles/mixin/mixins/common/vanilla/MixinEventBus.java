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

package com.falsepattern.laggoggles.mixin.mixins.common.vanilla;

import com.falsepattern.laggoggles.util.IMixinASMEventHandler;
import com.google.common.base.Throwables;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.IEventExceptionHandler;
import cpw.mods.fml.common.eventhandler.IEventListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;
import static com.falsepattern.laggoggles.profiler.ProfileManager.timingManager;

@Mixin(value = EventBus.class, remap = false)
public abstract class MixinEventBus implements IEventExceptionHandler {

    @Shadow
    private IEventExceptionHandler exceptionHandler;

    @Shadow
    @Final
    private int busID;

    private boolean postWithScan(Event event) {
        IEventListener[] listeners = event.getListenerList().getListeners(this.busID);
        int index = 0;
        try {
            for(; index < listeners.length; index++) {
                long LAGGOGGLES_START = System.nanoTime();
                listeners[index].invoke(event);
                long nanos = System.nanoTime() - LAGGOGGLES_START;

                if(listeners[index] instanceof IMixinASMEventHandler) {
                    ModContainer mod = ((IMixinASMEventHandler) listeners[index]).getOwner();
                    if (mod != null) {
                        String identifier = mod.getName() + " (" + mod.getSource().getName() + ")";
                        timingManager.addEventTime(identifier, event, nanos);
                    }
                }
            }
        }catch (Throwable throwable){
            this.exceptionHandler.handleException((EventBus) (IEventExceptionHandler) this, event, listeners, index, throwable);
            Throwables.propagate(throwable);
        }
        return event.isCancelable() && event.isCanceled();
    }

    @Inject(method = "post",
            at = @At("HEAD"),
            cancellable = true,
            require = 1)
    private void beforePost(Event event, CallbackInfoReturnable<Boolean> ci){
        if(PROFILE_ENABLED.get()){
            ci.setReturnValue(postWithScan(event));
        }
    }

}
