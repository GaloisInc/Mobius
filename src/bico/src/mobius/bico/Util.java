package mobius.bico;

import java.io.File;
import java.io.FileFilter;

import mobius.bico.executors.Constants;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.Repository;

/**
 * A class containing some utility methods.
 * 
 * @author J. Charles (julien.charles@inria.fr), P. Czarnik
 *         (czarnik@mimuw.edu.pl), L. Hubert (laurent.hubert@irisa.fr)
 */
public final class Util {
  /** the size of a tab used in writeln. */
  public static final String TAB = "  ";
  
  /**
   * @deprecated
   */
  private Util() {
  
  }
  
  /**
   * Replaces all chars not accepted by coq by "_".
   * 
   * @param raw
   *            the string to coqify
   * @return null only if str == null
   */
  public static String coqify(final String raw) {
    String str = raw;
    if (str == null) {
      return null;
    } 
    else {
      str = str.replace('.', '_');
      str = str.replace('/', '_');
      // strout = strout.replace("(","_");
      // strout = strout.replace(")","_");
      str = str.replace('>', '_');
      str = str.replace('$', '_');
      str = str.replace('<', '_');
      return str;
    }
  }
  
  /**
   * @param s
   *            a string
   * @return s with the first char toUpperCase
   */
  public static String upCase(final String s) {
    if (s != null && s.length() > 0) {
      final char c1 = Character.toUpperCase(s.charAt(0));
      return Character.toString(c1) + s.substring(1);
    } 
    else {
      return null;
    }
  }
  
  /**
   * for printing offsets.
   * 
   * @param index
   *            the offset to print
   * @return i%Z or (-i)%Z
   */
  public static String printZ(final Number index) {
    return Util.printZ(index.intValue());
  }
  
  /**
   * for printing offsets.
   * 
   * @param index
   *            the offset to print
   * @return i%Z or (-i)%Z
   */
  public static String printZ(final int index) {
    if (index < 0) {
      return "(" + index + ")%Z";
    } 
    else {
      return String.valueOf(index) + "%Z";
    }
  }
  
  /**
   * For instruction which are not implemented (yet) in Bico. Prints an error
   * message and returns a translation of the instruction.
   * 
   * @param instruction
   *            a String containing a representation of the instruction
   * @param ins
   *            the instruction object
   * @return the name of the instruction, uppercase, tagged as Unimplemented
   */
  public static String unimplemented(final String instruction,
                                     final Instruction ins) {
    final String name = ins.getName();
    System.err.println("Unimplemented " + instruction + ": " + name);
    return upCase(name) + " (* Unimplemented *)";
  }
  
  /**
   * For instructions which should not exist - this is probably an error in
   * Bico.
   * 
   * @param str
   *            a string denoting the object
   * @param ins
   *            the instruction to state as unhandled
   * @return a valid coq structure
   */
  public static String unknown(final String str, final Instruction ins) {
    final String name = ins.getName();
    System.err.println("Unknown " + str + ": " + name);
    return "Nop (* " + name + " *)";
  }
  
  /**
   * Write the error message if an unhandled type is encountered.
   * 
   * @param str
   *            the structure which was typed by the given type
   * @param t
   *            the type which was found
   */
  static void unhandled(final String str, final Type t) {
    System.err.println("Unhandled type (" + str + "): " + t.toString());
  }
  
  /**
   * Variant with some more information about instruction kind.
   * 
   * @param str
   *            a string denoting the object
   * @param ins
   *            the instruction to state as unhandled
   * @return a valid coq structure
   */
  public static String unhandled(final String str, final Instruction ins) {
    final String name = ins.getName();
    System.err.println("Unhandled " + str + ": " + name);
    return "Nop (* " + name + " *)";
  }
  
  /**
   * For instructions not handled by Bicolano.
   * 
   * @param ins
   *            the instruction to treat.
   * @return a String saying that the instruction is unhandled
   */
  public static String unhandled(final Instruction ins) {
    return unhandled("Instruction", ins);
  }
  
