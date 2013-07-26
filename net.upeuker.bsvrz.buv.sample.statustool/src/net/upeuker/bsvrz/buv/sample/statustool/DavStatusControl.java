/*
 * Beispiel-Plugin zur Demonstration der Einbindung eines neuen Statuszeilenelements in das NERZ-Rahmenwerk 2.0.
 *
 * Copyright (c) 2013 Uwe Peuker.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GPL v3.
 */

package net.upeuker.bsvrz.buv.sample.statustool;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

import de.bsvrz.buv.rw.basislib.Rahmenwerk;
import de.bsvrz.buv.rw.basislib.dav.DavVerbindungsEvent;
import de.bsvrz.buv.rw.basislib.dav.DavVerbindungsListener;
import de.bsvrz.buv.rw.basislib.menu.AbstractRwMenuControl;
import de.bsvrz.buv.rw.basislib.util.RwColor;
import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ClientApplication;
import de.bsvrz.dav.daf.main.config.DataModel;

/**
 * Ein Element für die Statuszeile des NERZ-Rahmenwerk, welches den
 * Sendepufferzustand der Rahmenwerks-Applikation selbst visualisiert.
 * 
 * Bei einer bestehenden Datenverteilerverbindung wird der Datensatz
 * "atg.angemeldeteApplikationen" des lokalen Datenverteilers empfangen. Dieser
 * enthält unter anderem den Sendepufferzustand der bei diesem Datenverteiler
 * registrierten Applikationen. Aus dem Datensatz wird der entsprechende Wert
 * für die aktuelle Rahmenwerksapplikation ermittelt und angezeigt.
 * 
 * Der Prototyp und die Standparameter des Elements werden per Extension-Point
 * zur Verfügung gestellt.
 * 
 * @author Uwe Peuker
 */
public class DavStatusControl extends AbstractRwMenuControl {

	/**
	 * Interne Implementierung der {@link ClientReceiverInterface} über die die
	 * gewünschten Daten beim Datenverteiler angemeldet, empfangen und
	 * aufbereitet werden.
	 */
	private class DataReceiver implements ClientReceiverInterface {

		private ClientDavInterface dav;
		private DataDescription desc;

		/* enthält den aktuellen Wert. */
		private String value = "init";

		public DataReceiver(final ClientDavInterface dav) {
			if (dav != null) {
				this.dav = dav;
				final DataModel dataModel = dav.getDataModel();
				final AttributeGroup atg = dataModel
						.getAttributeGroup("atg.angemeldeteApplikationen");
				final Aspect aspect = dataModel.getAspect("asp.standard");
				desc = new DataDescription(atg, aspect);
				dav.subscribeReceiver(this, dav.getLocalDav(), desc,
						ReceiveOptions.normal(), ReceiverRole.receiver());
			}
		}

		public void disconnect() {
			if (this.dav != null) {
				dav.unsubscribeReceiver(this, dav.getLocalDav(), desc);
			}
			dav = null;
			desc = null;
		}

