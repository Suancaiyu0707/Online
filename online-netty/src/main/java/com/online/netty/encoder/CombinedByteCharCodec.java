package com.online.netty.encoder;

import com.online.netty.decoder.ByteToCharDecoder;
import io.netty.channel.CombinedChannelDuplexHandler;

public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder,CharToByteEncoder> {
    public CombinedByteCharCodec(ByteToCharDecoder inboundHandler, CharToByteEncoder outboundHandler) {
        super(inboundHandler, outboundHandler);
    }
}
