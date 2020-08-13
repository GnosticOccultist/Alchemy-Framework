package fr.alchemy.utilities.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.alchemy.utilities.Instantiator;

/**
 * <code>InstantiatorTest</code> is a test class concerning {@link Instantiator} features.
 * 
 * @version 0.1.1
 * @since 0.1.1
 * 
 * @author GnosticOccultist
 */
public class InstantiatorTest {

	@Test
	void testFromClass() {
		DummyClass instance = Instantiator.fromClass(DummyClass.class);
		
		Assertions.assertNotNull(instance);
		
		DummyEnum value = Instantiator.fromClass(DummyEnum.class, "DUMMY_1");
		
		Assertions.assertNotNull(value);
		Assertions.assertEquals(DummyEnum.DUMMY_1, value);
	}
	
	@Test
	void testFromEnum() {
		DummyEnum value1 = Instantiator.fromClassAsEnum(DummyEnum.class, DummyEnum.DUMMY_1);
		
		Assertions.assertNotNull(value1);
		Assertions.assertEquals(DummyEnum.DUMMY_1, value1);
		
		DummyEnum value2 = Instantiator.fromClassAsEnum(DummyEnum.class, "DUMMY_2");
		
		Assertions.assertNotNull(value2);
		Assertions.assertEquals(DummyEnum.DUMMY_2, value2);
		
		DummyEnum value3 = Instantiator.fromClassAsEnum(DummyEnum.class, 2);
		
		Assertions.assertNotNull(value3);
		Assertions.assertEquals(DummyEnum.DUMMY_3, value3);
	}
	
	@Test
	void testInvokation() {
		DummyClass instance = new DummyClass();
		
		boolean success = Instantiator.invokeMethod("dummyMethod", instance, "Test");
		Assertions.assertTrue(success);
	}
	
	/**
	 * <code>DummyClass</code> represents a dummy class for testing purposes.
	 * 
	 * @author GnosticOccultist
	 */
	public static class DummyClass {
		
		/**
		 * A dummy protected constructor for testing instantiation using {@link Instantiator}.
		 */
		protected DummyClass() {}
		
		/**
		 * Dummy method for testing invokation using {@link Instantiator}.
		 * 
		 * @param arg An argument to check correct invokation of the method.
		 */
		public void dummyMethod(String arg) {}
	}
	
	/**
	 * <code>DummyEnum</code> enumerates dummy constants for testing purposes.
	 * 
	 * @author GnosticOccultist
	 */
	public enum DummyEnum {
		/**
		 * The first dummy enum.
		 */
		DUMMY_1,
		/**
		 * The second dummy enum.
		 */
		DUMMY_2,
		/**
		 * The third dummy enum.
		 */
		DUMMY_3;
	}
}
