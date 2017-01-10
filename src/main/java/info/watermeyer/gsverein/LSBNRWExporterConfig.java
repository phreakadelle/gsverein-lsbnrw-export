package info.watermeyer.gsverein;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class LSBNRWExporterConfig {

	private static final String SEPARATOR = ",";

	private final static Logger LOGGER = Logger.getLogger(LSBNRWExporterConfig.class);

	final Properties mProps;
	final Map<String, String> mMapping;

	public LSBNRWExporterConfig() {
		mProps = new Properties();
		mMapping = new HashMap<String, String>();
	}

	public void load(InputStream inStream) throws IOException {
		mProps.load(inStream);

		Enumeration<Object> keys = mProps.keys();
		while (keys.hasMoreElements()) {
			String verband = keys.nextElement().toString();
			String abteilungen = mProps.getProperty(verband);
			if (moreAbteilungenDefined(abteilungen)) {
				final String[] split = abteilungen.split(SEPARATOR);
				for (String current : split) {
					addMapping(verband, current);
				}
			} else {
				addMapping(verband, abteilungen);
			}

		}
	}

	private void addMapping(String verband, String current) {
		LOGGER.debug("Abteilung '" + current + "' wird dem Verband '" + verband + "' zugeordnet");
		mMapping.put(current.trim(), verband);
	}

	private boolean moreAbteilungenDefined(String abteilungen) {
		return abteilungen.contains(SEPARATOR);
	}

	public String getVerband(String pAbteilung) {
		return mMapping.get(pAbteilung.trim());
	}
}
