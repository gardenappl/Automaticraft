package goldenapple.automaticraft.block;

import goldenapple.automaticraft.block.tileentity.TileEntityConveyorDetector;
import goldenapple.automaticraft.util.Filter;
import goldenapple.automaticraft.util.TextHelper;
import net.minecraft.block.BlockRailDetector;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BlockConveyorDetector extends BlockConveyor implements ITileEntityProvider {
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public final Filter filter;

    public BlockConveyorDetector(String name, Filter filter){
        super(name, false);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
        this.filter = filter;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, POWERED);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityConveyorDetector(filter, FULL_BLOCK_AABB);
    }

    private TileEntityConveyorDetector getTileEntity(IBlockAccess world, BlockPos pos){
        return (TileEntityConveyorDetector) world.getTileEntity(pos);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        super.onEntityCollidedWithBlock(world, pos, state, entity);
        if(!world.isRemote && entity instanceof EntityItem)
            updatePoweredState(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            if(player.isSneaking()){
                TextHelper.addChatMessage(player, "automaticraft.detector.remove_filter");
                getTileEntity(world, pos).removeFilter();
            }else{
                if (getTileEntity(world, pos).hasFilter()) {
                    TextHelper.addChatMessage(player, "automaticraft.detector.filter", getTileEntity(world, pos).getFilter().getDisplayName());
                } else {
                    if(heldItem != null) {
                        TextHelper.addChatMessage(player, "automaticraft.detector.set_filter", heldItem.getDisplayName());
                        getTileEntity(world, pos).setFilter(heldItem);
                    }else{
                        TextHelper.addChatMessage(player, "automaticraft.detector.no_filter");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        if(!world.isRemote)
            updatePoweredState(world, pos, state);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? getWeakPower(state, world, pos, side) : 0;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return getTileEntity(world, pos).count;
    }

    @Override
    public int tickRate(World world) {
        return 5;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if(!world.isRemote)
            updatePoweredState(world, pos, state);
    }

    /** Very similar to {@link BlockRailDetector#updatePoweredState(World, BlockPos, IBlockState)} )} */

    private void updatePoweredState(World world, BlockPos pos, IBlockState state){
        if(!world.isRemote) {
            int count = getTileEntity(world, pos).findMatchingItems(world);
            boolean powered = state.getValue(POWERED);
            getTileEntity(world, pos).count = count;
            if (count > 0 && !powered) {
                world.setBlockState(pos, state.withProperty(BlockConveyorDetector.POWERED, true), 1 | 2);
                world.playSound(null, pos, SoundEvents.block_wood_pressplate_click_on, SoundCategory.BLOCKS, 0.3F, 0.8F);
                world.notifyNeighborsOfStateExcept(pos.down(), this, EnumFacing.UP);
            } else if (count == 0 && powered) {
                world.setBlockState(pos, state.withProperty(BlockConveyorDetector.POWERED, false), 1 | 2);
                world.playSound(null, pos, SoundEvents.block_wood_pressplate_click_off, SoundCategory.BLOCKS, 0.3F, 0.7F);
                world.notifyNeighborsOfStateExcept(pos.down(), this, EnumFacing.UP);
            }
            if (count > 0)
                world.scheduleUpdate(new BlockPos(pos), this, tickRate(world));
            world.updateComparatorOutputLevel(pos, this);
        }
    }

//    private int findMatchingItems(World world, BlockPos pos){
//        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, FULL_BLOCK_AABB.offset(pos));
//        int count = 0;
//        for(EntityItem entity : list) {
//            if (!getTileEntity(world, pos).hasFilter() || getTileEntity(world, pos).matchesFilter(entity.getEntityItem())){
//                count += entity.getEntityItem().stackSize;
//            }
//        }
//        return count;
//    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(POWERED, meta > 4);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(state.getValue(POWERED))
            return super.getMetaFromState(state) + 4;
        else
            return super.getMetaFromState(state);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        TextHelper.addLocalisedTooltip(tooltip, "automaticraft.detector.desc");
        if(filter != Filter.NORMAL)
            TextHelper.addLocalisedTooltip(tooltip, "automaticraft.detector.%s.desc", filter.name().toLowerCase(Locale.ROOT));
    }
}
