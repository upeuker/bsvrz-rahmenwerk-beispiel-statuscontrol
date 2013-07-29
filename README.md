bsvrz-rahmenwerk-beispiel-statuscontrol
=======================================

Ein Beispiel-Plugin mit dem die Einbindung neuer Statuszeilen-Elemente in das Rahmenwerk  der NERZ-Software demonstriert wird.

Desweiteren wird die Einbindung des Rahmenwerk-Services über die ServiceRegistry des Bundles gezeigt.

Nähere Informationen zur NERZ-Software finden sich unter diesem [Link](http://www.nerz-ev.de/ "NERZ").

Enthalten ist ein Element, welches den
Sendepufferzustand der Rahmenwerks-Applikation selbst visualisiert.
 
Bei einer bestehenden Datenverteilerverbindung wird der Datensatz
"atg.angemeldeteApplikationen" des lokalen Datenverteilers empfangen. Dieser
enthält unter anderem den Sendepufferzustand der bei diesem Datenverteiler
registrierten Applikationen. Aus dem Datensatz wird der entsprechende Wert
für die aktuelle Rahmenwerksapplikation ermittelt und angezeigt.
 
Der Prototyp und die Standardparameter des Elements werden per Extension-Point
zur Verfügung gestellt.
