package info.watermeyer.gsverein;

import java.util.HashMap;
import java.util.Map;

public class LSBNRWExportErgebnis {

	Map<String, LSBNRWExportVerbandsErgebnis> mAlleErgebnisse;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public LSBNRWExportErgebnis() {
		mAlleErgebnisse = new HashMap<String, LSBNRWExportVerbandsErgebnis>();
	}

	public void count(final String pVerband, final int pGeburtsjahr, final String pGeschlecht) {
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
		mAlleJahrgaenge = new HashMap<Integer, LSBNRWExportVerbandsJahresErgebnis>();
	}

	public Object toCSV() {
		StringBuilder sb = new StringBuilder();
		for (Integer current : mAlleJahrgaenge.keySet()) {
			sb.append(mVerband).append(";");
			sb.append(current).append(";");
			LSBNRWExportVerbandsJahresErgebnis currentJahrgang = mAlleJahrgaenge.get(current);
			sb.append(currentJahrgang.getAnzahlMaennlein()).append(";");
			sb.append(currentJahrgang.getAnzahlWeiblein()).append(";");
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	public void count(int pGeburtsjahr, String pGeschlecht) {
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

	public void count(String pGeschlecht) {
		if ("W".equalsIgnoreCase(pGeschlecht)) {
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