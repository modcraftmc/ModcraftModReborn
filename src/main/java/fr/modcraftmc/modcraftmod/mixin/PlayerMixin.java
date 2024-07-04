package fr.modcraftmc.modcraftmod.mixin;

import dev.ftb.mods.ftbchunks.data.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.data.Protection;
import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow @Final private Abilities abilities;

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    /**
     * @author
     * @reason
     */
    @Inject(method = "mayUseItemAt", at = @At("HEAD"), cancellable = true)
    public void mayUseItemAt(BlockPos p_36205_, Direction p_36206_, ItemStack p_36207_, CallbackInfoReturnable<Boolean> cir) {
        boolean shouldPrevent =FTBChunksAPI.getManager().protect((Player) (Object) this, InteractionHand.MAIN_HAND, p_36205_, Protection.INTERACT_BLOCK, null);
        if (shouldPrevent)
            cir.setReturnValue(false);
    }
}
