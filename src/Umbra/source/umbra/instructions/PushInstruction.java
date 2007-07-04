/*
 * Created on 2005-05-19
 *
 */
package umbra.instructions;

import org.apache.bcel.generic.BIPUSH;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.SIPUSH;

import umbra.UmbraHelper;
import umbra.editor.parsing.IBytecodeStrings;

/**
 * This class is related to some subset of instructions
 * depending on parameters. It redefines some crucial while
 * handling with single instruction methods(correctness, getting handle).
 * Here are two instructions resposible for pushing onto the stack.
 *
 * @author Jarosław Paszek (jp209217@students.mimuw.edu.pl)
 * @version a-01
 */
public class PushInstruction extends NumInstruction {

  /**
   * This creates an instance of an instruction
   * named as <code>a_name</code> in a line the text of which is
   * <code>a_line</code>. Currently it just calls the constructor of the
   * superclass.
   *
   * @param a_line_text the line number of the instruction
   * @param a_name the mnemonic name of the instruction
   * @see InstructionLineController#InstructionLineController(String, String)
   */
  public PushInstruction(final String a_line_text, final String a_name) {
    super(a_line_text, a_name);
  }

  /**
   * Push instruction line is correct if it has
   * one simple number parameter.
   *
   *@see InstructionLineController#correct()
   */
  public final boolean correct()
  {
    String s;
    s = UmbraHelper.stripAllWhitespace(my_line_text);
    final String[] s2 = IBytecodeStrings.push;
    int j;
    int y;
    for (j = 0; j < s2.length; j++) {
      if ((s.indexOf(s2[j]) > 0) && (s.indexOf(s2[j]) < s.indexOf(":") + 2)) {
        for (y = ((s.indexOf(s2[j])) + (s2[j].length())); y < s.length(); y++) {
          if (!(Character.isDigit(s.charAt(y)))) return false;
        }
        int counter = 0;
        boolean lastisdig = false;
        for (y = ((my_line_text.indexOf(s2[j])) + (s2[j].length()) + 1); y < my_line_text.length(); y++) {
          if (Character.isDigit(my_line_text.charAt(y))) {
            if (!(lastisdig)) counter++;
            lastisdig = true;
          } else if (Character.isWhitespace(my_line_text.charAt(y))) {
            lastisdig = false;
          }
        }
        if (counter == 1) return true;
      }
    }
    return false;
  }

  /**
   * TODO
   */
  private int getInd() {
    boolean isd;
    final String licznik = "0123456789";
    int liczba;

    String line1;
    line1 = UmbraHelper.stripAllWhitespace(my_line_text);

    isd = true;
    // zakladam ze poprawnosc jest juz wyzej
    final int dokad = line1.length();
    final int skad = line1.indexOf(name) + name.length();

    if (isd) {
      liczba = 0;
      for (int i = skad; i < dokad; i++) {
        liczba = 10 * liczba + licznik.indexOf(line1.substring(i, i + 1));
      }
      return liczba;
    }
    return 0;
  }

  /**
   * TODO
   *
   * @see BytecodeLineController#getInstruction()
   */
  public final Instruction getInstruction() {
    int index = 0;
    if (!correct())
      return null;
    index = getInd();

    byte b = 0;
    b = (byte)index;

    if (name.compareTo("bipush") == 0) {
      return new BIPUSH(b);
    }
    if (name.compareTo("sipush") == 0) {
      return new SIPUSH(b);
    }


    return null;

    }
}