  /**
   * Handles type or void.
   * 
   * @param t
   *            the type to convert
   * @param repos
   *            is the repository where information on classes can be found
   * @return (Some "coq type t") or None
   * @throws ClassNotFoundException
   *             if the type couldn't be resolved
   */
  public static String convertTypeOption(final Type t, final Repository repos)
    throws ClassNotFoundException {
    if (t == Type.VOID || t == null) {
      return "None";
    }
    return "(Some " + Util.convertType(t, repos) + ")";
  }
  
  /**
   * Convert a type to a Coq valid type.
   * 
   * @param t
   *            the type to convert
   * @param repos
   *            is the repository where information on classes can be found
   * @return Coq value of t of type type
   * @throws ClassNotFoundException
   *             if the type cannot be resolved
   */
  public static String convertType(final Type t, final Repository repos)
    throws ClassNotFoundException {
    if (t instanceof BasicType) {
      return "(PrimitiveType " + Util.convertPrimitiveType((BasicType) t) + ")";
    } 
    else if (t instanceof ReferenceType) {
      return "(ReferenceType " + Util.convertReferenceType((ReferenceType) t, repos) + ")";
    } 
    else {
      unhandled("Unhandled Type: ", t);
      return "(ReferenceType (ObjectType javaLangObject (* " + t.toString() + " *) )";
    }
  }
  
  /**
   * @param type
   *            the type to convert
   * @param repos
   *            is the repository where information on classes can be found
   * @return Coq value of t of type refType
   * @throws ClassNotFoundException
   *             if a type cannot have its class resolved
   */
  public static String convertReferenceType(final ReferenceType type,
                                            final Repository repos) 
    throws ClassNotFoundException {
    String convertedType;
    if (type instanceof ArrayType) {
      convertedType = "(ArrayType " + 
                        convertType(((ArrayType) type).getElementType(), repos) + ")";
    } 
    else if (type instanceof ObjectType) {
      final ObjectType ot = (ObjectType) type;
      try {
        if (ot.referencesClassExact()) {
          convertedType = "(ClassType " + coqify(ot.getClassName()) + "Type.name)";
        } 
        else if (ot.referencesInterfaceExact()) {
          convertedType = "(InterfaceType " + coqify(ot.getClassName()) + "Type.name)";
        } 
        else {
          unhandled("ObjectType", type);
          convertedType = "(ObjectType javaLangObject (* " + type.toString() + " *) )";
        }
      }
      catch (ClassNotFoundException e) {
        final JavaClass jc = repos.findClass(ot.getClassName());
        if (jc != null) {
          if (jc.isClass()) {
            return "(ClassType " + coqify(ot.getClassName()) + "Type.name)";
          } 
          else if (jc.isInterface()) {
            return "(InterfaceType " + coqify(ot.getClassName()) + "Type.name)";
          }
        }
        throw e;
      }
    } 
    else {
      unhandled("ReferenceType", type);
      convertedType = "(ObjectType javaLangObject (* " + type.toString() + " *) )";
    }
    return convertedType;
  }
  
  /**
   * Converts a primitive type to a string.
   * @param t the type to convert
   * @return Coq value of t of type primitiveType
   */
  static String convertPrimitiveType(final BasicType t) {
    if (t.equals(Type.BOOLEAN) || t == Type.BYTE || 
        t == Type.SHORT || t == Type.INT) {
      return (t.toString().toUpperCase());
    } 
    else {
      unhandled("BasicType", t);
      return ("INT (* " + t.toString() + " *)");
    }
  }
  
