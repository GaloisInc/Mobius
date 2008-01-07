/*
 * @title       "Umbra"
 * @description "An editor for the Java bytecode and BML specifications"
 * @copyright   "(c) ${date} University of Warsaw"
 * @license     "All rights reserved. This program and the accompanying
 *               materials are made available under the terms of the LGPL
 *               licence see LICENCE.txt file"
 */
package umbra.editor.parsing;

import org.objectweb.asm.tree.LdcInsnNode;

import b2bpl.bytecode.instructions.LdcInstruction;

import umbra.instructions.IncInstruction;
import umbra.instructions.LoadStoreArrayInstruction;
import umbra.instructions.PushInstruction;


/**
 * String arrays used to identify keywords and instruction
 * names in bytecode.
 *
 * @author Wojciech Wąs (ww209224@students.mimuw.edu.pl)
 * @author Jarosław Paszek (jp209217@students.mimuw.edu.pl)
 * @version a-01
 */
public class BytecodeStrings {

  /**
   * TODO.
   */
  public static final String[] INSTRUCTIONS = new String[] {"aconst_null",
                                                            "dadd", "ddiv",
                                                            "dmul", "dneg",
                                                            "drem", "dsub",
                                                            "fadd", "fdiv",
                                                            "fmul", "fneg",
                                                            "frem", "fsub",
                                                            "iadd", "iand",
                                                            "idiv", "imul",
                                                            "ineg", "ior",
                                                            "irem", "isub",
                                                            "iushr", "ixor",
                                                            "ladd", "land",
                                                            "ldiv", "lmul",
                                                            "lneg", "lor",
                                                            "lrem", "lshl",
                                                            "lshr", "lsub",
                                                            "lushr", "lxor",
                                                            "aaload",
                                                            "aastore",
                                                            "baload", "bastore",
                                                            "caload", "castore",
                                                            "daload",
                                                            "dastore",
                                                            "faload",
                                                            "fastore",
                                                            "iaload",
                                                            "iastore",
                                                            "laload",
                                                            "lastore",
                                                            "saload",
                                                            "sastore",
                                                            "arraylength",
                                                            "athrow", "bipush",
                                                            "goto", "goto_w",
                                                            "if_acmpeq",
                                                            "if_acmpne",
                                                            "if_icmpeq",
                                                            "if_icmpge",
                                                            "if_icmpgt",
                                                            "if_icmple",
                                                            "if_icmplt",
                                                            "if_icmpne",
                                                            "ifeq", "ifge",
                                                            "ifgt", "ifle",
                                                            "iflt", "ifne",
                                                            "ifnonnull",
                                                            "ifnull", "jsr",
                                                            "jsr_w",
                                                            "lookupswitch",
                                                            "tableswitch",
                                                            "breakpoint",
                                                            "d2f", "d2i",
                                                            "d2l", "f2d",
                                                            "f2i", "f2l",
                                                            "i2b", "i2c",
                                                            "i2d", "i2f",
                                                            "i2l", "i2s",
                                                            "l2d", "l2f",
                                                            "l2i", "anewarray",
                                                            "checkcast",
                                                            "getfield",
                                                            "getstatic",
                                                            "putfield",
                                                            "putstatic",
                                                            "invokeinterface",
                                                            "invokespecial",
                                                            "invokestatic",
                                                            "invokevirtual",
                                                            "instanceof",
                                                            "ldc", "ldc_w",
                                                            "ldc2_w",
                                                            "multilinewarray",
                                                            "new", "dcmpg",
                                                            "dcmpl", "dconst",
                                                            "fcmpg", "fcmpl",
                                                            "fconst", "iconst",
                                                            "iconst_0",
                                                            "iconst_1",
                                                            "iconst_2",
                                                            "iconst_3",
                                                            "iconst_4",
                                                            "iconst_5",
                                                            "iconst_m1",
                                                            "impdep1",
                                                            "impdep2", "lcmp",
                                                            "lconst", "iinc",
                                                            "aload", "aload_0",
                                                            "aload_1",
                                                            "aload_2",
                                                            "aload_3",
                                                            "astore",
                                                            "astore_0",
                                                            "astore_1",
                                                            "astore_2",
                                                            "astore_3",
                                                            "dload", "dload_0",
                                                            "dload_1",
                                                            "dload_2",
                                                            "dload_3",
                                                            "dstore",
                                                            "dstore_0",
                                                            "dstore_1",
                                                            "dstore_2",
                                                            "dstore_3",
                                                            "fload", "fload_0",
                                                            "fload_1",
                                                            "fload_2",
                                                            "fload_3",
                                                            "fstore",
                                                            "fstore_0",
                                                            "fstore_1",
                                                            "fstore_2",
                                                            "fstore_3",
                                                            "iload", "iload_0",
                                                            "iload_1",
                                                            "iload_2",
                                                            "iload_3",
                                                            "istore",
                                                            "istore_0",
                                                            "istore_1",
                                                            "istore_2",
                                                            "istore_3",
                                                            "lload", "lload_0",
                                                            "lload_1",
                                                            "lload_2",
                                                            "lload_3",
                                                            "lstore",
                                                            "lstore_0",
                                                            "lstore_1",
                                                            "lstore_2",
                                                            "lstore_3",
                                                            "monitorenter",
                                                            "monitorexit",
                                                            "newarray", "nop",
                                                            "ret", "areturn",
                                                            "dreturn",
                                                            "freturn",
                                                            "ireturn",
                                                            "lreturn",
                                                            "return", "sipush",
                                                            "dup", "dup_x1",
                                                            "dup_x2", "dup2",
                                                            "dup2_x1",
                                                            "dup2_x2", "pop",
                                                            "pop2", "swap"};

