package io.github.dawncraft.magnetics.tileentity;

import java.util.ArrayList;
import java.util.List;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.dawncraft.magnetics.CommonProxy;
import io.github.dawncraft.magnetics.api.item.IItemCard;
import io.github.dawncraft.magnetics.sound.ModSounds;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.common.asm.SimpleComponentTickHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * POS terminal's tile entity
 *
 * @author QingChenW
 */
@Optional.InterfaceList({
    @Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = CommonProxy.CC_MODID),
    @Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = CommonProxy.OC_MODID),
    @Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = CommonProxy.OC_MODID)
})
public class TileEntityPosTerminal extends TileEntity implements IWorldNameable, IPeripheral, Environment, ManagedPeripheral
{
    private String customName;
    private final ItemStackHandler inventory = new ItemStackHandler()
    {
        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return stack.getItem() instanceof IItemCard;
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }
    };

    public TileEntityPosTerminal()
    {
        if (CommonProxy.isOCLoaded)
        {
            this.node = Network.newNode(this, Visibility.Network).withComponent(this.getComponentName()).withConnector(0.01D).create();
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos blockPos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void validate()
    {
        super.validate();
        if (CommonProxy.isOCLoaded)
        {
            SimpleComponentTickHandler.schedule(this);
        }
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (CommonProxy.isOCLoaded)
        {
            if (this.node() != null) this.node().remove();
        }
    }

    @Override
    public void onChunkUnload()
    {
        super.onChunkUnload();
        if (CommonProxy.isOCLoaded)
        {
            if (this.node() != null) this.node().remove();
        }
    }

    public void swipeCard(EntityPlayer player, ItemStack stack)
    {
        player.world.playSound(null, this.getPos(), ModSounds.BLOCK_POS_TERMINAL_SWIPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    /**
     * 若不是特殊情况不要用这个
     *
     * @return
     */
    @Deprecated
    public ItemStack getCard()
    {
        return this.inventory.getStackInSlot(0);
    }

    /**
     * 返回是否有卡
     *
     * @return
     */
    public boolean hasCard()
    {
        return this.inventory.getStackInSlot(0).getItem() instanceof IItemCard;
    }

    /**
     * 若无卡或为空白卡则返回空
     *
     * @param key
     * @return
     */
    public String readCard(String key)
    {
        if (this.hasCard())
        {
            ItemStack stack = this.inventory.getStackInSlot(0);
            IItemCard itemCard = (IItemCard) stack.getItem();
            return itemCard.getData(stack, key);
        }
        return null;
    }

    /**
     * 无卡返回false
     *否则返回true
     *
     * @param key
     * @param value
     * @return
     */
    public boolean writeCard(String key, String value)
    {
        if (this.hasCard())
        {
            ItemStack stack = this.inventory.getStackInSlot(0);
            IItemCard itemCard = (IItemCard) stack.getItem();
            itemCard.setData(stack, key, value);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String name)
    {
        this.customName = name;
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.posTerminal";
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
        this.connected = tag.getString("Computer");
        if (CommonProxy.isOCLoaded)
        {
            if (this.node() != null)
            {
                this.node().load(tag.getCompoundTag("oc:node"));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setTag("Inventory", this.inventory.serializeNBT());
        tag.setString("Computer", this.connected);
        if (CommonProxy.isOCLoaded)
        {
            if (this.node() != null)
            {
                NBTTagCompound tagNode = new NBTTagCompound();
                this.node().save(tagNode);
                tag.setTag("oc:node", tagNode);
            }
        }
        return tag;
    }

    // Common computer
    public static final String DEVICE_NAME = "pos_terminal";
    private static final String[] METHOD_NAMES = { "hasCard", "readCard", "writeCard" };
    private List<String> computerList = new ArrayList<>();
    private String connected = "";

    public List<String> getComputerList()
    {
        return this.computerList;
    }

    public String getConnected()
    {
        return this.connected;
    }

    public void setConnected(String connected)
    {
        this.connected = connected;
    }

    public Object[] invokeMethod(String method, Object... args) throws Exception
    {
        if (method.equals("hasCard"))
        {
            if (args.length == 0)
            {
                return new Boolean[] { this.hasCard() };
            }
            throw new IllegalArgumentException("bad argument (no args expected)");
        }
        else if (method.equals("readCard"))
        {
            if (args.length == 1 && args[0] instanceof String)
            {
                return new String[] { this.readCard((String) args[0])};
            }
            throw new IllegalArgumentException("bad argument (string expected)");
        }
        else if (method.equals("writeCard"))
        {
            if (args.length == 2 && args[0] instanceof String && args[1] instanceof String)
            {
                return new Boolean[] { this.writeCard((String) args[0], (String) args[1])};
            }
            throw new IllegalArgumentException("bad argument (string, string expected)");
        }
        throw new NoSuchMethodError();
    }

    // ComputerCraft
    @Override
    @Optional.Method(modid = CommonProxy.CC_MODID)
    public String getType()
    {
        return DEVICE_NAME;
    }

    @Override
    @Optional.Method(modid = CommonProxy.CC_MODID)
    public void attach(IComputerAccess computer)
    {
        this.computerList.add("" + computer.getID());
    }

    @Override
    @Optional.Method(modid = CommonProxy.CC_MODID)
    public void detach(IComputerAccess computer)
    {
        this.computerList.remove("" + computer.getID());
    }

    @Override
    @Optional.Method(modid = CommonProxy.CC_MODID)
    public String[] getMethodNames()
    {
        return METHOD_NAMES;
    }

    @Override
    @Optional.Method(modid = CommonProxy.CC_MODID)
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException
    {
        try
        {
            if (!this.connected.equals("" + computer.getID()))
            {
                throw new LuaException("Not connect to this computer!");
            }
            return this.invokeMethod(METHOD_NAMES[method], arguments);
        }
        catch (Exception e)
        {
            throw new LuaException(e.getMessage());
        }
    }

    @Override
    @Optional.Method(modid = CommonProxy.CC_MODID)
    public boolean equals(IPeripheral other)
    {
        return this.hashCode() == other.hashCode();
    }

    // OpenComputers
    private Object node;

    @Optional.Method(modid = CommonProxy.OC_MODID)
    public String getComponentName()
    {
        return DEVICE_NAME;
    }

    @Override
    @Optional.Method(modid = CommonProxy.OC_MODID)
    public Node node()
    {
        return (Node) this.node;
    }

    @Override
    @Optional.Method(modid = CommonProxy.OC_MODID)
    public void onConnect(Node node)
    {
        this.computerList.add(node.address());
    }

    @Override
    @Optional.Method(modid = CommonProxy.OC_MODID)
    public void onDisconnect(Node node)
    {
        this.computerList.remove(node.address());
    }

    @Override
    @Optional.Method(modid = CommonProxy.OC_MODID)
    public void onMessage(Message message) {}

    @Override
    @Optional.Method(modid = CommonProxy.OC_MODID)
    public String[] methods()
    {
        return METHOD_NAMES;
    }

    @Override
    @Optional.Method(modid = CommonProxy.OC_MODID)
    public Object[] invoke(String method, Context context, Arguments args) throws Exception
    {
        if (!this.connected.equals(context.node().address()))
        {
            throw new IllegalArgumentException("Not connect to this computer!");
        }
        for (String name : METHOD_NAMES)
        {
            if (name.equals(method)) return this.invokeMethod(method, args.toArray());
        }
        throw new NoSuchMethodError();
    }
}
