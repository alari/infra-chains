package infra.chains;

import infra.ca.AtomPush;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import infra.ca.Atom;
import infra.ca.AtomsManager;
import infra.ca.StringIdContainer;
import infra.ca.ex.CreativeAtomException;
import infra.chains.ex.NotFoundInChainException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author alari
 * @since 11/19/12 1:26 AM
 */
@Service
public interface ChainsManager {
    /**
     * Builds a new Chain object
     *
     * @return instance of Chain
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Chain buildChain() throws IllegalAccessException, InstantiationException;

    /**
     * Adds an atom into a chain, possibly creates a new band object
     *
     * @param chain
     * @param atom
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void addAtom(Chain chain, Atom atom) throws IllegalAccessException, InstantiationException;

    /**
     * Builds an atom from a user-provided data and adds it into a chain
     *
     * @param chain
     * @param data
     * @return a new built atom (already injected into a chain)
     * @throws CreativeAtomException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Atom pushAtom(Chain chain, AtomPush data) throws CreativeAtomException, InstantiationException, IllegalAccessException;

    /**
     * Builds and pushes an atom into specified band
     *
     * @param chain
     * @param data
     * @param bandId
     * @return
     */
    public Atom pushAtom(Chain chain, AtomPush data, String bandId) throws CreativeAtomException, InstantiationException, IllegalAccessException;

    /**
     * Retrieves an atom from chain by id
     *
     * @param chain
     * @param id
     * @return
     */
    public Atom getAtom(Chain chain, String id) throws NotFoundInChainException;

    /**
     * Removes an atom from chain by its id
     *
     * @param chain
     * @param id
     */
    public void removeAtom(Chain chain, String id) throws NotFoundInChainException;

    /**
     * Deletes atom contents and removes it from a chain
     *
     * @param chain
     * @param id
     * @throws CreativeAtomException
     */
    public void deleteAtom(Chain chain, String id) throws CreativeAtomException;

    /**
     * Prepares a chain to render update
     *
     * @param chain
     * @throws CreativeAtomException
     */
    public void forUpdate(Chain chain) throws CreativeAtomException;

    /**
     * Prepares a chain for common render
     *
     * @param chain
     * @throws CreativeAtomException
     */
    public void forRender(Chain chain) throws CreativeAtomException;

    /**
     * Deletes all chain atoms contents
     *
     * @param chain
     * @throws CreativeAtomException
     */
    public void delete(Chain chain) throws CreativeAtomException;

    /**
     * Returns a band by its id
     *
     * @param chain
     * @param bandId
     * @return
     * @throws NotFoundInChainException
     */
    public Band getBand(Chain chain, String bandId) throws NotFoundInChainException;

    /**
     * Returns a band atom belongs to
     *
     * @param chain
     * @param atomId
     * @return
     * @throws infra.chains.ex.NotFoundInChainException
     *
     */
    public Band getAtomBand(Chain chain, String atomId) throws NotFoundInChainException;

    /**
     * Moves an atom to the specified position in its band
     *
     * @param chain
     * @param atomId
     * @param moveToPosition
     * @throws NotFoundInChainException
     */
    public void moveInBand(Chain chain, String atomId, int moveToPosition) throws NotFoundInChainException;

    /**
     * Moves a band to the specified position in a chain
     *
     * @param chain
     * @param bandId
     * @param moveToPosition
     */
    public void moveBand(Chain chain, String bandId, int moveToPosition);


    /**
     * Moves an atom from band to band, places it to the end of a target band
     *
     * @param chain
     * @param atomId
     * @param bandId
     */
    public void moveToBand(Chain chain, String atomId, String bandId) throws NotFoundInChainException, IllegalAccessException, InstantiationException;

    /**
     * Moves an atom to specified position in a target band, splits a target if it's of the wrong type
     *
     * @param chain
     * @param atomId
     * @param bandId
     * @param moveToPosition
     */
    public void moveToBand(Chain chain, String atomId, String bandId, int moveToPosition) throws NotFoundInChainException, InstantiationException, IllegalAccessException;

    /**
     * Moves an atom in a chain
     *
     * @param chain
     * @param atomId
     * @param moveToPosition
     */
    public void moveAtom(Chain chain, String atomId, int moveToPosition) throws NotFoundInChainException, InstantiationException, IllegalAccessException;

    /**
     * Sets band style
     * @param chain
     * @param bandId
     * @param style
     * @throws NotFoundInChainException
     */
    public void setBandStyle(Chain chain, String bandId, Map<String,String> style) throws NotFoundInChainException;

}
