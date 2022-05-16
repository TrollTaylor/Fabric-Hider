package net.fabric_hider.config;

public class ConfigIndex {
    public String[] hiddenF3;
    public String[] hiddenLogs;

    public boolean hideClient;

    public boolean hideServer;

    public boolean showResourcePack;

    public String[] getDefaultString(String s) {
        switch (s) {
            case "hiddenF3":
                return new String[]{"[Iris]", "[Entity Batching]", "Sodium","[Culling]"};
            case "hiddenLogs":
                return new String[]{"Fabric", "fabric", "- java", "- minecraft", "Indigo", "Compatibility", "mods", "BiomeModificationImpl"};
            default:
                return new String[]{""};
        }
    }

    public Boolean getDefaultBoolean(String s) {
        switch (s) {
            case "hideClient":
                return true;
            case "hideServer":
                return true;
            case "showResourcePack":
                return false;
            default:
                return false;
        }
    }
}
