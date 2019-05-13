package devlopment.manualDisassembler;

import java.util.HashMap;

public abstract class AbstractOpCodeMap  {

	public AbstractOpCodeMap() {
		// TODO Auto-generated constructor stub
	}//
	
	 public OperationStructure getOperationStructure(String key) {
		if (codeMap.containsKey(key)) {
			return codeMap.get(key);
		} else {
			return new OperationStructure("00", InstrucionType.I00, 1, "** DATA **", "", "", ""); // NOP
		} // if
	}// getOperationStructure

	 public OperationStructure get(String index) {
		return getOperationStructure(index);
	}// get

	 public int getOpCodeSize(String opCodeMapKey) {
		return codeMap.get(opCodeMapKey).getSize();
	}// getSize

	// ----------------------------------------------------------

	protected static HashMap<String, OperationStructure> codeMap;// = new HashMap<String, OperationStructure>();


}//abstract class AbstractOpCodeMap
