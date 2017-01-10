package info.watermeyer.gsverein;

import java.util.List;

import junit.framework.TestCase;

public class LSBNRWExporterTest extends TestCase {

	public void testParse() {
		LSBNRWExporter v = new LSBNRWExporter();
		try {
			List<String> validate = v.validate("src/test/resources/config.properties", "src/test/resources/test.csv",
					"target/out.csv");
			for (String current : validate) {
				System.out.println(current);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}