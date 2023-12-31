package fr.modcraftmc.modcraftmod.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class LoadingScreen extends Screen {
    private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
    static final Logger LOGGER = LogUtils.getLogger();
    private static final long NARRATION_DELAY_MS = 2000L;
    public static final Component UNKNOWN_HOST_MESSAGE = Component.translatable("disconnect.genericReason", Component.translatable("disconnect.unknownHost"));
    @Nullable
    volatile Connection connection;
    volatile boolean aborted;
    final Screen parent;
    private Component status = Component.translatable("connect.connecting");
    private long lastNarration = -1L;

    private LoadingScreen(Screen p_169263_) {
        super(GameNarrator.NO_TITLE);
        this.parent = p_169263_;
    }

    public static void startConnecting(Screen p_169268_, Minecraft p_169269_, ServerAddress p_169270_, @Nullable ServerData p_169271_) {
        LoadingScreen connectscreen = new LoadingScreen(p_169268_);
        p_169269_.clearLevel();
        p_169269_.prepareForMultiplayer();
        p_169269_.setCurrentServer(p_169271_);
        p_169269_.setScreen(connectscreen);
        connectscreen.connect(p_169269_, p_169270_);
    }

    private void connect(final Minecraft p_169265_, final ServerAddress p_169266_) {
        final CompletableFuture<Optional<ProfilePublicKey.Data>> completablefuture = p_169265_.getProfileKeyPairManager().preparePublicKey();
        LOGGER.info("Connecting to {}, {}", p_169266_.getHost(), p_169266_.getPort());
        Thread thread = new Thread("Server Connector #" + UNIQUE_THREAD_ID.incrementAndGet()) {
            public void run() {
                InetSocketAddress inetsocketaddress = null;

                try {
                    if (LoadingScreen.this.aborted) {
                        return;
                    }

                    Optional<InetSocketAddress> optional = ServerNameResolver.DEFAULT.resolveAddress(p_169266_).map(ResolvedServerAddress::asInetSocketAddress);
                    if (LoadingScreen.this.aborted) {
                        return;
                    }

                    if (!optional.isPresent()) {
                        p_169265_.execute(() -> {
                            p_169265_.setScreen(new DisconnectedScreen(LoadingScreen.this.parent, CommonComponents.CONNECT_FAILED, LoadingScreen.UNKNOWN_HOST_MESSAGE));
                        });
                        return;
                    }

                    inetsocketaddress = optional.get();
                    LoadingScreen.this.connection = Connection.connectToServer(inetsocketaddress, p_169265_.options.useNativeTransport());
                    LoadingScreen.this.connection.setListener(new ClientHandshakePacketListenerImpl(LoadingScreen.this.connection, p_169265_, LoadingScreen.this.parent, LoadingScreen.this::updateStatus));
                    LoadingScreen.this.connection.send(new ClientIntentionPacket(inetsocketaddress.getHostName(), inetsocketaddress.getPort(), ConnectionProtocol.LOGIN));
                    LoadingScreen.this.connection.send(new ServerboundHelloPacket(p_169265_.getUser().getName(), completablefuture.join(), Optional.ofNullable(p_169265_.getUser().getProfileId())));
                } catch (Exception exception2) {
                    if (LoadingScreen.this.aborted) {
                        return;
                    }

                    Throwable throwable = exception2.getCause();
                    Exception exception;
                    if (throwable instanceof Exception exception1) {
                        exception = exception1;
                    } else {
                        exception = exception2;
                    }

                    LoadingScreen.LOGGER.error("Couldn't connect to server", (Throwable)exception2);
                    String s = inetsocketaddress == null ? exception.getMessage() : exception.getMessage().replaceAll(inetsocketaddress.getHostName() + ":" + inetsocketaddress.getPort(), "").replaceAll(inetsocketaddress.toString(), "");
                    p_169265_.execute(() -> {
                        p_169265_.setScreen(new DisconnectedScreen(LoadingScreen.this.parent, CommonComponents.CONNECT_FAILED, Component.translatable("disconnect.genericReason", s)));
                    });
                }

            }
        };
        thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        thread.start();
    }

    private void updateStatus(Component p_95718_) {
        this.status = p_95718_;
    }

    public void tick() {
        if (this.connection != null) {
            if (this.connection.isConnected()) {
                this.connection.tick();
            } else {
                this.connection.handleDisconnection();
            }
        }

    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected void init() {
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20, CommonComponents.GUI_CANCEL, (p_95705_) -> {
            this.aborted = true;
            if (this.connection != null) {
                this.connection.disconnect(Component.translatable("connect.aborted"));
            }

            this.minecraft.setScreen(this.parent);
        }));
    }

    public void render(PoseStack p_95700_, int p_95701_, int p_95702_, float p_95703_) {
        this.renderBackground(p_95700_);
        long i = Util.getMillis();
        if (i - this.lastNarration > 2000L) {
            this.lastNarration = i;
            this.minecraft.getNarrator().sayNow(Component.translatable("narrator.joining"));
        }

        drawCenteredString(p_95700_, this.font, this.status, this.width / 2, this.height / 2 - 50, 16777215);
        super.render(p_95700_, p_95701_, p_95702_, p_95703_);
    }
}