package fr.modcraftmc.modcraftmod.mixin;

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
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
    @Overwrite
    public boolean mayUseItemAt(BlockPos p_36205_, Direction p_36206_, ItemStack p_36207_) {
        if (this.abilities.mayBuild) {
            return true;
        } else {
            BlockPos blockpos = p_36205_.relative(p_36206_.getOpposite());
            BlockInWorld blockinworld = new BlockInWorld(this.level, blockpos, false);
            ForgeHooks.onItemRightClick((Player) (Object) this, InteractionHand.MAIN_HAND);
            return p_36207_.hasAdventureModePlaceTagForBlock(this.level.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY), blockinworld);
        }
    }

}
