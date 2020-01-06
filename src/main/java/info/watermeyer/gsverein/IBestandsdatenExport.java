package info.watermeyer.gsverein;

public interface IBestandsdatenExport {

	static final String VERBAND_IGNORE = "IGNORE";

	void count(final String pVerband, final int pGeburtsjahr, final Geschlecht pGeschlecht);

	String getExportName();

	String toCSV();

	enum Geschlecht {
		MAENNLEIN("M"), WEIBLEIN("W"), MAENNLICH("männlich"), WEIBLICH("weiblich");

		final String Abkuerzung;

		Geschlecht(String pShortcut) {
			Abkuerzung = pShortcut;
		}

		static Geschlecht fromString(final String pString) {
			if (MAENNLEIN.Abkuerzung.equalsIgnoreCase(pString)) {
				return Geschlecht.MAENNLEIN;
			} else if (WEIBLEIN.Abkuerzung.equalsIgnoreCase(pString)) {
				return Geschlecht.WEIBLEIN;
			} else if (MAENNLICH.Abkuerzung.equalsIgnoreCase(pString)) {
				return Geschlecht.MAENNLEIN;
			} else if (WEIBLICH.Abkuerzung.equalsIgnoreCase(pString)) {
				return Geschlecht.WEIBLEIN;
			} else {
				return Enum.valueOf(Geschlecht.class, pString);
			}

		}
	}
}
