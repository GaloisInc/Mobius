package annot.io;

/**
 * This interface contains all opcodes in the .class file.
 * These opcodes are used to determine which type of
 * expression should be created and loaded (from BCEL's
 * Unknown attribute's data). They are also stored in these
 * expressions as a connector.
 * 
 * @author tomekb
 */
public interface Code {

	// old opcodes, already supported by .class file format:
	public static final int TRUE = 0x00;
	public static final int FALSE = 0x01;
	public static final int AND = 0x02;
	public static final int OR = 0x03;
	public static final int IMPLIES = 0x04;
	public static final int NOT = 0x05;
	public static final int FORALL = 0x06;
	public static final int EXISTS = 0x07;
	public static final int EQ = 0x10;
	public static final int GRT = 0x11;
	public static final int LESS = 0x12;
	public static final int LESSEQ = 0x13;
	public static final int GRTEQ = 0x14;
	public static final int NOTEQ = 0x17;
	public static final int PLUS = 0x20;
	public static final int MINUS = 0x21;
	public static final int MULT = 0x22;
	public static final int DIV = 0x23;
	public static final int REM = 0x24;
	public static final int NEG = 0x25;
	public static final int BITWISEAND = 0x30;
	public static final int BITWISEOR = 0x31;
	public static final int BITWISEXOR = 0x32;
	public static final int SHL = 0x33;
	public static final int USHR = 0x34;
	public static final int SHR = 0x35;
	public static final int INT_LITERAL = 0x40;
	public static final int RESULT = 0x52; 
	public static final int FIELD_ACCESS = 0x63; // .
	public static final int COND_EXPR = 0x64; // ?:
	public static final int THIS = 0x70;
	public static final int OLD_THIS = 0x71;
	public static final int NULL = 0x72;
	public static final int FIELD_REF = 0x80;
	public static final int OLD_FIELD_REF = 0x81;
	public static final int LOCAL_VARIABLE = 0x90;
	public static final int OLD_LOCAL_VARIABLE = 0x91;
	public static final int JAVA_TYPE = 0xC0;
	public static final int MODIFIES_EVERYTHING = 0xD0;
	public static final int MODIFIES_NOTHING = 0xD1;
	public static final int MODIFIES_IDENT = 0xD2;
	public static final int MODIFIES_DOT = 0xD3;
	public static final int BOUND_VAR = 0xE0;

	// new opcodes:
	public static final int EQUIV = 0x08;
	public static final int NOTEQUIV = 0x09;
	public static final int FORALL_WITH_NAME = 0x0A;
	public static final int EXISTS_WITH_NAME = 0x0B;
	public static final int SINGLE_OCCURENCE = 0xBF;
	public static final int EXPRESSION_ROOT = 0xBE;
	public static final int MODIFIES_LIST = 0xDF;
	public static final int OLD = 0x99;

}
