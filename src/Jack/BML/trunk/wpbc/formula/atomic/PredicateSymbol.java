/*
 * Created on Feb 23, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package formula.atomic;


/**
 * @author mpavlova
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface PredicateSymbol {
	public static byte GRT = 0; // ">"
	public static byte GRTEQ = 1; //">="
	public static byte LESS = 2; //"<";
	public static byte LESSEQ = 3; //"<=";
	
	public static byte GRT_uscmp = 4; // ">"
	public static byte GRTEQ_uscmp = 5; //">="
	public static byte LESS_uscmp = 6; //"<";
	public static byte LESSEQ_uscmp = 7; //"<=";
	
	public static byte EQ = 8; //"==";
	public static byte NOTEQ = 9; //"!=";
	public static byte SUBTYPE = 10;//"<:";
	public static byte INSTANCEOF = 11; // "instanceof";
		
}
