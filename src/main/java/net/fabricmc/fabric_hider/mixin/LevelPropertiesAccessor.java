package net.fabricmc.fabric_hider.mixin;

import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(LevelProperties.class)
public interface LevelPropertiesAccessor {
    @Accessor("serverBrands")
    public static Set<String> getbrands() {
        throw new AssertionError();
    }
}
