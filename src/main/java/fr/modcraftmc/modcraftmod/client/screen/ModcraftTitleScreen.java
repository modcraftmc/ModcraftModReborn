package fr.modcraftmc.modcraftmod.client.screen;

import com.google.common.util.concurrent.Runnables;
import com.mojang.authlib.minecraft.BanDetails;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Vector3f;

import java.util.function.Consumer;
import javax.annotation.Nullable;

import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import fr.modcraftmc.modcraftmod.threads.ModcraftModExecutor;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class ModcraftTitleScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Component COPYRIGHT_TEXT = Component.literal("Copyright Mojang AB. Do not distribute!");
    public static final CubeMap CUBE_MAP = new CubeMap(new ResourceLocation("textures/gui/title/background/panorama"));
    private static final ResourceLocation PANORAMA_OVERLAY = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
    private static final ResourceLocation ACCESSIBILITY_TEXTURE = new ResourceLocation("textures/gui/accessibility.png");
    @Nullable
    private String splash;
    private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation MINECRAFT_EDITION = new ResourceLocation("textures/gui/title/edition.png");
    @Nullable
    private final PanoramaRenderer panorama = new PanoramaRenderer(CUBE_MAP);
    @Nullable
    private ModcraftTitleScreen.WarningLabel warningLabel;
    private ServerData modcraftServerData = new ServerData("ModcraftMC", "play.modcraftmc.fr", false);
    public ModcraftTitleScreen() {
        super(Component.translatable("narrator.screen.title"));
        ModcraftModExecutor.executorService.submit(ModcraftModReborn.discordActivity::setWaitingStatus);
    }

    public boolean isPauseScreen() {
        return false;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected void init() {
        if (this.splash == null) {
            this.splash = this.minecraft.getSplashManager().getSplash();
        }

        int i = this.font.width(COPYRIGHT_TEXT);
        int j = this.width - i - 2;
        int k = 24;
        int l = this.height / 4 + 48;
        Button modButton = null;
        this.createNormalMenuOptions(l, 24);
        modButton = this.addRenderableWidget(new Button(this.width / 2 - 100, l + 24 * 2, 98, 20, Component.translatable("fml.menu.mods"), button -> {
            this.minecraft.setScreen(new net.minecraftforge.client.gui.ModListScreen(this));
        }));

        this.addRenderableWidget(new ImageButton(this.width / 2 - 124, l + 72 + 12, 20, 20, 0, 106, 20, Button.WIDGETS_LOCATION, 256, 256, (p_96791_) -> {
            this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }, Component.translatable("narrator.button.language")));
        this.addRenderableWidget(new Button(this.width / 2 - 100, l + 72 + 12, 98, 20, Component.translatable("menu.options"), (p_96788_) -> {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));
        this.addRenderableWidget(new Button(this.width / 2 + 2, l + 72 + 12, 98, 20, Component.translatable("menu.quit"), (p_96786_) -> {
            this.minecraft.stop();
        }));
        this.addRenderableWidget(new ImageButton(this.width / 2 + 104, l + 72 + 12, 20, 20, 0, 0, 20, ACCESSIBILITY_TEXTURE, 32, 64, (p_96784_) -> {
            this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options));
        }, Component.translatable("narrator.button.accessibility")));
        this.addRenderableWidget(new PlainTextButton(j, this.height - 10, i, 10, COPYRIGHT_TEXT, (p_211790_) -> {
            this.minecraft.setScreen(new WinScreen(false, Runnables.doNothing()));
        }, this.font));
        this.minecraft.setConnectedToRealms(false);

        if (!this.minecraft.is64Bit()) {
            this.warningLabel = new ModcraftTitleScreen.WarningLabel(this.font, MultiLineLabel.create(this.font, Component.translatable("title.32bit.deprecation"), 350, 2), this.width / 2, l - 24);
        }

    }

    private void createNormalMenuOptions(int p_96764_, int p_96765_) {
        this.addRenderableWidget(new Button(this.width / 2 - 100, p_96764_, 200, 20, Component.translatable("menu.singleplayer"), (p_232779_) -> {
            this.minecraft.setScreen(new SelectWorldScreen(this));
        }));
        final Component component = this.getMultiplayerDisabledReason();
        boolean flag = component == null;
        Button.OnTooltip button$ontooltip = component == null ? Button.NO_TOOLTIP : new Button.OnTooltip() {
            public void onTooltip(Button p_169458_, PoseStack p_169459_, int p_169460_, int p_169461_) {
                ModcraftTitleScreen.this.renderTooltip(p_169459_, ModcraftTitleScreen.this.minecraft.font.split(component, Math.max(ModcraftTitleScreen.this.width / 2 - 43, 170)), p_169460_, p_169461_);
            }

            public void narrateTooltip(Consumer<Component> p_169456_) {
                p_169456_.accept(component);
            }
        };
        (this.addRenderableWidget(new Button(this.width / 2 - 100, p_96764_ + p_96765_ * 1, 200, 20, Component.literal("Se connecter"), (p_96776_) -> {

            LoadingScreen.startConnecting(this, this.minecraft, ServerAddress.parseString(modcraftServerData.ip), modcraftServerData);

        }, button$ontooltip))).active = flag;
        (this.addRenderableWidget(new Button(this.width / 2 + 2, p_96764_ + p_96765_ * 2, 98, 20, Component.literal("Discord"), (p_96771_) -> {
            Util.getPlatform().openUri("https://discord.gg/nvg7KWX");
        }, button$ontooltip))).active = flag;
    }

    @Nullable
    private Component getMultiplayerDisabledReason() {
        if (this.minecraft.allowsMultiplayer()) {
            return null;
        } else {
            BanDetails bandetails = this.minecraft.multiplayerBan();
            if (bandetails != null) {
                return bandetails.expires() != null ? Component.translatable("title.multiplayer.disabled.banned.temporary") : Component.translatable("title.multiplayer.disabled.banned.permanent");
            } else {
                return Component.translatable("title.multiplayer.disabled");
            }
        }
    }


    public void render(PoseStack p_96739_, int p_96740_, int p_96741_, float p_96742_) {
        float f = 1.0F;
        this.panorama.render(p_96742_, Mth.clamp(f, 0.0F, 1.0F));
        int i = 274;
        int j = this.width / 2 - 137;
        int k = 30;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, PANORAMA_OVERLAY);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        blit(p_96739_, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
        float f1 = 1.0F;
        int l = Mth.ceil(f1 * 255.0F) << 24;
        if ((l & -67108864) != 0) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, MINECRAFT_LOGO);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f1);
            this.blitOutlineBlack(j, 30, (p_210862_, p_210863_) -> {
                this.blit(p_96739_, p_210862_ + 0, p_210863_, 0, 0, 155, 44);
                this.blit(p_96739_, p_210862_ + 155, p_210863_, 0, 45, 155, 44);
            });

            RenderSystem.setShaderTexture(0, MINECRAFT_EDITION);
            blit(p_96739_, j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);
            if (this.warningLabel != null) {
                this.warningLabel.render(p_96739_, l);
            }

            if (this.splash != null) {
                p_96739_.pushPose();
                p_96739_.translate((double)(this.width / 2 + 90), 70.0D, 0.0D);
                p_96739_.mulPose(Vector3f.ZP.rotationDegrees(-20.0F));
                float f2 = 1.8F - Mth.abs(Mth.sin((float)(Util.getMillis() % 1000L) / 1000.0F * ((float)Math.PI * 2F)) * 0.1F);
                f2 = f2 * 100.0F / (float)(this.font.width(this.splash) + 32);
                p_96739_.scale(f2, f2, f2);
                drawCenteredString(p_96739_, this.font, this.splash, 0, -8, 16776960 | l);
                p_96739_.popPose();
            }

            String s = "Minecraft " + SharedConstants.getCurrentVersion().getName();
            if (this.minecraft.isDemo()) {
                s = s + " Demo";
            } else {
                s = s + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType());
            }

            if (Minecraft.checkModStatus().shouldReportAsModified()) {
                s = s + I18n.get("menu.modded");
            }

            net.minecraftforge.internal.BrandingControl.forEachLine(true, true, (brdline, brd) ->
                    drawString(p_96739_, this.font, brd, 2, this.height - ( 10 + brdline * (this.font.lineHeight + 1)), 16777215 | l)
            );

            net.minecraftforge.internal.BrandingControl.forEachAboveCopyrightLine((brdline, brd) ->
                    drawString(p_96739_, this.font, brd, this.width - font.width(brd), this.height - (10 + (brdline + 1) * ( this.font.lineHeight + 1)), 16777215 | l)
            );


            for(GuiEventListener guieventlistener : this.children()) {
                if (guieventlistener instanceof AbstractWidget) {
                    ((AbstractWidget)guieventlistener).setAlpha(f1);
                }
            }

            super.render(p_96739_, p_96740_, p_96741_, p_96742_);
        }
    }

    public boolean mouseClicked(double p_96735_, double p_96736_, int p_96737_) {
        if (super.mouseClicked(p_96735_, p_96736_, p_96737_)) {
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    static record WarningLabel(Font font, MultiLineLabel label, int x, int y) {
        public void render(PoseStack p_232791_, int p_232792_) {
            this.label.renderBackgroundCentered(p_232791_, this.x, this.y, 9, 2, 1428160512);
            this.label.renderCentered(p_232791_, this.x, this.y, 9, 16777215 | p_232792_);
        }
    }
}
