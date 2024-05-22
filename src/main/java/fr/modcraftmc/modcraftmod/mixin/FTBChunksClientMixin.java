package fr.modcraftmc.modcraftmod.mixin;

import dev.architectury.event.EventResult;
import dev.ftb.mods.ftbchunks.client.FTBChunksClient;
import dev.ftb.mods.ftblibrary.ui.CustomClickEvent;
import fr.modcraftmc.modcraftmod.client.ClientEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FTBChunksClient.class, remap = false)
public class FTBChunksClientMixin {

    @Inject(method = "customClick", at = @At("HEAD"), cancellable = true)
    private void checkServer(CustomClickEvent event, CallbackInfoReturnable<EventResult> cir) {
        if (ClientEventHandler.serverName.equalsIgnoreCase("lobby"))
            cir.setReturnValue(EventResult.interruptFalse());
    }
}
