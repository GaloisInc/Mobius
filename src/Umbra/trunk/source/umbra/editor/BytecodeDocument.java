/*
 * @title       "Umbra"
 * @description "An editor for the Java bytecode and BML specifications"
 * @copyright   "(c) 2006-2008 University of Warsaw"
 * @license     "All rights reserved. This program and the accompanying
 *               materials are made available under the terms of the LGPL
 *               licence see LICENCE.txt file"
 */
package umbra.editor;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.MethodGen;
import org.eclipse.jface.text.Document;

import umbra.instructions.BytecodeController;
import umbra.lib.BMLParsing;
import umbra.lib.UmbraException;
import umbra.lib.UmbraLocationException;
import umbra.lib.UmbraMethodException;
import annot.bcclass.BCClass;

/**
 * This class is an abstract model of a byte code textual document.
 * It mainly handles the synchronisation between a byte code file and a
 * Java source code file (in both directions).
 *
 * It mediates between an editor which edits the document and the line structure
 * of the byte code document. It also provides the connection with BMLLib
 * structures.
 *
 * @author Tomasz Batkiewicz (tb209231@students.mimuw.edu.pl)
 * @author Wojciech Wąs (ww209224@students.mimuw.edu.pl)
 * @version a-01
 */
public class BytecodeDocument extends Document {

  /**
   * The byte code editor that manipulates the current document.
   */
  private BytecodeEditor my_bcode_editor;

  /**
   * The object which contains the internal Umbra representation of the
   * current document.
   */
  private BytecodeController my_bcc;

  /**
   * This flag is <code>true</code> when the internal structures that connect
   * the text .btc file with the BCEL representation are initialised.
   */
  private boolean my_ready_flag; //@ initially false;

  /**
   * BML-annotated byte code (text + AST) displayed in this
   * editor. All byte code modifications should be made
   * on this object.
   */
  private BMLParsing my_bmlp;

  /**
   * It is true when the processing is inside the initialisation of the
   * document. This is to forbid double initialisation inside the init method.
   */
  private boolean my_is_in_init;


  /**
   * This constructor creates a {@link BytecodeDocument} and associates
   * a fresh, non-initialised model of the document.
   */
  public BytecodeDocument() {
    super();
    this.my_bcc = new BytecodeController();
  }

  /**
   * @return the current representation of the Java class associated with
   * the document.
   */
  public final JavaClass getJavaClass() {
    return getBmlp().getBcc().getJC();
  }

  /**
   * The method returns the {@link ClassGen} object for the current
   * representation of the Java class file. Each time this method is called
   * a new object is generated.
   *
   * @return the current generator of the Java class file
   */
  public final ClassGen getClassGen() {
    return new ClassGen(getBmlp().getBcc().getJC());
  }

  /**
   * This method updates the byte code editor associated with the
   * current document. Additionally, it updates the fields that
   * contain the representation of BML.
   *
   * @param an_editor the byte code editor
   * @param a_bmlp a BMLLib representation of the current class
   */
  public final void setEditor(final BytecodeEditor an_editor,
                              final BMLParsing a_bmlp) {
    my_bcode_editor = an_editor;
    an_editor.setDocument(this);
    if (a_bmlp != my_bmlp) {
      my_bmlp = a_bmlp;
    }
  }

  /**
   * @return the editor for the current byte code document
   */
  public final BytecodeEditor getEditor() {
    return my_bcode_editor;
  }

  /**
   * @return <code>true</code> when the document change listener has already
   * been added to the document
   */
  public final boolean isListenerAdded() {
    return !getDocumentListeners().isEmpty();
  }

  /**
   * Informs if the internal data structures that provide the model of the
   * document are initialised.
   *
   * @return <code>true</code> when the structures are initialised,
   *   <code>false</code> otherwise
   */
  public boolean isReady() {
    return my_ready_flag;
  }

