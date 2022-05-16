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

                w.hiddenF3 = w.getDefaultString("hiddenF3");
                w.hiddenLogs = w.getDefaultString("hiddenLogs");

                w.hideServer = w.getDefaultBoolean("hideServer");
                w.hideClient = w.getDefaultBoolean("hideClient");
                w.showResourcePack = w.getDefaultBoolean("showResourcePack");


                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(w, writer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } catch (Exception e) {}
        } else {
            addToConfig();
            updateConfigFile();
        }
    }

    public static void updateConfigFile() {
        try (Reader reader = new FileReader(file)) {
            JsonParser jp = new JsonParser();
            JsonObject jo = jp.parse(reader).getAsJsonObject();

            w = gson.fromJson(jo, ConfigIndex.class);

        }catch (Exception e) {}

    Config.hiddenF3 = w.hiddenF3;
    Config.hiddenLogs = w.hiddenLogs;
    Config.hideClient = w.hideClient;
    Config.hideServer = w.hideServer;
    Config.showResourcePack = w.showResourcePack;
    }

    public static void addToConfig() {
        String s[] = new String[]{"hiddenF3", "hiddenLogs", "hideClient", "hideServer", "showResourcePack"};

        try (Reader reader = new FileReader(file)) {
            JsonParser jp = new JsonParser();
            JsonObject jo = jp.parse(reader).getAsJsonObject();

            w = gson.fromJson(jo, ConfigIndex.class);

            for (String config : s) {
                if (!jo.has(config)) {
                    switch (config) {
                        case "hiddenF3":
                            w.hiddenF3 = w.getDefaultString(config);
                            break;
                        case "hiddenLogs":
                            w.hiddenLogs = w.getDefaultString(config);
                            break;
                        case "hideClient":
                            w.hideClient = w.getDefaultBoolean(config);
                            break;
                        case "hideServer":
                            w.hideServer = w.getDefaultBoolean(config);
                            break;
                        case "showResourcePack":
                            w.showResourcePack = w.getDefaultBoolean(config);
                    }
                }

                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(w, writer);
                } catch (Exception e) {}
            }
        }catch (Exception e) {}
    }
}

