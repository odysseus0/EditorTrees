package editortrees;

import static org.junit.Assert.*;

import org.junit.Test;

public class Milestone1TestThatMakesSense {

	// Below are tests created by George Zhang to test smaller chuncks as we proceed
	
	// Here we assume you have implemented constructor EditTree() and height() correctly.
	// Note: Every following test will and only will assume the correctness of the previous tests.
	
	@Test
	public void testAddToRightMost() {
		EditTree t = new EditTree();
		t.add('a');
		assertEquals("a", t.toString());
		t.add('b');
		assertEquals("ab", t.toString());
		t.add('c');
		assertEquals("abc", t.toString());
	}
	
	@Test
	public void testSize() {
		EditTree t = new EditTree();
		assertEquals(0, t.size());
		t.add('a');
		assertEquals(1, t.size());
		t.add('b');
		assertEquals(2, t.size());
		t.add('c');
		assertEquals(3, t.size());
	}
	
	@Test
	public void testEmpty() {
		EditTree t = new EditTree();
		assertEquals("", t.toString());
		assertEquals(-1, t.height());
	}
	
	@Test
	public void testSingleLeftRotation() {
		EditTree t = new EditTree();
		t.add('a');
		t.add('b');
		t.add('c');
		assertEquals("abc", t.toString());
		assertEquals(true, t.isBalanced());
		t.add('d');
		t.add('e');
		t.add('f');
		assertEquals("abcdef", t.toString());
		assertEquals(true, t.isBalanced());
	}
}
