/*
 * Created on Apr 28, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package bytecode.branch;

import org.apache.bcel.generic.InstructionHandle;

import bcclass.attributes.ExsuresTable;
import bcexpression.Expression;
import bcexpression.NumberLiteral;
import bcexpression.vm.Stack;

import formula.Connector;
import formula.Formula;
import formula.atomic.Predicate2Ar;
import formula.atomic.PredicateSymbol;

/**
 * @author mpavlova
 *
 * ifeq
 */
public class BCIFEQ extends BCConditionalBranch {

	/**
	 * @param _branchInstruction
	 */
	public BCIFEQ(InstructionHandle _branchInstruction) {
		super(_branchInstruction);
	}

	/* (non-Javadoc)
	 * @see bytecode.ByteCode#wp(formula.Formula, specification.Exsures)
	 */
	public Formula wp(
		Formula _normal_Postcondition,
		ExsuresTable _exc_Postcondition) {
		Formula wp;

		// in case of executing next instruction  - S(t) != 0
		Formula stackTop_not_eq_0 =
			new Predicate2Ar(
				new Stack(Expression.COUNTER),
				new NumberLiteral(0),
				PredicateSymbol.NOTEQ);
		Formula not_eq_branch =
			_normal_Postcondition.substitute(
				Expression.COUNTER,
				Expression.getCOUNTER_MINUS_1());
		wp =
			Formula.getFormula(
				stackTop_not_eq_0,
				not_eq_branch,
				Connector.IMPLIES);
		return wp;
	}

	/* (non-Javadoc)
	 * @see bytecode.branch.BCConditionalBranch#wpBranch(formula.Formula, bcclass.attributes.ExsuresTable)
	 */
	public Formula wpBranch(
		Formula _normal_Postcondition,
		ExsuresTable _exc_Postcondition) {
		Formula wp;
		//in case of jump - S(t) == 0
		Formula stackTop_eq_0 =
			new Predicate2Ar(
				new Stack(Expression.COUNTER),
				new NumberLiteral(0),
				PredicateSymbol.EQ);
	
		Formula eq_branch =
			_normal_Postcondition.substitute(
				Expression.COUNTER,
				Expression.getCOUNTER_MINUS_1());
		wp =
			Formula.getFormula(stackTop_eq_0, eq_branch, Connector.IMPLIES);

		return wp;
	}

}
