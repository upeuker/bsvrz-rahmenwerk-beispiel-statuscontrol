package net.upeuker.bsvrz.buv.sample.statustool;

import de.bsvrz.buv.rw.basislib.Rahmenwerk;
import de.bsvrz.buv.rw.basislib.berechtigung.Berechtigungen;
import de.bsvrz.buv.rw.basislib.einstellungen.Einstellungen;

public class RahmenwerkService {
	private static RahmenwerkService service;
    private Rahmenwerk rahmenWerk;
    private Berechtigungen berechtigungen;
    private Einstellungen einstellungen;

    public static RahmenwerkService getService() {
        return RahmenwerkService.service;
    }

    protected void activate() {
        RahmenwerkService.service = this;
    }

    protected void deactivate() {
        RahmenwerkService.service = null;
    }

    protected void bindRahmenwerk(final Rahmenwerk newRahmenWerk) {
        this.rahmenWerk = newRahmenWerk;
    }

    @SuppressWarnings("unused")
    protected void unbindRahmenwerk(final Rahmenwerk oldRahmenWerk) {
        this.rahmenWerk = null;
    }

    public Rahmenwerk getRahmenWerk() {
        return rahmenWerk;
    }

    protected void bindBerechtigungen(final Berechtigungen newBerechtigungen) {
        this.berechtigungen = newBerechtigungen;
    }

    @SuppressWarnings("unused")
    protected void unbindBerechtigungen(final Berechtigungen oldBerechtigungen) {
        this.berechtigungen = null;
    }

    public Berechtigungen getBerechtigungen() {
        return berechtigungen;
    }

    protected void bindEinstellungen(final Einstellungen newEinstellungen) {
        this.einstellungen = newEinstellungen;
    }

    @SuppressWarnings("unused")
    protected void unbindEinstellungen(final Einstellungen oldEinstellungen) {
        this.einstellungen = null;
    }

    public Einstellungen getEinstellungen() {
        return einstellungen;
    }
}