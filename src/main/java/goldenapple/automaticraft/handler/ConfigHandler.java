package goldenapple.automaticraft.handler;

import goldenapple.automaticraft.reference.Reference;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    public static boolean dumpAllOres = false;

    public static void init(File configFile) {
        if (config == null){
            config = new Configuration(configFile);
        }
        loadConfig();
    }

    private static void loadConfig(){
        dumpAllOres = config.get(Configuration.CATEGORY_GENERAL, "Dump all ores", false).setLanguageKey("automaticraft.config.dump_all_ores").setRequiresMcRestart(true).getBoolean();

        if (config.hasChanged())
            config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(Reference.MOD_ID))
            loadConfig();
    }
}