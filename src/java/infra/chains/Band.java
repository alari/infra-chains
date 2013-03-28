package infra.chains;

import infra.ca.Atom;
import infra.ca.StringIdContainer;

import java.util.List;
import java.util.Map;

/**
 * @author alari
 * @since 11/19/12 1:23 AM
 */
public interface Band<A extends Atom> extends StringIdContainer {
    Map<String, String> getStyles();

    void setStyles(Map<String, String> styles);

    String getType();

    void setType(String type);

    List<A> getAtoms();

    void setAtoms(List<A> atoms);
}
