/*
 * Beispiel-Plugin zur Demonstration der Einbindung eines 
 * neuen Statuszeilenelements in das NERZ-Rahmenwerk 2.0.
 *
 * Copyright (c) 2013 Uwe Peuker.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GPL v3.
 */

package net.upeuker.bsvrz.buv.sample.statustool;

import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.bsvrz.buv.rw.basislib.Rahmenwerk;

/**
 * Beispiel-Aktivator mit Zugriff auf die Rahmenwerk-Services �ber den
 * Bundle-Context des Plug-ins.
 */
public class SampleActivator extends AbstractUIPlugin {

	/** die ID des Plugins. */
	public static final String PLUGIN_ID = "net.upeuker.bsvrz.buv.sample.statustool";

	/** die globale Plugin-Instanz. */
	private static SampleActivator plugin;

	/** der Service zum Zugriff auf die Funktionalit�ten des Rahmenwerks. */
	private Rahmenwerk rahmenwerk;

	/**
	 * Der Rahmenwerk-Service wird beim Aktivieren des Bundles �ber den
	 * Servicekontext des Plugins ermittelt. Das ist eine M�glichkeit den
	 * Zugriff auf den Rahmenwerk-OSGI-Service zu erhalten. Zu Beachten ist
	 * dabei, das der Service auf diesem Weg erst nach der Aktivierung zur
	 * Verf�gung steht, d.h. Konstruktoren die von au�en aufgerufen werden
	 * k�nnen (wie bspw. Extension-Point-Instanzen) k�nnen unter Umst�nden noch
	 * nicht darauf zugreifen.
	 * 
	 * Alternativ k�nnte eine OSGI-Komponente installiert werden, die den
	 * Service zum Zeitpunkt der Aktivierung des OSGI-Frameworks �bergeben
	 * bekommt und diesen bereitstellen k�nnte. Eine weitere M�glichkeit w�re
	 * die Bereitstellung des erforderlichen Services �ber DI.
	 * 
	 * Genauere Ausf�hrungen gibt es im Migration-Guide f�r das Rahmenwerk 2.0.
	 * 
	 * @param context
	 *            der Bundle-Kontext bei der Aktivierung des Plug-ins.
	 * @throws Exception
	 *             das Plug-in konnte nicht initialisiert werden
	 */
	@Override
	public final void start(final BundleContext context) throws Exception {
		super.start(context);
		SampleActivator.plugin = this;
		rahmenwerk = EclipseContextFactory.getServiceContext(context).get(
				Rahmenwerk.class);
	}

	@Override
	public final void stop(final BundleContext context) throws Exception {
		SampleActivator.plugin = null;
		super.stop(context);
	}

	/**
	 * liefert die globale Instanz des Plug-ins. Wenn das Plugin noch nicht
	 * aktiviert wurde, wird <code>null</code> geliefert.
	 * 
	 * @return die Instanz oder <code>null</code>
	 */
	public static SampleActivator getPlugin() {
		return SampleActivator.plugin;
	}

	/**
	 * liefert den aus dem Bundle-Kontext ermittelten Rahmenwerk-Service.
	 * 
	 * @return den Service oder <code>null</code>, wenn keiner ermittelt werden
	 *         konnte
	 */
	public final Rahmenwerk getRahmenwerk() {
		return rahmenwerk;
	}
}
