package net.fabric_hider.mixin;

import net.fabric_hider.config.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.stream.Stream;
import static net.fabric_hider.config.ConfigFile.updateConfigFile;

@Mixin(PackScreen.class)
public abstract class PackScreenMixin extends Screen {


    @Shadow private PackListWidget selectedPackList;
    @Shadow @Final private ResourcePackOrganizer organizer;

    @Shadow protected abstract void updatePackLists();

    List<PackListWidget.ResourcePackEntry> allChildren;
    boolean showFabric = Config.showResourcePack;

    MatrixStack renderMatrixStack;
    int renderI;
    int renderJ;
    float renderF;
    boolean didRender = false;

    protected PackScreenMixin(Text text) {
        super(text);
    }

    @Inject(method="render", at =@At("HEAD"))
    public void render(MatrixStack matrixStack, int i, int j, float f, CallbackInfo ci) {
        hide(0, 1);
        renderMatrixStack = matrixStack;
        renderI = i;
        renderJ = j;
        renderF = f;
        didRender = true;
    }

    @Inject(method="updatePackList", at =@At("TAIL"))
    private void updatePackList(PackListWidget packListWidget, Stream<ResourcePackOrganizer.Pack> stream, CallbackInfo ci) {
        hide(1, 0);
    }

    @Inject(method="tick", at =@At("HEAD"))
    public void tick(CallbackInfo ci) {
        updateConfigFile();
        boolean oldShowFabric = showFabric;
        showFabric = Config.showResourcePack;




        if(oldShowFabric != showFabric) {
            updatePackLists();
            if (didRender && !showFabric) {
                selectedPackList.render(renderMatrixStack, renderI, renderJ, renderF);
            }
        }
    }

    public void hide(int i, int j) {
        if(i == 1)
        {
            allChildren = selectedPackList.children().stream().toList();
        }
        updateConfigFile();
        if (!Config.showResourcePack) {
            selectedPackList.children().clear();
            selectedPackList.children().addAll(allChildren.stream().filter(p -> !p.getNarration().toString().contains("Fabric Mods")).toList());
        } else {
            if(j == 1)
            {
                selectedPackList.children().clear();
                selectedPackList.children().addAll(allChildren);
            }
        }
    }
}
