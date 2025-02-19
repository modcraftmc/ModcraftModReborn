//package fr.modcraftmc.modcraftmod.mixin;
//
//import fr.modcraftmc.modcraftmod.ModcraftModReborn;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import net.minecraft.network.PacketEncoder;
//import net.minecraft.network.protocol.Packet;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(PacketEncoder.class)
//public class PacketEncoderMixin {
//
//    @Inject(method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;Lio/netty/buffer/ByteBuf;)V", at = @At(value = "INVOKE", target = "Ljava/io/IOException;<init>(Ljava/lang/String;)V", shift = At.Shift.BEFORE, ordinal = 0), cancellable = true)
//    public void disableException(ChannelHandlerContext p_130545_, Packet<?> p_130546_, ByteBuf p_130547_, CallbackInfo ci) {
//        ModcraftModReborn.LOGGER.warn("unregistered packet {}, normal if switching server", p_130545_.name());
//        ci.cancel();
//    }
//}
