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

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantMethodref;

import umbra.instructions.BytecodeController;
import umbra.instructions.InstructionParser;
import umbra.lib.BytecodeStrings;

/**
 * This is a class that represents CONSTANT_InterfaceMethodref_info constant
 * pool entry line controller.
 * @author Tomasz Olejniczak (to236111@students.mimuw.edu.pl)
 * @version a-01
 *
 */
public class InterfaceMethodrefCPLineController extends CPLineController {

  /**
   * This creates an instance of a bytecode line handle to handle
   * constant pool entry of the type CONSTANT_InterfaceMethodref_info.
   *
   * @param a_line_text the string representation of the line text
   * @param an_entry_type the string "InterfaceMethodref", ignored
   * parameter for compatibility with
   * @see BytecodeLineController#BytecodeLineController(String)
   */
  public InterfaceMethodrefCPLineController(final String a_line_text, final String an_entry_type) {
    super(a_line_text, an_entry_type);
  }
  
  /**
   * This method returns the string "InterfaceMethodref" which describes
   * CONSTANT_InterfaceMethodref_info constant pool entry type handled by the
   * current class.
   * 
   * @return handled entry type
   */
  public static String getEntryType() {
    return BytecodeStrings.INTERFACE_METHODREF_CP_ENTRY_KEYWORD;
  }
  
  /**
   * The CONSTANT_InterfaceMethodref_info constant pool entry line is correct if
   * it has format: <br> <br>
   * 
   * [ ]*const[ ]*&lt;ref&gt;[ ]*=[ ]*InterfaceMethodref[ ]*&lt;ref&gt;.&lt;ref&gt;[ ]*;[ ]*
   * <br> <br>
   * 
   * where &lt;ref&gt; ::= #&lt;positive integer&gt;.
   * 
   * @return <code> true </code> when the syntax of constant pool
   * entry is correct
   * @see CPLineController#correct()
   */
  public final boolean correct() {
    boolean res = parseTillEntryType();
    InstructionParser my_parser = getParser();
    res = res && my_parser.swallowWhitespace();
    res = res &&my_parser.swallowSingleMnemonic(BytecodeStrings.INTERFACE_METHODREF_CP_ENTRY_KEYWORD);
    res = res && my_parser.swallowWhitespace();
    res = res && my_parser.swallowDelimiter('#');
    res = res && my_parser.swallowCPReferenceNumber();
    res = res && my_parser.swallowDelimiter('.');
    res = res && my_parser.swallowDelimiter('#');
    res = res && my_parser.swallowCPReferenceNumber();
    res = res && my_parser.swallowWhitespace();
    res = res && my_parser.swallowDelimiter(';');
    res = res && !my_parser.swallowWhitespace();
    return res;
  }
  
  /**
  * This method retrieves the reference to the class from the
  * CONSTANT_InterfaceMethodref_info constant pool entry in
  * {@link BytecodeLineController#getMy_line_text()}. This parameter
  * is located after the entry type keyword. 
  * The method assumes {@link BytecodeLineController#getMy_line_text()}
  * is correct.
  * 
  * @return reference to the class described by constant pool entry
  */
  private int getClassReference() {
    parseTillEntryType();
    InstructionParser my_parser = getParser();
    my_parser.swallowWhitespace();
    my_parser.swallowSingleMnemonic(BytecodeStrings.INTERFACE_METHODREF_CP_ENTRY_KEYWORD);
    my_parser.swallowWhitespace();
    my_parser.swallowDelimiter('#');
    my_parser.swallowCPReferenceNumber();
    return my_parser.getResult();
  }
  
  /**
   * This method retrieves the reference to the CONSTANT_NameAndType_info
   * constant pool entry from the CONSTANT_InterfaceMethodref_info constant pool entry
   * in {@link BytecodeLineController#getMy_line_text()}. This parameter
   * is located after the first parameter (reference to the class) with
   * dot in between.
   * The method assumes {@link BytecodeLineController#getMy_line_text()}
   * is correct. It also assumes that the internal parser state has not
   * been modified between the call to {@link #getClassReference()} and the call
   * of this method.
   * 
   * @return reference to the CONSTANT_NameAndType_info constant pool
   * entry described by constant pool entry
   */
  private int getNameAndTypeReference() {
    InstructionParser my_parser = getParser();
    my_parser.swallowDelimiter('.');
    my_parser.swallowDelimiter('#');
    my_parser.swallowCPReferenceNumber();
    return my_parser.getResult();
  }
  
  /**
   * Returns the link to the BCEL interface methodref constant represented
   * by the current line. If there is no such constant it creates the constant
   * before returning. Newly created constant should then be associated
   * with BML constant pool representation. <br> <br>
   * 
   * The constant reference numbers set for the newly created constant are
   * the "dirty" numbers. They should be changed to "clean" numbers in
   * {@link BytecodeController#recalculateCPNumbers()}. <br> <br>
   * 
   * For explantation of "dirty" and "clean" number concepts see
   * {@link BytecodeController#recalculateCPNumbers()}.
   * 
   * @return a BCEL constant represented by the current line
   */
  public Constant getConstant() {
    if (my_constant != null) return my_constant;
    my_constant = new ConstantInterfaceMethodref(getClassReference(), getNameAndTypeReference());
    return my_constant;
  }
  
  /**
   * This method changes references to the CP entries referenced from this CP entry.
   * <br>
   * The change has effect only in BCEL representation of constant pool and does
   * not affect internal Umbra representation. <br> <br>
   * 
   * See {@link BytecodeController#recalculateCPNumbers()} for explantation of
   * "dirty" and "clean" numbers concepts. <br> <br>
   * 
   * @param f a hash map which maps "dirty" numbers to "clean" ones
   */
  public void updateReferences(HashMap f) {
    ((ConstantInterfaceMethodref) my_constant).
    setClassIndex((Integer) f.get(getClassReference()));
    ((ConstantInterfaceMethodref) my_constant).
    setNameAndTypeIndex((Integer) f.get(getNameAndTypeReference()));
  }
  
}
