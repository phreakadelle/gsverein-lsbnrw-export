package info.watermeyer.gsverein;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import info.watermeyer.gsverein.IBestandsdatenExport.Geschlecht;

public class LSBNRWExportErgebnis implements IBestandsdatenExport {

	Map<String, LSBNRWExportVerbandsErgebnis> mAlleErgebnisse;

	public String getExportName() {
		return "lsbnrw";
	}

	public LSBNRWExportErgebnis() {
		mAlleErgebnisse = new HashMap<String, LSBNRWExportVerbandsErgebnis>();
	}

	public void count(final String pVerband, final int pGeburtsjahr, final Geschlecht pGeschlecht) {
		if (VERBAND_IGNORE.equals(pVerband)) {
			return;
		}

		if (mAlleErgebnisse.containsKey(pVerband) == false) {
			mAlleErgebnisse.put(pVerband, new LSBNRWExportVerbandsErgebnis(pVerband));
		}

		mAlleErgebnisse.get(pVerband).count(pGeburtsjahr, pGeschlecht);
	}

	public String toCSV() {
		StringBuilder sb = new StringBuilder();
		for (String currentVerband : mAlleErgebnisse.keySet()) {
			sb.append(mAlleErgebnisse.get(currentVerband).toCSV());
		}
		return sb.toString();
	}
}

class LSBNRWExportVerbandsErgebnis {

	final Map<Integer, LSBNRWExportVerbandsJahresErgebnis> mAlleJahrgaenge;
	final String mVerband;

	public LSBNRWExportVerbandsErgebnis(String pVerband) {
		mVerband = pVerband;
		mAlleJahrgaenge = new TreeMap<Integer, LSBNRWExportVerbandsJahresErgebnis>();
	}

	public Object toCSV() {
		StringBuilder sb = new StringBuilder();
		for (Integer current : mAlleJahrgaenge.keySet()) {
			LSBNRWExportVerbandsJahresErgebnis currentJahrgang = mAlleJahrgaenge.get(current);

			// Maennlein
			sb.append(mVerband).append(";");
			sb.append(current).append(";");
			sb.append("m").append(";");
			sb.append(currentJahrgang.getAnzahlMaennlein());
			sb.append(System.getProperty("line.separator"));
			
			// Weiblein
			sb.append(mVerband).append(";");
			sb.append(current).append(";");
			sb.append("w").append(";");
			sb.append(currentJahrgang.getAnzahlWeiblein());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	public void count(int pGeburtsjahr, Geschlecht pGeschlecht) {
		if (!mAlleJahrgaenge.containsKey(pGeburtsjahr)) {
			mAlleJahrgaenge.put(pGeburtsjahr, new LSBNRWExportVerbandsJahresErgebnis());
		}
		mAlleJahrgaenge.get(pGeburtsjahr).count(pGeschlecht);

	}

}

class LSBNRWExportVerbandsJahresErgebnis {

	private int mCountM;
	private int mCountW;

	public LSBNRWExportVerbandsJahresErgebnis() {
		mCountM = 0;
		mCountW = 0;
	}

	public void count(Geschlecht pGeschlecht) {
		if (Geschlecht.WEIBLEIN.equals(pGeschlecht)) {
			mCountW++;
		} else {
			mCountM++;
		}
	}

	public int getAnzahlMaennlein() {
		return mCountM;
	}

	public int getAnzahlWeiblein() {
		return mCountW;
	}

}
