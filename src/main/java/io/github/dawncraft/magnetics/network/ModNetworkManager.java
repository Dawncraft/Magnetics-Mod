package io.github.dawncraft.magnetics.network;

import io.github.dawncraft.magnetics.MagneticsMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModNetworkManager
{
    public static final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(MagneticsMod.MODID);
    private static int nextID = 0;

    public static void init()
    {
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<REQ> requestMessage, Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Side handlerSide)
    {
        channel.registerMessage(messageHandler, requestMessage, nextID++, handlerSide);
    }

    // 向某个玩家发包
    public static void sendMessageToPlayer(IMessage msg, EntityPlayerMP player)
    {
        channel.sendTo(msg, player);
    }

    // 向所有人发包
    public static void sendMessageToAll(IMessage msg)
    {
        channel.sendToAll(msg);
    }

    // 向某个维度的某个点发包
    public static void sendMessageAroundPos(IMessage msg, int dim, BlockPos pos)
    {
        channel.sendToAllAround(msg, new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), 64.0D));
    }

    // 向某个维度发包
    public static void sendMessageToDimension(IMessage msg, int dim)
    {
        channel.sendToDimension(msg, dim);
    }

    // 向正在追踪某实体的人发包
    public static void sendMessageToAllTracking(IMessage msg, Entity entity)
    {
        channel.sendToAllTracking(msg, entity);
    }

    // 向正在追踪某点的人发包
    public static void sendMessageToAllTracking(IMessage msg, int dim, BlockPos pos)
    {
        channel.sendToAllTracking(msg, new TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), 64.0D));
    }

    // 向服务器发包
    @SideOnly(Side.CLIENT)
    public static void sendMessageToServer(IMessage msg)
    {
        channel.sendToServer(msg);
    }
}
