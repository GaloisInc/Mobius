package annot.bcexpression;

import java.util.HashMap;

import annot.bcexpression.javatype.JavaType;
import annot.bcexpression.util.ExpressionWalker;
import annot.io.AttributeReader;
import annot.io.AttributeWriter;
import annot.io.Code;
import annot.io.ReadAttributeException;
import annot.textio.BMLConfig;
import annot.textio.IDisplayStyle;
import annot.textio.Priorities;

/**
 * This class represents an BML expression. It's a superclass
 * for all types of expression. To add a new subexpression
 * create a subclass of this class or one of it's subclasses
 * (eg. AbstractIntExpression for expression that returns
 * integer value, AbstractFormula for boolean expression).
 * 
 * @author tomekb
 */
public abstract class BCExpression {

	/**
	 * Type of expression, from {@link annot.io.Code} interface,
	 * used also for .class file i/o operations.
	 */
	private int connector = -1;

	/**
	 * Array of subexpressions.
	 */
	private BCExpression[] subExpr;

	/**
	 * Expression's priority.
	 */
	private int priority = 0;

	/**
	 * Size of expression tree (accurate only after calling
	 * {@link #computeSize()}.
	 */
	private int treeSize = -1;

	/**
	 * Parent node, accurate only in iterator.
	 */
	private BCExpression parent;

	/**
	 * Position in parent's subexpression list
	 * (parent.subExpr[childNo] == this).
	 * Accurate only in iterator.
	 */
	private int childNo = -1;

	/**
	 * This field should be used from outsite this library,
	 * to enable storeing data specific for one computation
	 * type. If you'r application need storeing it's data
	 * in AST used by other application, it should obtain
	 * it's global identifier using {@link #getNewHandle()}
	 * and read / write data to this hashMap at this key.
	 */
	private HashMap<Object, Object> privateData;
	
	/**
	 * A constructor for 0Arg expressions.
	 * Do NOT use it, use {@link #BCExpression(int)} instead.
	 */
	@Deprecated
	protected BCExpression() {
		this.subExpr = new BCExpression[0];
		init();
		this.priority = getPriority();
	}

	/**
	 * Another constructor for 0Arg expressions.
	 * 
	 * @param connector - type of expression
	 * 		(from annot.io.Code interface).
	 */
	protected BCExpression(int connector) {
		this.connector = connector;
		this.priority = getPriority();
		this.subExpr = new BCExpression[0];
		init();
	}

	/**
	 * A Constructor for unary expressions.
	 * 
	 * @param connector - type of expression
	 * 		(from annot.io.Code interface),
	 * @param subExpr - subexpression.
	 */
	protected BCExpression(int connector, BCExpression subExpr) {
		this.connector = connector;
		this.priority = getPriority();
		this.subExpr = new BCExpression[1];
		this.subExpr[0] = subExpr;
		init();
	}

	/**
	 * A constructor for binary expressions.
	 * 
	 * @param connector - type of expression
	 * 		(from annot.io.Code interface),
	 * @param left - left subexpression,
	 * @param right - right subexrpession.
	 */
	protected BCExpression(int connector, BCExpression left, BCExpression right) {
		this.connector = connector;
		this.priority = getPriority();
		this.subExpr = new BCExpression[2];
		this.subExpr[0] = left;
		this.subExpr[1] = right;
		init();
	}

	/**
	 * A constructor from AttributeReader. After reading
	 * type of expression (connector) Attribuet reader
	 * creates a proper subclass of BCExpression using
	 * this constructor, passing to it connector and
	 * AttributeReader itself. This constructor calls
	 * init() method for private fields initializetion
	 * and than read(ar, root) method that reads expression
	 * data ()including it's subexpression) from given
	 * AttributeReader.
	 * 
	 * @param ar - stream to load from,
	 * @param root - expression type (connector).
	 * @throws ReadAttributeException - if connector + stream
	 * 		in <code>ar</code> doesn't represent any
	 * 		expression from constructing subclass.
	 */
	protected BCExpression(AttributeReader ar, int root)
			throws ReadAttributeException {
		this.connector = root;
		this.subExpr = new BCExpression[0];
		init();
		read(ar, root);
		this.priority = getPriority();
	}

