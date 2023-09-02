package fr.modcraftmc.modcraftmod.client.reset;

import fr.modcraftmc.modcraftmod.client.screen.ModcraftTitleScreen;
import fr.modcraftmc.modcraftmod.client.screen.ResetScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.GameData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ResetHandler {

    public static Field handshakeField;
    public static Constructor contextConstructor;
    static final Logger logger = LogManager.getLogger();
    static final Marker RESETMARKER = MarkerManager.getMarker("RESETPACKET").setParents(MarkerManager.getMarker("FMLNETWORK"));

    public static SimpleChannel handshakeChannel;

    public static void register() {
        handshakeField = fetchHandshakeChannel();
        contextConstructor = fetchNetworkEventContext();

        if (handshakeField == null) {
            logger.error(RESETMARKER, "Failed to find FML's handshake channel. Disabling mod.");
            return;
        }
        if (contextConstructor == null) {
            logger.error(RESETMARKER, "Failed to find FML's network event context constructor. Disabling mod.");
            return;
        }
        try {
            //handshakeField.setAccessible(true);
            //contextConstructor.setAccessible(true);
            Object handshake = handshakeField.get(null);
            if (handshake instanceof SimpleChannel) {
                handshakeChannel = (SimpleChannel)handshake;
                logger.info(RESETMARKER, "Registering forge reset packet.");
                handshakeChannel.messageBuilder(S2CResetPacket.class, 98)
                        .loginIndex(S2CResetPacket::getLoginIndex, S2CResetPacket::setLoginIndex)
                        .decoder(S2CResetPacket::decode)
                        .encoder(S2CResetPacket::encode)
                        .consumerNetworkThread(HandshakeHandler.biConsumerFor(ResetHandler::handleReset))
                        .add();
                logger.info(RESETMARKER, "Registered forge reset packet successfully.");
            }
        }
        catch (Exception e) {
            logger.error(RESETMARKER, "Caught exception when attempting to utilize FML's handshake. Disabling mod. Exception: " + e.getMessage());
        }
    }

    public static void handleReset(HandshakeHandler handler, S2CResetPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        Connection connection = context.getNetworkManager();

        if (context.getDirection() != NetworkDirection.LOGIN_TO_CLIENT && context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
            connection.disconnect(Component.literal("Illegal packet received, terminating connection"));
            throw new IllegalStateException("Invalid packet received, aborting connection");
        }

        logger.info(RESETMARKER, "Received reset packet from server.");

        ResetScreen resetScreen =  new ResetScreen(new ModcraftTitleScreen());
        if (!handleClear(context, resetScreen)) {
            return;
        }

        NetworkHooks.registerClientLoginChannel(connection);
        connection.setProtocol(ConnectionProtocol.LOGIN);
        connection.setListener(new ClientHandshakePacketListenerImpl(
                connection, Minecraft.getInstance(), new TitleScreen(), resetScreen::updateStatus
        ));
        Minecraft.getInstance().pendingConnection = connection;

        context.setPacketHandled(true);
        try {
            handshakeChannel.reply(
                    new HandshakeMessages.C2SAcknowledge(),
                    (NetworkEvent.Context)contextConstructor.newInstance(connection, NetworkDirection.LOGIN_TO_CLIENT, 98)
            );
        }
        catch (Exception e) {
            logger.error(RESETMARKER, "Exception occurred when attempting to reply to reset packet.  Exception: " + e.getMessage());
            context.setPacketHandled(false);
            return;
        }
        logger.info(RESETMARKER, "Reset complete.");
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean handleClear(NetworkEvent.Context context, ResetScreen resetScreen) {
        CompletableFuture<Void> future = context.enqueueWork(() -> {
            logger.debug(RESETMARKER, "Clearing");

            // Preserve
            ServerData serverData = Minecraft.getInstance().getCurrentServer();
            Pack serverPack = Minecraft.getInstance().getClientPackSource().serverPack;

            // Clear
            if (Minecraft.getInstance().level == null) {
                // Ensure the GameData is reverted in case the client is reset during the handshake.
                GameData.revertToFrozen();
            }
            Minecraft.getInstance().getClientPackSource().serverPack = null;

            // Clear
            Minecraft.getInstance().clearLevel(resetScreen);

            try {
                context.getNetworkManager().channel().pipeline().remove("forge:forge_fixes");
            } catch (NoSuchElementException ignored) {
            }
            try {
                context.getNetworkManager().channel().pipeline().remove("forge:vanilla_filter");
            } catch (NoSuchElementException ignored) {
            }
            // Restore
            Minecraft.getInstance().getClientPackSource().serverPack = serverPack;
            Minecraft.getInstance().setCurrentServer(serverData);
        });

        logger.debug(RESETMARKER, "Waiting for clear to complete");
        try {
            future.get();
            logger.debug("Clear complete, continuing reset");
            return true;
        } catch (Exception ex) {
            logger.error(RESETMARKER, "Failed to clear, closing connection", ex);
            context.getNetworkManager().disconnect(Component.literal("Failed to clear, closing connection"));
            return false;
        }
    }

    private static Field fetchHandshakeChannel() {
        try {
            return ObfuscationReflectionHelper.findField(NetworkConstants.class, "handshakeChannel");
        }
        catch (Exception e) {
            logger.error("Exception occurred while accessing handshakeChannel: " + e.getMessage());
            return null;
        }
    }

    private static Constructor fetchNetworkEventContext() {
        try {
            return ObfuscationReflectionHelper.findConstructor(NetworkEvent.Context.class, Connection.class, NetworkDirection.class, int.class);
        }
        catch (Exception e) {
            logger.error("Exception occurred while accessing getLoginIndex: " + e.getMessage());
            return null;
        }
    }
}
