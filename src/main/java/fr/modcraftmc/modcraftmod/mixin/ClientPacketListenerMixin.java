package fr.modcraftmc.modcraftmod.mixin;

import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import fr.modcraftmc.modcraftmod.threads.ModcraftModExecutor;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    private int tickCounter = 600;
    @Inject(method = "handleKeepAlive", at = @At("HEAD"))
    public void updateDiscordActivity(ClientboundKeepAlivePacket p_105020_, CallbackInfo ci) {
        if (++tickCounter > 50) {
            ModcraftModExecutor.executorService.submit(ModcraftModReborn.discordActivity::updatePlayerCount);
            tickCounter = 0;
        }
    }
}
