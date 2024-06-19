package fr.modcraftmc.modcraftmod.mixin;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private List<GuiMessage.Line> trimmedMessages;

    @Shadow @Final private List<GuiMessage> allMessages;

    @Shadow @Final private List<String> recentChat;

    /**
     * @author manugame_
     * @reason do not clear chat
     */
    @Overwrite
    public void clearMessages(boolean p_93796_) {
        this.minecraft.getChatListener().clearQueue();
    }
}
