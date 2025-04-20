package fr.modcraftmc.modcraftmod;

import com.mojang.logging.LogUtils;
import fr.modcraftmc.crossservercore.api.CrossServerCoreAPI;
import fr.modcraftmc.modcraftmod.client.ClientEventHandler;
import fr.modcraftmc.modcraftmod.common.network.PacketHandler;
import fr.modcraftmc.modcraftmod.common.network.packets.S2CServerInfos;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

import java.io.IOException;
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

    }

    public void gatherData(GatherDataEvent event) {
        LOGGER.info("GatherDataEvent");
        DataGenerator gen = event.getGenerator();
      //   gen.addProvider(event.includeServer(), new ModcraftAdvancements(gen, event.getExistingFileHelper()));
    }

    private void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // maybe we should use the configuration phase to handle this??
        if (ServerLifecycleHooks.getCurrentServer().isDedicatedServer())
            PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new S2CServerInfos(CrossServerCoreAPI.getServerName()));
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(ClientEventHandler.class);
    }

    private void registerPackets(final RegisterPayloadHandlersEvent event) {
        PacketHandler.register(event);
    }

    public static Manifest getManifest() throws IOException {
        return new Manifest((ModcraftModReborn.class.getResourceAsStream("/META-INF/MANIFEST.MF")));
    }
}
