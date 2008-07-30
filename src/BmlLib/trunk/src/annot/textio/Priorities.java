package annot.textio;

import annot.io.Code;

/**
 * This class contains priorities of all expression types.
 *
 * @author Tomasz Batkiewicz (tb209231@students.mimuw.edu.pl)
 * @version a-01
 */
public abstract class Priorities extends Code {

  public static final int ABOTH = 3;

  public static final int ALEFT = 1;

  public static final int ANONE = 0;

  public static final int ARIGHT = 2;

  /**
   * Priority of expressions that have no subexpressions.
   */
  public static final int LEAF = 0;

  /**
   * Maximum possible expression priority.
   */
  public static final int MAX_PRI = 18;

  /**
   * The same priority as in it's parent, so it will never
   * be surrounded by ( ).
   */
  public static final int PRI_TRANSPARENT = -2;

  /**
   * Whether binary operators are associative
   * (ANONE, ALEFT, ARIGHT or ABOTH, for non-associative,
   * left-, right- and both-associative opcodes).
   */
  private static int[] associative;

  /**
   * Expression's priority array, from expression's type
   * (connector, from {@link Code} interface) to it's
   * priority.
   */
  private static int[] priorities;

  /**
   * An empty private constructor to disallow the creation of instances.
   */
  private Priorities() {
  }

  /**
   * Returns priority of expression with given type
   * (connector). Sets all priorities at first call.
   *
   * @param opcode - expression type (connector), from
   *     {@link Code} interface.
   * @return Priority of expressions of given type.
   */
  public static int getPriority(final int opcode) {
    if (priorities == null) {
      setPriorities();
    }
    if (opcode  >  255) {
      throw new RuntimeException("invalid opcode: " + opcode);
    }
    if (priorities[opcode] == -1) {
      throw new RuntimeException("invalid opcode: " + opcode);
    }
    return priorities[opcode];
  }

  /**
   * Checks whether given operator is associative.
   *
   * @param opcode - operator opcode,
   * @param dir - associavity direction (ANONE, ALEFT,
   *         ARIGHT or ABOTH, for non-associative,
   *         left-, right- and both-associative
   *         opcodes)
   * @return true for binary operators op that are assotiative
   *         in (at least) given direction.
   */
  public static boolean isAssociative(final int opcode, final int dir) {
    if (priorities == null) {
      setPriorities();
    }
    if (opcode  >  255) {
      throw new RuntimeException("invalid opcode: " + opcode);
    }
    return (associative[opcode] & dir)  >  0;
  }

  /**
   * Initializes <code>priorities</code> array.
   */
  private static void setPriorities() {
    priorities = new int[255];
    associative = new int[255];
    for (int i = 0; i  <  255; i++) {
      associative[i] = ALEFT;
    }
    for (int i = 0; i  <  255; i++) {
      priorities[i] = LEAF;
    }
    priorities[ARRAY_ACCESS] = 1;
    priorities[FIELD_ACCESS] = 1;
    priorities[NOT] = 3;
    priorities[NEG] = 3;
    priorities[MULT] = 4;
    priorities[DIV] = 4;
    priorities[REM] = 4;
    priorities[PLUS] = 5;
    priorities[MINUS] = 5;
    priorities[SHL] = 6;
    priorities[SHR] = 6;
    priorities[USHR] = 6;
    priorities[LESS] = 7;
    priorities[LESSEQ] = 7;
    priorities[GRT] = 7;
    priorities[GRTEQ] = 7;
    priorities[EQ] = 8;
    priorities[NOTEQ] = 8;
    priorities[BITWISEAND] = 9;
    priorities[BITWISEXOR] = 10;
    priorities[BITWISEOR] = 11;
    priorities[AND] = 12;
    priorities[OR] = 13;
    priorities[IMPLIES] = 14;
    priorities[EQUIV] = 15;
    priorities[NOTEQUIV] = 15;
    priorities[COND_EXPR] = 16;
    // ?
    priorities[FORALL] = 17;
    priorities[EXISTS] = 17;
    priorities[FORALL_WITH_NAME] = 17;
    priorities[EXISTS_WITH_NAME] = 17;

    associative[MULT] = ABOTH;
    associative[PLUS] = ABOTH;
    associative[AND] = ABOTH;
    associative[OR] = ABOTH;
    associative[BITWISEAND] = ABOTH;
    associative[BITWISEOR] = ABOTH;
    associative[BITWISEXOR] = ABOTH;
    associative[EQ] = ABOTH;
    associative[NOTEQ] = ABOTH;
    associative[EQUIV] = ABOTH;
    associative[NOTEQUIV] = ABOTH;
    associative[COND_EXPR] = ABOTH;

    associative[IMPLIES] = ARIGHT;
  }

}
