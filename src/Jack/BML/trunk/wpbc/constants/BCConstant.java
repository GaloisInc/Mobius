/*
 * Created on Mar 3, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package constants;
/**
 * @author mpavlova
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BCConstant {
	private int cpIndex;
	private byte tag;
	//used just for the arraylength constant
	public BCConstant() {
	}
	public BCConstant(int _cpIndex) {
		cpIndex = _cpIndex;
	}
	public int getCPIndex() {
		return cpIndex;
	}
	public String toString() {
		return "_" + cpIndex;
	}
	
	public byte getTag() {
		return tag;
	}
	
	public boolean equals(BCConstant _constant) {
		if (_constant == this) {
			return true;
		}
		if ((this instanceof ArrayLengthConstant)
				&& (_constant instanceof ArrayLengthConstant)) {
			return true;
		}
		return false;
	}
}
