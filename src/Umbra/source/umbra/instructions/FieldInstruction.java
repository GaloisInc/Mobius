/*
 * Created on 2005-05-19
 *
 */
package umbra.instructions;

import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.PUTSTATIC;

import umbra.UmbraHelper;
import umbra.editor.parsing.BytecodeStrings;


/**
 * This class is related to some subset of instructions
 * depending on parameters. It redefines some crucial while
 * handling with single instruction methods (correctness, getting handle).
 * This subset is similar to ordinary Java subset.
 *
 * @author Jarosław Paszek (jp209217@students.mimuw.edu.pl)
 * @version a-01
 */
public class FieldInstruction extends StringInstruction {

  /**
   * A position before which the '(' character cannot occur in a correct line.
   */
  private static final int LEFT_PAREN_FORBIDDEN_BOUND = 2;

  /**
   * A position before which the ')' character cannot occur in a correct line.
   */
  private static final int RIGHT_PAREN_FORBIDDEN_BOUND = 2;

  /**
   * This creates an instance of an instruction
   * named as <code>a_name</code> in a line the text of which is
   * <code>a_line_text</code>. Currently it just calls the constructor of the
   * superclass.
   *
   * @param a_line_text the line number of the instruction
   * @param a_name the mnemonic name of the instruction
   * @see InstructionLineController#InstructionLineController(String, String)
   */
  public FieldInstruction(final String a_line_text, final String a_name) {
    super(a_line_text, a_name);
  }


  /**
   * Field instruction line is correct if its
   * parameter contains a number in ().
   *
   * @return TODO
   * @see InstructionLineController#correct()
   */
  public final boolean correct() {
    final String my_line_text = getMy_line_text();
    final String s = UmbraHelper.stripAllWhitespace(my_line_text);
    final String[] s2 = BytecodeStrings.FIELD_INS;
    int j;
    for (j = 0; j < s2.length; j++) {
      if ((s.indexOf(s2[j]) > 0) &&
          (s.indexOf(s2[j]) <= s.indexOf(":") + 1)) {

        if (s.lastIndexOf("(") < LEFT_PAREN_FORBIDDEN_BOUND) return false;
        if (s.lastIndexOf(")") < RIGHT_PAREN_FORBIDDEN_BOUND) return false;
        int m, n, o;
        m = my_line_text.lastIndexOf("(");
        n = my_line_text.lastIndexOf(")");
        //UmbraPlugin.messagelog(m + " " + n + " " + my_line_text);
        if (m + 1 >= n) return false;
        for (o = m + 1; o < n; o++) {
          if (!(Character.isDigit(my_line_text.charAt(o)))) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  /**
   * This method, based on the value of the field
   * {@ref InstructionLineController#my_name}, creates a new BCEL instruction
   * object for a field access instruction. It computes the index parameter
   * of the instruction before the instruction is constructed. The method can
   * construct one of the instructions:
   * <ul>
   *   <li>getfield,</li>
   *   <li>getstatic,</li>
   *   <li>putfield,</li>
   *   <li>putstatic.</li>
   * </ul>
   * This method also checks the syntactical correctness of the current
   * instruction line.
   *
   * @return the freshly constructed BCEL instruction or <code>null</code>
   *         in case the instruction is not a field access instruction and
   *         in case the instruction line is incorrect
   * @see BytecodeLineController#getInstruction()
   */
  public final Instruction getInstruction() {
    int index;
    index = getInd();

    final boolean isOK = correct();
    Instruction res = null;
    if (isOK) {
      if (getName().compareTo("getfield") == 0) {
        res = new GETFIELD(index);
      }
      if (getName().compareTo("getstatic") == 0) {
        res = new GETSTATIC(index);
      }
      if (getName().compareTo("putfield") == 0) {
        res = new PUTFIELD(index);
      }
      if (getName().compareTo("putstatic") == 0) {
        res = new PUTSTATIC(index);
      }
    }
    return res;
  }
}
