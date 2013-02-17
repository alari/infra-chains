package infra.chains.impl;

import infra.ca.Atom;
import infra.chains.Band;

import java.util.List;
import java.util.Map;

/**
 * @author alari
 * @since 11/19/12 1:29 AM
 */
public class BandPOJO implements Band {
    private String id;
    private Map<String,String> styles;
    private String type;
    private List<Atom> atoms;

    public String toString() {
        return "Band:"+id;
    }

    public Map<String, String> getStyles() {
        return styles;
    }

    public void setStyles(Map<String, String> styles) {
        this.styles = styles;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Atom> getAtoms() {
        return atoms;
    }

    public void setAtoms(List<Atom> atoms) {
        this.atoms = atoms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
