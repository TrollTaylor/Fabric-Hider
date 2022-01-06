package net.fabricmc.fabric_hider.mixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({TitleScreen.class})
public class TitleScreenMixin {

    @Redirect(method = {"render"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isModded()Z"))
    public boolean isModded(MinecraftClient client) {
        return false;
    }

}
