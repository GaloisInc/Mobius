package bytecode.arithmetic;

import org.apache.bcel.generic.IMUL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.LMUL;


import bcexpression.javatype.JavaType;

import specification.ExceptionalPostcondition;

import formula.Formula;


import bcexpression.ArithmeticExpression;
import bcexpression.Expression;
import bcexpression.ExpressionConstants;
import bcexpression.NumberLiteral;
import bcexpression.vm.Counter;
import bcexpression.vm.Stack;
import bytecode.BCConstants;
import bytecode.BCInstructionCodes;

/**
 * @author Mariela
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BCTypeMUL extends BCArithmeticInstruction {
	
	/**
	 * @param _instruction
	 */
	public BCTypeMUL(InstructionHandle _instruction, JavaType _type) {
		super(_instruction,_type);
		if (_instruction.getInstruction() instanceof IMUL ) {
			setArithmeticOperation(BCConstants.MUL_INT);
			setInstructionCode(BCInstructionCodes.IMUL);
		} else if (_instruction.getInstruction() instanceof LMUL ) {
			setArithmeticOperation(BCConstants.MUL_LONG);
			setInstructionCode(BCInstructionCodes.LMUL);
		} 
	}
	
	/**
	 * @see bytecode.BCInstruction#wp(formula.Formula, specification.ExceptionalPostcondition)
	 */
	public Formula wp(Formula _normal_Postcondition, ExceptionalPostcondition _exc_Postcondition) {
		Formula wp;
		Stack stackTop = new Stack(Expression.getCounter());
		Stack stackTop_minus_1 = new Stack(Expression.getCounter_minus_1());
		ArithmeticExpression mult = new ArithmeticExpression(stackTop, stackTop_minus_1, ExpressionConstants.MULT);
		_normal_Postcondition.substitute(Expression.getCounter(), Expression.getCounter_minus_1());
		_normal_Postcondition.substitute(stackTop_minus_1, mult);
		wp = _normal_Postcondition;
		return wp; 
	}

}
