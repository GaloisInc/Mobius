/*
 * @title       "Umbra"
 * @description "An editor for the Java bytecode and BML specifications"
 * @copyright   "(c) 2006-2008 University of Warsaw"
 * @license     "All rights reserved. This program and the accompanying
 *               materials are made available under the terms of the LGPL
 *               licence see LICENCE.txt file"
 */
package umbra.instructions;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author alx
 * @version a-01
 *
 */
public class BytecodeTextParserTest {

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#BytecodeTextParser()}.
   */
  @Test
  public void testBytecodeTextParser() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#extractCommentFromLine(java.lang.String, umbra.instructions.LineContext)}.
   */
  @Test
  public void testExtractCommentFromLine() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#getMethodGenFromDoc(umbra.editor.BytecodeDocument, int)}.
   */
  @Test
  public void testGetMethodGenFromDoc() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#removeCommentFromLine(java.lang.String)}.
   */
  @Test
  public void testRemoveCommentFromLine() {
    assertEquals("simple comment",
      "a",
      BytecodeTextParser.removeCommentFromLine("a // some comment"));
    assertEquals("no comment",
                 "a",
                 BytecodeTextParser.removeCommentFromLine("a "));
    assertEquals("string + comment",
                 "\"a\"",
                 BytecodeTextParser.removeCommentFromLine("\"a\" // c"));
    assertEquals("string + comment in string",
                 "\"//\"",
                 BytecodeTextParser.removeCommentFromLine("\"//\" // c"));
    assertEquals("string + string in comment",
                 "\"//\"",
                 BytecodeTextParser.removeCommentFromLine("\"//\" // \" \"c"));
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#getEditorLines()}.
   */
  @Test
  public void testGetEditorLines() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#addEditorLine(int, umbra.instructions.ast.BytecodeLineController)}.
   */
  @Test
  public void testAddEditorLineIntBytecodeLineController() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#getPosOfLine(int)}.
   */
  @Test
  public void testGetPosOfLine() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#insertAt(int, java.lang.String)}.
   */
  @Test
  public void testInsertAt() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#enrichWithComment(umbra.instructions.ast.BytecodeLineController, int, int)}.
   */
  @Test
  public void testEnrichWithCommentBytecodeLineControllerIntInt() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#enrichWithComment(umbra.instructions.ast.BytecodeLineController, int)}.
   */
  @Test
  public void testEnrichWithCommentBytecodeLineControllerInt() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#addEditorLine(umbra.instructions.ast.BytecodeLineController)}.
   */
  @Test
  public void testAddEditorLineBytecodeLineController() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#getInstructions()}.
   */
  @Test
  public void testGetInstructions() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#addInstruction(umbra.instructions.ast.InstructionLineController)}.
   */
  @Test
  public void testAddInstruction() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#adjustCommentsForInstruction(umbra.instructions.ast.InstructionLineController, int)}.
   */
  @Test
  public void testAdjustCommentsForInstruction() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#incInstructionNo()}.
   */
  @Test
  public void testIncInstructionNo() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#initInstructionNo()}.
   */
  @Test
  public void testInitInstructionNo() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#updateAnnotations(umbra.instructions.LineContext)}.
   */
  @Test
  public void testUpdateAnnotations() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link umbra.instructions.BytecodeTextParser#getInstructionNoForLine(int)}.
   */
  @Test
  public void testGetInstructionNoForLine() {
    fail("Not yet implemented");
  }

}
