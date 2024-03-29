package fr.modcraftmc.modcraftmod;

import com.mojang.logging.LogUtils;
import fr.modcraftmc.crossservercore.api.CrossServerCoreAPI;
import fr.modcraftmc.modcraftmod.client.ClientEventHandler;
import fr.modcraftmc.modcraftmod.client.discord.DiscordActivity;
import fr.modcraftmc.modcraftmod.client.reset.ResetHandler;
import fr.modcraftmc.modcraftmod.common.advancements.ModcraftAdvancements;
import fr.modcraftmc.modcraftmod.common.network.PacketHandler;
import fr.modcraftmc.modcraftmod.common.network.packets.S2CServerInfos;
import fr.modcraftmc.modcraftmod.threads.ModcraftModExecutor;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.jar.Manifest;

@Mod(ModcraftModReborn.MODID)
public class ModcraftModReborn {

    public static final String MODID = "modcraftmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DiscordActivity discordActivity = new DiscordActivity();
    public ModcraftModReborn() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::gatherData);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerJoin);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PacketHandler.register();
    }


    public void gatherData(GatherDataEvent event) {
        LOGGER.info("GatherDataEvent");
        DataGenerator gen = event.getGenerator();
         gen.addProvider(event.includeServer(), new ModcraftAdvancements(gen, event.getExistingFileHelper()));
    }

    private void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PacketHandler.sendTo(new S2CServerInfos(CrossServerCoreAPI.instance.getServerName()), ((ServerPlayer) event.getEntity()));
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
        ModcraftModExecutor.executorService.execute(discordActivity::start);
        ResetHandler.register();
    }

    public static Manifest getManifest() throws IOException {
        return new Manifest((ModcraftModReborn.class.getResourceAsStream("/META-INF/MANIFEST.MF")));
    }
}
