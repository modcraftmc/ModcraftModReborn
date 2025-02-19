//package fr.modcraftmc.modcraftmod.client.reset;
//
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraftforge.network.HandshakeMessages;
//
//public class S2CResetPacket extends HandshakeMessages.C2SAcknowledge {
//
//    private int loginIndex;
//    private final String destinationServer;
//
//    public S2CResetPacket(String destinationServer) {
//        super();
//        this.destinationServer = destinationServer;
//    }
//
//    public void encode(FriendlyByteBuf buffer) {
//        buffer.writeUtf(destinationServer);
//    }
//
//    public static S2CResetPacket decode(FriendlyByteBuf buffer) {
//        String destinationServer = buffer.readUtf();
//        return new S2CResetPacket(destinationServer);
//    }
//
//    public void setLoginIndex(final int loginIndex) {
//        this.loginIndex = loginIndex;
//    }
//
//    public int getLoginIndex() {
//        return loginIndex;
//    }
//
//    public String getDestinationServer() {
//        return destinationServer;
//    }
//}
