package info.watermeyer.gsverein;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

public class LSBNRWExporterConfigTest extends TestCase {

	@Test
	public void testInitConfig() throws FileNotFoundException, IOException {
		LSBNRWExporterConfig config = new LSBNRWExporterConfig();
		config.load(new FileInputStream(new File("src/test/resources/config.properties")));
	}
}
