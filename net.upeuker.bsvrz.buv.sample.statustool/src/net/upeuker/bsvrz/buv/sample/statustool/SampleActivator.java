/*
 * Beispiel-Plugin zur Demonstration der Einbindung eines neuen Statuszeielenelements in das NERZ-Rahmenwerk 2.0.
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
 * The activator class controls the plug-in life cycle
 */
public class SampleActivator extends AbstractUIPlugin {

	/** die ID des Plugins. */
	public static final String PLUGIN_ID = "net.upeuker.bsvrz.buv.sample.statustool"; //$NON-NLS-1$

	/** die globale Plugin-Instanz */
	private static SampleActivator plugin;

	/** der Service zum Zugriff auf die Funktionalitäten des Rahmenwerks. */
	private Rahmenwerk rahmenwerk;

	/**
	 * Der Rahmenwerk-Service wird beim Aktivieren des Bundles über den
	 * Servicekontext des Plugins ermittelt. Das ist eine Möglichkeit den
	 * Zugriff auf den Rahmenwerk-OSGI-Service zu erhalten. Zu Beachten ist
	 * dabei, das der Service auf diesem Weg erst nach der Aktivierung zur
	 * Verfügung steht, d.h. Konstruktoren die von außen aufgerufen werden
	 * können (wie bspw. Extension-Point-Instanzen) können unter Umständen noch
	 * nicht darauf zugreifen.
	 * 
	 * Alternativ könnte eine OSGI-Komponente installiert werden, die den
	 * Service zum Zeitpunkt der Aktivierung des OSGI-Frameworks übergeben
	 * bekommt und diesen bereitstellen könnte. Eine weitere Möglichkeit wäre
	 * die Bereitstellung des erforderlichen Services über DI.
	 * 
	 * Genauere Ausführungen gibt es im Migration-Guide für das Rahmenwerk 2.0.
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		SampleActivator.plugin = this;
		rahmenwerk = EclipseContextFactory.getServiceContext(context).get(
				Rahmenwerk.class);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		SampleActivator.plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static SampleActivator getPlugin() {
		return SampleActivator.plugin;
	}

	public Rahmenwerk getRahmenwerk() {
		return rahmenwerk;
	}
}
