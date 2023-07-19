package fr.modcraftmc.modcraftmod.common.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CServerInfos {
    public static void encode(S2CServerInfos msg, FriendlyByteBuf friendlyByteBuf) {
    }

    public static S2CServerInfos decode(FriendlyByteBuf friendlyByteBuf) {

        return new S2CServerInfos();
    }

    public static void handle(S2CServerInfos msg, Supplier<NetworkEvent.Context> ctx) {
    }
}
