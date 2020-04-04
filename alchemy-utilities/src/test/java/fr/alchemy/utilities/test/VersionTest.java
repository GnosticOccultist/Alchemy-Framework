package fr.alchemy.utilities.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.alchemy.utilities.Version;
import fr.alchemy.utilities.Version.DevelopmentStage;

/**
 * <code>VersionTest</code> is a test class concerning {@link Version} features.
 * 
 * @version 0.1.1
 * @since 0.1.1
 * 
 * @author GnosticOccultist
 */
public class VersionTest {
	
	@Test
	void testVersionCompare() {
		Version version1 = new Version(DevelopmentStage.ALPHA, "0.1.0");
		Version version2 = new Version(DevelopmentStage.ALPHA, "0.1.0.1");
		
		Assertions.assertEquals(1, version2.compareTo(version1));
		Assertions.assertEquals(-1, version1.compareTo(version2));
	}

	@Test
	void testVersionCompareQualifier() {
		Version version1 = new Version(DevelopmentStage.ALPHA, "0.1.0");
		Version version2 = new Version(DevelopmentStage.BETA, "0.1.0");
		
		Assertions.assertEquals(1, version2.compareTo(version1));
		Assertions.assertEquals(-1, version1.compareTo(version2));
	}
	
	@Test
	void testVersionComparePatch() {
		Version version1 = new Version(DevelopmentStage.ALPHA, "0.1.0");
		Version version2 = new Version(DevelopmentStage.ALPHA, "0.1");
		
		Assertions.assertEquals(0, version2.compareTo(version1));
	}
}
