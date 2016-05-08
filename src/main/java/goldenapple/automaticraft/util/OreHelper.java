package goldenapple.automaticraft.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreHelper {
    public static boolean isItemThisOre(ItemStack stack, String ore){
        for (int o : OreDictionary.getOreIDs(stack)) {
            if (OreDictionary.getOreName(o).equals(ore)) {
                return true;
            }
        }
        return false;
    }

    public static void dumpAllOres(){
        for (String l : OreDictionary.getOreNames()){
            LogHelper.info(l);
        }
    }
}
