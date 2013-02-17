package infra.chains.impl;

import infra.ca.Atom;
import infra.ca.AtomPush;
import infra.ca.AtomsManager;
import infra.ca.StringIdContainer;
import infra.ca.ex.CreativeAtomException;
import infra.chains.*;
import infra.chains.ex.NotFoundInChainException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author alari
 * @since 11/19/12 1:26 AM
 */
@Service
public class ChainsManagerImpl implements ChainsManager {
    @Autowired
    private AtomsManager atomsManager;
    @Autowired
    private ChainFactory chainFactory;
    @Autowired
    private BandFactory bandFactory;

    private int idLength = 8;

    /**
     * Builds a new Chain object
     *
     * @return instance of Chain
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Chain buildChain() throws IllegalAccessException, InstantiationException {
        return chainFactory.buildChain();
    }

    /**
     * Adds an atom into a chain, possibly creates a new band object
     *
     * @param chain
     * @param atom
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void addAtom(Chain chain, Atom atom) throws IllegalAccessException, InstantiationException {
        if (chain.getBands() == null) {
            chain.setBands(new LinkedList<Band>());
        }
        Band band = null;
        if (chain.getBands().size() > 0) {
            band = chain.getBands().get(chain.getBands().size() - 1);
            if (!band.getType().equalsIgnoreCase(atom.getType())) {
                band = null;
            }
        }
        if (band == null) {
            band = createBand(chain);
            band.setType(atom.getType());
            chain.getBands().add(band);
        }
        band.getAtoms().add(atom);
    }

    /**
     * Builds an atom from a user-provided data and adds it into a chain
     *
     * @param chain
     * @param data
     * @return a new built atom (already injected into a chain)
     * @throws infra.ca.ex.CreativeAtomException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Atom pushAtom(Chain chain, AtomPush data) throws CreativeAtomException, InstantiationException, IllegalAccessException {
        data.setId(getUniqueAtomId(chain));
        Atom atom = atomsManager.build(data);
        addAtom(chain, atom);
        return atom;
    }

    /**
     * Builds and pushes an atom into specified band
     *
     * @param chain
     * @param data
     * @param bandId
     * @return
     */
    public Atom pushAtom(Chain chain, AtomPush data, String bandId) throws CreativeAtomException, InstantiationException, IllegalAccessException {
        data.setId(getUniqueAtomId(chain));
        Atom atom = atomsManager.build(data);
        Band band = getBand(chain, bandId);

        if (band.getType().equalsIgnoreCase(atom.getType())) {
            band.getAtoms().add(atom);
        } else {
            addAtom(chain, atom);
            moveToBand(chain, atom.getId(), bandId);
        }
        return atom;
    }

    /**
     * Retrieves an atom from chain by id
     *
     * @param chain
     * @param id
     * @return
     */
    public Atom getAtom(Chain chain, String id) throws NotFoundInChainException {
        for (Band b : chain.getBands()) for (Atom a : b.getAtoms()) if (a.getId().equalsIgnoreCase(id)) return a;
        throw new NotFoundInChainException();
    }

    /**
     * Removes an atom from chain by its id
     *
     * @param chain
     * @param id
     */
    public void removeAtom(Chain chain, String id) throws NotFoundInChainException {
        for (Band b : chain.getBands())
            for (Atom a : b.getAtoms())
                if (a.getId().equalsIgnoreCase(id)) {
                    b.getAtoms().remove(a);
                    if (b.getAtoms().size() == 0) {
                        chain.getBands().remove(b);
                    }
                    return;
                }
        throw new NotFoundInChainException();
    }

    /**
     * Deletes atom contents and removes it from a chain
     *
     * @param chain
     * @param id
     * @throws infra.ca.ex.CreativeAtomException
     */
    public void deleteAtom(Chain chain, String id) throws CreativeAtomException {
        atomsManager.delete(getAtom(chain, id));
        removeAtom(chain, id);
    }

    /**
     * Prepares a chain to render update
     *
     * @param chain
     * @throws infra.ca.ex.CreativeAtomException
     */
    public void forUpdate(Chain chain) throws CreativeAtomException {
        for (Band b : chain.getBands()) for (Atom a : b.getAtoms()) atomsManager.forUpdate(a);
    }