  /**
   * This constant contains an array with all the names of instructions handled
   * in {@ref ArithmeticInstruction} class.
   */
  public static final String[] ARITHMETIC_INS = new String[] {"dadd", "ddiv",
                                                              "dmul", "dneg",
                                                              "drem", "dsub",
                                                              "fadd", "fdiv",
                                                              "fmul", "fneg",
                                                              "frem", "fsub",
                                                              "iadd", "iand",
                                                              "imul", "idiv",
                                                              "ineg", "ior",
                                                              "isub", "irem",
                                                              "iushr", "ixor",
                                                              "lsub", "ladd",
                                                              "land", "ldiv",
                                                              "lmul", "lneg",
                                                              "lor", "lrem",
                                                              "lshl", "lshr",
                                                              "lushr", "lxor"};

  /**
   * This constant contains an array with all the names of instructions handled
   * in {@ref IConstInstruction} class.
   */
  public static final String[] ICONST_INS = new String[] {"iconst_0",
                                                          "iconst_1",
                                                          "iconst_2",
                                                          "iconst_3",
                                                          "iconst_4",
                                                          "iconst_5"};

  /**
   * This constant contains an array with all the names of instructions handled
   * in {@ref LoadStoreConstInstruction} class.
   */
  public static final String[] LOAD_STORE_INS = new String[] {"aload_0",
                                                              "aload_1",
                                                              "aload_2",
                                                              "aload_3",
                                                              "astore_0",
                                                              "astore_1",
                                                              "astore_2",
                                                              "astore_3",
                                                              "dload_0",
                                                              "dload_1",
                                                              "dload_2",
                                                              "dload_3",
                                                              "dstore_0",
                                                              "dstore_1",
                                                              "dstore_2",
                                                              "dstore_3",
                                                              "fload_0",
                                                              "fload_1",
                                                              "fload_2",
                                                              "fload_3",
                                                              "fstore_0",
                                                              "fstore_1",
                                                              "fstore_2",
                                                              "fstore_3",
                                                              "iload_0",
                                                              "iload_1",
                                                              "iload_2",
                                                              "iload_3",
                                                              "istore_0",
                                                              "istore_1",
                                                              "istore_2",
                                                              "istore_3",
                                                              "lload_0",
                                                              "lload_1",
                                                              "lload_2",
                                                              "lload_3",
                                                              "lstore_0",
                                                              "lstore_1",
                                                              "lstore_2",
                                                              "lstore_3"};

  /**
   * This constant contains an array with all the names of instructions handled
   * in {@ref LoadStoreArrayInstruction} class.
   */
  public static final String[] LOAD_STORE_ARRAY_INS = {"aaload",
                                                       "aastore",
                                                       "baload",
                                                       "bastore",
                                                       "caload",
                                                       "castore",
                                                       "daload",
                                                       "dastore",
                                                       "faload",
                                                       "fastore",
                                                       "iaload",
                                                       "iastore",
                                                       "laload",
                                                       "lastore",
                                                       "saload",
                                                       "sastore"};

  /**
   * TODO.
   * instructions with no arguments required
   */
  public static final String[] SINGLE_INS = new String[] {"aconst_null",
                                                          "arraylength",
                                                          "athrow", "lcmp",
                                                          "monitorenter",
                                                          "monitorexit",
                                                          "areturn", "dreturn",
                                                          "freturn", "ireturn",
                                                          "lreturn", "return",
                                                          "dup", "dup_x1",
                                                          "dup_x2", "dup2",
                                                          "dup2_x1", "dup2_x2",
                                                          "pop", "pop2",
                                                          "swap"};

