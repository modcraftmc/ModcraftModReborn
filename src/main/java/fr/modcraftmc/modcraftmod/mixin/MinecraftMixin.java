package fr.modcraftmc.modcraftmod.mixin;

import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import fr.modcraftmc.modcraftmod.client.screen.JoiningWorldBridgeScreen;
import fr.modcraftmc.modcraftmod.client.screen.ReconfigBridgeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.ServerReconfigScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.jar.Attributes;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;

    @Shadow @Nullable public Connection pendingConnection;

    @Shadow
    @Nullable
    public abstract ClientPacketListener getConnection();

    @Shadow @Nullable public Screen screen;

    @Shadow @Nullable public ClientLevel level;

    /**
     * @author manugame_
     * @reason always use our title
     */
    @Overwrite
    private String createTitle() {
        try {
            Attributes manifest = ModcraftModReborn.getManifest().getMainAttributes();
            return String.format("ModcraftMC: Reborn (ver: %s-%s)", manifest.getValue("Release-Type"),  manifest.getValue("Build-Time"));
        } catch (Exception e) {
            return "ModcraftMC: Reborn (ver: DEV)";
        }
    }

    @ModifyVariable(at = @At("HEAD"), method = "setScreen", ordinal = 0, argsOnly = true)
    public Screen setScreen(Screen screen) {
        if (screen instanceof ReceivingLevelScreen) {
            return null;
        } else if (screen instanceof ServerReconfigScreen) {
            return new ReconfigBridgeScreen(this.getConnection().getConnection());
        }  else if (this.screen instanceof JoiningWorldBridgeScreen && screen instanceof JoiningWorldBridgeScreen) {
            return null;
        }
        return screen;
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void setScreenCancelCloseScreen(Screen screen, CallbackInfo ci) {
        if (this.level != null && screen instanceof JoiningWorldBridgeScreen) {
            ci.cancel();
        }
    }

    @ModifyArg(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;updateScreenAndTick(Lnet/minecraft/client/gui/screens/Screen;)V", opcode = Opcodes.INVOKEVIRTUAL), index = 0)
    private Screen setLevelUpdateScreenAndTick(Screen screen) {
        // Make sure we clean up what needs cleaning up, just that we don't set a new screen on server switches within a proxy
        // Can't just set it to null during reconfiguration, so set an empty screen
        return new JoiningWorldBridgeScreen();
    }

    @Inject(method = "clearClientLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;onDisconnected()V"))
    public void fireUnloadEvent(Screen p_294558_, CallbackInfo ci) {
        if (this.level != null) net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new net.neoforged.neoforge.event.level.LevelEvent.Unload(this.level));
    }
}
