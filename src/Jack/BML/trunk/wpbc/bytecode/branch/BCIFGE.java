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
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BCIFGE extends BCConditionalBranch {

	/**
	 * @param _branchInstruction
	 */
	public BCIFGE(InstructionHandle _branchInstruction) {
		super(_branchInstruction);
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
		 * @see bytecode.ByteCode#wp(formula.Formula, specification.Exsures)
		 */
		public Formula wp(Formula _normal_Postcondition, ExsuresTable _exc_Postcondition) {
			Formula wp;
		
			// in case of executing next instruction - S(t) < 0
			Formula stackTop_not_geq_0 = new Predicate2Ar(new Stack(Expression.COUNTER), new NumberLiteral(0), PredicateSymbol.LESS);
			Formula not_geq_branch = _normal_Postcondition.substitute(Expression.COUNTER, Expression.getCOUNTER_MINUS_1());
			wp = Formula.getFormula( stackTop_not_geq_0, not_geq_branch, Connector.IMPLIES);
		
			return wp;
		}
		/* (non-Javadoc)
		 * @see bytecode.branch.BCConditionalBranch#wpBranch(formula.Formula, bcclass.attributes.ExsuresTable)
		 */
		public Formula wpBranch(Formula _normal_Postcondition, ExsuresTable _exc_Postcondition) {
			Formula wp;
			//in case of jump - S(t) >= 0
			Formula stackTop_geq_0 = new Predicate2Ar(new Stack(Expression.COUNTER), new NumberLiteral(0), PredicateSymbol.GRTEQ);
			Formula geq_branch  = _normal_Postcondition.substitute(Expression.COUNTER, Expression.getCOUNTER_MINUS_1());
			wp = Formula.getFormula( stackTop_geq_0, geq_branch, Connector.IMPLIES);
			return wp;
		}


}
