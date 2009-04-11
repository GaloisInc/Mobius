/*
 * @title       "Umbra"
 * @description "An editor for the Java bytecode and BML specifications"
 * @copyright   "(c) 2006-2009 University of Warsaw"
 * @license     "All rights reserved. This program and the accompanying
 *               materials are made available under the terms of the LGPL
 *               licence see LICENCE.txt file"
 */
package umbra.instructions.ast;

import java.util.HashMap;

import org.apache.bcel.generic.MethodGen;

import b2bpl.bytecode.instructions.Instruction;

import umbra.UmbraPlugin;
import umbra.instructions.BytecodeController;
import umbra.instructions.InstructionParser;
import umbra.lib.UmbraException;

/**
 * This is abstract class for all instructions with at least one
 * parameter.
 *
 * @author Jarosław Paszek (jp209217@students.mimuw.edu.pl)
 * @author Tomasz Olejniczak (to236111@students.mimuw.edu.pl)
 * @author Aleksy Schubert (alx@mimuw.edu.pl)
 * @version a-01
 */
public class MultiInstruction extends InstructionLineController {

  /**
   * This creates an instance of an instruction
   * named as <code>a_name</code> at the line number
   * <code>a_line_text</code>. Currently it just calls the constructor of the
   * superclass.
   *
   * @param a_line_text the string representation of the line text
   * @param a_name the mnemonic name of the instruction
   * @see InstructionLineController#InstructionLineController(String, String)
   */
  public MultiInstruction(final String a_line_text, final String a_name) {
    super(a_line_text, a_name);
    use_stored_ind = false;
  }
  
  /**
   * Stores parameter of current instruction.
   */
  protected int my_ind;
  
  /**
   * Has value true if my_ind should be used instead of parsing parameter
   * from textual representation of this line.
   */
  protected boolean use_stored_ind;

  /**
   * This method checks if the last parenthesis in the given string
   * contains only digits.
   *
   * @param a_line_text the string to check
   * @return <code>true</code> when the last parenthesis contains only digits,
   *   <code>false</code> otherwise
   */
  public static /*@ pure @*/ boolean onlyDigitsInParen(
              final /*@ non_null @*/ String a_line_text) {
    for (int i = a_line_text.lastIndexOf("(") + 1;
         i < a_line_text.lastIndexOf(")"); i++) {
      if (!Character.isDigit(a_line_text.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * The method returns the number between the final parenthesis in the given
   * string. It assumes that the string between the parenthesis indeed
   * represents a number.
   *
   * @param a_line_text a string to extract the number from
   * @return the extracted number
   */
  public static /*@ pure @*/ int getNumInParen(
         final /*@ non_null @*/ String a_line_text) {
    final String numstring = a_line_text.substring(
      a_line_text.lastIndexOf("(") + 1, a_line_text.lastIndexOf(")"));
    return Integer.parseInt(numstring);
  }

  /**
   * This method parses the parameter of the current instruction.
   *
   * The default behaviour is that it parses the content of the final
   * parenthesis in the instruction with a numeric argument. It checks if the
   * argument is indeed the number and in that case it returns the number. In
   * case the argument is not a number, the method returns 0. It also issues
   * some logging information when the line has incorrect format.
   *
   * @return the parsed number or 0 in case the number cannot be parsed
   */
  protected int getInd() {
    if (use_stored_ind) return my_ind;
    final String my_line_text = getMy_line_text();
    if (my_line_text.lastIndexOf("(") >= my_line_text.lastIndexOf(")")) {
      UmbraPlugin.messagelog("A line is incorrect\n" +
                             "line content: >" + my_line_text + "<\n" +
                             "wrong content:" +
                             my_line_text.lastIndexOf("(") + " " +
                             my_line_text.lastIndexOf(")"));
    } else {
      if (MultiInstruction.onlyDigitsInParen(my_line_text)) {
        return MultiInstruction.getNumInParen(my_line_text);
      }
    }
    return 0;
  }

  /**
   * This method tries to parse a number in (). The precise format is:
   *    ( whitespace number whitespace )
   *
   * @param a_parser the parser which is to parse the number
   * @return <code>true</code> when the syntax of the number is
   *         correct
   */
  protected boolean numberWithDelimiters(final InstructionParser a_parser) {
    boolean res = true;
    res = res && a_parser.swallowDelimiter('('); // (
    res = res && a_parser.swallowWhitespace();
    res = res && a_parser.swallowNumber(); // number
    res = res && a_parser.swallowDelimiter(')'); // )
    return res;
  }
  
  /**
   * This method changes all references to constant pool
   * from a "dirty" numbers to a "clean" ones in BCEL representation
   * of this instruction. <br> <br>
   * 
   * See {@link BytecodeController#recalculateCPNumbers(JavaClass a_jc)} for explantation of
   * "dirty" and "clean" numbers concepts. <br> <br>
   * 
   * @param f a hash map which maps "dirty" numbers to "clean" ones
   * @param a_pos position in method
   * @throws UmbraException 
   */
  public void updateReferences(HashMap f, int a_pos) throws UmbraException {
    my_ind = (Integer) f.get(getInd());
    use_stored_ind = true;
    //int a_pos = this.getList().
    a_pos = getNoInMethod();
    
    //bcelDump();
    
    dispose();
    makeHandleForPosition(getMethod(), a_pos);
    
    //bcelDump();
    
    use_stored_ind = false;
  }
  
  /**
   * Prints BCEL instruction representation.
   * For debug use only.
   */
  private void bcelDump() {
    MethodGen mg = this.getMethod();
    for (int i = 0; i < mg.getInstructionList().size(); i++) {
      org.apache.bcel.generic.Instruction in = mg.getInstructionList().getInstructions()[i];
      System.err.println(in.toString());
    }
  }
  
}
