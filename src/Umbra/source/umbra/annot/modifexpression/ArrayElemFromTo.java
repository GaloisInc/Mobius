package umbra.annot.modifexpression;

import umbra.annot.bcexpression.Expression;
 
public class ArrayElemFromTo extends SpecArray {
	
	public ArrayElemFromTo( Expression _start, Expression _end) {
		super(_start, _end);
	}

//	public Expression getStart() {
//		Expression start = getSubExpressions()[0];
//		return start;
//	}
//	
//	public Expression getEnd() {
//		Expression end = getSubExpressions()[1];
//		return end;
//	}
//		
//	/* (non-Javadoc)
//	 * @see bcexpression.Expression#getType()
//	 */
//	public Expression getType() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see bcexpression.Expression#substitute(bcexpression.Expression, bcexpression.Expression)
//	 
//	public Expression substitute(Expression _e1, Expression _e2) {
//		return this;
//	}*/

	/* (non-Javadoc)
	 * @see bcexpression.Expression#toString()
	 */
	public String toString() {
		Expression[] exprs =  getSubExpressions();
		return exprs[0] + " .. "+ exprs[1];
	}

/*	 (non-Javadoc)
	 * @see bcexpression.Expression#copy()
	 
	public Expression copy() {
		Expression[] exprs =  getSubExpressions();
		Expression copyStart = exprs[0].copy(); 
		Expression copyEnd = exprs[1].copy();
		ArrayElemFromTo copyThis= new ArrayElemFromTo(copyStart, copyEnd);
		return copyThis;
	}*/
}