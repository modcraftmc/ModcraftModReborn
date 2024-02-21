package fr.modcraftmc.modcraftmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEventHandler {

    public static String serverName = "";

    @SubscribeEvent
    public static void debugText(CustomizeGuiOverlayEvent.DebugText event) {
        if (Minecraft.getInstance().options.renderDebug) {
            event.getLeft().add("");
            event.getLeft().add("[ModcraftMC]");
            event.getLeft().add("current server: " + serverName);
        }
    }
}