	/**
	 * This method should be implemented in subclasses
	 * to return expression's String representation.
	 * Subclasses should call printCode(conf) method instead
	 * of this method for recursive displaying subecpressions,
	 * unless they want them to be displayed in the same line
	 * and without parenthness.
	 * 
	 * @param conf - see {@link BMLConfig}.
	 * @return String representation of expression,
	 * 		without (block marks (used for line-breaking
	 * 		by prettyPrinter) and parenthness) at root.
	 */
	protected abstract String printCode1(BMLConfig conf);

	/**
	 * @return Simple String representation of this
	 * 		expression, for debugging only.
	 */
	@Override
	public abstract String toString();

	/**
	 * Reads the exression from an AttributeReader (except
	 * connector, that has been already read and set).
	 * 
	 * @param ar - stream to load from,
	 * @param root - connentor.
	 * @throws ReadAttributeException - if connector + stream
	 * 		in <code>ar</code> doesn't represent any
	 * 		expression from calling subclass.
	 */
	protected void read(AttributeReader ar, int root)
			throws ReadAttributeException {
		throw new RuntimeException("'read' method unavaliable for this class.");
	}

	/**
	 * Writes this expression to AttributeWirter.
	 * While overriding, don't forget to write connector first,
	 * then other data and finally call writeSubExpressions(aw)
	 * to write all subexpressions recursivly while
	 * implementing this method in subclasses.
	 * 
	 * @param aw - stream to save to.
	 */
	public void write(AttributeWriter aw) {
		aw.writeByte(connector);
		writeSubExpressions(aw);
	}

	/**
	 * Initialize private data of subclass.
	 * While overriding, use this method instead of initialize
	 * private fields in constructor, becouse read() method
	 * is called in spuerclass constructor
	 * (from AttributeReader, that is, before calling subclass
	 * constructor).
	 */
	protected void init() {};

	/**
	 * @return priority of this expression
	 * 		(from annot.textio.Priorities).
	 */
	protected int getPriority() {
		return Priorities.getPriority(connector);
	}

	/**
	 * This method should check if all subexpression have
	 * correct types and return type of this expression.
	 * You can assume that all subexpressions are not-null.
	 * 
	 * @return JavaType of result of this exrpession,
	 * 		or null if it's invalid (if one of it's
	 * 		subexpression have wrong type or is invalid).
	 */
	//XXX shouldn't it return boolean value?
	protected abstract JavaType checkType1();

	/**
	 * Checks if all subexpressions have correct types
	 * (recursivly) and return type of this expression.
	 * 
	 * @return (bmllib's) JavaType of result of this
	 * 		exrpession, or null if it's invalid (if one or more
	 * 		of it's subexpression have wrong type
	 * 		or are invalid).
	 */
	public final JavaType checkType() {
		if (subExpr == null)
			return null;
		for (int i = 0; i < subExpr.length; i++) {
			if (subExpr[i] == null)
				return null;
			JavaType st = subExpr[i].checkType();
			if ((st == null) || (st != subExpr[i].getType()))
				return null;
		}
		return checkType1();
	}

	/**
	 * This method should return (bmllib's) type of this
	 * expression without checking whether all subexpressions
	 * have correct types (recursivly).
	 * 
	 * @return (bmllib's) JavaType of result of this
	 * 		exrpession, or null if it's invalid.
	 */
	public abstract JavaType getType1();

	/**
	 * Returns type of this expression without checking
	 * whether all subexpressions have correct types
	 * (recursivly).
	 * 
	 * @return (bmllib's) JavaType of result of this
	 * 		exrpession, or null if it's invalid.
	 */
	public final JavaType getType() {
		if (subExpr == null)
			return null;
		for (int i = 0; i < subExpr.length; i++)
			if (subExpr[i] == null)
				return null;
		return getType1();
	}

