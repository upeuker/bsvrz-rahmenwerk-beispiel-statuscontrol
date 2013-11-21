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
 * Beispiel-Aktivator mit Zugriff auf die Rahmenwerk-Services über den
 * Bundle-Context des Plug-ins.
 */
public class SampleActivator extends AbstractUIPlugin {

	/** die ID des Plugins. */
	public static final String PLUGIN_ID = "net.upeuker.bsvrz.buv.sample.statustool";

	/** die globale Plugin-Instanz. */
	private static SampleActivator plugin;

	/**
	 * Der Rahmenwerk-Service wird über eine OSGI-Komponente bereitgestellt, die den
	 * Service zum Zeitpunkt der Aktivierung des OSGI-Frameworks übergeben
	 * bekommt und diesen bereitstellen könnte. Eine weitere Möglichkeit wäre
	 * die Bereitstellung des erforderlichen Services über DI.
	 * 
	 * Genauere Ausführungen gibt es im Migration-Guide für das Rahmenwerk 2.0.
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
}
