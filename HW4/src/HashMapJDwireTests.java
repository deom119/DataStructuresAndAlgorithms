import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * HashMapJDwireTests
 *
 * @author Joshua Dwire
 * @version 1.7
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HashMapJDwireTests {

    private HashMap<HackedString, String> directory;
    private static final int TIMEOUT = 200;

    @Before
    public void setUp() {
        directory = new HashMap<>();
    }

    @Test(timeout = TIMEOUT)
    public void test01SizeParam() {
        HashMap<HackedString, String> map = new HashMap<>(15);
        assertEquals(15, map.getTable().length);
    }

    @Test(timeout = TIMEOUT)
    public void test02put() {
        directory.put(new HackedString("Item 1", 8), "Value 1a");
        directory.put(new HackedString("Item 2", 6), "Value 2a");
        directory.put(new HackedString("Item 3", 4), "Value 3a");

        String prof = directory.put(new HackedString("Item 2", 6), "Value 2b");
        assertEquals("Value 2a", prof);

        directory.put(new HackedString("Item 4", 2), "Value 4a");
        assertNull(directory.put(new HackedString("Item 5", 0), "Value 5a"));

        String oldLang = directory.put(new HackedString("Item 5", 0), "Value 5b");
        assertEquals("Value 5a", oldLang);

        assertEquals(5, directory.size());

        MapEntry<String, String>[] expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        new MapEntry<>("Item 5", "Value 5b"),
                        null,
                        new MapEntry<>("Item 4", "Value 4a"),
                        null,
                        new MapEntry<>("Item 3", "Value 3a"),
                        null,
                        new MapEntry<>("Item 2", "Value 2b"),
                        null,
                        new MapEntry<>("Item 1", "Value 1a"),
                        null,
                        null,
                        null,
                        null
                };

        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testPHPUsefulness() {
        assertTrue("Java is broken", true);
        // https://blog.codinghorror.com/php-sucks-but-it-doesnt-matter/
        // See testput in TA tests...
        // Disclaimer: I'm a professional PHP developer
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void test03putNullKey() {
        directory.put(null, "test");
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void test04putNullValue() {
        directory.put(new HackedString("test", 9), null);
    }

    @Test(timeout = TIMEOUT)
    public void test05putWithProbe() {
        directory.put(new HackedString("Item 1", 4), "Value 1");
        directory.put(new HackedString("Item 2", 4), "Value 2");
        directory.put(new HackedString("Item 3", 4), "Value 3");

        assertEquals(3, directory.size());

        MapEntry<String, String>[] expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        null,
                        null,
                        null,
                        null,
                        new MapEntry<>("Item 1", "Value 1"),
                        new MapEntry<>("Item 2", "Value 2"),
                        new MapEntry<>("Item 3", "Value 3"),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                };

        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void test06putWithProbeWraparound() {
        directory.put(new HackedString("Item 1", 7), "Value 1");
        directory.put(new HackedString("Item 2", 7), "Value 2");
        directory.put(new HackedString("Item 3", 7), "Value 3");
        directory.put(new HackedString("Item 4", 7), "Value 4");

        assertEquals(4, directory.size());

        MapEntry<String, String>[] expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        new MapEntry<>("Item 1", "Value 1"),
                        new MapEntry<>("Item 2", "Value 2"),
                        new MapEntry<>("Item 3", "Value 3"),
                        new MapEntry<>("Item 4", "Value 4"),
                        null,
                        null
                };

        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void test07putWithProbeHop() {
        directory.put(new HackedString("Item 1", 3), "Value 1");
        directory.put(new HackedString("Item 2", 3), "Value 2");
        directory.put(new HackedString("Hashcode5", 5), "asdf");
        directory.put(new HackedString("Item 3", 3), "Value 3");
        directory.put(new HackedString("Item 4", 3), "Value 4");

        assertEquals(5, directory.size());

        MapEntry<String, String>[] expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        null,
                        null,
                        null,
                        new MapEntry<>("Item 1", "Value 1"),
                        new MapEntry<>("Item 2", "Value 2"),
                        new MapEntry<>("Hashcode5", "asdf"),
                        new MapEntry<>("Item 3", "Value 3"),
                        new MapEntry<>("Item 4", "Value 4"),
                        null,
                        null,
                        null,
                        null,
                        null
                };

        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void test08putWithProbeHopRemove() {
        directory.put(new HackedString("Item 1", 3), "Value 1");
        directory.put(new HackedString("Item 2", 3), "Value 2");
        directory.put(new HackedString("Hashcode5", 5), "asdf");
        directory.put(new HackedString("Item 3", 3), "Value 3");

        directory.remove(new HackedString("Hashcode5", 5));
        directory.put(new HackedString("Item 4", 3), "Value 4");

        assertEquals(4, directory.size());

        MapEntry<String, String>[] expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        null,
                        null,
                        null,
                        new MapEntry<>("Item 1", "Value 1"),
                        new MapEntry<>("Item 2", "Value 2"),
                        new MapEntry<>("Item 4", "Value 4"),
                        new MapEntry<>("Item 3", "Value 3"),
                        null,
                        null
                };

        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void test09putRegrow() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");
        directory.put(new HackedString("Hash6", 6), "e");
        directory.put(new HackedString("Hash3", 3), "f");
        directory.put(new HackedString("Hash5", 5), "g");

        MapEntry<HackedString, String>[] expected =
                (MapEntry<HackedString, String>[]) new MapEntry[]{
                        null,
                        null,
                        null,
                        new MapEntry<>("Hash3", "f"),
                        new MapEntry<>("Hash4", "c"),
                        new MapEntry<>("Hash5", "g"),
                        new MapEntry<>("Hash6", "e"),
                        new MapEntry<>("Hash7", "b"),
                        new MapEntry<>("Hash8", "a"),
                        null,
                        new MapEntry<>("Hash10", "d"),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                };

        assertArrayEquals(expected, directory.getTable());
        assertEquals(7, directory.size());
    }

    @Test(timeout = TIMEOUT)
    public void test10LoadFactorComparison() {
        directory.resizeBackingTable(100);

        for (int i = 1; i < 68; i++) {
            directory.put(new HackedString("Item" + i, i), "Value" + i);
        }

        assertEquals(
                "You regrew too early. " +
                        "Load factor greater than, not greater than or equal to.",
                100, directory.getTable().length);

        directory.put(new HackedString("Item68", 68), "Value68");
        assertEquals("Regrow didn't happen.", 201, directory.getTable().length);
    }

    @Test(timeout = TIMEOUT)
    public void test11ForceResize() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");

        directory.resizeBackingTable(11);

        MapEntry<HackedString, String>[] expected =
                (MapEntry<HackedString, String>[]) new MapEntry[]{
                        null,
                        null,
                        null,
                        null,
                        new MapEntry<>("Hash4", "c"),
                        null,
                        null,
                        new MapEntry<>("Hash7", "b"),
                        new MapEntry<>("Hash8", "a"),
                        null,
                        null
                };

        assertArrayEquals(expected, directory.getTable());
        assertEquals(3, directory.size());

        directory.resizeBackingTable(3);

        expected =
                (MapEntry<HackedString, String>[]) new MapEntry[]{
                        new MapEntry<>("Hash8", "a"),
                        new MapEntry<>("Hash4", "c"),
                        new MapEntry<>("Hash7", "b"),
                };

        assertEquals("Looks like you regrew the table", 3, directory.getTable().length);
        assertArrayEquals(expected, directory.getTable());
        assertEquals(3, directory.size());
    }

    @Test(timeout = TIMEOUT)
    public void test12Remove() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        assertEquals("a", directory.remove(new HackedString("Hash8", 8)));

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        assertNotNull(actuals[8]);
        assertTrue(actuals[8].isRemoved());
        assertEquals(3, directory.size());
    }

    @Test(timeout = TIMEOUT)
    public void test13RemoveWraparound() {
        directory.put(new HackedString("Item 1", 7), "Value 1");
        directory.put(new HackedString("Item 2", 7), "Value 2");
        directory.put(new HackedString("Item 3", 7), "Value 3");
        directory.put(new HackedString("Item 4", 7), "Value 4");

        assertEquals("Value 3", directory.remove(new HackedString("Item 3", 7)));

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        assertNotNull(actuals[0]);
        assertTrue(actuals[0].isRemoved());
        assertEquals(3, directory.size());
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void test14RemoveEfficient() {
        directory.put(new HackedString("Item 1", 2), "Value 1");
        directory.put(new HackedString("Item 2", 2), "Value 2");
        directory.put(new HackedString("Item 3", 2), "Value 3");

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        actuals[6] = new MapEntry<>(new HackedString("Item 4", 2), "Value 4");

        // This shouldn't get found, since it's after a null
        directory.remove(new HackedString("Item 4", 2));
    }

    @Test(timeout = TIMEOUT)
    public void test15RemoveResize() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        assertEquals("a", directory.remove(new HackedString("Hash8", 8)));

        directory.resizeBackingTable(3);

        MapEntry<HackedString, String>[] expected =
                (MapEntry<HackedString, String>[]) new MapEntry[]{
                        new MapEntry<>("Hash7", "b"),
                        new MapEntry<>("Hash10", "d"),
                        new MapEntry<>("Hash4", "c"),
                };

        assertArrayEquals(expected, directory.getTable());
        assertEquals(3, directory.size());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void test16RemoveNull() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        directory.remove(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void test17RemoveNonexistent() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        directory.remove(new HackedString("Hash8 (again)", 8));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void test18RemoveNonexistentFull() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");
        directory.resizeBackingTable(4);

        directory.remove(new HackedString("Hash8 (again)", 8));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void test19RemoveTwice() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        assertEquals("a", directory.remove(new HackedString("Hash8", 8)));

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        assertNotNull(actuals[8]);
        assertTrue(actuals[8].isRemoved());
        assertEquals(3, directory.size());

        directory.remove(new HackedString("Hash8", 8));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void test20RemoveEmpty() {
        // Just to make sure there's not a NullReferenceException
        directory.remove(new HackedString("Hash8", 8));
    }

    @Test(timeout = TIMEOUT)
    public void test21RemoveputDuplicate() {
        directory.resizeBackingTable(7);


        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash1", 1), "b");
        directory.put(new HackedString("Hash15", 15), "c");

        assertEquals("a", directory.remove(new HackedString("Hash8", 8)));
        assertEquals(2, directory.size());

        MapEntry<String, String>[] expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        null,
                        removed(new MapEntry<>("Hash8", "a")),
                        new MapEntry<>("Hash1", "b"),
                        new MapEntry<>("Hash15", "c"),
                        null,
                        null,
                        null,
                };
        assertArrayEquals(expected, directory.getTable());

        assertEquals("c", directory.put(new HackedString("Hash15", 15), "d"));
        assertEquals(2, directory.size());

        expected = (MapEntry<String, String>[]) new MapEntry[]{
                null,
                removed(new MapEntry<>("Hash8", "a")),
                new MapEntry<>("Hash1", "b"),
                new MapEntry<>("Hash15", "d"),
                null,
                null,
                null,
        };
        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void test22RemoveReput() {
        directory.resizeBackingTable(7);


        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash1", 1), "b");
        directory.put(new HackedString("Hash15", 15), "c");

        assertEquals("b", directory.remove(new HackedString("Hash1", 1)));
        assertEquals(2, directory.size());

        MapEntry<String, String>[] expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        null,
                        new MapEntry<>("Hash8", "a"),
                        removed(new MapEntry<>("Hash1", "b")),
                        new MapEntry<>("Hash15", "c"),
                        null,
                        null,
                        null,
                };
        assertArrayEquals(expected, directory.getTable());

        assertNull("Replacing a duplicate but removed item should return null.", directory.put(new HackedString("Hash1", 1), "d"));
        assertEquals("Incorrect size after re-puting duplicate.", 3, directory.size());

        expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        null,
                        new MapEntry<>("Hash8", "a"),
                        new MapEntry<>("Hash1", "d"),
                        new MapEntry<>("Hash15", "c"),
                        null,
                        null,
                        null,
                };
        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void test23RemoveRemoveReput() {
        directory.resizeBackingTable(7);


        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash1", 1), "b");
        directory.put(new HackedString("Hash15", 15), "c");
        directory.put(new HackedString("Hash22", 22), "d");

        assertEquals("b", directory.remove(new HackedString("Hash1", 1)));
        assertEquals(3, directory.size());

        assertEquals("c", directory.remove(new HackedString("Hash15", 1)));
        assertEquals(2, directory.size());

        MapEntry<String, String>[] expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        null,
                        new MapEntry<>("Hash8", "a"),
                        removed(new MapEntry<>("Hash1", "b")),
                        removed(new MapEntry<>("Hash15", "c")),
                        new MapEntry<>("Hash22", "d"),
                        null,
                        null,
                };
        assertArrayEquals(expected, directory.getTable());

        assertNull("Replacing a duplicate but removed item should return null.", directory.put(new HackedString("Hash15", 15), "e"));
        assertEquals("Incorrect size after re-puting duplicate.", 3, directory.size());

        expected =
                (MapEntry<String, String>[]) new MapEntry[]{
                        null,
                        new MapEntry<>("Hash8", "a"),
                        new MapEntry<>("Hash15", "e"),
                        removed(new MapEntry<>("Hash15", "c")),
                        new MapEntry<>("Hash22", "d"),
                        null,
                        null,
                };
        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void test24Get() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        assertEquals("a", directory.get(new HackedString("Hash8", 8)));

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        assertFalse(actuals[8].isRemoved());
        assertEquals(4, directory.size());
    }

    @Test(timeout = TIMEOUT)
    public void test25GetWraparound() {
        directory.put(new HackedString("Item 1", 7), "Value 1");
        directory.put(new HackedString("Item 2", 7), "Value 2");
        directory.put(new HackedString("Item 3", 7), "Value 3");
        directory.put(new HackedString("Item 4", 7), "Value 4");

        assertEquals("Value 3", directory.get(new HackedString("Item 3", 7)));

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        assertNotNull(actuals[0]);
        assertFalse(actuals[0].isRemoved());
        assertEquals(4, directory.size());
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void test26GetEfficient() {
        directory.put(new HackedString("Item 1", 2), "Value 1");
        directory.put(new HackedString("Item 2", 2), "Value 2");
        directory.put(new HackedString("Item 3", 2), "Value 3");

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        actuals[6] = new MapEntry<>(new HackedString("Item 4", 2), "Value 4");

        // This shouldn't get found, since it's after a null
        directory.get(new HackedString("Item 4", 2));
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void test27GetNull() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        directory.get(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void test28GetNonexistent() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        directory.get(new HackedString("Hash8 (again)", 8));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void test29GetNonexistentFull() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");
        directory.resizeBackingTable(4);

        directory.get(new HackedString("Hash8 (again)", 8));
    }

    @Test(timeout = TIMEOUT)
    public void test30GetTwice() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        assertEquals("a", directory.get(new HackedString("Hash8", 8)));

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        assertNotNull(actuals[8]);
        assertFalse(actuals[8].isRemoved());
        assertEquals(4, directory.size());

        assertEquals("a", directory.get(new HackedString("Hash8", 8)));
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void test31GetEmpty() {
        // Just to make sure there's not a NullReferenceException
        directory.get(new HackedString("Hash8", 8));
    }

    @Test(timeout = TIMEOUT)
    public void test32Contains() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        assertTrue(directory.containsKey(new HackedString("Hash8", 8)));

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        assertFalse(actuals[8].isRemoved());
        assertEquals(4, directory.size());
    }

    @Test(timeout = TIMEOUT)
    public void test33ContainsWraparound() {
        directory.put(new HackedString("Item 1", 7), "Value 1");
        directory.put(new HackedString("Item 2", 7), "Value 2");
        directory.put(new HackedString("Item 3", 7), "Value 3");
        directory.put(new HackedString("Item 4", 7), "Value 4");

        assertTrue(directory.containsKey(new HackedString("Item 3", 7)));

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        assertNotNull(actuals[0]);
        assertFalse(actuals[0].isRemoved());
        assertEquals(4, directory.size());
    }

    @Test(timeout = TIMEOUT)
    public void test34ContainsEfficient() {
        directory.put(new HackedString("Item 1", 2), "Value 1");
        directory.put(new HackedString("Item 2", 2), "Value 2");
        directory.put(new HackedString("Item 3", 2), "Value 3");

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        actuals[6] = new MapEntry<>(new HackedString("Item 4", 2), "Value 4");

        // This shouldn't get found, since it's after a null
        assertFalse(
                "Make sure you stop after hitting a null item.",
                directory.containsKey(new HackedString("Item 4", 2))
        );
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void test35ContainsNull() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        directory.containsKey(null);
    }

    @Test(timeout = TIMEOUT)
    public void test36ContainsNonexistent() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        assertFalse(directory.containsKey(new HackedString("Hash8 (again)", 8)));
    }

    @Test(timeout = TIMEOUT)
    public void test37ContainsNonexistentFull() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");
        directory.resizeBackingTable(4);

        assertFalse(directory.containsKey(new HackedString("Hash8 (again)", 8)));
    }

    @Test(timeout = TIMEOUT)
    public void test38ContainsTwice() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        assertTrue(directory.containsKey(new HackedString("Hash8", 8)));

        MapEntry<HackedString, String>[] actuals = directory.getTable();
        assertNotNull(actuals[8]);
        assertFalse(actuals[8].isRemoved());
        assertEquals(4, directory.size());

        assertTrue(directory.containsKey(new HackedString("Hash8", 8)));
    }

    @Test(timeout = TIMEOUT)
    public void test39ContainsEmpty() {
        // Just to make sure there's not a NullReferenceException
        assertFalse(directory.containsKey(new HackedString("Hash8", 8)));
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void test40ResizeTo0() {
        directory.resizeBackingTable(0);
    }

    @Test(timeout = TIMEOUT)
    public void test41ResizeTo1() {
        directory.put(new HackedString("Hash8", 8), "a");

        directory.resizeBackingTable(1);

        MapEntry<String, String>[]expected = (MapEntry<String, String>[]) new MapEntry[]{
                new MapEntry<>("Hash8", "a")
        };
        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void test42ResizeTooSmall() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");

        directory.resizeBackingTable(2);
    }

    @Test(timeout = TIMEOUT)
    public void test43Clear() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        assertEquals(4, directory.size());
        directory.resizeBackingTable(HashMap.INITIAL_CAPACITY * 2);
        assertEquals(HashMap.INITIAL_CAPACITY * 2, directory.getTable().length);

        directory.clear();

        MapEntry<String, String>[] expected =
                (MapEntry<String, String>[]) new MapEntry[HashMap.INITIAL_CAPACITY];

        assertEquals(0, directory.size());
        assertEquals("Size was not reset to default", HashMap.INITIAL_CAPACITY, directory.getTable().length);
        assertArrayEquals(expected, directory.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void test44KeySet() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");

        Set<HackedString> keys = directory.keySet();
        assertEquals(4, keys.size());

        Object[] keysArr = keys.toArray();
        Arrays.sort(keysArr);

        assertArrayEquals(
                new HackedString[]{
                        new HackedString("Hash10", 8),
                        new HackedString("Hash4", 7),
                        new HackedString("Hash7", 4),
                        new HackedString("Hash8", 10)
                },
                keysArr
        );
    }

    @Test(timeout = TIMEOUT)
    public void test45KeySetEmpty() {
        assertArrayEquals(new HackedString[]{}, directory.keySet().toArray());
    }

    @Test(timeout = TIMEOUT)
    public void test46Values() {
        directory.put(new HackedString("Hash8", 8), "a");
        directory.put(new HackedString("Hash7", 7), "b");
        directory.put(new HackedString("Hash4", 4), "c");
        directory.put(new HackedString("Hash10", 10), "d");
        directory.put(new HackedString("Hash12", 10), "a");

        List<String> values = directory.values();
        assertEquals(5, values.size());

        Object[] valuesArr = values.toArray();
        Arrays.sort(valuesArr);

        assertArrayEquals(
                new String[]{
                        "a",
                        "a",
                        "b",
                        "c",
                        "d"
                },
                valuesArr
        );
    }

    @Test(timeout = TIMEOUT)
    public void test47KeySetEmpty() {
        assertArrayEquals(new String[]{}, directory.values().toArray());
    }

    private MapEntry removed(MapEntry entry){
        entry.setRemoved(true);
        return entry;
    }

    private static class HackedString implements Comparable<HackedString> {
        private String s;
        private int hashcode;

        /**
         * Create a wrapper object around a String object, for the purposes
         * of controlling the hash code.
         *
         * @param s        string to store in this object
         * @param hashcode the hashcode to return
         */
        public HackedString(String s, int hashcode) {
            this.s = s;
            this.hashcode = hashcode;
        }

        @Override
        public int hashCode() {
            return this.hashcode;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof HackedString) {
                return s.equals(((HackedString) o).s);
            }
            if (o instanceof String) {
                return s.equals(o);
            }
            return false;
        }

        /**
         * Stop highlighting "public" in read
         *
         * @param o The object to compare
         * @return Same thing a compareTo normally does
         */
        public int compareTo(HackedString o) {
            return s.compareTo(o.toString());
        }

        @Override
        public String toString() {
            return s;
        }
    }
}