package goldenapple.automaticraft;

import goldenapple.automaticraft.handler.ConfigHandler;
import goldenapple.automaticraft.proxy.Proxy;
import goldenapple.automaticraft.init.*;
import goldenapple.automaticraft.reference.Reference;
import goldenapple.automaticraft.util.LogHelper;
import goldenapple.automaticraft.util.OreHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, useMetadata = true, guiFactory = Reference.GUI_FACTORY)
public class Automaticraft {

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static Proxy proxy;

    public static final AutomaticraftTab creativeTab = new AutomaticraftTab();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        LogHelper.logger = event.getModLog();
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
        ModItems.init();
        ModBlocks.init();
        proxy.registerRenders();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        if(ConfigHandler.dumpAllOres) OreHelper.dumpAllOres();
    }
}
