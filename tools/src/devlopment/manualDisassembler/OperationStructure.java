package devlopment.manualDisassembler;

 class OperationStructure {
	private String opCode;
	private InstrucionType type;
	private int size;
	private String instruction;
	private String source;
	private String destination;
	private String function;
	private PCaction pcAction;

	public OperationStructure(String opCode, InstrucionType type,int size,
			String instruction, String destination, String source, String function) {
		this(opCode,type,size,instruction,destination,source,function,PCaction.NORMAL);
	}// CONSTRUCTOR
	
	public OperationStructure(String opCode, InstrucionType type,int size,
			String instruction, String destination, String source, String function,PCaction pcAction) {
		this.opCode = opCode;
		this.type = type;
		this.size = size;
		this.instruction = instruction;
		this.source = source;
		this.destination = destination;
		this.function = function;
		this.pcAction=pcAction;
	}// CONSTRUCTOR

	 public String getOpCode() {
		return this.opCode;
	}// getOpCode

	public int getSize() {
		return this.size;
	}// getSize

	public String getInstruction() {
		return this.instruction;
	}// getInstruction

	public String getSource() {
		return this.source;
	}// getSource

	public String getDestination() {
		return this.destination;
	}// getDestination

	public String getFunction() {
		return this.function;
	}// getFunction

	public InstrucionType getType() {
		return this.type;
	}// getFunction
	
	public PCaction getPCaction() {
		return this.pcAction;
	}//getPCaction

}// class operationStructure
