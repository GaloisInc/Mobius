package mobius.prover.gui.editor;

import mobius.prover.Prover;
import mobius.prover.gui.editor.outline.BasicContentOutline;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;



/**
 * The editor used to edit any prover language defined file.
 * It selects the right scanner to highlight with the right color
 * and parse for the right language.
 * 
 * @author J. Charles (julien.charles@inria.fr)
 */
public class ProverEditor extends TextEditor {
  /** the viewer associated with the editor. */
  private BasicSourceViewerConfig fViewer;
  /** a rule scanner to highlight the file in the editor. */
  private LimitRuleScanner fScanner;
  
  
  /**
   * Create a new editor.
   */
  public ProverEditor() {
    super();
    fViewer = new BasicSourceViewerConfig(this);
    setSourceViewerConfiguration(fViewer);
  }
  
  /**
   * Return the source viewer associated with the editor.
   * @return a source viewer, not <code>null</code>.
   */
  public BasicSourceViewerConfig getSourceViewerConfig() {
    return fViewer;
  }
  
  
  /**
   * Returns the scanner associated with the editor.
   * @return A scanner to highlight the file opened in the editor.
   */
  public LimitRuleScanner getScanner() {
    if (fScanner == null) {
      final Prover p = Prover.findProverFromFile(getTitle());
      if (p != null) {
        fScanner = p.getRuleScanner();
      }
      else { 
        fScanner = new LimitRuleScanner(null);
      }
    }
    return fScanner;
  }
  
  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("unchecked")
  public Object getAdapter(final Class cl) {
    if (cl == IContentOutlinePage.class) {
      final IContentOutlinePage cop = new BasicContentOutline(this);
      return cop;
    }
    else {
      return super.getAdapter(cl);
    }
  }
  
  /** {@inheritDoc} */
  @Override
  protected void initializeKeyBindingScopes() {
    setKeyBindingScopes(
            new String[] {"ProverEditor.context"});
       //"org.eclipse.ui.textEditorScope",          
  }

}
