package fr.modcraftmc.modcraftmod.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WelcomeScreen extends Screen {

    public WelcomeScreen() {
        super(Component.literal("modcraftmc.welcome.title"));
    }

    @Override
    public void render(GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        super.render(p_281549_, p_281550_, p_282878_, p_282465_);
    }
}
