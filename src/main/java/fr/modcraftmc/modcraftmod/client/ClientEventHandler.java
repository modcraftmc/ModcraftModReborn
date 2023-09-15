package fr.modcraftmc.modcraftmod.client;

import fr.modcraftmc.modcraftmod.client.screen.ModcraftPauseScreen;
import fr.modcraftmc.modcraftmod.client.screen.ModcraftTitleScreen;
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

    public static String serverName = "error";
    public static String toSwitchServer = "error";

    @SubscribeEvent
    public static void guiEvent(final ScreenEvent.Opening event) {
        if (ModList.get().isLoaded("replaymod")) return;
        if (event.getScreen() instanceof TitleScreen) {
            event.setNewScreen(new ModcraftTitleScreen());
        } else if (event.getScreen() instanceof PauseScreen) {
            event.setNewScreen(new ModcraftPauseScreen(true));
        }
    }

    @SubscribeEvent
    public static void debugText(CustomizeGuiOverlayEvent.DebugText event) {
        if (Minecraft.getInstance().options.renderDebug) {
            event.getLeft().add("");
            event.getLeft().add("[ModcraftMC]");
            event.getLeft().add("current server: " + serverName);
        }
    }
}
