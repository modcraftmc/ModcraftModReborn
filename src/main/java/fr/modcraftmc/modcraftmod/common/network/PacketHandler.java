package fr.modcraftmc.modcraftmod.common.network;

import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import fr.modcraftmc.modcraftmod.common.network.packets.Packet;
import fr.modcraftmc.modcraftmod.common.network.packets.S2CServerInfos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static short index = 0;

    public static final SimpleChannel MAIN = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ModcraftModReborn.MODID, "main"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        MAIN.registerMessage(index++, S2CServerInfos.class, S2CServerInfos::encode, S2CServerInfos::decode, S2CServerInfos::handle);
    }
    public static void sendTo(Packet message, ServerPlayer player) {
        MAIN.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
