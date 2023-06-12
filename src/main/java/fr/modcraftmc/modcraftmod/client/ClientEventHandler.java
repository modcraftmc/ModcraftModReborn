package fr.modcraftmc.modcraftmod.client;

import fr.modcraftmc.modcraftmod.client.screen.ModcraftTitleScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void guiEvent(final ScreenEvent.Opening event) {
        if (event.getScreen() instanceof TitleScreen) {
            event.setNewScreen(new ModcraftTitleScreen());
        }
    }
}
