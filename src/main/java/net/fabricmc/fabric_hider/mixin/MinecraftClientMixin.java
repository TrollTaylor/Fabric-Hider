package net.fabricmc.fabric_hider.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric_hider.util.Config;
import net.fabricmc.fabric_hider.util.ConfigIndex;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.resource.language.I18n;
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

    @Inject(at = @At("HEAD"), method = "run", cancellable =true)
    protected void run(CallbackInfo info) {
        File file = new File("config/fabric_hider/config.json");
        File dir = new File("config/fabric_hider");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ConfigIndex w = new ConfigIndex();

        if(!file.exists())
        {
            try {
                dir.mkdir();
                file.createNewFile();

                w.hiddenF3 = new String[]{"[Iris]","[Entity Batching]"};
                w.hiddenLogs = new String[]{"Fabric","fabric","- java","- minecraft", "Indigo", "Compatibility", "mods"};

                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(w, writer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            try (Reader reader = new FileReader(file)) {

                w = gson.fromJson(reader, ConfigIndex.class);

            }catch (IOException e) {
                e.printStackTrace();
            }
        }



        Config.hiddenF3 = w.hiddenF3;
        Config.hiddenLogs = w.hiddenLogs;

    }

    public boolean ContainsHiddenLogs(String s){
        for(int i = 0; i < Config.hiddenLogs.length; i++){
            if(s.contains(Config.hiddenLogs[i])){
                return true;
            }
        }
        return false;
    }

    @Inject(at = @At("HEAD"), method = "close", cancellable =true)
    protected void close(CallbackInfo info)  {

        File file = new File("logs/latest.log");
        List<String> out = null;
        try {

            out = Files.lines(file.toPath())
                    .filter(line -> !ContainsHiddenLogs(line))
//                    .filter(line -> !line.contains("Fabric") && !line.contains("fabric") && !line.contains("- java") && !line.contains("- minecraft") && !line.contains("Indigo"))
                    .collect(Collectors.toList());

            for(int i = 0; i < out.size(); i++){
                if(out.get(i).toUpperCase().contains("MINECRAFT")){
                    out.set(i, Pattern.compile(" (Minecraft)", Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(out.get(i))
                            .replaceAll(Matcher.quoteReplacement(":")));
                }
            }

            Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        try{
//            FileInputStream fstream = new FileInputStream("logs/latest.log");
//            File tempFile = new File("myTempFile.txt");
//
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//
//            String strLine;
//
//            while ((strLine = br.readLine()) != null)   {
//                if(strLine.contains("Fabric")){
//                    String trimmedLine = strLine.trim();
//                    if(trimmedLine.contains("Fabric")) continue;
//                    writer.write(strLine + System.getProperty("line.separator"));
//                }
//            }
//            fstream.close();
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//        }
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

