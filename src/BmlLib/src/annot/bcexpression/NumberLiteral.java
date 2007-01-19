package annot.bcexpression;

import annot.bcclass.BMLConfig;

//import bytecode_wp.bcexpression.javatype.JavaBasicType;

public class NumberLiteral extends ArithmeticExpression {
	private int literal;

	/**
	 * this constructor expects that _literal must be a correct representation
	 * of an integer litreral.
	 * 
	 * @param _literal -
	 *            a string representation of integer e.g. new
	 *            NumberLiteral("12")
	 */
	public NumberLiteral(int _literal) {
		literal = _literal;
	}

//	/**
//	 * @param value -
//	 *            a correct value
//	 * @param radix -
//	 *            the radix in which the value is interpreted
//	 */
//	/*
//	 * public NumberLiteral(int _literal, JavaBasicType _type) { literal =
//	 * _literal; type = _type; }
//	 */
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see bcexpression.Expression#getType()
//	 */
//	public Expression getType() {
//		return JavaBasicType.JavaINT;
//	}
//
//	/**
//	 * @return Returns the value.
//	 */
//	public int getLiteral() {
//		return literal;
//	}

	public String printCode(BMLConfig conf) {
		return "" + literal;
	}
	
//	public boolean equals(Expression _expr) {
//		try {
//			boolean equals = super.equals(_expr);
//
//			if (equals == false) {
//				return false;
//			}
//			NumberLiteral nl = (NumberLiteral) _expr;
//			if (getType() != nl.getType()) {
//				return false;
//			}
//			if (getLiteral() != nl.getLiteral()) {
//				return false;
//			}
//			return true;
//		} catch (NullPointerException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	public Expression substitute(Expression _e1, Expression _e2) {
//		return this;
//	}
//
	public Expression copy() {
		return this;
	}
}
