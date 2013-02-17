package infra.chains.impl;

import infra.chains.Chain;
import infra.chains.ChainFactory;

/**
 * @author alari
 * @since 2/18/13 1:04 AM
 */
public class ChainFactoryImpl implements ChainFactory {
    @Override
    public Chain buildChain() {
        return new ChainPOJO();
    }
}