		/**
		 * der empfangene Datensatz wird analysiert und der aktuelle
		 * Eregbniswert berechnet. Der Ergebniswert wird mit dem aktuellen
		 * Datenzeitstempel eingeleitet.
		 * 
		 * Wenn keine Daten empfangen worden bzw. die Applikation nicht in der
		 * Liste der beim ausgewerteten Datenverteiler angemeldeten
		 * Applikationen enthalten ist, wird ein entsrechender Meldungstext
		 * ausgegeben. Ansonsten wird der aktuell geliefert Zustandwert in das
		 * Ergebnis übernommen.
		 */
		@Override
		public void update(final ResultData[] results) {
			for (final ResultData result : results) {
				if (result.hasData()) {
					boolean appFound = false;
					final ClientApplication application = dav
							.getLocalApplicationObject();
					final long zeit = result.getDataTime();
					value = DateFormat.getTimeInstance().format(new Date(zeit))
							+ ": ";
					final Array array = result.getData().getArray(
							"angemeldeteApplikation");
					for (int idx = 0; idx < array.getLength(); idx++) {
						if (application.equals(array.getItem(idx)
								.getReferenceValue("applikation")
								.getSystemObject())) {
							value += array.getItem(idx)
									.getTextValue("sendepufferzustand")
									.getValueText();
							appFound = true;
							break;
						}
					}
					if (!appFound) {
						value += "Applikation nicht gefunden!";
					}
				} else {
					value += "Keine Daten!";
				}
				updateText();
			}
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * interner Listener für den Verbindungszustand mit einem Datenverteiler.
	 * 
	 * Je nach Zustand werden die Empfänger für die Daten des
	 * Datenverteilerdatensatzes an- oder abgemeldet und der Anzeigetext
	 * aktualisiert.
	 */
	private class VerbindungsListener implements DavVerbindungsListener {

		@Override
		public void verbindungHergestellt(final DavVerbindungsEvent event) {
			registerReceiver(event.getDavVerbindung());
			updateText();
		}

		@Override
		public void verbindungGetrennt(final DavVerbindungsEvent event) {
			deregisterReceiver();
			updateText();
		}

		@Override
		public boolean verbindungHalten(final DavVerbindungsEvent event) {
			return false;
		}
	}

	/** die Parameter des Elements. */
	private static final String PARAM_FARBE = "farbe";
	private static final String PARAM_HINTERGRUND = "hintergrund";

	/** der Wert der aktuellen Parameter. */
	private String foreGround;
	private String backGround;

	private CLabel anzeigeText;
	private VerbindungsListener verbindungsListener;
	private DataReceiver dataReceiver;

	@Override
	public void fill(final Composite parent) {

		if (anzeigeText == null) {
			anzeigeText = new CLabel(parent, SWT.SHADOW_NONE | SWT.BORDER);
			anzeigeText.setAlignment(SWT.CENTER);

			Color color = new Color(anzeigeText.getDisplay(),
					RwColor.getRgbFromString(foreGround));
			anzeigeText.setForeground(color);
			color.dispose();

			if ((backGround != null) && (!backGround.trim().isEmpty())) {
				color = new Color(anzeigeText.getDisplay(),
						RwColor.getRgbFromString(backGround));
				anzeigeText.setBackground(color);
				color.dispose();
			} else {
				anzeigeText.setBackground((Color) null);
			}

			anzeigeText.setToolTipText("Sendepufferzustand der Applikation");
			anzeigeText.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(final DisposeEvent e) {
					deregisterReceiver();
					anzeigeText = null;
					if (verbindungsListener != null) {
						SampleActivator
								.getPlugin()
								.getRahmenwerk()
								.removeDavVerbindungsListener(
										verbindungsListener);
					}
					verbindungsListener = null;
				}

			});

			verbindungsListener = new VerbindungsListener();
			SampleActivator.getPlugin().getRahmenwerk()
					.addDavVerbindungsListener(verbindungsListener);
			if (SampleActivator.getPlugin().getRahmenwerk().isOnline()) {
				registerReceiver(SampleActivator.getPlugin().getRahmenwerk()
						.getDavVerbindung());
			}

			updateText();
		}
	}

	public void deregisterReceiver() {
		if (dataReceiver != null) {
			dataReceiver.disconnect();
		}
		dataReceiver = null;
	}

	public void registerReceiver(final ClientDavInterface dav) {
		deregisterReceiver();
		dataReceiver = new DataReceiver(dav);
	}

	/**
	 * der Text wird immerhalb des UI-Threads asynchron aktualisiert, da die
	 * Daten bzw. der geänderte Verbindungszustand aus einem Thread der
	 * Datenverteiler-Applikationsfunktionen heraus erfolgt.
	 */
	private void updateText() {

		if (PlatformUI.getWorkbench().isClosing()) {
			return;
		}

		new UIJob("Aktualisiere Status") {

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor) {

				if ((anzeigeText != null) && (!anzeigeText.isDisposed())) {

					final String text = getCurrentText();

					anzeigeText.setText(text);
					final Point preferredSize = ((Control) anzeigeText)
							.computeSize(SWT.DEFAULT, SWT.DEFAULT);
					final int widthHint = preferredSize.x + 10;
					final StatusLineLayoutData data = new StatusLineLayoutData();
					data.widthHint = widthHint;
					anzeigeText.setLayoutData(data);
					anzeigeText.getParent().layout(true);
				}
				return Status.OK_STATUS;
			}

			@Override
			public boolean belongsTo(final Object family) {
				return Rahmenwerk.JOB_FAMILY.equals(family)
						|| super.belongsTo(family);
			}
		}.schedule();
	}

	private String getCurrentText() {
		if (dataReceiver == null) {
			return "Keine Verbindung";
		}

		return dataReceiver.getValue();
	}

	@Override
	public void setParameter(final Map<String, String> parameters) {
		for (final Entry<String, String> parameter : parameters.entrySet()) {
			if (DavStatusControl.PARAM_FARBE.equals(parameter.getKey())) {
				foreGround = parameter.getValue();
			} else if (DavStatusControl.PARAM_HINTERGRUND.equals(parameter
					.getKey())) {
				backGround = parameter.getValue();
			}
		}
	}

	@Override
	public boolean useInStatusLine() {
		return true;
	}
}
