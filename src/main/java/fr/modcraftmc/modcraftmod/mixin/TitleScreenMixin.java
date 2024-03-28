package fr.modcraftmc.modcraftmod.mixin;

import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import fr.modcraftmc.modcraftmod.threads.ModcraftModExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    private ServerData modcraftServerData = new ServerData("ModcraftMC", "play.modcraftmc.fr", false);

    @Inject(method = "<init>(Z)V", at = @At("RETURN"))
    private void updateDiscordActivity(CallbackInfo ci) {
        ModcraftModExecutor.executorService.submit(ModcraftModReborn.discordActivity::setWaitingStatus);
    }

    @Redirect(method = "createNormalMenuOptions", at = @At(value = "NEW", target = "(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;Lnet/minecraft/client/gui/components/Button$OnTooltip;)Lnet/minecraft/client/gui/components/Button;", ordinal = 1))
    public Button customMultiplayerButton(int p_93728_, int p_93729_, int p_93730_, int p_93731_, Component p_93732_, Button.OnPress p_93733_, Button.OnTooltip tooltip, int a, int b) { // mixin plugin fucked this up
        return new Button(p_93728_, p_93729_, p_93730_, p_93731_, Component.literal("ModcraftMC"), (button) -> {
            ConnectScreen.startConnecting((TitleScreen) (Object) this, Minecraft.getInstance(), ServerAddress.parseString(modcraftServerData.ip), modcraftServerData);
        }, tooltip);
    }
}
