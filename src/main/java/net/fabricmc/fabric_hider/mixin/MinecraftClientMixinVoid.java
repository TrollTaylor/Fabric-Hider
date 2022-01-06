package net.fabricmc.fabric_hider.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({MinecraftClient.class})
public class MinecraftClientMixinVoid {

    @Redirect(method = {"getWindowTitle"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isModded()Z"))
    public boolean isModded(MinecraftClient client) {
        return false;
    }

}
