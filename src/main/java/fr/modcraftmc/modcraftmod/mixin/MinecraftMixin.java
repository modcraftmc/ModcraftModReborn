package fr.modcraftmc.modcraftmod.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    /**
     * @author manugame_
     * @reason always use our title
     */
    @Overwrite
    public String createTitle() {
        return "ModcraftMC: Reborn (ver: PLAYTEST-07072023-0001)";
    }

}
