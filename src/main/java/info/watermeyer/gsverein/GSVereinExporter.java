package info.watermeyer.gsverein;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import info.watermeyer.gsverein.IBestandsdatenExport.Geschlecht;

public class GSVereinExporter {

	private static final String DATE_FORMAT = "dd.MM.yyyy";

	private final static Logger LOGGER = Logger.getLogger(GSVereinExporter.class);

	private final IBestandsdatenExport mResult;

	public GSVereinExporter(IBestandsdatenExport pErgebnis) {
		mResult = pErgebnis;
	}

	public List<String> export(final String pConfigFilePath, final String pInputFilePath, final String pOutputFile)
			throws Exception {
		if (pConfigFilePath == null) {
			throw new IllegalArgumentException("Die Pfadangabe ist ungueltig " + pConfigFilePath);
		}
		if (pInputFilePath == null) {
			throw new IllegalArgumentException("Die Pfadangabe ist ungueltig " + pInputFilePath);
		}

		final File pFile = new File(pInputFilePath);
		if (pFile.exists() == false || pFile.isFile() == false) {
			throw new IllegalArgumentException(
					"Die Input-Datei konnte nicht gefunden werden: " + pFile.getAbsolutePath());
		}

		final File pConfigFile = new File(pConfigFilePath);
		if (pConfigFile.exists() == false || pConfigFile.isFile() == false) {
			throw new IllegalArgumentException(
					"Die Konfigurationsdatei konnte nicht gefunden werden: " + pFile.getAbsolutePath());
		}

		String out = pOutputFile;
		if (out == null) {
			out = mResult.getExportName() + "_" + pFile.getName();
		}
		return doExport(pInputFilePath, out, pFile, pConfigFile);
	}

	/**
	 * 
	 * @param pInputFilePath
	 * @param pOut
	 * @param pFile
	 * @param pConfigFile
	 * @return List of failed entries.
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	List<String> doExport(final String pInputFilePath, final String pOutputFile, final File pFile,
			final File pConfigFile) throws IOException, FileNotFoundException, Exception {
		GSVereinExporterConfig pConfig = new GSVereinExporterConfig();
		pConfig.load(new FileInputStream(pConfigFile));

		int counter = 1;
		final List<String> retVal = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(pFile));
				OutputLog out = new OutputLog(pOutputFile)) {
			String line = null;
			while ((line = br.readLine()) != null) {
				final String validationResult = handleLine(pConfig, line);
				if (validationResult != null) {
					LOGGER.info(validationResult);
					retVal.add(validationResult);
				}
				counter++;
			}

			// Das End-Ergebnis in die CSV Datei schreiben
			out.write(mResult.toCSV());
		} catch (Exception e) {
			throw new Exception("Fehler beim Lesen der Datei: " + pInputFilePath, e);
		} finally {
			LOGGER.info("Ueberpruefte Datensaetze. Anzahl: " + counter + " Fehlerhaft: " + retVal.size());
		}
		return retVal;
	}

	String handleLine(final GSVereinExporterConfig pConfig, final String pLine) {
		String retVal = null;
		String[] split = pLine.split(";");
		try {
			if (split.length < 3) {
				retVal = createMessage(pLine, "Zeile ungueltig. Anzahl Felder muss 3 oder groesser sein.");
			} else {
				final String abteilung = removeTrailingLeadingQuotes(split[0]);
				final String geburtsdatum =  removeTrailingLeadingQuotes(split[1]);
				final String geschlecht = removeTrailingLeadingQuotes( split[2]);

				final String austritt;
				if (split.length > 3) {
					austritt =  removeTrailingLeadingQuotes(split[3]);
				} else {
					austritt = null;
				}

				retVal = handleEintrag(pConfig, abteilung, geburtsdatum, geschlecht, austritt);
			}
		} catch (Exception e) {
			LOGGER.warn("Fehler beim Verarbeiten der Zeile: '" + pLine + "' Fehler: " + e.getMessage(), e);
			retVal = "Fehler beim Verarbeiten der Zeile: '" + pLine + "' Fehler: " + e.getMessage();
		}
		return retVal;
	}

	String removeTrailingLeadingQuotes(String pString) {
		String retVal = pString;
		if(retVal.startsWith("\"")) {
			retVal = retVal.substring(1);
		}
		
		if(retVal.endsWith("\"")) {
			retVal = retVal.substring(0,retVal.length() -1);
		}
		return retVal;
	}
	/**
	 * 
	 * @param pProps
	 * @param pAbteilung
	 * @param pGeburtsdatum
	 * @param pGeschlecht
	 * @param pAustritt
	 * @return NULL if everything is OK, otherwise Error-String.
	 */
	String handleEintrag(final GSVereinExporterConfig pProps, final String pAbteilung, final String pGeburtsdatum,
			final String pGeschlecht, final String pAustritt) {

		// Austrittsdatum
		String retVal = handleAustrittsdatum(pAustritt);
		if (retVal != null) {
			return retVal;
		}

		// Geschlecht
		Geschlecht geschlecht = null;
		try {
			geschlecht = Geschlecht.fromString(pGeschlecht);
		} catch (Exception e) {
			return "Das Geschlecht muss M oder W sein, ist aber: '" + pGeschlecht + "'";
		}

		// Verband
		final String verband = pProps.getVerband(pAbteilung);
		if (verband == null) {
			return "Der Abteilung '" + pAbteilung + "' wurde in der Konfigurationsdatei kein Verband zugeordnet.";
		}

		// Geburtsdatum
		final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		int geburtsjahr = 0;
		try {
			Date d = sdf.parse(pGeburtsdatum);
			Calendar geburtsdatum = GregorianCalendar.getInstance();
			geburtsdatum.setTime(d);
			geburtsjahr = geburtsdatum.get(Calendar.YEAR);
		} catch (ParseException e) {
			return "Das Geburtsdatum kann nicht verarbeitet werden: '" + pGeburtsdatum + "'";
		}

		// Auswertung
		mResult.count(verband, geburtsjahr, geschlecht);

		// Wenn alles ok, dann keinen Fehler-Eintrag zurückliefern.
		return null;
	}

