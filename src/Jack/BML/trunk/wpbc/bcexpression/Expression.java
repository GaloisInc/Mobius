/*
 * Created on 8 mars 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package bcexpression;
import bcexpression.jml.RESULT;
import bcexpression.jml.TYPEOF;
import bcexpression.vm.Counter;

/**
 * @author io
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public abstract class Expression {
	private Expression[] subExpressions;
	
	public static final Counter COUNTER = Counter.getCounter();

	public static Expression getCOUNTER_PLUS_2() {
		return ArithmeticExpression.getArithmeticExpression(
			COUNTER,
			new NumberLiteral(2),
			ExpressionConstants.ADD);
	}

	public static Expression getCOUNTER_PLUS_1() {
		return ArithmeticExpression.getArithmeticExpression(
			COUNTER,
			new NumberLiteral(1),
			ExpressionConstants.ADD);
	}

	public static Expression getCOUNTER_MINUS_1() {
		return ArithmeticExpression.getArithmeticExpression(
			COUNTER,
			new NumberLiteral(1),
			ExpressionConstants.SUB);
	}

	public static Expression getCOUNTER_MINUS_2() {
		return ArithmeticExpression.getArithmeticExpression(
			COUNTER,
			new NumberLiteral(2),
			ExpressionConstants.SUB);
	}
	
	
	public static Expression getCOUNTER_MINUS_3() {
		return ArithmeticExpression.getArithmeticExpression(
			COUNTER,
			new NumberLiteral(3),
			ExpressionConstants.SUB);
	}
	
	public static Expression  getCOUNTER_MINUS_4() {
		return ArithmeticExpression.getArithmeticExpression(
			COUNTER,
			new NumberLiteral(4),
			ExpressionConstants.SUB);
	}
	
	public static final NULL _NULL = NULL.getNULL();

	public static final RESULT _RESULT = RESULT.getResult();
	
	protected Expression() {
	}
	
	public Expression(Expression _subExpr) {
		subExpressions = new Expression[1];
		subExpressions[0] = _subExpr;
	}
	
	public Expression(Expression _subExpr1, Expression _subExpr2) {
		subExpressions = new Expression[2];
		subExpressions[0] = _subExpr1;
		subExpressions[1] = _subExpr2;
	}
	public Expression(Expression[] _subExprs) {
		if (_subExprs != null) {
			subExpressions = _subExprs;
		}
	}

	/**
	 * @return Returns the subExpressions.
	 */
	public Expression[] getSubExpressions() {
		return subExpressions;
	}
	/**
	 * @param subExpressions
	 *            The subExpressions to set.
	 */
	protected void setSubExpressions(Expression[] subExpressions) {
		this.subExpressions = subExpressions;
	}

	/**
	 * substitute the subexpression (if there are ) equal to _e1 with _e2
	 * 
	 * @param _e1
	 * @param _e2
	 * @return - this object with the substitutions made
	 */
	public abstract Expression substitute(Expression _e1, Expression _e2);
	
	public abstract String toString();

	
	public  Expression getType() {
		return new TYPEOF( this );
	}
	/**
	 * two expressions are equals if they are from the same type and if they
	 * have the same number of subexpressions and they are equal.
	 * 
	 * @param _expr
	 * @return
	 */
	public boolean equals(Expression _expr) {
		if (_expr == null) {
			return false;
		}
		//if this object and _expr do not have the same type then they are not equal
		if (_expr.getClass() != this.getClass()) {
			return false;
		}
		//		test if the subexpressions are equal
		Expression[] subEofThis = getSubExpressions();
		Expression[] subEofExpr = _expr.getSubExpressions();
		if (((subEofExpr == null) && (subEofThis != null))
			|| ((subEofExpr != null) && (subEofThis == null))) {
			return false;
		}
		//both expressions don't have subexpressions, return true
		if ((subEofExpr == null) && (subEofThis == null)) {
			return true;
		}
		boolean subExprEquals = true;
		if ((subEofExpr != null) && (subEofThis != null)) {
			for (int i = 0; i < subEofThis.length; i++) {
				subExprEquals =
					subExprEquals && subEofThis[i].equals(subEofExpr[i]);
			}
		}
		return subExprEquals;
	}

	public Expression[] copySubExpressions() {
		if (subExpressions == null) {
			return null;
		}
		if (subExpressions.length == 0) {
			return null;
		}
		Expression[] copySubExpr = new Expression[subExpressions.length];
		for (int i = 0; i < copySubExpr.length; i++) {
			copySubExpr[i] = subExpressions[i].copy();
		}
		return copySubExpr;
	}
	/**
	 * renames subexpression of this expression
	 * Renaming must be done in such a way that no capture of variables should be done , i.e. the expr2 must be a fresh variable 
	 * @param expr1
	 * @param expr2
	 * @return
	 */
	public Expression rename(Expression expr1,  Expression expr2 ) {
		if (this.equals( expr1)) {
			return expr2;
		}
		if (subExpressions == null) {
			return this;
		}
		for (int i =0 ; i< subExpressions.length; i++) {
			subExpressions[i] = subExpressions[i].rename(expr1, expr2);
		}
		return this;
	}
	/**
	 * generalises qn expression 
	 * example : generalise(1, var ) ==> returns var  
	 * @param _e1
	 * @param _e2
	 * @return
	 */
	public Expression generalize(Expression _e1 , Expression _e2) {
		if ( this.equals(_e1)) { 
				return _e2;
		}
		if ( subExpressions == null) {
			return this;
		} 
		for (int i = 0; i < subExpressions.length; i++) {
			subExpressions[i] = subExpressions[i].generalize(_e1, _e2);
		}
		return this;
	}
	public abstract Expression copy();

}
