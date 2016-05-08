package goldenapple.automaticraft.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public enum Filter {
    NORMAL,
    PRECISE{
        @Override
        public boolean matches(ItemStack filter, ItemStack stack) {
            return filter == null || (filter.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(filter, stack));
        }
    },
    FUZZY{
        @Override
        public boolean matches(ItemStack filter, ItemStack stack) {
            return filter == null || (stack != null && filter.getItem() == stack.getItem());
        }
    },
    ORE{
        @Override
        public boolean matches(ItemStack filter, ItemStack stack) {
            if(filter == null)
                return true;
            if(filter.isItemEqual(stack))
                return true;
            for(int ore1 : OreDictionary.getOreIDs(filter))
                for(int ore2 : OreDictionary.getOreIDs(stack))
                    if(ore1 == ore2)
                        return true;
            return false;
        }
    };

    public boolean matches(ItemStack filter, ItemStack stack){
        return filter == null || filter.isItemEqual(stack);
    }
}
