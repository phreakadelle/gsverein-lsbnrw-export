package info.watermeyer.gsverein;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class GSVereinExporterConfig {

	private static final String SEPARATOR = ",";

	private final static Logger LOGGER = Logger.getLogger(GSVereinExporterConfig.class);

	final Properties mProps;
	final Map<String, String> mMapping;

	public GSVereinExporterConfig() {
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

	void addMapping(final String pVerband, final String pAbteilung) {
		LOGGER.debug("Abteilung '" + pAbteilung + "' wird dem Verband '" + pVerband + "' zugeordnet");
		mMapping.put(pAbteilung.trim(), pVerband);
	}

	boolean moreAbteilungenDefined(final String pAbteilung) {
		return pAbteilung.contains(SEPARATOR);
	}

	public String getVerband(String pAbteilung) {
		return mMapping.get(pAbteilung.trim());
	}
}