	String handleAustrittsdatum(final String pAustritt) {
		String retVal = null;
		if (pAustritt != null && !pAustritt.trim().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			try {
				Date parse = sdf.parse(pAustritt);
				if (parse.before(new Date())) {
					retVal = "Das Austrittsdatum liegt in der Vergangeheit. Datensatz wird nicht beruecks";
				}
			} catch (ParseException e) {
				retVal = "Das Austrittsdatum kann nicht geparsed werden: \"" + pAustritt+ "\"";
			}
		} else {
			// Wenn kein Austrittsdatum gegeben ist, alles OK
			retVal = null;
		}
		return retVal;
	}

	String createMessage(final String pLine, final String pErrorMessage) {
		final StringBuilder sb = new StringBuilder();

		sb.append(pLine);
		if (!pLine.endsWith(";")) {
			sb.append(";");
		}
		sb.append(pErrorMessage).append(";");
		return sb.toString();
	}
}

class OutputLog implements AutoCloseable {

	final static String LINESEP = System.getProperty("line.separator");
	final static Logger LOGGER = Logger.getLogger(OutputLog.class);

	final String mOutPath;
	BufferedWriter mOut;

	public OutputLog(final String pOutPath) {
		mOutPath = pOutPath;
	}

	public void write(final String pLine) throws IOException {
		if (mOutPath == null) {
			return;
		}

		if (mOut == null) {
			final File file = new File(mOutPath);
			mOut = new BufferedWriter(new FileWriter(file));
			LOGGER.info("Ausgabe-Datei: " + file.getAbsolutePath());
		}

		mOut.write(pLine);
		mOut.write(LINESEP);
	}

	@Override
	public void close() throws Exception {
		if (mOut == null) {
			return;
		}

		mOut.close();
	}

}
