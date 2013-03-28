package infra.chains;

import java.util.List;

/**
 * @author alari
 * @since 11/19/12 1:23 AM
 */
public interface Chain<B extends Band> {
    List<B> getBands();

    void setBands(List<B> bands);
}
