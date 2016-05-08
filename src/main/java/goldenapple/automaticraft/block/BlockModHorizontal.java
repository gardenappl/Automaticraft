package goldenapple.automaticraft.block;

import goldenapple.automaticraft.Automaticraft;
import goldenapple.automaticraft.reference.Reference;
import goldenapple.automaticraft.util.LogHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockModHorizontal extends BlockHorizontal {
    public boolean faceAgainstPlacer;
    public boolean placeOnBlockSide;

    protected BlockModHorizontal(Material material, String name) {
        super(material);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setCreativeTab(Automaticraft.creativeTab);
        this.register(name);
    }

    protected void register(String name) {
        this.setUnlocalizedName(Reference.MOD_ID + ":" + name);
        this.setRegistryName(name);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), this.getRegistryName());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if(!placeOnBlockSide)
            side = placer.getHorizontalFacing();
        return this.getDefaultState().withProperty(FACING, faceAgainstPlacer ? side.getOpposite() : side);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(state.getValue(FACING).getAxis() == EnumFacing.Axis.Y){
            LogHelper.warn("Horizontal block's 'facing' property is %s!", state.getValue(FACING));
            return getDefaultState().getValue(FACING).getHorizontalIndex();
        }
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withProperty(FACING, mirror.mirror(state.getValue(FACING)));
    }
}
