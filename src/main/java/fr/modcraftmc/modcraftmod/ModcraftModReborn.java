package fr.modcraftmc.modcraftmod;

import com.mojang.logging.LogUtils;
import fr.modcraftmc.modcraftmod.client.discord.DiscordActivity;
import fr.modcraftmc.modcraftmod.threads.ModcraftModExecutor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

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
    }
}
