package com.online.netty.channel;

import com.online.netty.decoder.ByteToCharDecoder;
import com.online.netty.encoder.CharToByteEncoder;
import io.netty.channel.CombinedChannelDuplexHandler;

public class CombinedByetCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public CombinedByetCharCodec(ByteToCharDecoder inboundHandler, CharToByteEncoder outboundHandler) {
        super(inboundHandler, outboundHandler);
    }
}
