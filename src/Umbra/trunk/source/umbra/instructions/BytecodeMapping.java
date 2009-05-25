/*
 * @title       "Umbra"
 * @description "An editor for the Java bytecode and BML specifications"
 * @copyright   "(c) 2006-2009 University of Warsaw"
 * @license     "All rights reserved. This program and the accompanying
 *               materials are made available under the terms of the LGPL
 *               licence see LICENCE.txt file"
 */
package umbra.instructions;

import java.util.IdentityHashMap;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.PMGClass;
import org.apache.bcel.classfile.Field;

/**
 * This class maps the references to constant pool entries in BCEL
 * representation of bytecode onto editor lines that represents that entries
 * (for constants having such editor line) and references to constant pool
 * entries that was generated automatically when new field was added
 * from those fields.
 *
 * @author Tomasz Olejniczak (to236111@students.mimuw.edu.pl)
 * @version a-01
 *
 */
public class BytecodeMapping {

  /**
   * Contains mapping of field names.
   */
  private IdentityHashMap < Field, Integer > my_field_names;

  /**
   * Contains mapping of field signatures.
   */
  private IdentityHashMap < Field, Integer > my_field_signatures;

  /**
   * Determines whether to allow constant pool edition. It should be
   * true in all cases with exception of situation when new field was
   * added and bytecode wasn't refreshed or saved.
   */
  private boolean my_cp_edition_allowed;

  /**
   * Contains mapping of method names.
   */
  private IdentityHashMap < Method, Integer > my_method_names;

  /**
   * Contains mapping of method signatures.
   */
  private IdentityHashMap < Method, Integer > my_method_signatures;

  /**
   * Contains mapping of field names for newly added fields.
   */
  private IdentityHashMap < Field, Integer > my_field_name_constants;

  /**
   * Contains mapping of field signatures for newly added fields.
   */
  private IdentityHashMap < Field, Integer > my_field_signature_constants;

  /**
   * Contains mapping of class name.
   */
  private int my_class_name_index;

  /**
   * Contains mapping of superclass name.
   */
  private int my_superclass_name_index;

  /**
   * Contains mapping of attribute names.
   */
  private IdentityHashMap < Attribute, Integer > my_attribute_names;

  /**
   * Contains mapping of source files, constant values, PMGs and signatures.
   */
  private IdentityHashMap < Attribute, Integer > my_attribute_second_indices;

  /**
   * Contains mapping of PMG classes.
   */
  private IdentityHashMap < PMGClass, Integer > my_pmg_classes;

  /**
   * Contains mapping of exception tables.
   */
  private IdentityHashMap < ExceptionTable, int[] > my_exception_tables;

  /**
   * Default constructor.
   */
  public BytecodeMapping() {
    reset();
  }

  /**
   * Clears all maps stored in this class and reinitializes them to
   * the empty ones.
   */
  public void reset() {
    my_field_names = new IdentityHashMap < Field, Integer > ();
    my_field_signatures = new IdentityHashMap < Field, Integer > ();
    my_method_names = new IdentityHashMap < Method, Integer > ();
    my_method_signatures = new IdentityHashMap < Method, Integer > ();
    my_field_name_constants = new IdentityHashMap < Field, Integer > ();
    my_field_signature_constants = new IdentityHashMap < Field, Integer > ();
    my_attribute_names = new IdentityHashMap < Attribute, Integer > ();
    my_attribute_second_indices = new IdentityHashMap < Attribute, Integer > ();
    my_pmg_classes = new IdentityHashMap < PMGClass, Integer > ();
    my_exception_tables = new IdentityHashMap < ExceptionTable, int[] > ();
    my_cp_edition_allowed = true;
  }

  /**
   * Maps field onto the number of constant (in textual representation of
   * bytecode) that represents constant contatining field's name.
   *
   * @param a_field a field which is mapped
   * @param a_name_index the number of constant (in textual representation of
   * bytecode) that represents constant contatining field's name
   */
  public void addFieldName(final Field a_field, final Integer a_name_index) {
    my_field_names.put(a_field, a_name_index);
  }

  /**
   * Maps field onto the number of constant (in textual representation of
   * bytecode) that represents constant contatining field's signature.
   *
   * @param a_field a field which is mapped
   * @param a_signature_index the number of constant (in textual representation
   * of bytecode) that represents constant contatining field's signature
   */
  public void addFieldSignature(final Field a_field,
                                final Integer a_signature_index) {
    my_field_signatures.put(a_field, a_signature_index);
  }