	/**
	 * Prints expression as a whole attribute.
	 * This method should not be called by subclasses.
	 * 
	 * @param conf - see {@link BMLConfig},
	 * @param usedc - # of characters displayed in current
	 * 		line before this expression (excluding comment
	 * 		mark - annot.textio.IdisplayStyle.comment_*).
	 * @return pretty printed String representation
	 * 		of this expression.
	 */
	public String printLine(BMLConfig conf, String prefix) {
		conf.setRoot_pri(Priorities.MAX_PRI);
		conf.incInd();
		conf.setGoControlPrint(false);
		computeSize();
		String str = printCode(conf);
		prefix = conf.getPrettyPrinter().cleanup(prefix);
		str = conf.getPrettyPrinter().breakLines(str, prefix.length() + 1);
		if (IDisplayStyle.goControlPrint) {
			str += "\n------------------------------------------\n"
					+ printCode(conf);
			str += "\n" + conf.getPrettyPrinter().cleanup(printCode(conf));
			conf.setGoControlPrint(true);
			str += "\n" + printCode(conf);
		}
		conf.decInd();
		return conf.getIndent() + prefix + " " + str + "\n";
	}

	/**
	 * Adds parenthness and block marks to the root
	 * of this expression.
	 * 
	 * @param conf - see {@link BMLConfig}.
	 * @return String representation of expression, without
	 * 		line-breaking.
	 */
	private String printCode2(BMLConfig conf) {
		int rp = conf.getRoot_pri();
		if (priority != Priorities.PRI_TRANSPARENT)
			conf.setRoot_pri(priority);
		String str = "";
		boolean lvlinc = (rp != priority);
		if (subExpr.length == 0) {
			lvlinc = true;
		} else if (subExpr.length == 1)
			lvlinc = false;
		if (lvlinc)
			str += IDisplayStyle.expr_block_start;
		String sub = printCode1(conf);
		if ((subExpr.length == 1)
			&& (priority != Priorities.PRI_TRANSPARENT)) // ~~
				if (subExpr[0].subExpr.length == 1) {
					conf.setRoot_pri(conf.getRoot_pri() - 1);
					sub = printCode1(conf); // 2^n
					conf.setRoot_pri(priority);
				}
		str += sub;
		if (lvlinc)
			str += IDisplayStyle.expr_block_end;
		boolean addParenthness = priority > rp;
		int chn = childNo;
		BCExpression parn = parent;
		while (parn != null) {
			if (parn.getPriority() != Priorities.PRI_TRANSPARENT)
				break;
			chn = parn.childNo;
			parn = parn.getParent();
		}
		if (parn != null)
			if ((priority == rp)
					&& (((!Priorities.isAssociative(parn.connector, Priorities.ARIGHT))
							&& (chn > 0))
						|| ((!Priorities.isAssociative(parn.connector, Priorities.ALEFT))
								&& (chn < parent.getSubExprCount() - 1))
						)
				)
				addParenthness = true;
		if (addParenthness) {
			String str2 = "";
			for (int i = 0; i < str.length(); i++) {
				char ch = str.charAt(i);
				if ((ch == ' ') || (ch == '\n') || (ch == '*')) {
					str2 += ch;
				} else {
					str2 += IDisplayStyle.expr_block_start + "("
							+ str.substring(i, str.length()) + ")"
							+ IDisplayStyle.expr_block_end;
					break;
				}
			}
			str = str2;
		}
		conf.setRoot_pri(rp);
		return str;
	}

	/**
	 * Prints expression in debug (raw) mode.
	 * 
	 * @param conf - see {@link BMLConfig}.
	 * @return debug string representation of expression,
	 * 		with simple line-breaking and indention.
	 */
	public String controlPrint(BMLConfig conf) {
		String str = this.getClass().getName();
		str = "new " + str.substring(str.lastIndexOf(".") + 1);
		int length = 0;
		if (subExpr != null)
			length = subExpr.length;
		conf.incInd();
		str += "(";
		if (this.getConnector() == Code.TRUE) {
			str += "true";
		} else if (this.getConnector() == Code.FALSE) {
			str += "false";
		} else {
			str += connector;
		}
		for (int i = 0; i < length; i++)
			str += ",\n" + conf.getIndent() + subExpr[i].controlPrint(conf);
		str += ")";
		conf.decInd();
		return str;
	}

