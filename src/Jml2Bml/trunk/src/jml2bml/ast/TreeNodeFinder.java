/*
 * @title       "Jml2Bml"
 * @description "JML to BML Compiler"
 * @copyright   "(c) 2008-01-06 University of Warsaw"
 * @license     "All rights reserved. This program and the accompanying
 *               materials are made available under the terms of the LGPL
 *               licence see LICENCE.txt file"
 */
package jml2bml.ast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jmlspecs.openjml.JmlTree.JmlClassDecl;

import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;

/**
 * An utility class responsible for finding ancestors of nodes in a tree.
 *
 * @author Jedrek (fulara@mimuw.edu.pl)
 * @author kjk    (kjk@mimuw.edu.pl)
 */
public class TreeNodeFinder {
  /**
   * Class responsible for filling {@code parents} field in
   * {@code AncestorFinder} class.
   *
   * @author Jedrek (fulara@mimuw.edu.pl)
   * @author kjk    (kjk@mimuw.edu.pl)
   *
   */
  private class ParentFinder extends ExtendedJmlTreeScanner<Tree, Tree> {

    public Tree scan(final Tree node, final Tree p) {
      parents.put(node, p);
      return super.scan(node, node);
    }

    /**
     * When a node in a tree has many children (ie. block has list of statements,
     * class has a list of members), this method is executed. It joins executing
     * methods for blocks, classes etc. separately.
     */
    public Tree scan(Iterable<? extends Tree> nodes, Tree p) {
      Tree result = super.scan(nodes, p);
      final Iterator<? extends Tree> iter = nodes.iterator();
      if (iter.hasNext()) {
        Tree stmt = iter.next();
        while (iter.hasNext()) {
          final Tree next = iter.next();
          nextSiblingMap.put(stmt, next);
          stmt = next;
        }
        nextSiblingMap.put(stmt, null);
      }
      return result;
    }

    public Tree visitJmlClassDecl(JmlClassDecl classNode, Tree node) {
      final Tree result = super.visitClass(classNode, node);
      final Iterator<? extends Tree> iter = classNode.getMembers().iterator();
      if (iter.hasNext()) {
        Tree stmt = iter.next();
        while (iter.hasNext()) {
          final Tree next = iter.next();
          nextSiblingMap.put(stmt, next);
          stmt = next;
        }
        nextSiblingMap.put(stmt, null);
      }
      return result;
    }
  }

  /** Map of parents of tree nodes. */
  private Map<Tree, Tree> parents;

  /** Maps a tree node to next sibling in a tree. */
  private Map<Tree, Tree> nextSiblingMap;

  public TreeNodeFinder(final Tree tree) {
    parents = new HashMap<Tree, Tree>();
    nextSiblingMap = new HashMap<Tree, Tree>();
    tree.accept(new ParentFinder(), tree);
  }

  /**
   * Finds ancestor of treeElement in current tree.
   *
   * @param treeElement the element to find ancestor of
   * @param ancestorKind the kind of ancestor to find
   * @return the ancestor of treeElement that is of kind ancestorKind,
   *         when no ancestor of that kind found {@code null} is a result.
   */
  public Tree getAncestor(final Tree treeElement, final Kind ancestorKind) {
    if (!parents.containsKey(treeElement))
      throw new RuntimeException("tree element not from current tree");

    Tree element = parents.get(treeElement);
    while (element != null) {
      if (element.getKind() == ancestorKind)
        return element;
      element = parents.get(element);
    }
    return null;
  }

  /**
   * Finding ancestor of element that is of desired type.
   *
   *
   * @param treeElement element to find ancestor of
   * @param ofClass the class we want to find
   * @return the ancestor of treeElement that is assignable to type ofClass
   */
  //TODO: Is this dirty, should be changed?? (we can't locate JML nodes differently now)
  public Tree getAncestor(final Tree treeElement, final Class<?> ofClass) {
    if (!parents.containsKey(treeElement))
      throw new RuntimeException("tree element not from current tree");

    Tree element = parents.get(treeElement);
    while (element != null) {
      if (ofClass.isAssignableFrom(element.getClass()))
        return element;
      element = parents.get(element);
    }
    return null;
  }

  /**
   * Method finds a sibling of tree node in ast tree.
   *
   * @param tree the elem to get sibling of
   * @return a tree node following tree in parameter
   *         when no node after {@code null} is a result.
   */
  public Tree getNextSibling(final Tree tree) {
    return nextSiblingMap.get(tree);
  }
}
