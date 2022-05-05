package com.falsepattern.laggoggles.mixin;

import com.falsepattern.laggoggles.util.ASMEventHandler;
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

@Mixin(value = EventBus.class, remap = false, priority = 1001)
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

                if(listeners[index] instanceof ASMEventHandler) {
                    ModContainer mod = ((ASMEventHandler) listeners[index]).getOwner();
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
        return event.isCancelable() ? event.isCanceled() : false;
    }

    @Inject(method = "post(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", at = @At("HEAD"), cancellable = true)
    public void beforePost(Event event, CallbackInfoReturnable<Boolean> ci){
        if(PROFILE_ENABLED.get()){
            ci.setReturnValue(postWithScan(event));
        }
    }

}
