package goldenapple.automaticraft.item;

import com.google.common.collect.Multimap;
import goldenapple.automaticraft.reference.Names;
import goldenapple.automaticraft.reference.Reference;
import goldenapple.automaticraft.util.TextHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class ItemDebug extends Item{

    public ItemDebug(){
        this.register(Names.DEBUG);
    }

    protected void register(String name){
        this.setUnlocalizedName(Reference.MOD_ID + ":" + name);
        this.setRegistryName(name);
        this.setMaxStackSize(1);
        GameRegistry.register(this);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            player.addChatMessage(new TextComponentString(state.getBlock().getLocalizedName()).setChatStyle(new Style().setColor(TextFormatting.GOLD)));
            player.addChatMessage(new TextComponentString("Registry name: " + state.getBlock().getRegistryName()));
            player.addChatMessage(new TextComponentString("Metadata: " + state.getBlock().getMetaFromState(state)));
            player.addChatMessage(new TextComponentString("XYZ hit: " + hitX + " " + hitY + " " + hitZ));
            player.addChatMessage(new TextComponentString("-----------------------------").setChatStyle(new Style().setColor(TextFormatting.GOLD)));
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        player.setHealth(9001f);
        player.getFoodStats().addStats(20, 2f);
        player.playSound(SoundEvents.entity_experience_orb_pickup, 0.4f, 3.0f + world.rand.nextFloat() * 0.5f);
        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
        TextHelper.addLocalisedTooltip(tooltip, "automaticraft.debug.desc");
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND){
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 9000D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 0D, 0));
        }
        return multimap;
    }
}
