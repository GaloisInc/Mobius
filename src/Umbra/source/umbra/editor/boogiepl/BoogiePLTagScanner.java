package umbra.editor.boogiepl;

import org.eclipse.jface.text.rules.RuleBasedScanner;

import umbra.editor.ColorManager;

/**
 * This method defines coloring rules in tags.
 * It has been generated automatically except obtaing
 * color values from the array in IColorValues.
 */
public class BoogiePLTagScanner extends RuleBasedScanner {

  /**
   * TODO
   */
  public BoogiePLTagScanner(final ColorManager manager, final int mod) {
    /*
    IToken[] tokens = TokenGetter.getTokenTab(manager, mod);

    WordRule linerule = new WordRule(new SpecialWordDetector());
      linerule.addWord("<init>", tokens[IColorValues.KEY]);

    IRule[] rules = new IRule[4];

    // Add rule for double quotes
    rules[0] = new SingleLineRule("\"", "\"", tokens[IColorValues.STRING], '\\');
    // Add a rule for single quotes
    rules[1] = new SingleLineRule("'", "'", tokens[IColorValues.STRING], '\\');
    // Add generic whitespace rule.
    rules[2] = linerule;
    rules[3] = new WhitespaceRule(new BoogiePLWhitespaceDetector());

    setRules(rules);
    */
  }
}
