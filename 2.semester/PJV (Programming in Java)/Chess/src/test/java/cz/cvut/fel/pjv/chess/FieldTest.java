/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.chess;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author semenvol
 */
public class FieldTest {

    private Field f;
    private int xFrom, yFrom, xTo, yTo, pieceColor, fieldColor, index;

    public FieldTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        xFrom = 300;
        yFrom = 400;
        xTo = 350;
        yTo = 450;
        pieceColor = 2;
        fieldColor = 0;
        index = 30;
        f = new Field(xFrom, yFrom, xTo, yTo, pieceColor, fieldColor, index);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPieceColor method, of class Field.
     */
    @Test
    public void testGetPieceColor() {
        System.out.println("getPieceColor");
        int expResult = pieceColor;
        int result = f.getPieceColor();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPieceColor method, of class Field.
     */
    @Test
    public void testSetPieceColor() {
        System.out.println("setPieceColor");
        int expResult = this.pieceColor;
        int result = f.getPieceColor();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFieldColor method, of class Field.
     */
    @Test
    public void testGetFieldColor() {
        System.out.println("getFieldColor");
        int expResult = fieldColor;
        int result = f.getFieldColor();
        assertEquals(expResult, result);
    }

    /**
     * Test of getX_from method, of class Field.
     */
    @Test
    public void testGetX_from() {
        System.out.println("getX_from");
        int expResult = xFrom;
        int result = f.getX_from();
        assertEquals(expResult, result);
    }

    /**
     * Test of getY_from method, of class Field.
     */
    @Test
    public void testGetY_from() {
        System.out.println("getY_from");
        int expResult = yFrom;
        int result = f.getY_from();
        assertEquals(expResult, result);
    }

    /**
     * Test of getX_to method, of class Field.
     */
    @Test
    public void testGetX_to() {
        System.out.println("getX_to");
        int expResult = xTo;
        int result = f.getX_to();
        assertEquals(expResult, result);
    }

    /**
     * Test of getY_to method, of class Field.
     */
    @Test
    public void testGetY_to() {
        System.out.println("getY_to");
        int expResult = yTo;
        int result = f.getY_to();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPiece method, of class Field.
     */
    @Test
    public void testGetPiece() {
        System.out.println("getPiece");
        String expResult = null;
        String result = f.getPiece();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPiece method, of class Field.
     */
    @Test
    public void testSetPiece() {
        System.out.println("setPiece");
        String piece = "nb";
        f.setPiece(piece);
        assertEquals(f.getPiece(), piece);
    }

    /**
     * Test of getIsFree method, of class Field.
     */
    @Test
    public void testGetIsFree() {
        System.out.println("getIsFree");
        boolean expResult = true;
        boolean result = f.getIsFree();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIsFree method, of class Field.
     */
    @Test
    public void testSetIsFree() {
        System.out.println("setIsFree");
        boolean b = false;
        f.setIsFree(b);
        assertEquals(b, f.getIsFree());
    }

    /**
     * Test of getIsSelected method, of class Field.
     */
    @Test
    public void testGetIsSelected() {
        System.out.println("getIsSelected");
        boolean expResult = false;
        boolean result = f.getIsSelected();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIsSelected method, of class Field.
     */
    @Test
    public void testSetIsSelected() {
        System.out.println("setIsSelected");
        boolean b = true;
        f.setIsSelected(b);
        assertEquals(b, f.getIsSelected());
    }

    /**
     * Test of getIndex method, of class Field.
     */
    @Test
    public void testGetIndex() {
        System.out.println("getIndex");
        int expResult = index;
        int result = f.getIndex();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setIndex method, of class Field.
     */
    @Test
    public void testSetIndex() {
        System.out.println("setIndex");
        int index = 40;
        f.setIndex(index);
        int result = f.getIndex();
        assertEquals(index, result);
    }
}
