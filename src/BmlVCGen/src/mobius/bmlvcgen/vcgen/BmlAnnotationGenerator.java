package mobius.bmlvcgen.vcgen;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.util.Repository;

import mobius.bmlvcgen.bml.ClassFile;
import mobius.bmlvcgen.bml.bmllib.BcelType;
import mobius.bmlvcgen.bml.types.Type;
import mobius.bmlvcgen.finder.ClassFinder;
import mobius.bmlvcgen.finder.exceptions.FinderException;
import mobius.bmlvcgen.logging.Logger;
import mobius.bmlvcgen.main.Env;
import mobius.bmlvcgen.vcgen.exceptions.TranslationException;
import mobius.directVCGen.bico.IAnnotationGenerator;
import mobius.directVCGen.formula.Lookup;

/**
 * Annotation generator which reads bml specifications
 * and produces AnnotationDecorations and content
 * of Lookup object.
 * @author Tadeusz Sznuk (tsznuk@mimuw.edu.pl)
 */
public class BmlAnnotationGenerator implements IAnnotationGenerator {
  // Logger.
  private final Logger logger;
  // Environment
  private final Env env;
  // Object used to load classes.
  private final ClassFinder finder;
  // Object used to process classes.
  private VCClassVisitor classVisitor;
  
  /**
   * Constructor.
   * @param env Environment.
   */
  public BmlAnnotationGenerator(final Env env) {
    logger = env.getLoggerFactory().getLogger(this.getClass());
    finder = env.getClassFinder();
    this.env = env;
  }
  
  /**
   * Get variable name for function argument/local var.
   * @param i Local variable index.
   * @param name Suggested name.
   * @param type Type.
   * @return Variable name.
   */
  public static String localVarName(
      final int i, 
      final String name, 
      final Type type) {
    if (name != null) {
      return "lv_" + i + name;
    } else {
      // WARNING: keep this consistent
      // with BCEL's behaviour.
      return "lv_" + i + "arg" + (i - 1);
    }
  }
  
  /**
   * Annotate a class.
   * @param repos IGNORED.
   * @param clzz Class name.
   * @return True, unless an error occurred.
   */
  @Override
  public boolean annotateClass(final Repository repos, 
                               final ClassGen clzz) {
    classVisitor = 
      new VCClassVisitor(env, Lookup.getInst(), 
                         new ObjectType(clzz.getClassName()));
    final ClassFile clazz;  
    try {
      clazz = finder.findClass(clzz.getClassName());
    } catch (final FinderException e) {
      logger.exception(e);
      return false;
    }
    try {
      clazz.accept(classVisitor);
    } catch (final TranslationException e) {
      return false;
    }
    return true;
  }

  /**
   * Get argument names.
   * @param mg Method name and type.
   * @return List of argument names.
   */
  @Override
  public List<String> getArgumentsName(final MethodGen mg) {
    final List<String> result = new ArrayList<String>();
    int i = 0;
    final LocalVariableGen[] locals = 
      mg.getLocalVariables();
    final int delta = mg.isStatic() ? 0 : 1;
    
    for (final String arg : mg.getArgumentNames()) {
      final String name;
      if (mg.isAbstract()) {
        name = localVarName(i + delta, arg,                   
          BcelType.getInstance(mg.getArgumentType(i)));
      } else {
        name = localVarName(i + delta, 
                            locals[i + delta].getName(),                   
          BcelType.getInstance(mg.getArgumentType(i)));
      }
      result.add(name);
      i = i + 1;
    }
    return result;
  }
  
}
