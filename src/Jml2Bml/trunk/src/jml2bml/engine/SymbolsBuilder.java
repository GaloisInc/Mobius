package jml2bml.engine;

import jml2bml.ast.TreeNodeFinder;
import jml2bml.ast.ExtendedJmlTreeScanner;
import jml2bml.bytecode.BytecodeUtil;

import org.jmlspecs.openjml.JmlTree.JmlClassDecl;
import org.jmlspecs.openjml.JmlTree.JmlMethodDecl;
import org.jmlspecs.openjml.JmlTree.JmlVariableDecl;

import annot.bcclass.BCClass;
import annot.bcclass.BCMethod;
import annot.bcexpression.FieldRef;
import annot.bcexpression.LocalVariable;
import annot.bcexpression.SingleOccurence;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.util.Context;

public class SymbolsBuilder extends ExtendedJmlTreeScanner<Symbols, Symbols> {
  private final TreeNodeFinder ancestorFinder;
  private Context context;

  public SymbolsBuilder(Context context) {
    ancestorFinder = context.get(TreeNodeFinder.class);
    this.context = context;
  }
  public Symbols scan(Tree node, Symbols p) {
    return p;
  };

  public Symbols scan(Iterable<? extends Tree> nodes, Symbols p) {
    return p;
  };

  //TODO handle modifiers (static)
  @Override
  public Symbols visitJmlVariableDecl(final JmlVariableDecl node,
                                      final Symbols p) {
    if ("this".equals(node.name.toString())) {
      return p;
    }
    final Tree block = ancestorFinder.getAncestor(node, Tree.Kind.BLOCK);
    final Tree method = ancestorFinder.getAncestor(node, Tree.Kind.METHOD);
    if (method != null && block != null) {

      final Tree clazz = ancestorFinder.getAncestor(node, Tree.Kind.CLASS);
      if (method == ancestorFinder.getAncestor(clazz, Tree.Kind.METHOD)) {
        //field in an inner class
        handleField(node, clazz, p);
      } else {
        //local variable
        handleLocal(node, method, p);
      }
    } else if (method != null) {
      //parameter
      handleLocal(node, method, p);
    } else {
      //class field
      final Tree clazz = ancestorFinder.getAncestor(node, Tree.Kind.CLASS);
      handleField(node, clazz, p);
    }

    return p;
  }

  private void handleLocal(JmlVariableDecl node, Tree method, Symbols s) {
    final BCClass cl = s.findClass();
    final BCMethod m = BytecodeUtil.findMethod(((MethodTree) method).getName(),
                                               cl);
    final LocalVariable var = m.findLocalVariable(node.name.toString());
    s.put(node.name.toString(), new Variable(var, node));
  }

  private void handleField(JmlVariableDecl node, Tree clazz, Symbols s) {
    final BCClass cl = s.findClass();
    
    int nameIndex = cl.getFieldIndex(node.getName().toString());
    if (nameIndex == -1) {
      //FIXME throw an exception
    }
    s.put(node.name.toString(), new Variable((FieldRef) null, node));

  }

  @Override
  public Symbols visitBlock(BlockTree node, Symbols p) {
    return new Symbols(p);
  }

  @Override
  public Symbols visitJmlClassDecl(JmlClassDecl node, Symbols p) {
    final Symbols newSymbols = new Symbols(p);
    newSymbols.setClass(BytecodeUtil.createClass(node.name, context, p));
    return newSymbols;
  }

  @Override
  public Symbols visitJmlMethodDecl(JmlMethodDecl node, Symbols p) {
    return new Symbols(p);
  }
}
