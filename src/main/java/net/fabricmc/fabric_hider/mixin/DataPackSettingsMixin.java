package net.fabricmc.fabric_hider.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.resource.DataPackSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataPackSettings.class)
public class DataPackSettingsMixin {

    @Inject(at = @At("RETURN"), method = "getDisabled", cancellable =true)
    public void getDisabled(CallbackInfoReturnable r){
        r.setReturnValue(ImmutableList.of());
    }

}
