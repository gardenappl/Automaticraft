package goldenapple.automaticraft;

import goldenapple.automaticraft.init.ModItems;
import goldenapple.automaticraft.reference.Metadata;
import goldenapple.automaticraft.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class AutomaticraftTab extends CreativeTabs{
    public AutomaticraftTab() {
        super(Reference.MOD_ID);
    }

    @Override
    public Item getTabIconItem() {
        return ModItems.material;
    }

    @Override
    public int getIconItemDamage() {
        return Metadata.Material.GEAR;
    }
}
