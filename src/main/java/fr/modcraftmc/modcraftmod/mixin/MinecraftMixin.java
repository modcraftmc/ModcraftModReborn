package fr.modcraftmc.modcraftmod.mixin;

import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;

    @Shadow @Nullable public Connection pendingConnection;

    /**
     * @author manugame_
     * @reason always use our title
     */
    @Overwrite
    public String createTitle() {
        Attributes manifest = ModcraftModReborn.getManifest().getMainAttributes();
        return String.format("ModcraftMC: Reborn (ver: %s-%s)", manifest.getValue("Release-Type"),  manifest.getValue("Build-Time"));
    }

    /**
     * @author manugame_
     * @reason make this null-safe when switching servers
     */
    @Overwrite
    public ClientPacketListener getConnection() {
        if (this.player == null) {
            if (this.pendingConnection == null) {
                return null;
            } else {
                this.pendingConnection.getPacketListener();
            }
        } else {
            return this.player.connection;
        }
        return null;
    }
}
