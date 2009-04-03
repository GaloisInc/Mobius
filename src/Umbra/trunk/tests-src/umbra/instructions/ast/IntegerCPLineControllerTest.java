/*
 * @title       "Umbra"
 * @description "An editor for the Java bytecode and BML specifications"
 * @copyright   "(c) 2006-2009 University of Warsaw"
 * @license     "All rights reserved. This program and the accompanying
 *               materials are made available under the terms of the LGPL
 *               licence see LICENCE.txt file"
 */
package umbra.instructions.ast;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Tomasz Olejniczak (to236111@students.mimuw.edu.pl)
 * @version a-01
 *
 */
public class IntegerCPLineControllerTest extends TestCase {
  
  private String lines[] = {
    "   const    #5 =          Integer 21  ; ",
    "const#5=Integer 21;  ",
    "const #5 = Integer 21          ;",
    "   const    #5 =  Integer       21;"
  };
  
  private String lines_incorrect[] = {
    "   const    5 =          Integer 21;",
    "   const    #5 ==          Integer 21;",
    "   const    #5 =          Intijer 21;",
    "   const    #5 =          Integer 2 1;",
    "   const    #5 =          Integer ;",
    "   const    #5 =          Integer 21"
  };
  
  private IntegerCPLineController[] instructions;
  private IntegerCPLineController[] instructions_incorrect;
  
  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    instructions = new IntegerCPLineController[lines.length];
    instructions_incorrect = new IntegerCPLineController[lines_incorrect.length];
    for (int i = 0; i < lines.length; i++) {
      instructions[i] = new IntegerCPLineController(lines[i], null);
      instructions[i].correct();
    }
    for (int i = 0; i < lines_incorrect.length; i++) {
      instructions_incorrect[i] = new IntegerCPLineController(lines_incorrect[i], null);
      instructions_incorrect[i].correct();
    }
  }
  
  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() {
    
  }

  /**
   * Test method for {@link umbra.instructions.ast.IntegerCPLineController#getConstantNumber()}.
   */
  @Test
  public void testConstantNumber() {
    for (int i = 0; i < lines.length; i++) {
      assertTrue("integer, constant number, ins number "
                 + i, instructions[i].getConstantNumber() == 5);
    }
  }
  
  /**
   * Test method for {@link umbra.instructions.ast.IntegerCPLineController#correct()}.
   */
  @Test
  public void testCorrect() {
    for (int i = 0; i < lines.length; i++) {
      assertTrue("integer, correct lines, ins number "
                 + i, instructions[i].correct());
    }
    for (int i = 0; i < lines_incorrect.length; i++) {
      assertTrue("integer, incorrect lines, ins number "
                 + i, !instructions_incorrect[i].correct());
    }
  }

}
