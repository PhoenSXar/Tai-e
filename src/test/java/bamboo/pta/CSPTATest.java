/*
 * Bamboo - A Program Analysis Framework for Java
 *
 * Copyright (C)  2020 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C)  2020 Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * This software is designed for the "Static Program Analysis" course at
 * Nanjing University, and it supports a subset of Java features.
 * Bamboo is only for educational and academic purposes, and any form of
 * commercial use is disallowed.
 */

package bamboo.pta;

import org.junit.Test;

import static bamboo.TestUtils.testCSPTA;

public class CSPTATest {

    // Tests for context sensitivity variants
    @Test
    public void testOneCall() {
        testCSPTA("OneCall", "-cs", "1-call");
    }

    @Test
    public void testOneObject() {
        testCSPTA("OneObject", "-cs", "1-obj");
    }

    @Test
    public void testOneType() {
        testCSPTA("OneType", "-cs", "1-type");
    }

    @Test
    public void testTwoCall() {
        testCSPTA("TwoCall", "-cs", "2-call");
    }

    @Test
    public void testTwoObject() {
        testCSPTA("TwoObject", "-cs", "2-obj");
    }

    @Test
    public void testTwoType() {
        testCSPTA("TwoType", "-cs", "2-type");
    }

    // Tests for Java feature supporting
    @Test
    public void testStaticField() {
        testCSPTA("StaticField");
    }

    @Test
    public void testArray() {
        testCSPTA("Array");
    }

    @Test
    public void testCast() {
        testCSPTA("Cast");
    }

    @Test
    public void testNull() {
        testCSPTA("Null");
    }

    @Test
    public void testPrimitive() {
        testCSPTA("Primitive");
    }

    @Test
    public void testStrings() {
        testCSPTA("Strings");
    }

    @Test
    public void testMultiArray() {
        testCSPTA("MultiArray");
    }

    @Test
    public void testClinit() {
        testCSPTA("Clinit");
    }

    @Test
    public void testClassObj() {
        testCSPTA("ClassObj");
    }

    // Tests for handling of non-normal objects
    @Test
    public void testTypeSens() {
        testCSPTA("TypeSens", "-cs", "2-type");
    }

    @Test
    public void testSpecialHeapContext() {
        testCSPTA("SpecialHeapContext", "-cs", "2-object");
    }
}