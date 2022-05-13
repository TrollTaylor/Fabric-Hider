package net.fabric_hider.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.*;

@Mixin({LevelProperties.class})
public class LevelPropertiesMixin {

    @Inject(at = @At("RETURN"), method = "updateProperties(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/nbt/NbtCompound;)V", cancellable =true)
    protected void updateProperties(DynamicRegistryManager drm, NbtCompound nbt, NbtCompound nbt2, CallbackInfo info) {
        nbt.putBoolean("WasModded", false);
        NbtList nbtList = new NbtList();
        Set<String> h = new HashSet<>(Arrays.asList("vanilla"));
        h.stream().map(NbtString::of).forEach(nbtList::add);
        nbt.put("ServerBrands", nbtList);
        DataPackSettings w = new DataPackSettings(ImmutableList.of("vanilla"), ImmutableList.of());
        nbt.remove("SpawnAngle");
    }
}