  /**
   * Returns a Coq version of the package name. If the package is a.bc.de it
   * will return aBcDe
   * 
   * @param pkg the original package name
   * @return the coqified version
   */
  public static String getCoqPackageName(final String pkg) {
    String pn;
    if (pkg.length() == 0) {
      pn = "EmptyPackageName";
    } 
    else {
      final char[] pna = pkg.toCharArray();
      int j = 0;
      for (int i = 0; i < pna.length; i++) {
        if (pna[i] == '.') {
          pna[j] = Character.toUpperCase(pna[++i]);
        } 
        else {
          pna[j] = pna[i];
        }
        j++;
      }
      pn = new String(pna, 0, j);
    }
    return pn;
  }
  
  /**
   * In the class constant pool a String value representing a reference type
   * name have the following format : RefT ::= Lclname; | [ RefT | [ BasicT
   * where clName is a String representing a class name of the form a/b/c
   * BasicT ::= I | B | S.
   * 
   * @param clname String representing a reference type name 
   * in the class file format
   * @return
   */
  public static String classFormatName2Standard(final String clname) {
    String name = clname;
    // if it is an array type 
    while (name.startsWith("[")) {
      name = name.substring(1);
    }
    // if "name" denotes a basic type then return
    if (Type.BOOLEAN.getSignature().equals(name) ||
        Type.INT.getSignature().equals(name) ||
        Type.SHORT.getSignature().equals(name) ||
        Type.BYTE.getSignature().equals(name) ||
        Type.CHAR.getSignature().equals(name) ||
        Type.LONG.getSignature().equals(name) ||
        Type.FLOAT.getSignature().equals(name) ||
        Type.DOUBLE.getSignature().equals(name) ||
        Type.VOID.getSignature().equals(name)) {
      return null;
    } 
    
    // else the type is not a basic type and thus, proceed
    // to getting a well formed Java class type
    
    if (name.startsWith("L")) {
      name = name.substring(1);
    } 
    
    if (name.endsWith(";")) {
      name = name.substring(0, name.length() - 1);
    }
    name = name.replace('/', '.');
    return name;
  }
  
  
  /**
   * Removes the class suffix from the given string.
   * @param clzz the name to remove the suffix from
   * @return a string without the class suffix
   */
  public static String removeClassSuffix(final String clzz) {
    return clzz.substring(0, clzz.length() - 
                            Constants.CLASS_SUFFIX.length());
  }
  
  /**
   * Removes the coq suffix (.v) from the given string.
   * @param coqfile the string from which to remove the suffix
   * @return the same string without its last 2 characters
   */
  public static String removeCoqSuffix(final String coqfile) {
    return coqfile.substring(0, coqfile.length() - 
                            ".v".length());
  }
  
  /**
   * Filters only valid directory names (directories with names that can be packages).
   * @author J. Charles (julien.charles@inria.fr)
   */
  public static class DirectoryFilter implements FileFilter {
    /**
     * Accept directories.
     * @param cf the file to inspect
     * @return true if this is a directory, and contains no dots
     */
    public boolean accept(final File cf) {
      return cf.isDirectory() && !cf.getParent().equals(cf) && 
              cf.getName().indexOf('.') == -1;
    }
  }
  
  public static class ClassFilter implements FileFilter {
    public boolean accept(final File cf) {
      return ((cf.isFile()) && cf.getName().endsWith(Constants.CLASS_SUFFIX));
    }
  }
  
  public static File getPackageDir(final JavaClass javaClass) {
    return new File(javaClass.getPackageName().replace('.',
                                                       File.separatorChar));
  }
  
  
  public static String getTypeName(final ReferenceType rtyp) {
    String className = null;
    
    if (rtyp instanceof ObjectType) {
      final ObjectType otyp = (ObjectType) rtyp;
      className = otyp.getClassName();
    }
    else if (rtyp instanceof ArrayType) {
      final ArrayType atyp = (ArrayType) rtyp;
      final Type type = atyp.getBasicType();
      if (type instanceof ReferenceType) {
        className = getTypeName((ReferenceType) type);
      }
    }
    return className;
  }
}