    /**
     * Prepares a chain for common render
     *
     * @param chain
     * @throws infra.ca.ex.CreativeAtomException
     */
    public void forRender(Chain chain) throws CreativeAtomException {
        for (Band b : chain.getBands()) for (Atom a : b.getAtoms()) atomsManager.forRender(a);
    }

    /**
     * Deletes all chain atoms contents
     *
     * @param chain
     * @throws infra.ca.ex.CreativeAtomException
     */
    public void delete(Chain chain) throws CreativeAtomException {
        for (Band b : chain.getBands()) for (Atom a : b.getAtoms()) atomsManager.delete(a);
    }

    /**
     * Returns a band by its id
     *
     * @param chain
     * @param bandId
     * @return
     * @throws infra.chains.ex.NotFoundInChainException
     */
    public Band getBand(Chain chain, String bandId) throws NotFoundInChainException {
        for (Band b : chain.getBands()) if (b.getId().equalsIgnoreCase(bandId)) return b;
        throw new NotFoundInChainException();
    }

    /**
     * Returns a band atom belongs to
     *
     * @param chain
     * @param atomId
     * @return
     * @throws infra.chains.ex.NotFoundInChainException
     *
     */
    public Band getAtomBand(Chain chain, String atomId) throws NotFoundInChainException {
        for (Band b : chain.getBands()) for (Atom a : b.getAtoms()) if (a.getId().equalsIgnoreCase(atomId)) return b;
        throw new NotFoundInChainException();
    }

    /**
     * Moves an atom to the specified position in its band
     *
     * @param chain
     * @param atomId
     * @param moveToPosition
     * @throws infra.chains.ex.NotFoundInChainException
     */
    public void moveInBand(Chain chain, String atomId, int moveToPosition) throws NotFoundInChainException {
        Band band = getAtomBand(chain, atomId);
        moveInList(band.getAtoms(), atomId, moveToPosition);
    }

    /**
     * Moves a band to the specified position in a chain
     *
     * @param chain
     * @param bandId
     * @param moveToPosition
     */
    public void moveBand(Chain chain, String bandId, int moveToPosition) {
        moveInList(chain.getBands(), bandId, moveToPosition);
    }


    /**
     * Moves an atom from band to band, places it to the end of a target band
     *
     * @param chain
     * @param atomId
     * @param bandId
     */
    public void moveToBand(Chain chain, String atomId, String bandId) throws NotFoundInChainException, IllegalAccessException, InstantiationException {
        Band sourceBand = getAtomBand(chain, atomId);
        if (sourceBand.getId().equalsIgnoreCase(bandId)) {
            moveInList(sourceBand.getAtoms(), atomId, sourceBand.getAtoms().size() - 1);
        }

        Band targetBand = getBand(chain, bandId);
        Atom atom = null;
        for (Atom a : sourceBand.getAtoms()) {
            if (a.getId().equalsIgnoreCase(atomId)) {
                atom = a;
            }
        }
        // Simple case
        if (targetBand.getType().equalsIgnoreCase(sourceBand.getType())) {
            targetBand.getAtoms().add(atom);
            sourceBand.getAtoms().remove(atom);
            if (sourceBand.getAtoms().size() == 0) {
                chain.getBands().remove(sourceBand);
            }
            // Less simple case, but still without position
        } else {
            int targetPosition = chain.getBands().indexOf(targetBand);
            // Is the next band is of required type?
            if (targetPosition < chain.getBands().size() - 1) {
                Band nextBand = chain.getBands().get(targetPosition + 1);
                if (nextBand.getType().equalsIgnoreCase(sourceBand.getType())) {
                    sourceBand.getAtoms().remove(atom);
                    nextBand.getAtoms().add(0, atom);
                    if (sourceBand.getAtoms().size() == 0) {
                        chain.getBands().remove(sourceBand);
                    }
                    return;
                }
            }
            if (sourceBand.getAtoms().size() == 1) {
                // Move atom with its band
                chain.getBands().remove(sourceBand);
                chain.getBands().add(targetPosition, sourceBand);
            } else {
                // Create a new band
                Band newBand = copyBand(chain, sourceBand);
                sourceBand.getAtoms().remove(atom);
                newBand.getAtoms().add(atom);
                chain.getBands().add(targetPosition + 1, newBand);
            }
        }
    }

