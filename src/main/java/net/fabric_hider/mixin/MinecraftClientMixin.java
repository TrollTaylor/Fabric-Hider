package net.fabric_hider.mixin;

import net.fabric_hider.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.ModStatus;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static net.fabric_hider.config.ConfigFile.configFile;
import static net.fabric_hider.config.ConfigFile.updateConfigFile;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {


    @Shadow public abstract Window getWindow();

    @Inject(method="getGameVersion", at =@At("HEAD"), cancellable = true)
    public void getGameVersion(CallbackInfoReturnable we){
        updateConfigFile();
        if(Config.hideClient){
            we.setReturnValue(MinecraftClient.getInstance().getGame().getVersion().getReleaseTarget());
        }
    }

    @Inject(method="getVersionType", at =@At("HEAD"), cancellable = true)
    public void getVersionType(CallbackInfoReturnable we){
        updateConfigFile();
        if(Config.hideClient) {
            we.setReturnValue("release");
        }
    }

    @Overwrite
    public static ModStatus getModStatus() {
        updateConfigFile();
        if(Config.hideClient) {
            return new ModStatus(net.minecraft.util.ModStatus.Confidence.PROBABLY_NOT, "Client" + " jar signature and brand is untouched");
        }
        return new ModStatus(ModStatus.Confidence.DEFINITELY, "Client" + " jar signature and brand is untouched");
    }

    @Inject(at = @At("HEAD"), method = "run", cancellable =true)
    protected void run(CallbackInfo info) {
        configFile();
    }

    public boolean ContainsHiddenLogs(String s){
        updateConfigFile();
        for(int i = 0; i < Config.hiddenLogs.length; i++){
            if(s.contains(Config.hiddenLogs[i])){
                return true;
            }
        }
        return false;
    }

    @Inject(at = @At("HEAD"), method = "close", cancellable =true)
    protected void close(CallbackInfo info)  {
        updateConfigFile();
        if(Config.hideClient) {
            File file = new File("logs/latest.log");
            List<String> out = null;
            try {

                out = Files.lines(file.toPath())
                        .filter(line -> !ContainsHiddenLogs(line))
                        .collect(Collectors.toList());

                for (int i = 0; i < out.size(); i++) {
                    if (out.get(i).toUpperCase().contains("MINECRAFT")) {
                        out.set(i, Pattern.compile(" (Minecraft)", Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(out.get(i))
                                .replaceAll(Matcher.quoteReplacement(":")));
                    }
                }

                Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

