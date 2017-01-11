package info.watermeyer.gsverein;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

public class GSVereinExporterConfigTest extends TestCase {

	@Test
	public void testInitConfig() throws FileNotFoundException, IOException {
		GSVereinExporterConfig config = new GSVereinExporterConfig();
		config.load(new FileInputStream(new File("src/test/resources/config.properties")));
	}
}