    /**
     * Moves an atom to specified position in a target band, splits a target if it's of the wrong type
     *
     * @param chain
     * @param atomId
     * @param bandId
     * @param moveToPosition
     */
    public void moveToBand(Chain chain, String atomId, String bandId, int moveToPosition) throws NotFoundInChainException, InstantiationException, IllegalAccessException {
        Band sourceBand = getAtomBand(chain, atomId);
        // Target is a source -- degrade
        if (sourceBand.getId().equalsIgnoreCase(bandId)) {
            moveInBand(chain, atomId, moveToPosition);
            return;
        }

        Band targetBand = getBand(chain, bandId);
        // Moving right after the target band -- degrade
        if (targetBand.getAtoms().size() <= moveToPosition) {
            moveToBand(chain, atomId, bandId);
            return;
        }

        // We actually need to move
        Atom atom = null;
        for (Atom a : sourceBand.getAtoms()) {
            if (a.getId().equalsIgnoreCase(atomId)) {
                atom = a;
            }
        }
        if (atom == null) {
            throw new NotFoundInChainException();
        }
        // Simple case
        if (targetBand.getType().equalsIgnoreCase(sourceBand.getType())) {
            // Just moving an atom
            sourceBand.getAtoms().remove(atom);
            if (sourceBand.getAtoms().size() == 0) {
                chain.getBands().remove(sourceBand);
            }
            // Simply add and rearrange
            targetBand.getAtoms().add(atom);
            moveInList(targetBand.getAtoms(), atomId, moveToPosition);
        } else {
            // It's not so simple yet
            if (moveToPosition == 0) {
                // Check previous band
                int targetPosition = chain.getBands().indexOf(targetBand);
                if (targetPosition == 0) {
                    // Placing before everything
                    if (sourceBand.getAtoms().size() == 1) {
                        chain.getBands().remove(sourceBand);
                        chain.getBands().add(0, sourceBand);
                    } else {
                        sourceBand.getAtoms().remove(atom);
                        Band newBand = copyBand(chain, sourceBand);
                        newBand.getAtoms().add(atom);
                        chain.getBands().add(0, newBand);
                    }
                } else {
                    // Placing after the previous band
                    Band previousBand = chain.getBands().get(targetPosition - 1);
                    moveToBand(chain, atomId, previousBand.getId());
                }
            } else {
                // It's inside the target band and we have to split it
                // Prepare new band object
                Band newBand;
                if (sourceBand.getAtoms().size() == 1) {
                    newBand = sourceBand;
                    chain.getBands().remove(sourceBand);
                } else {
                    newBand = copyBand(chain, sourceBand);
                    sourceBand.getAtoms().remove(atom);
                    newBand.getAtoms().add(atom);
                }
                int targetPosition = chain.getBands().indexOf(targetBand);

                // Prepare second part of target band
                Band secondTarget = copyBand(chain, targetBand);
                secondTarget.getAtoms().addAll(targetBand.getAtoms().subList(moveToPosition, targetBand.getAtoms().size()));
                targetBand.setAtoms(targetBand.getAtoms().subList(0, moveToPosition));

                List<Band> bands = new LinkedList<Band>();
                bands.add(newBand);
                bands.add(secondTarget);

                chain.getBands().addAll(targetPosition + 1, bands);
            }
        }
    }

