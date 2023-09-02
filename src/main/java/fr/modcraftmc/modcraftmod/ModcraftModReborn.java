package fr.modcraftmc.modcraftmod;

import com.mojang.logging.LogUtils;
import fr.modcraftmc.modcraftmod.client.discord.DiscordActivity;
import fr.modcraftmc.modcraftmod.client.reset.ResetHandler;
import fr.modcraftmc.modcraftmod.threads.ModcraftModExecutor;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        //MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        //PacketHandler.register();
    }


    private void clientSetup(final FMLClientSetupEvent event) {
        ModcraftModExecutor.executorService.execute(discordActivity::start);
        ResetHandler.register();
    }

    public static Manifest getManifest() {
        try {
            return new Manifest((ModcraftModReborn.class.getResourceAsStream("/META-INF/MANIFEST.MF")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
