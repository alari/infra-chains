package infra.chains.impl;

import infra.chains.Band;
import infra.chains.BandFactory;

/**
 * @author alari
 * @since 2/18/13 1:03 AM
 */
public class BandFactoryImpl implements BandFactory {
    @Override
    public Band buildBand() {
        return new BandPOJO();
    }
}