  /**
   * Maps method onto the number of constant (in textual representation of
   * bytecode) that represents constant contatining method's name.
   *
   * @param a_method a method which is mapped
   * @param a_name_index the number of constant (in textual representation of
   * bytecode) that represents constant contatining method's name
   */
  public void addMethodName(final Method a_method, final Integer a_name_index) {
    my_method_names.put(a_method, a_name_index);
  }

  /**
   * Maps method onto the number of constant (in textual representation of
   * bytecode) that represents constant contatining method's signature.
   *
   * @param a_method a method which is mapped
   * @param a_signature_index the number of constant (in textual representation
   * of bytecode) that represents constant contatining method's signature
   */
  public void addMethodSignature(final Method a_method,
                                final Integer a_signature_index) {
    my_method_signatures.put(a_method, a_signature_index);
  }

  /**
   * Maps field onto the constant (in BCEL representation of bytecode)
   * that contatins field's name.
   *
   * @param a_field a field which is mapped
   * @param a_constant the constant (in BCEL representation of bytecode)
   * that contatins field's name
   */
  public void addFieldNameConstant(final Field a_field,
                                   final int a_constant) {
    my_field_name_constants.put(a_field, a_constant);
  }

  /**
   * Maps field onto the constant (in BCEL representation of bytecode)
   * that contatins field's signature.
   *
   * @param a_field a field which is mapped
   * @param a_constant the constant (in BCEL representation of bytecode)
   * that contatins field's signature
   */
  public void addFieldSignatureConstant(final Field a_field,
                                        final int a_constant) {
    my_field_signature_constants.put(a_field, a_constant);
  }

  /**
   * Maps attribute onto the number of constant (in textual representation of
   * bytecode) that represents constant contatining attribute's name.
   *
   * @param an_attribute an attribute which is mapped
   * @param a_name_index the number of constant (in textual representation
   * of bytecode) that represents constant contatining attribute's name
   */
  public void addAttributeName(final Attribute an_attribute,
                                final Integer a_name_index) {
    my_attribute_names.put(an_attribute, a_name_index);
  }

  /**
   * Maps attribute onto the number of constant (in textual representation of
   * bytecode) that represents constant contatining source file, constant value,
   * PMG class or signature.
   *
   * @param an_attribute an attribute which is mapped
   * @param an_index the number of constant (in textual
   * representation of bytecode) that represents constant contatining source
   * file, constant value, PMG class or signature
   */
  public void addAttributeSecondValue(final Attribute an_attribute,
                                final Integer an_index) {
    my_attribute_second_indices.put(an_attribute, an_index);
  }

  /**
   * Maps PMGClass attribute onto the number of constant (in textual
   * representation of bytecode) that represents constant contatining PMG class.
   *
   * @param a_pmg_class an attribute which is mapped
   * @param an_index the number of constant (in textual representation of
   * bytecode) that represents constant contatining PMG class
   */
  public void addPMGClass(final PMGClass a_pmg_class, final Integer an_index) {
    my_pmg_classes.put(a_pmg_class, an_index);
  }


  /**
   * Maps ExceptionTable attribute onto the numbers of constants (in textual
   * representation of bytecode) that represents constants contatining
   * exception tables.
   *
   * @param an_exception_table an ExceptionTable attribute which is mapped
   * @param an_indices the numbers of constants (in textual representation of
   * bytecode) that represents constants contatining exception tables
   */
  public void addExceptionTable(final ExceptionTable an_exception_table,
                                final int[] an_indices) {
    my_exception_tables.put(an_exception_table, an_indices);
  }


  /**
   * Sets class name index.
   *
   * @param an_index an index to be set
   */
  public void setClassNameIndex(final int an_index) {
    my_class_name_index = an_index;
  }

  /**
   * Sets superclass name index.
   *
   * @param an_index an index to be set
   */
  public void setSuperclassNameIndex(final int an_index) {
    my_superclass_name_index = an_index;
  }

  /**
   * For a given field returns the number of constant (in textual representation
   * of bytecode) that represents constant contatining field's name.
   *
   * @param a_field a field for which the number is returned
   * @return the number of constant (in textual representation
   * of bytecode) that represents constant contatining field's name
   */
  public int getFieldName(final Field a_field) {
    return my_field_names.get(a_field);
  }

  /**
   * For a given field returns the number of constant (in textual representation
   * of bytecode) that represents constant contatining field's signature.
   *
   * @param a_field a field for which the number is returned
   * @return the number of constant (in textual representation
   * of bytecode) that represents constant contatining field's signature
   */
  public int getFieldSignature(final Field a_field) {
    return my_field_signatures.get(a_field);
  }

