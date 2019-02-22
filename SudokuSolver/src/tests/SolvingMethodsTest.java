package tests;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main_package.SolvingMethods;

public class SolvingMethodsTest {

	SolvingMethods sm;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sm = new SolvingMethods();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void singlePositionTest() {
		sm.putValueToArrays('7', 6);
		sm.putValueToArrays('7', 37);
		sm.putValueToArrays('7', 50);
		sm.putValueToArrays('7', 62);
		sm.putValueToArrays('8', 17);
		sm.putValueToArrays('8', 23);
		sm.putValueToArrays('8', 31);
		sm.putValueToArrays('8', 45);
		sm.putValueToArrays('8', 78);
		sm.setPossibleValues(sm.getCopyRowValues());
		sm.singlePosition();
		char[][] rowValues = sm.getCopyRowValues();
		assertEquals('7', rowValues[3][7]);
		sm.singlePosition();
		rowValues = sm.getCopyRowValues();
		assertEquals('8', rowValues[4][7]);
	}
	
	@Test
	public void singleCandidateTest() {
		sm.putValueToArrays('4', 2);
		sm.putValueToArrays('6', 5);
		sm.putValueToArrays('2', 7);
		sm.putValueToArrays('7', 11);
		sm.putValueToArrays('8', 12);
		sm.putValueToArrays('9', 15);
		sm.putValueToArrays('1', 16);
		sm.putValueToArrays('3', 24);
		sm.putValueToArrays('8', 26);
		sm.putValueToArrays('1', 28);
		sm.putValueToArrays('8', 29);
		sm.putValueToArrays('3', 30);
		sm.putValueToArrays('2', 33);
		sm.putValueToArrays('3', 36);
		sm.putValueToArrays('7', 39);
		sm.putValueToArrays('8', 40);
		sm.putValueToArrays('9', 41);
		sm.putValueToArrays('1', 44);
		sm.putValueToArrays('9', 47);
		sm.putValueToArrays('1', 50);
		sm.putValueToArrays('6', 52);
		sm.putValueToArrays('8', 54);
		sm.putValueToArrays('3', 56);
		sm.putValueToArrays('5', 60);
		sm.putValueToArrays('4', 64);
		sm.putValueToArrays('5', 65);
		sm.putValueToArrays('3', 68);
		sm.putValueToArrays('6', 69);
		sm.putValueToArrays('2', 73);
		sm.putValueToArrays('6', 74);
		sm.putValueToArrays('5', 75);
		sm.putValueToArrays('1', 78);
		sm.setPossibleValues(sm.getCopyRowValues());
		sm.singleCandidate();
		char[][] rowValues = sm.getCopyRowValues();
		assertEquals('7', rowValues[0][6]);
		assertEquals('2', rowValues[4][2]);
		assertEquals('4', rowValues[4][6]);
	}
	
	@Test
	public void candidateLinesTest() {
		sm.putValueToArrays('1', 2);
		sm.putValueToArrays('9', 3);
		sm.putValueToArrays('5', 4);
		sm.putValueToArrays('7', 5);
		sm.putValueToArrays('6', 7);
		sm.putValueToArrays('3', 8);
		sm.putValueToArrays('8', 12);
		sm.putValueToArrays('6', 14);
		sm.putValueToArrays('7', 16);
		sm.putValueToArrays('7', 18);
		sm.putValueToArrays('6', 19);
		sm.putValueToArrays('9', 20);
		sm.putValueToArrays('1', 21);
		sm.putValueToArrays('3', 22);
		sm.putValueToArrays('8', 24);
		sm.putValueToArrays('5', 26);
		sm.putValueToArrays('7', 29);
		sm.putValueToArrays('2', 30);
		sm.putValueToArrays('6', 31);
		sm.putValueToArrays('1', 32);
		sm.putValueToArrays('3', 33);
		sm.putValueToArrays('5', 34);
		sm.putValueToArrays('3', 36);
		sm.putValueToArrays('1', 37);
		sm.putValueToArrays('2', 38);
		sm.putValueToArrays('4', 39);
		sm.putValueToArrays('9', 40);
		sm.putValueToArrays('5', 41);
		sm.putValueToArrays('7', 42);
		sm.putValueToArrays('8', 43);
		sm.putValueToArrays('6', 44);
		sm.putValueToArrays('5', 46);
		sm.putValueToArrays('6', 47);
		sm.putValueToArrays('3', 48);
		sm.putValueToArrays('7', 49);
		sm.putValueToArrays('8', 50);
		sm.putValueToArrays('1', 54);
		sm.putValueToArrays('8', 56);
		sm.putValueToArrays('6', 57);
		sm.putValueToArrays('9', 59);
		sm.putValueToArrays('5', 60);
		sm.putValueToArrays('7', 62);
		sm.putValueToArrays('9', 64);
		sm.putValueToArrays('7', 66);
		sm.putValueToArrays('1', 67);
		sm.putValueToArrays('6', 69);
		sm.putValueToArrays('8', 71);
		sm.putValueToArrays('6', 72);
		sm.putValueToArrays('7', 73);
		sm.putValueToArrays('4', 74);
		sm.putValueToArrays('5', 75);
		sm.putValueToArrays('8', 76);
		sm.putValueToArrays('3', 77);
		sm.setPossibleValues(sm.getCopyRowValues());
		sm.candidateLine();
		LinkedHashMap<Integer, List<Character>> possibleValues = sm.getPossibleValues();
		assertEquals(1, possibleValues.get(25).size());
		assertEquals(3, possibleValues.get(52).size());
	}
	
	@Test
	public void doublePairsTest() {
		sm.putValueToArrays('9', 0);
		sm.putValueToArrays('3', 1);
		sm.putValueToArrays('4', 2);
		sm.putValueToArrays('6', 4);
		sm.putValueToArrays('5', 7);
		sm.putValueToArrays('6', 11);
		sm.putValueToArrays('4', 14);
		sm.putValueToArrays('9', 15);
		sm.putValueToArrays('2', 16);
		sm.putValueToArrays('3', 17);
		sm.putValueToArrays('8', 20);
		sm.putValueToArrays('9', 21);
		sm.putValueToArrays('4', 25);
		sm.putValueToArrays('6', 26);
		sm.putValueToArrays('8', 27);
		sm.putValueToArrays('5', 30);
		sm.putValueToArrays('4', 31);
		sm.putValueToArrays('6', 32);
		sm.putValueToArrays('7', 35);
		sm.putValueToArrays('6', 36);
		sm.putValueToArrays('1', 40);
		sm.putValueToArrays('5', 44);
		sm.putValueToArrays('5', 45);
		sm.putValueToArrays('3', 48);
		sm.putValueToArrays('9', 49);
		sm.putValueToArrays('6', 52);
		sm.putValueToArrays('2', 53);
		sm.putValueToArrays('3', 54);
		sm.putValueToArrays('6', 55);
		sm.putValueToArrays('4', 57);
		sm.putValueToArrays('1', 59);
		sm.putValueToArrays('2', 60);
		sm.putValueToArrays('7', 61);
		sm.putValueToArrays('4', 63);
		sm.putValueToArrays('7', 64);
		sm.putValueToArrays('6', 66);
		sm.putValueToArrays('5', 69);
		sm.putValueToArrays('8', 73);
		sm.putValueToArrays('6', 78);
		sm.putValueToArrays('3', 79);
		sm.putValueToArrays('4', 80);
		sm.setPossibleValues(sm.getCopyRowValues());
		LinkedHashMap<Integer, List<Character>> possibleValues = sm.getPossibleValues();
		assertEquals(1, possibleValues.get(75));
	}
}
