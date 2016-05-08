package goldenapple.automaticraft.block;

import goldenapple.automaticraft.reference.Names;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConveyorSitcky extends BlockModHorizontal {

    protected static final AxisAlignedBB[] AABB = new AxisAlignedBB[]{
            new AxisAlignedBB(0.125F, 0F, 0.875F, 0.875F, 1F, 1F), //south
            new AxisAlignedBB(0.125F, 0F, 0.125F, 0F, 1F, 0.875F), //west
            new AxisAlignedBB(0.125F, 0F, 0F, 0.875F, 1F, 0.125F), //north
            new AxisAlignedBB(0.875F, 0F, 0.125F, 1F, 1F, 0.875F)}; //east

    public BlockConveyorSitcky() {
        super(Material.circuits, Names.CONVEYOR_STICKY);
        this.setHardness(0.7F);
        this.setStepSound(SoundType.METAL);
        this.placeOnBlockSide = true;
        this.faceAgainstPlacer = true;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if(!world.isRemote) {
            if (entity.canBePushed() || entity instanceof EntityItem || entity instanceof EntityPlayer || entity instanceof EntityXPOrb) {
                EnumFacing facing = state.getValue(FACING);
                entity.addVelocity(facing.getFrontOffsetX() / 20D, 0, facing.getFrontOffsetZ() / 20D);
                entity.onGround = false;
                entity.motionY = 0.2D;
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        if(!world.isRemote) {
            EnumFacing facing = world.getBlockState(pos).getValue(FACING);
            if(!world.isSideSolid(pos.offset(facing), facing.getOpposite())) {
                if (world.getGameRules().getBoolean("doTileDrops")) {
                    dropBlockAsItem(world, pos, state, 0);
                }
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        world.notifyNeighborsOfStateExcept(pos.down(), this, EnumFacing.UP);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if(!world.isRemote)
            world.notifyNeighborsOfStateExcept(pos.down(), this, EnumFacing.UP);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
        if(side.getAxis() != EnumFacing.Axis.Y && world.isSideSolid(pos.offset(side.getOpposite()), side))
            return canPlaceBlockAt(world, pos);
        else
            return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB[state.getValue(FACING).getHorizontalIndex()];
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
