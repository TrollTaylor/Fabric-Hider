package net.fabricmc.fabric_hider.mixin;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ResourcePackManager.class)
public class ResourcePackOrganizerMixin {

    @Inject(at = @At("RETURN"), method = "getEnabledProfiles", cancellable =true)
    protected void scanPacks(CallbackInfoReturnable<Collection<ResourcePackProfile>> info) {
        info.setReturnValue(info.getReturnValue().stream().filter(p -> p.getName() != "Fabric Mods").toList());
    }


    @Inject(at = @At("RETURN"), method = "getProfiles", cancellable =true)
    protected void scanPacksE(CallbackInfoReturnable<Collection<ResourcePackProfile>> info) {
        info.setReturnValue(info.getReturnValue().stream().filter(p -> p.getName() != "Fabric Mods").toList());
    }
}