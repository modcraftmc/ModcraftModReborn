package fr.modcraftmc.modcraftmod.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class ResetScreen extends Screen {
    static final Logger LOGGER = LogUtils.getLogger();
    final Screen parent;
    private Component status;


    public ResetScreen(String destinationServer, Screen p_169263_) {
        super(GameNarrator.NO_TITLE);
        this.parent = p_169263_;
        status = Component.literal("Joining " + destinationServer);
    }
    public void updateStatus(Component p_95718_) {
        this.status = p_95718_;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected void init() {
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20, CommonComponents.GUI_CANCEL, (p_95705_) -> {
            if (this.minecraft.getConnection() != null && this.minecraft.getConnection().getConnection() != null) {
                this.minecraft.getConnection().getConnection().disconnect(Component.translatable("connect.aborted"));
            }

            this.minecraft.clearLevel(this.parent);
        }));
    }

    public void render(PoseStack p_95700_, int p_95701_, int p_95702_, float p_95703_) {
        this.renderBackground(p_95700_);

        drawCenteredString(p_95700_, this.font, this.status, this.width / 2, this.height / 2 - 50, 16777215);
        super.render(p_95700_, p_95701_, p_95702_, p_95703_);
    }
}