package fr.modcraftmc.modcraftmod.common.network.packets;

import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record S2CServerInfos(String serverName) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CServerInfos> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModcraftModReborn.MODID, "server_infos"));

    public static final StreamCodec<ByteBuf, S2CServerInfos> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            S2CServerInfos::serverName,
            S2CServerInfos::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
