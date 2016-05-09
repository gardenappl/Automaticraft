package goldenapple.automaticraft.block.tileentity;

import goldenapple.automaticraft.reference.NBTTypes;
import goldenapple.automaticraft.util.Filter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityConveyorDetector extends TileEntity{
    public int count = 0;
    private Filter filter;
    private ItemStack filterStack;

    public TileEntityConveyorDetector(Filter filterType){
        super();
        this.filter = filterType;
    }

    public TileEntityConveyorDetector(){
        this(Filter.NORMAL);
    }

    public ItemStack getFilter(){
        return filterStack;
    }

    public void setFilter(ItemStack stack){
        this.filterStack = stack.copy();
        markDirty();
    }

    public void removeFilter(){
        this.filterStack = null;
        markDirty();
    }

    public boolean hasFilter(){
        return filterStack != null;
    }

    public boolean matchesFilter(ItemStack stack){
        return filter.matches(filterStack, stack);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("filter", NBTTypes.COMPOUND))
            filterStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("filter"));
        else
            filterStack = null;
        filter = Filter.values()[compound.getInteger("type")];
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if(hasFilter())
            compound.setTag("filter", filterStack.serializeNBT());
        compound.setInteger("type", filter.ordinal());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }
}
