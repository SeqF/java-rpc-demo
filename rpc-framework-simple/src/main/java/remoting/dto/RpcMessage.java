package remoting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcMessage {

    private byte messageType;

    /**
     * serialization type
     */
    private byte codec;

    private byte compress;

    private int requestId;

    private Object data;

}
