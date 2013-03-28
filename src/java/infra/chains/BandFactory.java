package infra.chains;

/**
 * @author alari
 * @since 2/18/13 1:00 AM
 */
public interface BandFactory<B extends Band> {
    public B buildBand();
}
