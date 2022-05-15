package net.fabric_hider.mixin;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabric_hider.config.Config;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.ArrayList;
import java.util.List;
import static net.fabric_hider.config.ConfigFile.updateConfigFile;

@Mixin(DebugHud.class)
public class DebugHudMixin {

    @Inject(at = @At("RETURN"), method = "getLeftText", cancellable =true)
    protected void getLeftText(CallbackInfoReturnable<List<String>> info) {
        updateConfigFile();
        if(Config.hideClient) {
            info.cancel();

            if (RendererAccess.INSTANCE.hasRenderer()) {
                info.getReturnValue().remove("[Fabric] Active renderer: " + RendererAccess.INSTANCE.getRenderer().getClass().getSimpleName());
            } else {
                info.getReturnValue().remove("[Fabric] Active renderer: none (vanilla)");
            }

            List renderList = new ArrayList<String>();
            for (int i = 0; i < info.getReturnValue().size(); i++) {
                if (!(ContainsHiddenF3(info.getReturnValue().get(i)))) {
                    renderList.add(info.getReturnValue().get(i));
                }
            }
            if (renderList.size() >= 22) {
                renderList.remove(renderList.size() - 1);
                renderList.remove(renderList.size() - 1);
            }
            info.setReturnValue(renderList);
        }
    }


    public boolean ContainsHiddenF3(String s){
        updateConfigFile();
        for(int i = 0; i < Config.hiddenF3.length; i++){
            if(s.contains(Config.hiddenF3[i])){
                return true;
            }
        }
        return false;
    }
}