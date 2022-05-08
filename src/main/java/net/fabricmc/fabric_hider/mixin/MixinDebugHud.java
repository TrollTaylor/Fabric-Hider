package net.fabricmc.fabric_hider.mixin;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric_hider.util.Config;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(DebugHud.class)
public class MixinDebugHud {

    @Inject(at = @At("RETURN"), method = "getLeftText", cancellable =true)
    protected void getLeftText(CallbackInfoReturnable<List<String>> info) {

        info.cancel();

        if (RendererAccess.INSTANCE.hasRenderer()) {
            info.getReturnValue().remove("[Fabric] Active renderer: " + RendererAccess.INSTANCE.getRenderer().getClass().getSimpleName());
        } else {
            info.getReturnValue().remove("[Fabric] Active renderer: none (vanilla)");
        }
		
        List renderList = new ArrayList<String>();
        for(int i = 0; i < info.getReturnValue().size(); i++){
            if(!(ContainsHiddenF3(info.getReturnValue().get(i)))){
                renderList.add(info.getReturnValue().get(i));
            }
        }
        if(renderList.size() >= 22) {
            renderList.remove(renderList.size() - 1);
            renderList.remove(renderList.size() - 1);
        }
        info.setReturnValue(renderList);
    }


    public boolean ContainsHiddenF3(String s){
        for(int i = 0; i < Config.hiddenF3.length; i++){
            if(s.contains(Config.hiddenF3[i])){
                return true;
            }
        }
        return false;
    }

    @Inject(at = @At("RETURN"), method = "getRightText", cancellable = true)
    protected void getRightText(CallbackInfoReturnable<List<String>> info) {
		
        List renderList = new ArrayList<String>();
        for(int i = 0; i < info.getReturnValue().size(); i++){
            if(!(info.getReturnValue().get(i).contains("[Iris]") || info.getReturnValue().get(i).contains("[Entity Batching]") || info.getReturnValue().get(i).contains("Sodium") || info.getReturnValue().get(i).contains("IRIS") || info.getReturnValue().get(i).contains("Direct Buffers") || info.getReturnValue().get(i).contains("Off-Heap") || info.getReturnValue().get(i).contains("Device") || info.getReturnValue().get(i).contains("Chunk arena") || info.getReturnValue().get(i).contains("build") || info.getReturnValue().get(i).contains("buffer") || info.getReturnValue().get(i).contains("replaymod") || info.getReturnValue().get(i).contains("[Culling]") )){
                renderList.add(info.getReturnValue().get(i));
            }
        }
        if(renderList.size() >= 22) {
            renderList.remove(9);
			
        }
        info.setReturnValue(renderList);
		
    }
}