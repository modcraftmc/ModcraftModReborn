package fr.modcraftmc.modcraftmod.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    /**
     * @author manugame_
     * @reason always use our title
     */
    @Overwrite
    public String createTitle() {
        return "ModcraftMC: Reborn";
    }

}