	/**
	 * Prints expression as a string. This method should not
	 * be called in attributes nor subclasses to get full
	 * string representation. Use printLine(conf)
	 * in attributes and printCode1(conf) in subclasses.
	 * 
	 * @param conf - see {@link BMLConfig}.
	 * @return string representation of expression,
	 * 		without line-breaking.
	 */
	public String printCode(BMLConfig conf) {
		if (conf.isGoControlPrint()) {
			return controlPrint(conf);
		} else {
			return printCode2(conf);
		}
	}

	/**
	 * Displays code of a subexpression without adding
	 * parenthness at the root nor formatting (at the root).
	 * 
	 * @param conf - see {@link BMLConfig}.
	 * @return string representation of expression,
	 * 		without line-breaking.
	 */
	public String printRawCode(BMLConfig conf) {
		//XXX protected.
		return printCode1(conf);
	}
	
	/**
	 * Writes subexpressions to given AttributeWriter.
	 * 
	 * @param aw - stream to write to.
	 */
	protected void writeSubExpressions(AttributeWriter aw) {
		for (int i = 0; i < subExpr.length; i++)
			subExpr[i].write(aw);
	}

	/**
	 * Replaces this expression with given one (updates
	 * it's parent subexpression table).<br>
	 * Be sure to call {@link #computeSize(BCExpression, int)}
	 * for this expression (eg. by calling
	 * {@link #computeSize()} at the root expression).
	 * 
	 * @param expr - expression to replace this expression.
	 */
	public void replaceWith(BCExpression expr) {
		expr.childNo = childNo;
		expr.parent = parent;
		parent.setSubExpr(childNo, expr);
	}
	
	/**
	 * Computes parent node and subtree size for whole subtree.
	 * This should be called ONLY at the root
	 * of the expression.
	 * 
	 * @return size of expression tree.
	 */
	public int computeSize() {
		return computeSize(null, -1);
	}
	
	/**
	 * Computes parent node and subtree size for whole subtree.
	 *
	 * @param parent - this expression's parent,
	 * @param chn - position (subexpression numer) of this
	 * 		expression in <code>parent</code>.
	 * @return size of expression tree.
	 */
	private int computeSize(BCExpression parent, int chn) {
		this.parent = parent; //XXX doesn't work!
		this.childNo = chn; //XXX doesn;t work!
		treeSize = 0;
		for (int i=0; i<subExpr.length; i++)
			if (subExpr[i] != null)
				treeSize += subExpr[i].computeSize(this, i);
		return treeSize + 1;
	}

	/**
	 * Fills all subtree nodes to the given array.
	 * 
	 * @param arr - array to write subtree nodes to.
	 * 		Should be initialized and long enought to
	 * 		fit all subtree nodes after <code>pos</code>
	 * 		position,
	 * @param pos - initial position (index)
	 * 		in <code>arr</code> from with it sould be
	 * 		filled in,
	 * @param suffix - tree walk order (prefix order for false
	 * 		and suffix order for true).
	 * @return next free position in <code>arr</code>
	 * 		(pos + size of subtree).
	 */
	private int getAllNodes(BCExpression[] arr, int pos, boolean suffix) {
		if (!suffix)
			arr[pos++] = this;
		for (int i=0; i<subExpr.length; i++)
			pos = subExpr[i].getAllNodes(arr, pos, suffix);
		if (suffix)
			arr[pos++] = this;
		return pos;
	}
	
