package infra.chains.impl;

import infra.chains.Band;
import infra.chains.Chain;

import java.util.List;

/**
 * @author alari
 * @since 11/19/12 1:29 AM
 */
public class ChainPOJO implements Chain<Band> {
    public List<Band> getBands() {
        return bands;
    }

    public void setBands(List<Band> bands) {
        this.bands = bands;
    }

    private List<Band> bands;
}
