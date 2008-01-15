/*
 * @title       "Umbra"
 * @description "An editor for the Java bytecode and BML specifications"
 * @copyright   "(c) ${date} University of Warsaw"
 * @license     "All rights reserved. This program and the accompanying
 *               materials are made available under the terms of the LGPL
 *               licence see LICENCE.txt file"
 */
package umbra.instructions;


/**
 * This is a class that contains some information.
 * TODO
 *
 * @author Wojciech Wąs (ww209224@students.mimuw.edu.pl)
 * @version a-01
 */
public class AnnotationLineController extends BytecodeLineController {

  /**
   * This constructor remembers only the line text with the BML annotations.
   *
   * @param a_line_text the string representation of the line for the line
   *               with the BML annotations
   * @see BytecodeLineController#BytecodeLineController(String)
   */
  public AnnotationLineController(final String a_line_text) {
    super(a_line_text);
  }

  /**
   * TODO.
   * @return TODO
   * @see BytecodeLineController#correct()
   */
  public final boolean correct()
  {
    return true;
  }

  /**
   * Checks is the line can be an end of comment. This holds when the
   * final non-whitespace sequence in the line is * / string.
   *
   * @return <code>true</code> when the line contains the end of comment
   *   sequence, <code>false</code> otherwise
   */
  public boolean isCommentEnd() {
    String line = getMy_line_text();
    int where = line.lastIndexOf("*/");
    if (where > 0) {
      where += "*/".length();
      for (int i = where; i < line.length(); i++) {
        if (!Character.isWhitespace(line.charAt(i))) return false;
      }
      return true;
    }
    return false;
  }
}
