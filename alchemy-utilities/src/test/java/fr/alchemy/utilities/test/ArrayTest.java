package fr.alchemy.utilities.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.alchemy.utilities.collections.array.Array;

public class ArrayTest {

	@Test
	void testFastArrayAdd() {
		Array<Integer> array = Array.ofType(Integer.class);

		for (int i = 0; i < 21; ++i) {
			array.add(i);
		}

		array.add(12, 22);

		System.out.println(array);

		array.add(25, 25);

		System.out.println(array);
		array.add(22, 23);

		System.out.println(array);

		Assertions.assertEquals(22, array.get(12));
		Assertions.assertEquals(23, array.get(22));
		Assertions.assertEquals(25, array.get(25));
	}

	@Test
	void testFastArrayTrim() {
		Array<Integer> array = Array.ofType(Integer.class);

		for (int i = 0; i < 26; ++i) {
			array.add(i);
		}

		array.trim(10);

		Assertions.assertEquals(10, array.size());
		Assertions.assertEquals(9, array.last());
		Assertions.assertEquals(0, array.first());
	}
}