	/**
	 * @param suffix - tree walk order (prefix order for false
	 * 		and suffix order for true).
	 * @return All subtree nodes (recursively) of this
	 * 		expression tree.
	 */
	public BCExpression[] getAllNodes(boolean suffix) {
		int size = computeSize();
		BCExpression[] allNodes = new BCExpression[size];
		if (size != getAllNodes(allNodes, 0, suffix))
			throw new RuntimeException("Error in BCExpression.getAllNodes(): "
				+ size + " != " + getAllNodes(allNodes, 0, suffix));
		return allNodes;
	}

	/**
	 * Iterates trough expression tree.
	 * 
	 * @param suffix - processing order (true ==> suffix
	 * 		order, false ==> prefix order),
	 * @param ew - visitor (function that will be applied
	 * 		to each node of the expression's tree).
	 * @return given ExrpessionWalker (<code>ew</code>).
	 */
	public ExpressionWalker iterate(boolean suffix, ExpressionWalker ew) {
		computeSize();
		iterate1(null, -1, suffix, ew);
		return ew;
	}

	/**
	 * Iterates trough expression tree.
	 * 
	 * @param parent - parent of this expression,
	 * @param chn - position of parent's child equal that
	 * 		is equal (==) to this object,
	 * @param suffix - processing order (true ==> suffix
	 * 		order, false ==> prefix order),
	 * @param ew - visitor (function that will be applied
	 * 		to each node of the expression's tree).
	 */
	private void iterate1(BCExpression parent, int chn, boolean suffix, ExpressionWalker ew) {
		this.parent = parent;
		this.childNo = chn;
		if (!suffix)
			ew.iter(parent, this);
		for (int i=0; i<subExpr.length; i++)
			subExpr[i].iterate1(this, i, suffix, ew);
		if (suffix)
			ew.iter(parent, this);
	}

	/**
	 * @return expression type (connector),
	 * 		from annot.io.Code interface.
	 */
	public int getConnector() {
		return connector;
	}

	/**
	 * @param index - index of subespression.
	 * @return subexpression of this expression of given index
	 * 		(with 0 for left-most subexpression)
	 */
	public BCExpression getSubExpr(int index) {
		return subExpr[index];
	}

	/**
	 * @return number of subexpressions.
	 */
	public int getSubExprCount() {
		return subExpr.length;
	}

	/**
	 * @return subexpression array.
	 */
	public BCExpression[] getAllSubExpr() {
		return subExpr;
	}

	/**
	 * Sets given subexpression.
	 * 
	 * @param index - index of the subexpression to be set.
	 * @param subExpr - new subexpression to be set at
	 * 		<code>index</code> position.
	 */
	public void setSubExpr(int index, BCExpression subExpr) {
		this.subExpr[index] = subExpr;
	}

	/**
	 * Removes all subexpressions and initializes
	 * subexpression array.
	 * 
	 * @param n - subexpression count (size of the array).
	 */
	protected void setSubExprCount(int n) {
		this.subExpr = new BCExpression[n];
	}

	/**
	 * @return parent node. Accurate only in iterator.
	 */
	public BCExpression getParent() {
		return parent;
	}

	/**
	 * @return size of expression tree (accurate only after
	 * 		calling {@link #computeSize()}.
	 */
	public int getTreeSize() {
		return treeSize;
	}

	/**
	 * @return new key to store application-specific data
	 * 		in AST nodes.
	 */
	public static Object getNewHandle() {
		return new Object();
	}

	/**
	 * Gives application-specific data stored in this node.
	 * 
	 * @param handle application identifier, recived using
	 * 		{@link #getNewHandle()} method.
	 * @return application-specific data stored in this node.
	 */
	public Object getPrivateData(Object handle) {
		if (!privateData.containsKey(handle))
			return null;
		return privateData.get(handle);
	}

	/**
	 * Sets application-specific data that should be stored
	 * in this node.
	 * 
	 * @param handle application identifier, recived using
	 * 		{@link #getNewHandle()} method.
	 * @param data - application-specific data that should
	 * 		be stored in this node.
	 */
	public void setPrivateData(Object handle, Object data) {
		privateData.put(handle, data);
	}

}
