package goldenapple.automaticraft.block;

import goldenapple.automaticraft.reference.Names;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockConveyorPowered extends BlockConveyor {
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockConveyorPowered(){
        super(Names.CONVEYOR_REDSTONE, false);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, POWERED);
    }

//    @Override
//    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
//        if(!state.getValue(POWERED)) {
//            super.onEntityCollidedWithBlock(world, pos, state, entity);
//        }else if(!world.isRemote) {
//            pushEntity(entity, state.getValue(FACING).getOpposite(), false);
//        }
//    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        if(!world.isRemote)
            updatePoweredState(world, pos, state);
        super.onNeighborBlockChange(world, pos, state, neighborBlock);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if(!world.isRemote)
            updatePoweredState(world, pos, state);
        super.onBlockAdded(world, pos, state);
    }

    @Override
    public int tickRate(World world) {
        return 2;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
        if(!world.isRemote)
            updatePoweredState(world, pos, state);
    }

    private void updatePoweredState(World world, BlockPos pos, IBlockState state){
        boolean powered = world.isBlockPowered(pos);
        if (powered && !state.getValue(POWERED)) {
            world.setBlockState(pos, state.withProperty(POWERED, true).withProperty(FACING, state.getValue(FACING).getOpposite()), 2);
            world.scheduleUpdate(pos, this, tickRate(world));
        }else if (!powered && state.getValue(POWERED)) {
            world.setBlockState(pos, state.withProperty(POWERED, false).withProperty(FACING, state.getValue(FACING).getOpposite()), 2);
            world.scheduleUpdate(pos, this, tickRate(world));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(state.getValue(POWERED))
            return super.getMetaFromState(state) + 4;
        else
            return super.getMetaFromState(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(POWERED, meta > 4);
    }
}
