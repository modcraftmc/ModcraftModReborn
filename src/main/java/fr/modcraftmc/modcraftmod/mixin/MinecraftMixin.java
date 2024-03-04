package fr.modcraftmc.modcraftmod.mixin;

import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import fr.modcraftmc.modcraftmod.client.screen.JoiningWorldBridgeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;

    @Shadow @Nullable public Connection pendingConnection;

    @Shadow
    @Nullable
    public abstract ClientPacketListener getConnection();

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
        }
        return screen;
    }

    @ModifyArg(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;updateScreenAndTick(Lnet/minecraft/client/gui/screens/Screen;)V", opcode = Opcodes.INVOKEVIRTUAL), index = 0)
    private Screen setLevelUpdateScreenAndTick(Screen screen) {
        // Make sure we clean up what needs cleaning up, just that we don't set a new screen on server switches within a proxy
        // Can't just set it to null during reconfiguration, so set an empty screen
        return new JoiningWorldBridgeScreen();
    }
}
