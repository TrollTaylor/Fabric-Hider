package net.fabricmc.fabric_hider.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ModStatus;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MinecraftClient.class})
public class MinecraftClientMixin {


    @Shadow @Final
    private String gameVersion = "1.18.2";

    @Shadow @Final
    private String versionType = "release";


    @Overwrite
    public static ModStatus getModStatus() {
        return new ModStatus(net.minecraft.util.ModStatus.Confidence.PROBABLY_NOT, "Client" + " jar signature and brand is untouched");
    }
}



