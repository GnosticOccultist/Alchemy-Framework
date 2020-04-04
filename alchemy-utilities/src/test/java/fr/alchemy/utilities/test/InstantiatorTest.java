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
}