    /**
     * Moves an atom in a chain
     *
     * @param chain
     * @param atomId
     * @param moveToPosition
     */
    public void moveAtom(Chain chain, String atomId, int moveToPosition) throws NotFoundInChainException, InstantiationException, IllegalAccessException {
        // left position index in a band
        int bandOffset = 0;
        // right position index in a band
        int bandEdge = 0;

        // Where an atom is to be placed to
        Band targetBand = null;
        int targetBandPosition = 0;

        // Source info
        Band sourceBand = null;
        int sourceBandPosition = 0;

        // Iterating through bands to find source and target
        for (Band b : chain.getBands()) {
            bandEdge += b.getAtoms().size();
            if (moveToPosition >= bandOffset && moveToPosition < bandEdge) {
                targetBand = b;
                targetBandPosition = moveToPosition - bandOffset;
            }
            if (sourceBand != null && targetBand != null) {
                break;
            }
            sourceBandPosition = 0;
            for (Atom a : b.getAtoms()) {
                if (a.getId().equalsIgnoreCase(atomId)) {
                    sourceBand = b;
                    break;
                }
                ++sourceBandPosition;
            }
            bandOffset = bandEdge;
        }

        // Source not found
        if (sourceBand == null) {
            throw new NotFoundInChainException();
        }

        // Target not found -- moving to the end of a chain
        if (targetBand == null) {
            moveToBand(chain, atomId, chain.getBands().get(chain.getBands().size() - 1).getId());
            return;
        }

        // Moving inside a single band
        if (targetBand == sourceBand) {
            if (targetBandPosition != sourceBandPosition) {
                moveInList(targetBand.getAtoms(), atomId, targetBandPosition);
            }
            return;
        }

        // Moving to another band
        moveToBand(chain, atomId, targetBand.getId(), targetBandPosition);
    }

    /**
     * Sets band style
     * @param chain
     * @param bandId
     * @param style
     * @throws infra.chains.ex.NotFoundInChainException
     */
    public void setBandStyle(Chain chain, String bandId, Map<String,String> style) throws NotFoundInChainException {
        getBand(chain, bandId).setStyles(style);
    }

    /**
     * Routine to move objects in list
     *
     * @param list
     * @param id
     * @param moveToPosition
     * @param <T>
     */
    private <T extends StringIdContainer> void moveInList(List<T> list, String id, int moveToPosition) {
        if (moveToPosition < 0) moveToPosition = 0;
        if (moveToPosition >= list.size()) moveToPosition = list.size() - 1;

        int position = 0;
        T objectToMove = null;

        for (T o : list) {
            if (o.getId().equalsIgnoreCase(id)) {
                objectToMove = o;
                break;
            }
            ++position;
        }
        if (position == moveToPosition) return;

        list.remove(position);
        list.add(moveToPosition, objectToMove);
    }

    /**
     * Generates a random id
     *
     * @return
     */
    private String randomId() {
        return RandomStringUtils.randomAlphanumeric(idLength).toLowerCase();
    }

    /**
     * Builds a new Band object to place into a Chain. Unique ID is given
     *
     * @param chain
     * @return band object to place into chain
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private Band createBand(Chain chain) throws IllegalAccessException, InstantiationException {
        Band band = bandFactory.buildBand();
        band.setId(getUniqueBandId(chain));
        band.setAtoms(new LinkedList<Atom>());
        return band;
    }

    /**
     * Returns a unique id for a new atom in chain
     *
     * @param chain to place an atom in
     * @return unique id
     */
    private String getUniqueAtomId(Chain chain) {
        boolean idNotUnique = true;
        String id = null;
        while (idNotUnique) {
            id = randomId();
            idNotUnique = false;
            if (chain.getBands() != null) {
                for (Band b : chain.getBands())
                    if (b.getAtoms() != null) {
                        for (Atom a : b.getAtoms()) {
                            if (a.getId().equalsIgnoreCase(id)) {
                                idNotUnique = true;
                                break;
                            }
                        }
                        if (idNotUnique) break;
                    }
            }
        }
        return id;
    }

    /**
     * Provides unique band id (in chain scope)
     *
     * @param chain
     * @return band id
     */
    private String getUniqueBandId(Chain chain) {
        boolean idNotUnique = true;
        String id = null;
        while (idNotUnique) {
            id = randomId();
            idNotUnique = false;
            if (chain.getBands() != null) {
                for (Band b : chain.getBands())
                    if (b.getId().equalsIgnoreCase(id)) {
                        idNotUnique = true;
                        break;
                    }
            }
        }
        return id;
    }

    /**
     * Makes a copy of a band in terms of its style, type and so on
     *
     * @param chain
     * @param source
     * @return a new band to be included into a chain
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private Band copyBand(Chain chain, Band source) throws InstantiationException, IllegalAccessException {
        Band band = createBand(chain);
        band.setType(source.getType());
        band.setStyles(source.getStyles());
        return band;
    }
}
