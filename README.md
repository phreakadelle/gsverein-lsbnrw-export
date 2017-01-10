# IBAN Prüfung für GS-Verein
Mit diesem Programm können die IBAN Daten der Vereinsmitglieder validiert und so überprüft werden. Das Programm ist deshalb enstanden, da es in GS-Verein keine Möglichkeit gibt, die IBAN Daten bei Eingabe zu validieren.

So kann man sicherstellen, dass die eingegeben IBAN Daten korrekt sind und wird nicht erst unmittelbar vor dem Beitragseinzug darauf hingewiesen.

# Voraussetzungen
Auf Deinem PC muss Java installiert sein. Um Java zu installieren und für weitere Anweisungen, bitte folgende Webseite besuchen
www.java.com

# Vorgehen
1. Damit das Programm funktioniert, müssen die Mitgliesdaten nach Excel exportiert werden. Die Export-Datei muss mindestens die folgenden Felder enthalten: "Vorname", "Nachname", "IBAN". Die Felder Vor/Nachname müssen deshalb mit exportiert werden, damit Du anschließend die ungültigen IBANs wieder den Mitgliedern zuordnen kannst. Du kannst auch andere Felder statt des Names nehmen. 
*Wichtig ist nur, dass an 3. Stelle die IBAN steht.*
2. Danach öffnest Du die exportierte Excel-Datei und speicherst sie als "CSV (DOS Format)" ab, z.B. direkt auf _C:/_ um es einfacher zu haben.
3. Als nächstes lädst dur Dir diese Datei herunter: https://github.com/phreakadelle/gsverein-iban-validator/blob/master/target/gsverein-iban-tool.jar
Die Datei speicherst du entweder bei dir im Download-Ordner ab oder z.B. direkt auf _C:/_ um es im nächsten Schritt einfacher zu haben.

3. Du öffnest eine Kommando-Zeile (Start -> Ausführen -> "cmd"). Je nachdem, wo du deine CSV Datei und das Programm gespeichert hast, muss t du nun zu dem Ordner navigieren, z.B. mit "cd .. ". 
Wenn du aber die CSV Datei und das Programm direkt auf C:/ gespeichert hast, dann gibst du in dem schwarzen Fenster nur ein
```
cd \
java -jar gsverein-iban-tool.jar --input Mappe1.csv

ODER

java -jar gsverein-iban-tool.jar --input Mappe1.csv --output Ausgabe.csv
```
Die Bildschirm-Ausgabe müsste nun in etwa so aussehen:
![Alt text](/src/site/resources/2016-11-18_120801.jpg?raw=true "Beispielausgabe")

4. Du kannst nun auf dem Bildschirm die falschen Datensätze erkennen. Hast du das Programm mit dem Parameter "--output" gestartet, dann solltest du auch eine Datei mit dem angegeben Namen finden, wo nochmal alle Datensätze zu finden sind.
