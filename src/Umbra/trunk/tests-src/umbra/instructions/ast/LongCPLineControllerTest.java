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
public class LongCPLineControllerTest extends TestCase {
  
  private String lines[] = {
    "   const    #5 =          Long 21;    ",
    "const#5=Long 21;",
    "const #5 = Long 21          ;",
    "   const    #5 =  Long       21   ;   ",
    "   const    #5 =  Long       21l;",
    "   const    #5 =  Long       21L;"
  };
  
  private String lines_incorrect[] = {
    "   const    5 =          Long 21;",
    "   const    #5 ==          Long 21;",
    "   const    #5 =          long 21;",
    "   const    #5 =          Long 2 1;",
    "   const    #5 =          Long ",
    "   const    #5 =          Long 21 l;    ",
    "   const    #5 =          Long 21"
  };
  
  private LongCPLineController[] instructions;
  private LongCPLineController[] instructions_incorrect;
  
  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    instructions = new LongCPLineController[lines.length];
    instructions_incorrect = new LongCPLineController[lines_incorrect.length];
    for (int i = 0; i < lines.length; i++) {
      instructions[i] = new LongCPLineController(lines[i], null);
      instructions[i].correct();
    }
    for (int i = 0; i < lines_incorrect.length; i++) {
      instructions_incorrect[i] = new LongCPLineController(lines_incorrect[i], null);
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
   * Test method for {@link umbra.instructions.ast.LongCPLineController#getConstantNumber()}.
   */
  @Test
  public void testConstantNumber() {
    for (int i = 0; i < lines.length; i++) {
      assertTrue("long, constant number, ins number "
                 + i, instructions[i].getConstantNumber() == 5);
    }
  }
  
  /**
   * Test method for {@link umbra.instructions.ast.LongCPLineController#correct()}.
   */
  @Test
  public void testCorrect() {
    for (int i = 0; i < lines.length; i++) {
      assertTrue("long, correct lines, ins number "
                 + i, instructions[i].correct());
    }
    for (int i = 0; i < lines_incorrect.length; i++) {
      assertTrue("long, incorrect lines, ins number "
                 + i, !instructions_incorrect[i].correct());
    }
  }

}
