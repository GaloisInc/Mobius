/*
 * Created on Apr 21, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package bytecode.stackinstruction;

import org.apache.bcel.generic.InstructionHandle;

import formula.Formula;

import bcclass.attributes.ExsuresTable;
import bcexpression.Expression;
import bcexpression.vm.Stack;
import bytecode.BCInstruction;

/**
 * @author mpavlova
 * 
 * 
 * NB: should take off the part for long / computational type 2
 * dup2
 *
 * Operation :   Duplicate the top one or two operand stack values
 *
 *  Format : dup2 	
 * 
 *  Operand Stack: 
 *  Form 1:  ..., value2, value1 ==> ..., value2, value1, value2, value1
 *  where both value1 and value2 are values of a category 1 computational type 
 *
 *  Form 2:  ..., value ==> ..., value, value
 *  where value is a value of a category 2 computational type
 * 
 * Description: Duplicate the top one or two values on the operand stack and insert the duplicated values, in the original order, 
 * one value beneath the original value or values in the operand stack. 
 * 
 *  wp =  S(t) and S(t-1) of Type_1 == > psi^n[t <-- t +2][S(t+1) <-- S(t-1)][S(t+2) <-- S(t)]
 * 			and
 * 		  S(t)    of Type_2 ==> psi^n[t <-- t +1][S(t+1) <-- S(t)]
 */
public class BCDUP2 extends BCInstruction implements BCStackInstruction {

	/**
	 * @param _instruction
	 */
	public BCDUP2(InstructionHandle _instruction) {
		super(_instruction);
	}

	/**
	 *  NB: done only for the case of computational type 1 - i.e. not treating longsS
	 */
	public Formula wp(
		Formula _normal_Postcondition,
		ExsuresTable _exc_Postcondition) {
		Formula wp;
		//		psi^n[t <-- t +2][S(t+1) <-- S(t-1)][S(t+2) <-- S(t)]
		Stack stackTop_plus2 = new Stack(Expression.COUNTER_PLUS_2);
		Stack stackTop_plus1 = new Stack(Expression.COUNTER_PLUS_1);
		Stack stackTop = new Stack(Expression.COUNTER);
		Stack stackTop_minus1 = new Stack(Expression.COUNTER_MINUS_1);

		wp =
			_normal_Postcondition.substitute(
				Expression.COUNTER,
				Expression.COUNTER_PLUS_2);
		wp = wp.substitute(stackTop_plus2, stackTop);
		wp = wp.substitute(stackTop_plus1, stackTop_minus1);
		return wp;
	}

}
