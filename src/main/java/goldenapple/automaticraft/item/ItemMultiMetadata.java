package goldenapple.automaticraft.item;

import goldenapple.automaticraft.Automaticraft;
import goldenapple.automaticraft.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class ItemMultiMetadata extends Item {
    private String[] names;

    public ItemMultiMetadata(String name, String[] names){
        this.names = names;
        this.setHasSubtypes(true);
        this.setCreativeTab(Automaticraft.creativeTab);
        this.register(name);
    }

    protected void register(String name){
        this.setUnlocalizedName(Reference.MOD_ID + ":" + name);
        this.setRegistryName(name);
        GameRegistry.register(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs tab, List subItems) {
        for(int i = 0; i < names.length; i++)
            subItems.add(new ItemStack(item, 1, i));
    }

    @Override
    public int getMetadata(int meta) {
        return meta < names.length ? meta : 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return itemStack.getItemDamage() < names.length ? "item." + Reference.MOD_ID + ":" + names[itemStack.getItemDamage()] : getUnlocalizedName();
    }
}
