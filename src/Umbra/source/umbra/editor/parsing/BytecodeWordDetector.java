/*
 * Created on 2005-04-25
 *
 */
package umbra.editor.parsing;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * TODO.
 *
 * @author Wojciech Wąs  (ww209224@students.mimuw.edu.pl)
 * @version a-01
 */
public class BytecodeWordDetector implements IWordDetector {

  /**
   * This method returns true when the character is a legal character to start
   * a word. In this case it means it is a letter.
   *
   * @param a_char a character to check
   * @return <code>true</code> when the character can start a word
   */
  public final boolean isWordStart(final char a_char) {
    return Character.isLetter(a_char);
  }

  /**
   * TODO.
   * @param a_char TODO
   * @return TODO
   * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
   */
  public final boolean isWordPart(final char a_char) {
    return (Character.isLetterOrDigit(a_char) || a_char == '_');
  }
}
