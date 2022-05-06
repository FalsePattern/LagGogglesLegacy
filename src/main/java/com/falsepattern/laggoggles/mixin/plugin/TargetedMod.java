package com.falsepattern.laggoggles.mixin.plugin;

import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
public enum TargetedMod implements ITargetedMod {
    ;

    @Getter
    public final String modName;
    @Getter
    public final boolean loadInDevelopment;
    @Getter
    public final Predicate<String> condition;
}
