package infra.chains;

import infra.ca.Atom;
import infra.ca.StringIdContainer;

import java.util.List;
import java.util.Map;

/**
 * @author alari
 * @since 11/19/12 1:23 AM
 */
public interface Band extends StringIdContainer {
    Map<String, String> getStyles();

    void setStyles(Map<String, String> styles);

    String getType();

    void setType(String type);

    List<Atom> getAtoms();

    void setAtoms(List<Atom> atoms);
}
