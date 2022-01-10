package remoting.transport.netty.codec;

import lombok.extern.slf4j.Slf4j;
import remoting.constants.RpcConstants;

@Slf4j
public class RpcMessageDecoder {

    public RpcMessageDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthAdjustment, int initialBytesToStrip) {
        
    }
}
