package infra.chains;

import java.util.List;

/**
 * @author alari
 * @since 11/19/12 1:23 AM
 */
public interface Chain {
    List<Band> getBands();

    void setBands(List<Band> bands);
}
