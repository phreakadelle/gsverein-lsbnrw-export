package info.watermeyer.gsverein;

import java.util.List;

import junit.framework.TestCase;

public class GSVereinExporterTest extends TestCase {

	public void testParse() {
		GSVereinExporter v = new GSVereinExporter();
		try {
			List<String> validate = v.validate("src/test/resources/config.properties", "src/test/resources/ssv2016.csv",
					"target/out2.csv");
			for (String current : validate) {
				System.out.println(current);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
