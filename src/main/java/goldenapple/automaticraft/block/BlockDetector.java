package goldenapple.automaticraft.block;

import goldenapple.automaticraft.block.tileentity.TileEntityDetector;
import goldenapple.automaticraft.reference.Names;
import goldenapple.automaticraft.util.Filter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDetector extends BlockModHorizontal implements ITileEntityProvider{
    public static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public final Filter filter;

    public BlockDetector(Filter filter) {
        super(Material.circuits, Names.DETECTOR);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
        this.filter = filter;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, POWERED);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDetector(filter, FULL_BLOCK_AABB.offset(getStateFromMeta(meta).getValue(FACING).getFrontOffsetX(), 0, getStateFromMeta(meta).getValue(FACING).getFrontOffsetZ()));
    }

    private TileEntityDetector getTileEntity(IBlockAccess world, BlockPos pos){
        return (TileEntityDetector) world.getTileEntity(pos);
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

    private void updatePoweredState(World world, BlockPos pos, IBlockState state){
        if(!world.isRemote) {
            int count = getTileEntity(world, pos).findMatchingItems(world);
            boolean powered = state.getValue(POWERED);
            getTileEntity(world, pos).count = count;
            if (count > 0 && !powered) {
                world.setBlockState(pos, state.withProperty(POWERED, true), 1 | 2);
                world.playSound(null, pos, SoundEvents.block_wood_pressplate_click_on, SoundCategory.BLOCKS, 0.3F, 0.8F);
                world.notifyNeighborsOfStateExcept(pos.down(), this, EnumFacing.UP);
            } else if (count == 0 && powered) {
                world.setBlockState(pos, state.withProperty(POWERED, false), 1 | 2);
                world.playSound(null, pos, SoundEvents.block_wood_pressplate_click_off, SoundCategory.BLOCKS, 0.3F, 0.7F);
                world.notifyNeighborsOfStateExcept(pos.down(), this, EnumFacing.UP);
            }
            if (count > 0)
                world.scheduleUpdate(new BlockPos(pos), this, tickRate(world));
            world.updateComparatorOutputLevel(pos, this);
        }
    }

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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