  /**
   * For a given method returns the number of constant (in textual
   * representation of bytecode) that represents constant contatining
   * method's name.
   *
   * @param a_method a method for which the number is returned
   * @return the number of constant (in textual representation
   * of bytecode) that represents constant contatining method's name
   */
  public int getMethodName(final Method a_method) {
    return my_method_names.get(a_method);
  }

  /**
   * For a given method returns the number of constant (in textual
   * representation of bytecode) that represents constant contatining
   * method's signature.
   *
   * @param a_method a method for which the number is returned
   * @return the number of constant (in textual representation
   * of bytecode) that represents constant contatining method's signature
   */
  public int getMethodSignature(final Method a_method) {
    return my_method_signatures.get(a_method);
  }

  /**
   * For a given field returns the constant (in BCEL representation of bytecode)
   * that contatins field's name.
   *
   * @param a_field a field for which the constant is returned
   * @return the constant (in BCEL representation of bytecode)
   * that contatins field's name
   */
  public int getFieldNameConstant(final Field a_field) {
    return my_field_name_constants.get(a_field);
  }

  /**
   * For a given field returns the constant (in BCEL representation of bytecode)
   * that contatins field's signature.
   *
   * @param a_field a field for which the constant is returned
   * @return the constant (in BCEL representation of bytecode)
   * that contatins field's signature
   */
  public int getFieldSignatureConstant(final Field a_field) {
    return my_field_signature_constants.get(a_field);
  }

  /**
   * For a given attribute returns the number of constant (in textual
   * representation of bytecode) that represents constant contatining
   * attribute's name.
   *
   * @param an_attribute an attribute for which the number is returned
   * @return the number of constant (in textual representation
   * of bytecode) that represents constant contatining attribute's name
   */
  public int getAttributeName(final Attribute an_attribute) {
    return my_attribute_names.get(an_attribute);
  }

  /**
   * For a given attribute returns the number of constant (in textual
   * representation of bytecode) that represents constant contatining source
   * file, constant value, PMG class or signature.
   *
   * @param an_attribute an attribute for which the number is returned
   * @return the number of constant (in textual
   * representation of bytecode) that represents constant contatining source
   * file, constant value, PMG class or signature
   */
  public int getAttributeSecondValue(final Attribute an_attribute) {
    return my_attribute_second_indices.get(an_attribute);
  }

  /**
   * For a given PMGClass attribute returns the number of constant (in textual
   * representation of bytecode) that represents constant contatining PMG class.
   *
   * @param a_pmg_class an attribute for which the number is returned
   * @return the number of constant (in textual representation of
   * bytecode) that represents constant contatining PMG class
   */
  public int getPMGClass(final PMGClass a_pmg_class) {
    return my_pmg_classes.get(a_pmg_class);
  }

  /**
   * For a given ExceptionTable attribute returns the number of constant
   * (in textual representation of bytecode) that represents constants
   * contatining exception tables.
   *
   * @param an_exception_table an ExceptionTable attribute for which the numbers
   * are returned
   * @return the numbers of constants (in textual representation of
   * bytecode) that represents constants contatining exception tables
   */
  public int[] getExceptionTable(final ExceptionTable an_exception_table) {
    return my_exception_tables.get(an_exception_table);
  }


  /**
   * Returns class name index.
   *
   * @return class name index
   */
  public int getClassNameIndex() {
    return my_class_name_index;
  }

  /**
   * Returns superclass name index.
   *
   * @return superclass name index
   */
  public int getSuperclassNameIndex() {
    return my_superclass_name_index;
  }

  /**
   *
   * @param a_field a field which the method checks whether
   * there is a mapping stored for
   * @return true if there is a mapping stored in this object
   * for field a_field
   */
  public boolean containsField(final Field a_field) {
    return my_field_names.containsKey(a_field);
  }

  /*public Object[] getAttributes() {
    return my_attribute_names.keySet().toArray();
  }

  public Object[] getMethods() {
    return my_method_names.keySet().toArray();
  }*/

  /**
   * Sets whether constant pool edition should be allowed.
   *
   * @param a_b <code>true</code> if constant pool edition should be allowed,
   * <code>false</code> otherwise
   */
  public void setCPEditionAllowed(final boolean a_b) {
    my_cp_edition_allowed = a_b;
  }

  /**
   *
   * @return <code>true</code> if constant pool edition is allowed,
   * <code>false</code> otherwise
   */
  public boolean isCPEditionAllowed() {
    return my_cp_edition_allowed;
  }

}
