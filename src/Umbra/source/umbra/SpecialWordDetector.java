/*
 * Created on 2005-04-30
 */
package umbra;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Detector used for finding type symbols in Bytecode (like '(V)')
 * 
 * @author Wojtek W�s
 */
public class SpecialWordDetector implements IWordDetector {
	
    /**
     * TODO write description
     * 
     * @param c TODO write description
     */
    public boolean isWordStart(char c) {
		return (Character.isWhitespace(c));
	}

    /**
     * TODO write description
     * 
     * @param c TODO write description
     */
    public boolean isWordPart(char c) {
		for (int i = 0; i < IBytecodeStrings.keys.length; i++) {
			if (c == IBytecodeStrings.keys[i]) return true;
		}
		return (c == '[' || c == '(' || c == ')');
	}

}