  /**
   * This constant contains an array with all the names of instructions handled
   * in {@ref PushInstruction} class.
   */
  public static final String[] PUSH_INS = new String[] {"bipush", "sipush"};

  /**
   * This constant contains an array with all the names of instructions handled
   * in {@ref JumpInstruction} class.
   *
   * FIXME: "lookupswitch", "tableswitch" are not handled
   */
  public static final String[] JUMP_INS = new String[] {"goto", "goto_w",
                                                        "if_acmpeq",
                                                        "if_acmpne",
                                                        "if_icmpeq",
                                                        "if_icmpge",
                                                        "if_icmpgt",
                                                        "if_icmple",
                                                        "if_icmplt",
                                                        "if_icmpne", "ifeq",
                                                        "ifge", "ifgt", "ifle",
                                                        "iflt", "ifne",
                                                        "ifnonnull", "ifnull",
                                                        "jsr", "jsr_w",
                                                        "lookupswitch",
                                                        "tableswitch"};

  /**
   * This constant contains an array with all the names of instructions handled
   * in {@ref IncInstruction} class.
   */
  public static final String[] INCC_INS = new String[] {"iinc"};

  /**
   * TODO.
   */
  //dodatkowo %liczba
  public static final String[] STACK_INS = new String[] {"aload", "astore",
                                                         "dload", "dstore",
                                                         "fload", "fstore",
                                                         "iload", "istore",
                                                         "lload", "lstore"};

  /**
   * TODO.
   */
  //dodatkowo <char> - typ tablicy
  public static final String[] ARRAY_INS = new String[] {"newarray"};

  /**
   * This constant contains an array with all the names of instructions handled
   * in {@ref NewInstruction} class.
   */
  public static final String[] NEW_INS = new String[] {"anewarray",
                                                       "checkcast",
                                                       "instanceof", "new"};

  /**
   * TODO.
   */
  //fieldinstructions - dodatkowo java.costam (liczba)
  public static final String[] FIELD_INS = new String[] {"getfield",
                                                         "getstatic",
                                                         "putfield",
                                                         "putstatic"};

  /**
   * TODO.
   */
  //invokeinstr - java.costam; rozne nawiasy [( V i (liczba)
  public static final String[] INVOKE_INS = new String[] {"invokeinterface",
                                                          "invokespecial",
                                                          "invokestatic",
                                                          "invokevirtual"};

  /**
   * This constant contains an array with all the names of instructions handled
   * in {@ref LdcInstruction} class.
   */
  public static final String[] LDC_INS = new String[] {"ldc", "ldc_w",
                                                       "ldc2_w"};

  /**
   * TODO.
   */
  // nie bo nie bylo
  public static final String[] UNCLASSIFIED_INS =
                                          new String[] {"breakpoint",
                                                        "d2f",
                                                        "d2i",
                                                        "d2l",
                                                        "f2d",
                                                        "f2i",
                                                        "f2l",
                                                        "i2b",
                                                        "i2c",
                                                        "i2d",
                                                        "i2f",
                                                        "i2l",
                                                        "i2s",
                                                        "l2d",
                                                        "l2f",
                                                        "l2i",
                                                        "multilinewarray",
                                                        "dcmpg",
                                                        "dcmpl",
                                                        "dconst",
                                                        "fcmpg",
                                                        "fcmpl",
                                                        "fconst",
                                                        "iconst",
                                                        "impdep1",
                                                        "impdep2",
                                                        "lconst",
                                                        "nop", "ret"};

  /**
   * TODO.
   */
  public static final String[] BML_KEYWORDS = new String[] {"\\requires",
                                                            "\\ensures",
                                                            "\\exsures" };

  /**
   * TODO.
   */
  public static final String[] JAVA_KEYWORDS = new String[] {"public",
                                                             "protected",
                                                             "private",
                                                             "static", "void",
                                                             "int", "long",
                                                             "short", "char",
                                                             "byte", "boolean",
                                                             "class",
                                                             "interface",
                                                             "extends",
                                                             "implements",
                                                             "package"};

  /**
   * TODO.
   */
  public static final String[] LINENUM_KEYWORDS =
                                      new String[] {"Line numbers:",
                                                    "Local variable table:" };

  /**
   * TODO.
   */
  public static final String[] LINE_KEYWORDS = new String[] {"Line", "numbers",
                                                             "Local",
                                                             "variable",
                                                             "table"};

  /**
   * TODO.
   */
  public static final String[] CODE_KEYWORDS = new String[] {"Code",
                                                             "max_stack",
                                                             "max_locals",
                                                             "code_length"};

  /**
   * TODO.
   */
  public static final char[] KEY_TYPE_CHARS = new char[] {'B', 'C', 'D', 'I',
                                                          'S', 'V'};


}
