package mobius.prover.gui.actions;

import mobius.prover.gui.ProverFileContext;
import mobius.prover.gui.TopLevelManager;
import mobius.prover.gui.editor.BasicRuleScanner;
import mobius.prover.gui.editor.ProverEditor;
import mobius.prover.plugins.AProverTranslator;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.ui.IEditorPart;


public class JumpBackward extends AProverAction {
  /**
   * The method executed when jump is triggered.
   * Jump to the previous sentence in the editor.
   */
  public void trigger() {
    final IEditorPart ep = getActiveEditor();
    if (!(ep instanceof ProverEditor)) {
      return;
    }
    final ProverFileContext pfc = new ProverFileContext((ProverEditor) ep);
    final TopLevelManager tlm = TopLevelManager.getInstance();
    
    final int oldlimit = pfc.fViewer.getSelectedRange().x;
    BasicRuleScanner parser = tlm.getParser();
    if (parser == null) {
      tlm.reset(pfc);
      parser = tlm.getParser();
    }
    if (parser == null) {
      return; // second try we give up...
    }    
    parser.setRange(pfc.fDoc, 0, oldlimit - 1);
    IToken tok; int pos = 0;
    while ((tok = parser.nextToken()) != null) {
      if (tok.isEOF()) {
        break;
      }
      if (tok == AProverTranslator.SENTENCE_TOKEN) {
        pos = parser.getTokenOffset() + parser.getTokenLength();  
      }
    } 
    pfc.fViewer.setSelectedRange(pos, 0);
    pfc.fViewer.revealRange(pos, 0);
    return;
  }
}
