package fr.modcraftmc.modcraftmod.common.network;

import fr.modcraftmc.modcraftmod.client.ClientEventHandler;
import fr.modcraftmc.modcraftmod.common.network.packets.S2CServerInfos;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {


    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(S2CServerInfos.TYPE, S2CServerInfos.STREAM_CODEC, PacketHandler::handleServerInfos);


    }

    private static void handleServerInfos(S2CServerInfos s2CServerInfos, IPayloadContext iPayloadContext) {
        iPayloadContext.enqueueWork(() -> {
            ClientEventHandler.serverName = s2CServerInfos.serverName();
        });
    }
}
