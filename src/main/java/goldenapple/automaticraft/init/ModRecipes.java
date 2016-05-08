package goldenapple.automaticraft.init;

import goldenapple.automaticraft.reference.Metadata;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {
    public static void init(){
        //Gear
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.material, 2, Metadata.Material.GEAR),
                " i ",
                "iSi",
                " i ", 'i', "ingotIron", 'S', "plankWood"));

        //Conveyor Belt
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.conveyor, 8),
                "iii",
                "sGs", 'i', "ingotIron", 'G', new ItemStack(ModItems.material, 1, Metadata.Material.GEAR), 's', "stickWood"));

        //Redstone Belt
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.conveyor_redstone, 1),
                "ggg",
                "rGr", 'g', "ingotGold", 'r', "dustRedstone", 'G', new ItemStack(ModItems.material, 1, Metadata.Material.GEAR)));

        //Sticky Belt
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.conveyor_sticky),
                ModBlocks.conveyor, "slimeball"));
    }
}
