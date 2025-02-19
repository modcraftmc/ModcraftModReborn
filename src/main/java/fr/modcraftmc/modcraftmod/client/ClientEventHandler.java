package fr.modcraftmc.modcraftmod.client;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber
public class ClientEventHandler {

    public static String serverName = "";

    @SubscribeEvent
    public static void onScreenEvent(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof JoinMultiplayerScreen) {
            event.setNewScreen(new TitleScreen());
        }
    }

    @SubscribeEvent
    public static void debugText(CustomizeGuiOverlayEvent.DebugText event) {
        if (true) {
            event.getLeft().add("");
            event.getLeft().add("[ModcraftMC]");
            event.getLeft().add("current server: " + serverName);
        }
    }
}
