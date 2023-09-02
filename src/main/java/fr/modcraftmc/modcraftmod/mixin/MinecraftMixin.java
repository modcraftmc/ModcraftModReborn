package fr.modcraftmc.modcraftmod.mixin;

import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    /**
     * @author manugame_
     * @reason always use our title
     */
    @Overwrite
    public String createTitle() {
        Attributes manifest = ModcraftModReborn.getManifest().getMainAttributes();
        return String.format("ModcraftMC: Reborn (ver: %s-%s)", manifest.getValue("Release-Type"),  manifest.getValue("Build-Time"));
    }
}
