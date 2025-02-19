package fr.modcraftmc.modcraftmod;

import com.mojang.logging.LogUtils;
import fr.modcraftmc.modcraftmod.client.ClientEventHandler;
import fr.modcraftmc.modcraftmod.common.network.PacketHandler;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.jar.Manifest;

@Mod(ModcraftModReborn.MODID)
public class ModcraftModReborn {

    public static final String MODID = "modcraftmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public ModcraftModReborn(IEventBus modEventBus) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::registerPackets);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.addListener(this::onPlayerJoin);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onServerStartedEvent);
    }

    public void gatherData(GatherDataEvent event) {
        LOGGER.info("GatherDataEvent");
        DataGenerator gen = event.getGenerator();
      //   gen.addProvider(event.includeServer(), new ModcraftAdvancements(gen, event.getExistingFileHelper()));
    }

    private void onServerStartedEvent(ServerStartedEvent event) {
        LOGGER.info("waiting 10s to set the server ready");
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenRunAsync(() -> {
            LOGGER.info("server is ready");
            event.getServer().setMotd("READY");
        }, event.getServer());
    }

    private void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
//        if (ServerLifecycleHooks.getCurrentServer().isDedicatedServer()) // do not look for CSC in singleplayer
//            PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new S2CServerInfos(CrossServerCoreAPI.instance.getServerName()));
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(ClientEventHandler.class);
        //ResetHandler.register();
    }

    private void registerPackets(final RegisterPayloadHandlersEvent event) {
        PacketHandler.register(event);
    }

    public static Manifest getManifest() throws IOException {
        return new Manifest((ModcraftModReborn.class.getResourceAsStream("/META-INF/MANIFEST.MF")));
    }
}
