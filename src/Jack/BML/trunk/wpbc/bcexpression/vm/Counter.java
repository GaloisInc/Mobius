/*
 * Created on 8 mars 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package bcexpression.vm;



import type.BCType;

import bcexpression.Expression;

import bcexpression.javatype.JavaBasicType;
import bcexpression.javatype.JavaType;



/**
 * @author io
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Counter  extends Expression {

	private final JavaBasicType type = JavaType.JavaINT ; 
	private static Counter counter ;
	
	private  Counter() {
	}
	
	public static Counter getCounter() {
		if (counter == null) {
			counter = new Counter();
			return counter;
		}
		return counter;
	}

	/* *
	 * the type of the stack counter is int by default, that's why this method inherited from the super abstarct class Expression 
	 * doesn't do anything 
	 * @see bcexpression.Expression#setType()
	 */
	public void setType() {
	}


	/* (non-Javadoc)
	 * @see bcexpression.Expression#getType()
	 */
	public BCType getType() {
		return JavaType.JavaINT;
	}
	
	public String toString() {
		return "counter";
	}
	
	public   boolean equals(Expression _expr) {
		if (_expr == this) {
			return true;
		}
		return false;
	}
	/**
	 * method overriden from superclass Expression
	 */
	public Expression substitute(Expression _e1 , Expression _e2) {
		if (this.equals(_e1 )) {
			return _e2;
		}
		return this;
	}
	
	
}
