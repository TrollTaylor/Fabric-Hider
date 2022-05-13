package net.fabric_hider.mixin;

import net.fabric_hider.config.Config;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.fabric_hider.config.ConfigFile.updateConfigFile;

@Mixin({ClientBrandRetriever.class})
public abstract class ClientBrandRetrieverMixin {

    @Inject(at = @At("HEAD"), method = "getClientModName", cancellable = true, remap = false)
    private static void getClientModName(CallbackInfoReturnable<String> callback) {
        updateConfigFile();
        if(Config.hideServer) {
            callback.setReturnValue("vanilla");
        }
    }
}
