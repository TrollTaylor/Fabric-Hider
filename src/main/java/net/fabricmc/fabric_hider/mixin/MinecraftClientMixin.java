package net.fabricmc.fabric_hider.mixin;

import net.fabricmc.fabric_hider.ExampleMod;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.minecraft.util.ModStatus;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {


    @Shadow @Final
    private String gameVersion = "1.18.2";
    
	@Shadow @Final
    private String versionType = "release";


    @Shadow public abstract Window getWindow();

    @Overwrite
    public static ModStatus getModStatus() {
        return new ModStatus(net.minecraft.util.ModStatus.Confidence.PROBABLY_NOT, "Client" + " jar signature and brand is untouched");
    }

    @Overwrite
    public void updateWindowTitle() {
        MinecraftClient.getInstance().getWindow().setTitle(getWindowTitle());
    }

    @Overwrite
    private String getWindowTitle() {
        StringBuilder stringBuilder = new StringBuilder("Minecraft");

        stringBuilder.append(" ");
        stringBuilder.append(SharedConstants.getGameVersion().getName());
        ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (clientPlayNetworkHandler != null && clientPlayNetworkHandler.getConnection().isOpen()) {
            stringBuilder.append(" - ");
            if (MinecraftClient.getInstance().getServer() != null && !MinecraftClient.getInstance().getServer().isRemote()) {
                stringBuilder.append(I18n.translate("title.singleplayer", new Object[0]));
            } else if (MinecraftClient.getInstance().isConnectedToRealms()) {
                stringBuilder.append(I18n.translate("title.multiplayer.realms", new Object[0]));
            } else if (MinecraftClient.getInstance().getServer() == null && (MinecraftClient.getInstance().getCurrentServerEntry() == null || !MinecraftClient.getInstance().getCurrentServerEntry().isLocal())) {
                stringBuilder.append(I18n.translate("title.multiplayer.other", new Object[0]));
            } else {
                stringBuilder.append(I18n.translate("title.multiplayer.lan", new Object[0]));
            }
        }
        return stringBuilder.toString();
    }
}

