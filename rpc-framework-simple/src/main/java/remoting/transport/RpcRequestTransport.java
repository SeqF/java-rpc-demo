package remoting.transport;

import remoting.dto.RpcRequest;

public interface RpcRequestTransport {

    Object sendRpcRequest(RpcRequest rpcRequest);
}
