package net.fabricmc.fabric_hider.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MinecraftClient.class})
public interface MinecraftClientMixin {

    @Accessor("versionType") @Mutable
    public void setVersion(String version);

    @Accessor("gameVersion") @Mutable
    public void setGameVersion(String version);

}
