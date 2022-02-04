package loadbalance;

import extension.SPI;
import remoting.dto.RpcRequest;

import java.util.List;

@SPI
public interface LoadBalance {

    /**
     * Choose one from the list of existing service addresses list
     *
     * @param serviceAddresses Service address list
     * @param rpcRequest
     * @return target service address
     */
    String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest);
}
