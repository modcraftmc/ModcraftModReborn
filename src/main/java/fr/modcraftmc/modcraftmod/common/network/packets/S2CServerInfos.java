package fr.modcraftmc.modcraftmod.common.network.packets;

import fr.modcraftmc.modcraftmod.client.ClientEventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CServerInfos implements Packet {

    public String serverName;

    public S2CServerInfos(String serverName) {
        this.serverName = serverName;
    }
    public static void encode(S2CServerInfos msg, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(msg.serverName);
    }

    public static S2CServerInfos decode(FriendlyByteBuf friendlyByteBuf) {
        return new S2CServerInfos(friendlyByteBuf.readUtf());
    }

    public static void handle(S2CServerInfos msg, Supplier<NetworkEvent.Context> ctx) {
        ClientEventHandler.serverName = msg.serverName;
    }
}
