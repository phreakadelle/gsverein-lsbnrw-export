# GS-Verein Exporter für LSB und DOSB
Mit diesem Programm können die Mitgliedsdaten aus GS Verein so aufbereitet werden, dass Sie sich für den Import beim LSB NRW eignen. Das Programm ist deshalb enstanden, da es in GS-Verein keine Möglichkeit gibt, die Export Daten direkt zu erstellen.

# Voraussetzungen
Auf Deinem PC muss Java installiert sein. Um Java zu installieren und für weitere Anweisungen, bitte folgende Webseite besuchen
www.java.com

# Vorgehen
1. Damit das Programm funktioniert, müssen die Mitgliesdaten nach Excel exportiert werden. Die Export-Datei muss verbindlich die folgenden Felder enthalten: "Abteilung", "Geburtsdatum", "Geschlecht", "Austrittsdatum". Die Reihenfolge der Felder ist wichtig. Das Austrittsdatum wird beachtet, wenn das Mitglied in der Vergangenheit ausgetreten ist.

2. Danach öffnest Du die exportierte Excel-Datei und speicherst sie als "CSV (DOS Format)" ab, z.B. direkt auf _C:/_ um es einfacher zu haben.
3. Als nächstes lädst dur Dir diese Datei herunter: https://github.com/phreakadelle/gsverein-verband-export/blob/master/target/gsverein-verband-exporter.jar
Die Datei speicherst du entweder bei dir im Download-Ordner ab oder z.B. direkt auf _C:/_ um es im nächsten Schritt einfacher zu haben.
Außerdem speicherst du dir auch als Beispiel die Konfigurationsdatei ab: https://github.com/phreakadelle/gsverein-verband-export/blob/master/src/test/resources/config.properties

4. Du öffnest die Konfigurations-Datei mit einem Texteditor (Notepad). Die Datei ist wichtig, da Sie die Zuordnung zwischen den Abteilungen in GSVerein und den Verbänden beinhaltet. Ohne diese Zuordnung können die Verbandszahlen nicht erstellt werden. Die Datei hat den folgenden Aufbau
```
VerbandX=AbteilungA,AbteilungB
VerbandY=AbteilungC,AbteilungD
```
Solltet ihr die Zuordnung einer Abteilung zu einem Verband vergessen, so werdet ihr beim Starten des Exports mit einer Programm-Ausgabe darauf hingewiesen. Das Fehlen hat jedoch keine Auswirkung auf den Export, der Eintrag wird dann lediglich ignoriert und euer Export ist ggf. falsch.

5. Du öffnest eine Kommando-Zeile (Start -> Ausführen -> "cmd"). Je nachdem, wo du deine CSV Datei und das Programm gespeichert hast, muss t du nun zu dem Ordner navigieren, z.B. mit "cd .. ". 
Wenn du aber die CSV Datei und das Programm direkt auf C:/ gespeichert hast, dann gibst du in dem schwarzen Fenster nur ein
```
cd \
java -jar gsverein-verband-exporter.jar --input GSVereinExport.csv --config config.properties

ODER

java -jar gsverein-verband-exporter.jar --input GSVereinExport.csv --config config.properties --output BeliebigeDatei.csv
```

Die Bildschirm-Ausgabe müsste nun in etwa so aussehen:
![Alt text](/src/site/resources/2017-01-11_112440.jpg?raw=true "Beispielausgabe")

4. Du kannst nun auf dem Bildschirm die falschen Datensätze erkennen. Hast du das Programm mit dem Parameter "--output" gestartet, dann solltest du auch eine Datei mit dem angegeben Namen finden, wo nochmal alle Datensätze zu finden sind.
