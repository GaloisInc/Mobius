/*
 * Created on 8 mars 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package constants;

import org.apache.bcel.classfile.ConstantClass;


/**
 * @author io
 * basically this are the constant pool data structures that are pointing to Constant_Class_Info structures.
 * 
 *   CONSTANT_Fieldref_info {
    	u1 tag;
    	u2 class_index;
    	u2 name_and_type_index;
    }


    CONSTANT_Methodref_info {
    	u1 tag;
    	u2 class_index;
    	u2 name_and_type_index;
    }


    CONSTANT_InterfaceMethodref_info {
    	u1 tag;
    	u2 class_index;
    	u2 name_and_type_index;
    }

 */
public class BCConstantClass extends  BCConstant {
	
	//may be it is not needed 
	private int nameIndex;
	
	private String name;
	
	public  BCConstantClass(int _cpIndex,  int _nameIndex, String _name){
		super(_cpIndex);
		nameIndex  = _nameIndex;
		name = _name;
	}
	
	public int getNameIndex() {
		return  nameIndex;
	}
	
	public String getName() {
		return name;
	}
}