  /**
   * This method initialises the internal structures of the byte code
   * controller. In particular it initialises the object that
   * manages the BCEL operations and enables the relevant actions
   * in the Umbra plugin byte code contributor.
   * @param a_comment_array contains the texts of end-of-line comments, the
   *   i-th entry contains the comment for the i-th instruction in the file,
   *   if this parameter is null then the array is not taken into account
   * @param an_interline as above for multi-line comments
   * @throws UmbraLocationException thrown in case a position has been reached
   *   which is outside the current document
   * @throws UmbraMethodException in case the textual representation has
   *   more methods than the internal one
   */
  public void init(final String[] a_comment_array,
                   final String[] an_interline)
    throws UmbraLocationException, UmbraMethodException {
    final String str = my_bcc.init(this, a_comment_array, an_interline);
    my_ready_flag = true; //this causes the following line not to loop
    setTextWithDeadUpdate(str);
    my_bmlp.setCodeString(str);
    my_bcc.checkAllLines(0, getNumberOfLines() - 1);
  }

  /**
   * The method updates the internal structures of the document to reflect the
   * change. The change is already present in the textual representation of the
   * document.
   *
   * @param a_start the first changed line
   * @param an_oldend the last line of the change in the old version of the
   *   document
   * @param a_newend the last line of the change in the current version of the
   *   document
   * @throws UmbraException in case the change cannot be incorporated
   *   into the internal structures
   * @throws UmbraLocationException thrown in case a position has been reached
   *   which is outside the current document
   */
  public void updateFragment(final int a_start,
                             final int an_oldend,
                             final int a_newend)
    throws UmbraException, UmbraLocationException {
    my_bcc.removeIncorrects(a_start, an_oldend);
    my_bcc.addAllLines(this, a_start, an_oldend, a_newend);
    my_bcc.checkAllLines(a_start, a_newend);
  }

  /**
   * @return BML-annotated byte code (text + AST) displayed
   *   in this editor. All byte code modifications should
   *   be made through this object.
   *
   * @see BMLParsing
   */
  public BMLParsing getBmlp() {
    return my_bmlp;
  }

  /**
   * Commits all the changes in the BMLLib representation of a class
   * to a BCEL {@link JavaClass} object which is responsible for
   * saving the class content to a class file.
   */
  public void updateJavaClass() {
    //obtaining JavaClass from my_bmlp field
    final BCClass bcc = getBmlp().getBcc();
    bcc.saveJC();
  }

  /**
   * This method returns the textual representation of the byte code. The
   * textual representation is generated from the BMLlib structures.
   *
   * @return the textual representation of the byte code
   */
  public String printCode() {
    return getBmlp().getBcc().printCode();
  }

  /**
   * Returns the {@link MethodGen} structure which handles the modifications
   * in the method of the given number.
   *
   * @param a_method_no the number of the method to be returned
   * @return the BCEL structure which handles the editing of the given method
   * @throws UmbraMethodException thrown in case the given method number
   *   is outside the range of available methods
   */
  public MethodGen getMethodGen(final int a_method_no)
    throws UmbraMethodException {
    try {
      return my_bmlp.getBcc().getMethod(a_method_no).getBcelMethod();
    } catch (IndexOutOfBoundsException e) {
      throw new UmbraMethodException(a_method_no);
    }
  }

  /**
   * This method checks if the current document is in the initialisation process
   * so that the changes of its content should not be processed.
   *
   * @return <code>true</code> when the document is in the initialisation
   *   process, <code>false</code> otherwise
   */
  public boolean isInInit() {
    return my_is_in_init;
  }

  /**
   * This method changes the content of this document in such a way that
   * the update of the internal structures is not done. This is used when
   * the initial structure is generated.
   *
   * @param a_string the text of the document
   */
  public void setTextWithDeadUpdate(final String a_string) {
    my_is_in_init = true;
    set(a_string);
    my_is_in_init = false;
  }

  /**
   * Returns the information about the correctness of the last edited annotation
   * in the current document.
   *
   * @return <code>true</code> when the last annotation is syntactically correct
   *   and <code>false</code> otherwise
   */
  public boolean annotCorrect() {
    return my_bmlp.isCorrect();
  }

  /**
   * Returns the error message for the last edited annotation in the current
   * document.
   *
   * @return <code>true</code> when the last annotation is syntactically correct
   *   and <code>false</code> otherwise
   */
  public String getAnnotError() {
    return my_bmlp.getErrorMsg();
  }

  /**
   * Returns the abstract representation of the document contents.
   *
   * @return the abstract representation of the document contents
   */
  public BytecodeController getModel() {
    return my_bcc;
  }
}
