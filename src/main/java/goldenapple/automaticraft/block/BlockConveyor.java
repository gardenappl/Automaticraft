package goldenapple.automaticraft.block;

import goldenapple.automaticraft.reference.Names;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConveyor extends BlockModHorizontal {
    public static final PropertyEnum<EnumSlope> SLOPE = PropertyEnum.create("slope", BlockConveyor.EnumSlope.class);

    protected static final AxisAlignedBB[] NORMAL_AABB = new AxisAlignedBB[]{
            new AxisAlignedBB(0.125f, 0f, 0f, 0.875f, 0.125f, 1f), //south
            new AxisAlignedBB(0f, 0f, 0.125f, 1f, 0.125f, 0.875f), //west
            new AxisAlignedBB(0.125f, 0f, 0f, 0.875f, 0.125f, 1f), //north
            new AxisAlignedBB(0f, 0f, 0.125f, 1f, 0.125f, 0.875f)}; //east

    protected static final AxisAlignedBB[] SLOPED_AABB = new AxisAlignedBB[]{
            new AxisAlignedBB(0.125f, 0f, 0f, 0.875f, 0.625f, 1f), //south
            new AxisAlignedBB(0f, 0f, 0.125f, 1f, 0.625f, 0.875f), //west
            new AxisAlignedBB(0.125f, 0f, 0f, 0.875f, 0.625f, 1f), //north
            new AxisAlignedBB(0f, 0f, 0.125f, 1f, 0.625f, 0.875f)}; //east

    protected double movementSpeed = 0.07d;
    private boolean canBeSloped;

    public BlockConveyor(){
        this(Names.CONVEYOR, true);
    }

    protected BlockConveyor(String name, boolean canBeSloped){
        super(Material.circuits, name);
        this.canBeSloped = canBeSloped;
        if(canBeSloped)
            this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SLOPE, EnumSlope.NONE));
        else
            this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setHardness(0.7F);
        this.setStepSound(SoundType.METAL);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, canBeSloped ? new IProperty[]{FACING} : new IProperty[]{FACING, SLOPE});
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if(!world.isRemote) {
            pushEntity(entity, state.getValue(FACING), getSlope(state) == EnumSlope.UP);
        }
    }

    protected void pushEntity(Entity entity, EnumFacing direction, boolean pushUp){
        if (entity.canBePushed() || entity instanceof EntityItem || entity instanceof EntityPlayer || entity instanceof EntityXPOrb) {
            entity.isAirBorne = true;
            entity.motionX += movementSpeed * direction.getFrontOffsetX();
            entity.motionZ += movementSpeed * direction.getFrontOffsetZ();
            if(pushUp) {
                entity.onGround = false;
                entity.motionY += movementSpeed;
            }
            if(entity instanceof EntityItem)
                ((EntityItem) entity).setAgeToCreativeDespawnTime();
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!world.isSideSolid(pos.down(), EnumFacing.UP)) {
            if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops"))
                dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
            return;
        }
        if(canBeSloped) {
            if (state.getValue(SLOPE) == EnumSlope.UP) {
                EnumFacing facing = state.getValue(FACING);
                if (!world.isSideSolid(pos.offset(facing), EnumFacing.UP)) {
                    if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops"))
                        dropBlockAsItem(world, pos, state, 0);
                    world.setBlockToAir(pos);
                }
            } else if (state.getValue(SLOPE) == EnumSlope.DOWN) {
                EnumFacing facing = state.getValue(FACING);
                if (!world.isSideSolid(pos.offset(facing.getOpposite()), EnumFacing.UP)) {
                    if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
                        dropBlockAsItem(world, pos, state, 0);
                    }
                    world.setBlockToAir(pos);
                }
            } else
                updateSlope(world, pos, state);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        world.notifyNeighborsOfStateExcept(pos.down(), this, EnumFacing.UP);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if(!world.isRemote) {
            world.notifyNeighborsOfStateExcept(pos.down(), this, EnumFacing.UP);
            if(canBeSloped)
                updateSlope(world, pos, state);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && world.isSideSolid(pos.down(), EnumFacing.UP);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if(isSloped(state))
            return SLOPED_AABB[state.getValue(FACING).getHorizontalIndex()];
        else
            return NORMAL_AABB[state.getValue(FACING).getHorizontalIndex()];
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
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

    @Override
    public int getMetaFromState(IBlockState state) {
        if(!canBeSloped)
            return super.getMetaFromState(state);
        switch (state.getValue(SLOPE)){
            case UP: return super.getMetaFromState(state) + 4;
            case DOWN: return super.getMetaFromState(state) + 8;
            default: return super.getMetaFromState(state);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = super.getStateFromMeta(meta % 4);
        if(!canBeSloped)
            return state;

        if(meta < 4)
            return state.withProperty(SLOPE, EnumSlope.NONE);
        else if(meta < 8)
            return state.withProperty(SLOPE, EnumSlope.UP);
        else if(meta < 12)
            return state.withProperty(SLOPE, EnumSlope.DOWN);
        else
            return getDefaultState();
    }

    private void updateSlope(World world, BlockPos pos, IBlockState state){
        EnumFacing facing = state.getValue(FACING);

        if(world.getBlockState(pos.up()).getBlock() instanceof BlockConveyorSitcky) { //if there's a Sticky Conveyor at the top
            if (world.getBlockState(pos.up()).getValue(FACING) == facing) { //if it's facing the same direction
                world.setBlockState(pos, state.withProperty(SLOPE, EnumSlope.UP));
                return;
            }
        }

        if(world.getBlockState(pos.offset(facing).up()).getBlock() instanceof BlockConveyor) //if there's a conveyor on top of the block in front
            world.setBlockState(pos, state.withProperty(SLOPE, EnumSlope.UP), 2);
        else if(world.getBlockState(pos.offset(facing, -1).up()).getBlock() instanceof BlockConveyor) //if there's a conveyor on top of the block behind
            world.setBlockState(pos, state.withProperty(SLOPE, EnumSlope.DOWN), 2);
        else
            world.setBlockState(pos, state.withProperty(SLOPE, EnumSlope.NONE), 2);
    }

    public boolean isSloped(IBlockState state){
        return canBeSloped && (state.getValue(SLOPE) == EnumSlope.UP || state.getValue(SLOPE) == EnumSlope.DOWN);
    }

    public EnumSlope getSlope(IBlockState state){
        if(!canBeSloped)
            return EnumSlope.NONE;
        else
            return state.getValue(SLOPE);
    }

    public enum EnumSlope implements IStringSerializable {
        UP("up"),
        DOWN("down"),
        NONE("none");

        private String name;
        private EnumSlope(String name){
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
