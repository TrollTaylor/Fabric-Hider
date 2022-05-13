package net.fabric_hider.mixin;

import net.fabric_hider.config.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import static net.fabric_hider.config.ConfigFile.updateConfigFile;

@Mixin(PackScreen.class)
public abstract class PackScreenMixin extends Screen {


    @Shadow private PackListWidget availablePackList;
    @Shadow private PackListWidget selectedPackList;
    @Shadow private static Text DROP_INFO;
    @Shadow private ButtonWidget doneButton;
    @Shadow @Final private File file;
    @Shadow @Final public static Text FOLDER_INFO;
    @Shadow protected abstract void refresh();
    @Shadow @Final private ResourcePackOrganizer organizer;
    List<PackListWidget.ResourcePackEntry> allChildren;

    protected PackScreenMixin(Text text) {
        super(text);
    }

    @Overwrite
    public void render(MatrixStack matrixStack, int i, int j, float f) {
        this.renderBackgroundTexture(0);
        updateConfigFile();
        if (!Config.showResourcePack) {
            selectedPackList.children().clear();
            selectedPackList.children().addAll(allChildren.stream().filter(p -> !p.getNarration().toString().contains("Fabric Mods")).toList());
        } else {
            selectedPackList.children().clear();
            selectedPackList.children().addAll(allChildren);
        }
        availablePackList.render(matrixStack, i, j, f);
        selectedPackList.render(matrixStack, i, j, f);
        drawCenteredText(matrixStack, textRenderer, title, width / 2, 8, 16777215);
        drawCenteredText(matrixStack, textRenderer, DROP_INFO, width / 2, 20, 16777215);
        super.render(matrixStack, i, j, f);
    }

   @Overwrite
   protected void init() {
       this.doneButton = (ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height - 48, 150, 20, ScreenTexts.DONE, (buttonWidget) -> {
           this.close();
       }));

       this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height - 48, 150, 20, new TranslatableText("pack.openFolder"), (buttonWidget) -> {
           Util.getOperatingSystem().open(this.file);
       }, new ButtonWidget.TooltipSupplier() {
           public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrixStack, int i, int j) {
               PackScreenMixin.this.renderTooltip(matrixStack, new TranslatableText("pack.folderInfo"), i, j);
           }

           public void supply(Consumer<Text> consumer) {
               consumer.accept(new TranslatableText("pack.folderInfo"));
           }
       }));
       this.availablePackList = new PackListWidget(this.client, 200, this.height, new TranslatableText("pack.available.title"));
       this.availablePackList.setLeftPos(this.width / 2 - 4 - 200);
       this.addSelectableChild(this.availablePackList);
       this.selectedPackList = new PackListWidget(this.client, 200, this.height, new TranslatableText("pack.selected.title"));
       this.selectedPackList.setLeftPos(this.width / 2 + 4);
       this.addSelectableChild(this.selectedPackList);
       allChildren = selectedPackList.children().stream().toList();
       updateConfigFile();
       if (!Config.showResourcePack) {
           selectedPackList.children().clear();
           selectedPackList.children().addAll(selectedPackList.children().stream().filter(p -> !p.getNarration().toString().contains("Fabric Mods")).toList());
       }
       this.refresh();
   }

   @Overwrite
   private void updatePackList(PackListWidget packListWidget, Stream<ResourcePackOrganizer.Pack> stream) {
        packListWidget.children().clear();
        stream.forEach((pack) -> {
            packListWidget.children().add(new PackListWidget.ResourcePackEntry(this.client, packListWidget, this, pack));
            allChildren = selectedPackList.children().stream().toList();
            updateConfigFile();
            if (!Config.showResourcePack) {
                selectedPackList.children().clear();
                selectedPackList.children().addAll(selectedPackList.children().stream().filter(p -> !p.getNarration().toString().contains("Fabric Mods")).toList());
            }
        });
   }
}
