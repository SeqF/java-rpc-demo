package registry.zk;

import lombok.extern.slf4j.Slf4j;
import registry.ServiceDiscovery;
import remoting.dto.RpcRequest;

import java.net.InetSocketAddress;
import java.util.PrimitiveIterator;

@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {


    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        return null;
    }
}
