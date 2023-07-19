package fr.modcraftmc.modcraftmod.client.reset;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.HandshakeMessages;

public class S2CResetPacket extends HandshakeMessages.C2SAcknowledge {

    private int loginIndex;

    public S2CResetPacket() {
        super();
    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public static S2CResetPacket decode(FriendlyByteBuf buffer) {
        return new S2CResetPacket();
    }

    public void setLoginIndex(final int loginIndex) {
        this.loginIndex = loginIndex;
    }

    public int getLoginIndex() {
        return loginIndex;
    }
}
