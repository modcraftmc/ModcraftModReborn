package fr.modcraftmc.modcraftmod.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WelcomeScreen extends Screen {

    public WelcomeScreen() {
        super(Component.literal("modcraftmc.welcome.title"));
    }

    @Override
    public void render(PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        this.renderBackground(p_96562_);
        super.render(p_96562_, p_96563_, p_96564_, p_96565_);
    }
}
