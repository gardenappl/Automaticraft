package goldenapple.automaticraft.proxy;

import goldenapple.automaticraft.reference.Names;
import goldenapple.automaticraft.init.ModBlocks;
import goldenapple.automaticraft.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends Proxy {
    @Override
    public void registerRenders() {
        registerRender(ModBlocks.conveyor);
        registerRender(ModBlocks.conveyor_redstone);
        registerRender(ModBlocks.conveyor_sticky);
        registerRender(ModBlocks.conveyor_detector);
        registerRender(ModBlocks.conveyor_detector_precise);
        registerRender(ModBlocks.conveyor_detector_fuzzy);
        registerRender(ModBlocks.conveyor_detector_ore);
        registerRender(ModBlocks.detector);

        registerRender(ModItems.debug);
        registerRenders(ModItems.material, Names.MATERIALS);
    }

    private static void registerRender(Block block){
        registerRender(Item.getItemFromBlock(block));
    }

    private static void registerRenders(Block block, String[] names){
        registerRenders(Item.getItemFromBlock(block), names);
    }

    private static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    private static void registerRenders(Item item, String[] names){
        for(int i = 0; i < names.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName() + "_" + names[i], "inventory"));
        }
    }
}
