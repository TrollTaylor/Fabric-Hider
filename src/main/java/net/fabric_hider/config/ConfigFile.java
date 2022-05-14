package net.fabric_hider.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
public class ConfigFile {

    static File file = new File("config/fabric_hider/config.json");
    static File dir = new File("config/fabric_hider");
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static ConfigIndex w = new ConfigIndex();

    public static void configFile() {
        if (!file.exists()) {
            try {
                dir.mkdir();
                file.createNewFile();

                w.hiddenF3 = new String[]{"[Iris]", "[Entity Batching]", "Sodium","[Culling]"};
                w.hiddenLogs = new String[]{"Fabric", "fabric", "- java", "- minecraft", "Indigo", "Compatibility", "mods", "BiomeModificationImpl"};

                w.hideServer = true;
                w.hideClient = true;
                w.showResourcePack = true;


                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(w, writer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            updateConfigFile();
        }
    }

    public static void updateConfigFile() {
        try (Reader reader = new FileReader(file)) {
            JsonParser jp = new JsonParser();
            JsonObject jo = jp.parse(reader).getAsJsonObject();

            w = gson.fromJson(jo, ConfigIndex.class);

            if(!jo.has("hideServer")){
                w.hideServer = true;
                w.hideClient = true;
                w.showResourcePack = true;

                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(w, writer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    Config.hiddenF3 = w.hiddenF3;
    Config.hiddenLogs = w.hiddenLogs;
    Config.hideClient = w.hideClient;
    Config.hideServer = w.hideServer;
    Config.showResourcePack = w.showResourcePack;
    }
}

