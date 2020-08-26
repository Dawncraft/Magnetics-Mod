package io.github.dawncraft.magnetics.network;

import java.io.IOException;
import java.util.List;

import io.github.dawncraft.magnetics.api.item.IItemCard;
import io.github.dawncraft.magnetics.container.ContainerPosTerminal;
import io.github.dawncraft.magnetics.tileentity.TileEntityPosTerminal;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 当客户端使用pos机时发包给服务端
 *
 * @author QingChenW
 */
public class MessageWriteCard implements IMessage
{
    Operation op;
    String key;
    String value;
    BlockPos pos;
    ItemStack stack;

    public MessageWriteCard() {}

    public MessageWriteCard(Operation op, String key, String value)
    {
        this(op, key, value, BlockPos.ORIGIN, ItemStack.EMPTY);
    }

    public MessageWriteCard(Operation op, String key, String value, BlockPos pos, ItemStack stack)
    {
        this.op = op;
        this.key = key;
        this.value = value;
        this.pos = pos;
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        PacketBuffer pb = new PacketBuffer(buf);
        this.op = pb.readEnumValue(Operation.class);
        this.key = pb.readString(32);
        this.value = pb.readString(65535);
        if (this.op == Operation.SWIPE)
        {
            try
            {
                this.pos = pb.readBlockPos();
                this.stack = pb.readItemStack();
            }
            catch (IOException e)
            {
                this.stack = ItemStack.EMPTY;
                e.printStackTrace();
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeEnumValue(this.op);
        pb.writeString(this.key);
        pb.writeString(this.value);
        if (this.op == Operation.SWIPE)
        {
            pb.writeBlockPos(this.pos);
            pb.writeItemStack(this.stack);
        }
    }

    public static MessageWriteCard createReadMessage(String key)
    {
        return new MessageWriteCard(Operation.READ, key, "");
    }

    public static MessageWriteCard createWriteMessage(String key, String value)
    {
        return new MessageWriteCard(Operation.WRITE, key, value);
    }

    public static MessageWriteCard createConnectMessage(String id)
    {
        return new MessageWriteCard(Operation.CONNECT, id, "");
    }

    public static MessageWriteCard createSwipeMessage(BlockPos pos, ItemStack stack)
    {
        return new MessageWriteCard(Operation.SWIPE, "", "", pos, stack);
    }

    public static class MessageWriteCardHandler implements IMessageHandler<MessageWriteCard, IMessage>
    {
        @Override
        public IMessage onMessage(MessageWriteCard msg, MessageContext ctx)
        {
            if (ctx.side == Side.SERVER)
            {
                final EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
                serverPlayer.getServer().addScheduledTask(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (serverPlayer.openContainer instanceof ContainerPosTerminal)
                        {
                            ContainerPosTerminal container = (ContainerPosTerminal) serverPlayer.openContainer;
                            if (msg.op == Operation.READ)
                            {
                                return;
                            }
                            else if (msg.op == Operation.WRITE)
                            {
                                try
                                {
                                    writeCard(container.getTileentityPosTerminal(), msg.key, msg.value);
                                }
                                catch (IItemCard.WriteException e)
                                {
                                    if (e.reason.equals("read-only"))
                                    {
                                        serverPlayer.sendMessage(new TextComponentString("磁卡无法写入: 该键值只读! "));
                                    }
                                }
                            }
                            else if (msg.op == Operation.CONNECT)
                            {
                                if (connect(container.getTileentityPosTerminal(), msg.key))
                                {
                                    serverPlayer.sendMessage(new TextComponentString("已连接至: " + container.getTileentityPosTerminal().getConnected()));
                                }
                                else
                                {
                                    serverPlayer.sendMessage(new TextComponentString("连接失败! "));
                                }
                            }
                            container.detectAndSendChanges();
                        }
                    }
                });
            }
            else if (ctx.side == Side.CLIENT)
            {
                final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
                Minecraft.getMinecraft().addScheduledTask(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (msg.op == Operation.SWIPE)
                        {
                            TileEntity tileentity = clientPlayer.world.getTileEntity(msg.pos);
                            if (tileentity instanceof TileEntityPosTerminal)
                            {
                                TileEntityPosTerminal tileentityPosTerminal = (TileEntityPosTerminal) tileentity;
                                tileentityPosTerminal.setLastCard(msg.stack);
                            }
                        }
                    }
                });
            }
            return null;
        }

        public static String readCard(TileEntityPosTerminal tileentityPosTerminal, String key)
        {
            if (!key.isEmpty())
            {
                ItemStack stack = tileentityPosTerminal.getCard();
                if (stack.getItem() instanceof IItemCard)
                {
                    IItemCard itemCard = (IItemCard) stack.getItem();
                    String value = itemCard.getData(stack, key);
                    if (value != null) return value;
                }
            }
            return "";
        }

        public static boolean writeCard(TileEntityPosTerminal tileentityPosTerminal, String key, String value) throws IItemCard.WriteException
        {
            ItemStack stack = tileentityPosTerminal.getCard();
            if (stack.getItem() instanceof IItemCard)
            {
                IItemCard itemCard = (IItemCard) stack.getItem();
                itemCard.setData(stack, key, value);
            }
            return false;
        }

        public static boolean connect(TileEntityPosTerminal tileentityPosTerminal, String id)
        {
            List<String> list = tileentityPosTerminal.getComputerList();
            if (!list.isEmpty())
            {
                id = list.get(0);
                tileentityPosTerminal.setConnected(id);
                return true;
            }
            /*
            if (tileentityPosTerminal.getComputerList().contains(id))
            {
                tileentityPosTerminal.setConnected(id);
                return true;
            }
            */
            return false;
        }
    }

    public static enum Operation
    {
        READ, WRITE, CONNECT, SWIPE;
    }
}
