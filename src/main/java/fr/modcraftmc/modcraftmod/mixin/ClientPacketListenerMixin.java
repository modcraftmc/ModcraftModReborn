package fr.modcraftmc.modcraftmod.mixin;

import fr.modcraftmc.modcraftmod.client.screen.JoiningWorldBridgeScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

//    @Redirect(method = "handleConfigurationStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/multiplayer/ServerReconfigScreen;<init>(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/Connection;)V", args = "ldc="))
//    public void customReconfigureMessage(ServerReconfigScreen instance, Component p_294744_, Connection p_294619_) {
//        instance = new ServerReconfigScreen(Component.literal("Changement de serveur"), p_294619_);
//    }

    @ModifyArg(method = "startWaitingForNewLevel(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/gui/screens/ReceivingLevelScreen$Reason;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceKey;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", opcode = Opcodes.INVOKEVIRTUAL), index = 0)
    private Screen setLevelUpdateScreenAndTick(Screen screen) {
        return new JoiningWorldBridgeScreen();
    }
}
