package fr.modcraftmc.modcraftmod.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {

    public static String serverName = "";

    @SubscribeEvent
    public static void onScreenEvent(ScreenEvent.Opening event) {
//        if (event.getScreen() instanceof JoinMultiplayerScreen) {
//            event.setNewScreen(new TitleScreen());
//        }
    }

    @SubscribeEvent
    public static void debugText(CustomizeGuiOverlayEvent.DebugText event) {
        event.getLeft().add("");
        event.getLeft().add("§6[ModcraftMC]");
        event.getLeft().add("§6current server: " + serverName);
        event.getLeft().add("");
    }
}
