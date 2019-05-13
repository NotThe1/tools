package devlopment.manualDisassembler;

import java.util.HashMap;

public class OpCodeMapIntel extends AbstractOpCodeMap {
	
	public OpCodeMapIntel() {
		makeMap();
	}//Constructor
	
	private void makeMap() {
//	static {
		 codeMap = new HashMap<String, OperationStructure>();
		codeMap.put("00", new OperationStructure("00", InstrucionType.I00, 1, "NOP", "", "", ""));
		codeMap.put("01", new OperationStructure("01", InstrucionType.I21, 3, "LXI", "BC", "D16", "B<- byte3,C<- byte2"));
		codeMap.put("00", new OperationStructure("00", InstrucionType.I00, 1, "NOP", "", "", ""));
		codeMap.put("01", new OperationStructure("01", InstrucionType.I21, 3, "LXI", "BC", "D16", "B<- byte3,C<- byte2"));
		codeMap.put("02", new OperationStructure("02", InstrucionType.I02, 1, "STAX", "(BC)", "A", "(BC)<-A"));
		codeMap.put("03", new OperationStructure("03", InstrucionType.I01, 1, "INX", "BC", "", "BC<-BC+1"));
		codeMap.put("04", new OperationStructure("04", InstrucionType.I01, 1, "INR", "B", "", "B<-B+1"));
		codeMap.put("05", new OperationStructure("05", InstrucionType.I01, 1, "DCR", "B", "", "B<-B-1"));
		codeMap.put("06", new OperationStructure("06", InstrucionType.I11, 2, "MVI", "B", "D8", "B<-byte2"));
		codeMap.put("07", new OperationStructure("07", InstrucionType.I00, 1, "RLC", "", "", "A=A << not thru carry"));
		codeMap.put("08", new OperationStructure("08", InstrucionType.I00, 1, "ALT", "", "'", "NOP"));
		codeMap.put("09", new OperationStructure("09", InstrucionType.I02, 1, "DAD", "HL", "BC", "HL = HL + BC"));
		codeMap.put("0A", new OperationStructure("0A", InstrucionType.I02, 1, "LDAX", "A", "(BC)", "A<-(BC)"));
		codeMap.put("0B", new OperationStructure("0B", InstrucionType.I01, 1, "DCX", "BC", "", "BC = BC-1"));
		codeMap.put("0C", new OperationStructure("0C", InstrucionType.I01, 1, "INR", "C", "", "C <-C+1"));
		codeMap.put("0D", new OperationStructure("0D", InstrucionType.I01, 1, "DCR", "C", "", "C <-C-1"));
		codeMap.put("0E", new OperationStructure("0E", InstrucionType.I11, 2, "MVI", "C", "D8", "C,-byte2"));
		codeMap.put("0F", new OperationStructure("0F", InstrucionType.I00, 1, "RRC", "", "", "A = A>> not thru carry"));
		//
		codeMap.put("10", new OperationStructure("10", InstrucionType.I00, 1, "ALT", "", "","NOP"));
		codeMap.put("11", new OperationStructure("11", InstrucionType.I21, 3, "LXI", "DE", "D16", "D<-byte3,E<-byte2"));
		codeMap.put("12", new OperationStructure("12", InstrucionType.I02, 1, "STAX", "(DE)", "A", "(DE)<-A"));
		codeMap.put("13", new OperationStructure("13", InstrucionType.I01, 1, "INX", "DE", "", "DE<-DE + 1"));
		codeMap.put("14", new OperationStructure("14", InstrucionType.I01, 1, "INR", "D", "", "D<-D+1"));
		codeMap.put("15", new OperationStructure("15", InstrucionType.I01, 1, "DCR", "D", "", "D<-D-1"));
		codeMap.put("16", new OperationStructure("16", InstrucionType.I11, 2, "MVI", "D", "D8", "D<-byte2"));
		codeMap.put("17", new OperationStructure("17", InstrucionType.I00, 1, "RAL", "", "", "A=A << thru carry"));
		codeMap.put("18", new OperationStructure("18", InstrucionType.I00, 1, "ALT", "", "","NOP"));
		codeMap.put("19", new OperationStructure("19", InstrucionType.I02, 1, "DAD", "HL", "DE", "HL = HL + DE"));
		codeMap.put("1A", new OperationStructure("1A", InstrucionType.I02, 1, "LDAX", "A", "(DE)", "A<-(DE)"));
		codeMap.put("1B", new OperationStructure("1B", InstrucionType.I01, 1, "DCX", "DE", "", "DE = DE-1"));
		codeMap.put("1C", new OperationStructure("1C", InstrucionType.I01, 1, "INR", "E", "", "E <-E+1"));
		codeMap.put("1D", new OperationStructure("1D", InstrucionType.I01, 1, "DCR", "E", "", "E <-E-1"));
		codeMap.put("1E", new OperationStructure("1E", InstrucionType.I11, 2, "MVI", "E", "D8", "E,-byte2"));
		codeMap.put("1F", new OperationStructure("1f", InstrucionType.I00, 1, "RRA", "", "", "A = A>>  thru carry"));
		//
		codeMap.put("20", new OperationStructure("20", InstrucionType.I00, 1, "ALT", "", "","NOP"));
		codeMap.put("21", new OperationStructure("21", InstrucionType.I21, 3, "LXI", "HL", "D16", "H<-byte3,L<-byte2"));
		codeMap.put("22", new OperationStructure("22", InstrucionType.I24, 3, "SHLD", "addr", "HL", "(addr)<-L;(addr+1)<-H"));
		codeMap.put("23", new OperationStructure("23", InstrucionType.I01, 1, "INX", "HL", "", "HL<-HL + 1"));
		codeMap.put("24", new OperationStructure("24", InstrucionType.I01, 1, "INR", "H", "", "H<-H+1"));
		codeMap.put("25", new OperationStructure("25", InstrucionType.I01, 1, "DCR", "H", "", "H<-H-1"));
		codeMap.put("26", new OperationStructure("26", InstrucionType.I11, 2, "MVI", "H", "D8", "H<-byte2"));
		codeMap.put("27", new OperationStructure("27", InstrucionType.I00, 1, "DAA", "", "", "Decimal Adjust Acc."));
		codeMap.put("28", new OperationStructure("28", InstrucionType.I00, 1, "ALT", "", "","NOP"));
		codeMap.put("29", new OperationStructure("29", InstrucionType.I02, 1, "DAD", "HL", "HL", "HL = HL + HL"));
		codeMap.put("2A", new OperationStructure("2A", InstrucionType.I26, 3, "LHLD", "HL", "addr", "L<-(addr);H<-(addr+1)"));
		codeMap.put("2B", new OperationStructure("2B", InstrucionType.I01, 1, "DCX", "HL", "", "HL = HL-1"));
		codeMap.put("2C", new OperationStructure("2C", InstrucionType.I01, 1, "INR", "L", "", "L <-L+1"));
		codeMap.put("2D", new OperationStructure("2D", InstrucionType.I01, 1, "DCR", "L", "", "L <-L-1"));
		codeMap.put("2E", new OperationStructure("2E", InstrucionType.I11, 2, "MVI", "L", "D8", "L,-byte2"));
		codeMap.put("2F", new OperationStructure("2F", InstrucionType.I00, 1, "CMA", "", "", "A = !A"));
		//
		codeMap.put("30", new OperationStructure("30", InstrucionType.I00, 1, "ALT", "", "","NOP"));
		codeMap.put("31", new OperationStructure("31", InstrucionType.I21, 3, "LXI", "SP", "D16", "SP-data"));
		codeMap.put("32", new OperationStructure("32", InstrucionType.I24, 3, "STA", "addr", "A", "(addr)<-A"));
		codeMap.put("33", new OperationStructure("33", InstrucionType.I01, 1, "INX", "SP", "", "SP<-SP + 1"));
		codeMap.put("34", new OperationStructure("34", InstrucionType.I01, 1, "INR", "(HL)", "", "(HL)<-(HL)+1"));
		codeMap.put("35", new OperationStructure("35", InstrucionType.I01, 1, "DCR", "(HL)", "", "(HL)<-(HL)-1"));
		codeMap.put("36", new OperationStructure("36", InstrucionType.I11, 2, "MVI", "(HL)", "D8", "(HL)<-byte2"));
		codeMap.put("37", new OperationStructure("37", InstrucionType.I00, 1, "STC", "", "", "CY=1"));
		codeMap.put("38", new OperationStructure("38", InstrucionType.I00, 1, "ALT", "", "","NOP"));
		codeMap.put("39", new OperationStructure("39", InstrucionType.I02, 1, "DAD", "HL", "SP", "HL = HL + SP"));
		codeMap.put("3A", new OperationStructure("3A", InstrucionType.I26, 3, "LDA", "A", "addr", "A<-(addr)"));
		codeMap.put("3B", new OperationStructure("3B", InstrucionType.I01, 1, "DCX", "SP", "", "SP = SP-1"));
		codeMap.put("3C", new OperationStructure("3C", InstrucionType.I01, 1, "INR", "A", "", "A <-A+1"));
		codeMap.put("3D", new OperationStructure("3D", InstrucionType.I01, 1, "DCR", "A", "", "A <-A-1"));
		codeMap.put("3E", new OperationStructure("3E", InstrucionType.I11, 2, "MVI", "A", "D8", "A,<-byte2"));
		codeMap.put("3F", new OperationStructure("3F", InstrucionType.I00, 1, "CMC", "", "", "CY=!CY"));
		//
		codeMap.put("40", new OperationStructure("40", InstrucionType.I02, 1, "MOV", "B", "B", "B <- B"));
		codeMap.put("41", new OperationStructure("41", InstrucionType.I02, 1, "MOV", "B", "C", "B <- C"));
		codeMap.put("42", new OperationStructure("42", InstrucionType.I02, 1, "MOV", "B", "D", "B <- D"));
		codeMap.put("43", new OperationStructure("43", InstrucionType.I02, 1, "MOV", "B", "E", "B <- E"));
		codeMap.put("44", new OperationStructure("44", InstrucionType.I02, 1, "MOV", "B", "H", "B <- H"));
		codeMap.put("45", new OperationStructure("45", InstrucionType.I02, 1, "MOV", "B", "L", "B <- L"));
		codeMap.put("46", new OperationStructure("46", InstrucionType.I02, 1, "MOV", "B", "(HL)", "B <- (HL)"));
		codeMap.put("47", new OperationStructure("47", InstrucionType.I02, 1, "MOV", "B", "A", "B <- A"));
		// InstrucionType.I02,
		codeMap.put("48", new OperationStructure("48", InstrucionType.I02, 1, "MOV", "C", "B", "C <- B"));
		codeMap.put("49", new OperationStructure("49", InstrucionType.I02, 1, "MOV", "C", "C", "C <- C"));
		codeMap.put("4A", new OperationStructure("4A", InstrucionType.I02, 1, "MOV", "C", "D", "C <- D"));
		codeMap.put("4B", new OperationStructure("4B", InstrucionType.I02, 1, "MOV", "C", "E", "C <- E"));
		codeMap.put("4C", new OperationStructure("4C", InstrucionType.I02, 1, "MOV", "C", "H", "C <- H"));
		codeMap.put("4D", new OperationStructure("4D", InstrucionType.I02, 1, "MOV", "C", "L", "C <- L"));
		codeMap.put("4E", new OperationStructure("4E", InstrucionType.I02, 1, "MOV", "C", "(HL)", "C <- (HL)"));
		codeMap.put("4F", new OperationStructure("4F", InstrucionType.I02, 1, "MOV", "C", "A", "C <- A"));
		//
		codeMap.put("50", new OperationStructure("50", InstrucionType.I02, 1, "MOV", "D", "B", "D <- B"));
		codeMap.put("51", new OperationStructure("51", InstrucionType.I02, 1, "MOV", "D", "C", "D <- C"));
		codeMap.put("52", new OperationStructure("52", InstrucionType.I02, 1, "MOV", "D", "D", "D <- D"));
		codeMap.put("53", new OperationStructure("53", InstrucionType.I02, 1, "MOV", "D", "E", "D <- E"));
		codeMap.put("54", new OperationStructure("54", InstrucionType.I02, 1, "MOV", "D", "H", "D <- H"));
		codeMap.put("55", new OperationStructure("55", InstrucionType.I02, 1, "MOV", "D", "L", "D <- L"));
		codeMap.put("56", new OperationStructure("56", InstrucionType.I02, 1, "MOV", "D", "(HL)", "D <- (HL)"));
		codeMap.put("57", new OperationStructure("57", InstrucionType.I02, 1, "MOV", "D", "A", "D <- A"));
		//
		codeMap.put("58", new OperationStructure("58", InstrucionType.I02, 1, "MOV", "E", "B", "E <- B"));
		codeMap.put("59", new OperationStructure("59", InstrucionType.I02, 1, "MOV", "E", "C", "E <- C"));
		codeMap.put("5A", new OperationStructure("5A", InstrucionType.I02, 1, "MOV", "E", "D", "E <- D"));
		codeMap.put("5B", new OperationStructure("5B", InstrucionType.I02, 1, "MOV", "E", "E", "E <- E"));
		codeMap.put("5C", new OperationStructure("5C", InstrucionType.I02, 1, "MOV", "E", "H", "E <- H"));
		codeMap.put("5D", new OperationStructure("5D", InstrucionType.I02, 1, "MOV", "E", "L", "E <- L"));
		codeMap.put("5E", new OperationStructure("5E", InstrucionType.I02, 1, "MOV", "E", "(HL)", "E <- (HL)"));
		codeMap.put("5F", new OperationStructure("5F", InstrucionType.I02, 1, "MOV", "E", "A", "E <- A"));
		//
		codeMap.put("60", new OperationStructure("60", InstrucionType.I02, 1, "MOV", "H", "B", "H <- B"));
		codeMap.put("61", new OperationStructure("61", InstrucionType.I02, 1, "MOV", "H", "C", "H <- C"));
		codeMap.put("62", new OperationStructure("62", InstrucionType.I02, 1, "MOV", "H", "D", "H <- D"));
		codeMap.put("63", new OperationStructure("63", InstrucionType.I02, 1, "MOV", "H", "E", "H <- E"));
		codeMap.put("64", new OperationStructure("64", InstrucionType.I02, 1, "MOV", "H", "H", "H <- H"));
		codeMap.put("65", new OperationStructure("65", InstrucionType.I02, 1, "MOV", "H", "L", "H <- L"));
		codeMap.put("66", new OperationStructure("66", InstrucionType.I02, 1, "MOV", "H", "(HL)", "H <- (HL)"));
		codeMap.put("67", new OperationStructure("67", InstrucionType.I02, 1, "MOV", "H", "A", "H <- A"));
		//
		codeMap.put("68", new OperationStructure("68", InstrucionType.I02, 1, "MOV", "L", "B", "L <- B"));
		codeMap.put("69", new OperationStructure("69", InstrucionType.I02, 1, "MOV", "L", "C", "L <- C"));
		codeMap.put("6A", new OperationStructure("6A", InstrucionType.I02, 1, "MOV", "L", "D", "L <- D"));
		codeMap.put("6B", new OperationStructure("6B", InstrucionType.I02, 1, "MOV", "L", "E", "L <- E"));
		codeMap.put("6C", new OperationStructure("6C", InstrucionType.I02, 1, "MOV", "L", "H", "L <- H"));
		codeMap.put("6D", new OperationStructure("6D", InstrucionType.I02, 1, "MOV", "L", "L", "L <- L"));
		codeMap.put("6E", new OperationStructure("6E", InstrucionType.I02, 1, "MOV", "L", "(HL)", "L <- (HL)"));
		codeMap.put("6F", new OperationStructure("6F", InstrucionType.I02, 1, "MOV", "L", "A", "L <- A"));
		//
		codeMap.put("70", new OperationStructure("70", InstrucionType.I02, 1, "MOV", "(HL)", "B", "(HL) <- B"));
		codeMap.put("71", new OperationStructure("71", InstrucionType.I02, 1, "MOV", "(HL)", "C", "(HL) <- C"));
		codeMap.put("72", new OperationStructure("72", InstrucionType.I02, 1, "MOV", "(HL)", "D", "(HL) <- D"));
		codeMap.put("73", new OperationStructure("73", InstrucionType.I02, 1, "MOV", "(HL)", "E", "(HL) <- E"));
		codeMap.put("74", new OperationStructure("74", InstrucionType.I02, 1, "MOV", "(HL)", "H", "(HL) <- H"));
		codeMap.put("75", new OperationStructure("75", InstrucionType.I02, 1, "MOV", "(HL)", "L", "(HL) <- L"));
		codeMap.put("76", new OperationStructure("76", InstrucionType.I00, 1, "HALT", "", "", "Halt")); // Special
		codeMap.put("77", new OperationStructure("77", InstrucionType.I02, 1, "MOV", "(HL)", "A", "(HL) <- A"));
		//
		codeMap.put("78", new OperationStructure("78", InstrucionType.I02, 1, "MOV", "A", "B", "A <- B"));
		codeMap.put("79", new OperationStructure("79", InstrucionType.I02, 1, "MOV", "A", "C", "A <- C"));
		codeMap.put("7A", new OperationStructure("7A", InstrucionType.I02, 1, "MOV", "A", "D", "A <- D"));
		codeMap.put("7B", new OperationStructure("7B", InstrucionType.I02, 1, "MOV", "A", "E", "A <- E"));
		codeMap.put("7C", new OperationStructure("7C", InstrucionType.I02, 1, "MOV", "A", "H", "A <- H"));
		codeMap.put("7D", new OperationStructure("7D", InstrucionType.I02, 1, "MOV", "A", "L", "A <- L"));
		codeMap.put("7E", new OperationStructure("7E", InstrucionType.I02, 1, "MOV", "A", "(HL)", "A <- (HL)"));
		codeMap.put("7F", new OperationStructure("7F", InstrucionType.I02, 1, "MOV", "A", "A", "A <- A"));
		//
		codeMap.put("80", new OperationStructure("80", InstrucionType.I02, 1, "ADD", "A", "B", "A <- A+B"));
		codeMap.put("81", new OperationStructure("81", InstrucionType.I02, 1, "ADD", "A", "C", "A <- A+C"));
		codeMap.put("82", new OperationStructure("82", InstrucionType.I02, 1, "ADD", "A", "D", "A <- A+D"));
		codeMap.put("83", new OperationStructure("83", InstrucionType.I02, 1, "ADD", "A", "E", "A <- A+E"));
		codeMap.put("84", new OperationStructure("84", InstrucionType.I02, 1, "ADD", "A", "H", "A <- A+H"));
		codeMap.put("85", new OperationStructure("85", InstrucionType.I02, 1, "ADD", "A", "L", "A <- A+L"));
		codeMap.put("86", new OperationStructure("86", InstrucionType.I02, 1, "ADD", "A", "(HL)", "A <- A+(HL)"));
		codeMap.put("87", new OperationStructure("87", InstrucionType.I02, 1, "ADD", "A", "A", "A <- A+A"));
		//
		codeMap.put("88", new OperationStructure("88", InstrucionType.I02, 1, "ADC", "A", "B", "A <- A+B + CY"));
		codeMap.put("89", new OperationStructure("89", InstrucionType.I02, 1, "ADC", "A", "C", "A <- A+C + CY"));
		codeMap.put("8A", new OperationStructure("8A", InstrucionType.I02, 1, "ADC", "A", "D", "A <- A+D + CY"));
		codeMap.put("8B", new OperationStructure("8B", InstrucionType.I02, 1, "ADC", "A", "E", "A <- A+E + CY"));
		codeMap.put("8C", new OperationStructure("8C", InstrucionType.I02, 1, "ADC", "A", "H", "A <- A+H + CY"));
		codeMap.put("8D", new OperationStructure("8D", InstrucionType.I02, 1, "ADC", "A", "L", "A <- A+L + CY"));
		codeMap.put("8E", new OperationStructure("8E", InstrucionType.I02, 1, "ADC", "A", "(HL)", "A <- A+(HL) + CY"));
		codeMap.put("8F", new OperationStructure("8F", InstrucionType.I02, 1, "ADC", "A", "A", "A <- A+A + CY"));
		//
		codeMap.put("90", new OperationStructure("90", InstrucionType.I01, 1, "SUB", "B", "", "A <- A-B"));
		codeMap.put("91", new OperationStructure("91", InstrucionType.I01, 1, "SUB", "C", "", "A <- A-C"));
		codeMap.put("92", new OperationStructure("92", InstrucionType.I01, 1, "SUB", "D", "", "A <- A-D"));
		codeMap.put("93", new OperationStructure("93", InstrucionType.I01, 1, "SUB", "E", "", "A <- A-E"));
		codeMap.put("94", new OperationStructure("94", InstrucionType.I01, 1, "SUB", "H", "", "A <- A-H"));
		codeMap.put("95", new OperationStructure("95", InstrucionType.I01, 1, "SUB", "L", "", "A <- A-L"));
		codeMap.put("96", new OperationStructure("96", InstrucionType.I01, 1, "SUB", "(HL)", "", "A <- A-(HL)"));
		codeMap.put("97", new OperationStructure("97", InstrucionType.I01, 1, "SUB", "A", "", "A <- A-A"));
		//
		codeMap.put("98", new OperationStructure("98", InstrucionType.I02, 1, "SBB", "A", "B", "A <- A-B - CY"));
		codeMap.put("99", new OperationStructure("99", InstrucionType.I02, 1, "SBB", "A", "C", "A <- A-C - CY"));
		codeMap.put("9A", new OperationStructure("9A", InstrucionType.I02, 1, "SBB", "A", "D", "A <- A-D - CY"));
		codeMap.put("9B", new OperationStructure("9B", InstrucionType.I02, 1, "SBB", "A", "E", "A <- A-E - CY"));
		codeMap.put("9C", new OperationStructure("9C", InstrucionType.I02, 1, "SBB", "A", "H", "A <- A-H - CY"));
		codeMap.put("9D", new OperationStructure("9D", InstrucionType.I02, 1, "SBB", "A", "L", "A <- A-L - CY"));
		codeMap.put("9E", new OperationStructure("9E", InstrucionType.I02, 1, "SBB", "A", "(HL)", "A <- A-(HL) - CY"));
		codeMap.put("9F", new OperationStructure("9F", InstrucionType.I02, 1, "SBB", "A", "A", "A <- A-A - CY"));
		//
		codeMap.put("A0", new OperationStructure("A0", InstrucionType.I01, 1, "ANA", "B", "", "A <- A&B"));
		codeMap.put("A1", new OperationStructure("A1", InstrucionType.I01, 1, "ANA", "C", "", "A <- A&C"));
		codeMap.put("A2", new OperationStructure("A2", InstrucionType.I01, 1, "ANA", "D", "", "A <- A&D"));
		codeMap.put("A3", new OperationStructure("A3", InstrucionType.I01, 1, "ANA", "E", "", "A <- A&E"));
		codeMap.put("A4", new OperationStructure("A4", InstrucionType.I01, 1, "ANA", "H", "", "A <- A&H"));
		codeMap.put("A5", new OperationStructure("A5", InstrucionType.I01, 1, "ANA", "L", "", "A <- A&L"));
		codeMap.put("A6", new OperationStructure("A6", InstrucionType.I01, 1, "ANA", "(HL)", "", "A <- A&(HL)"));
		codeMap.put("A7", new OperationStructure("A7", InstrucionType.I01, 1, "ANA", "A", "", "A <- A&A"));
		// ,
		codeMap.put("A8", new OperationStructure("A8", InstrucionType.I01, 1, "XRA", "B", "", "A <- A^B"));
		codeMap.put("A9", new OperationStructure("A9", InstrucionType.I01, 1, "XRA", "C", "", "A <- A^C"));
		codeMap.put("AA", new OperationStructure("AA", InstrucionType.I01, 1, "XRA", "D", "", "A <- A^D"));
		codeMap.put("AB", new OperationStructure("AB", InstrucionType.I01, 1, "XRA", "E", "", "A <- A^E"));
		codeMap.put("AC", new OperationStructure("AC", InstrucionType.I01, 1, "XRA", "H", "", "A <- A^H"));
		codeMap.put("AD", new OperationStructure("AD", InstrucionType.I01, 1, "XRA", "L", "", "A <- A^L"));
		codeMap.put("AE", new OperationStructure("AE", InstrucionType.I01, 1, "XRA", "(HL)", "", "A <- A^(HL)"));
		codeMap.put("AF", new OperationStructure("AF", InstrucionType.I01, 1, "XRA", "A", "", "A <- A^A"));
		//
		codeMap.put("B0", new OperationStructure("B0", InstrucionType.I01, 1, "ORA", "B", "", "A <- A|B"));
		codeMap.put("B1", new OperationStructure("B1", InstrucionType.I01, 1, "ORA", "C", "", "A <- A|C"));
		codeMap.put("B2", new OperationStructure("B2", InstrucionType.I01, 1, "ORA", "D", "", "A <- A|D"));
		codeMap.put("B3", new OperationStructure("B3", InstrucionType.I01, 1, "ORA", "E", "", "A <- A|E"));
		codeMap.put("B4", new OperationStructure("B4", InstrucionType.I01, 1, "ORA", "H", "", "A <- A|H"));
		codeMap.put("B5", new OperationStructure("B5", InstrucionType.I01, 1, "ORA", "L", "", "A <- A|L"));
		codeMap.put("B6", new OperationStructure("B6", InstrucionType.I01, 1, "ORA", "(HL)", "", "A <- A|(HL)"));
		codeMap.put("B7", new OperationStructure("B7", InstrucionType.I01, 1, "ORA", "A", "", "A <- A|A"));
		//
		codeMap.put("B8", new OperationStructure("B8", InstrucionType.I01, 1, "CMP", "B", "", "A - B"));
		codeMap.put("B9", new OperationStructure("B9", InstrucionType.I01, 1, "CMP", "C", "", "A - C"));
		codeMap.put("BA", new OperationStructure("BA", InstrucionType.I01, 1, "CMP", "D", "", "A - D"));
		codeMap.put("BB", new OperationStructure("BB", InstrucionType.I01, 1, "CMP", "E", "", "A - E"));
		codeMap.put("BC", new OperationStructure("BC", InstrucionType.I01, 1, "CMP", "H", "", "A - H"));
		codeMap.put("BD", new OperationStructure("BD", InstrucionType.I01, 1, "CMP", "L", "", "A - L"));
		codeMap.put("BE", new OperationStructure("BE", InstrucionType.I01, 1, "CMP", "(HL)", "", "A - (HL)"));
		codeMap.put("BF", new OperationStructure("BF", InstrucionType.I01, 1, "CMP", "A", "", "A - A"));
		//
		codeMap.put("C0", new OperationStructure("C0", InstrucionType.I00, 1, "RNZ", "", "", "if NZ, (PC)<-(SP)"));
		codeMap.put("C1", new OperationStructure("C1", InstrucionType.I01, 1, "POP", "BC", "", "(BC<-(SP)"));
		codeMap.put("C2", new OperationStructure("C2", InstrucionType.I20, 3, "JNZ", "", "D16", "if NZ,PC<-D16",PCaction.CONTINUATION));
		codeMap.put("C3", new OperationStructure("C3", InstrucionType.I20, 3, "JMP", "D16", "", "PC<-D16",PCaction.TOTAL));
		codeMap.put("C4", new OperationStructure("C4", InstrucionType.I20, 3, "CNZ", "", "D16","if Z,CALL D16",PCaction.CONTINUATION));
		codeMap.put("C5", new OperationStructure("C5", InstrucionType.I01, 1, "PUSH", "BC", "", "(SP)<-(BC)"));
		codeMap.put("C6", new OperationStructure("C6", InstrucionType.I11, 2, "ADI", "A", "D8", "A<-A + byte2"));
		codeMap.put("C7", new OperationStructure("C7", InstrucionType.I01, 1, "RST", "00H", "", "CALL $0",PCaction.CONTINUATION));
		codeMap.put("C8", new OperationStructure("C8", InstrucionType.I00, 1, "RZ", "", "", "if Z set, PC<-(SP)"));
		codeMap.put("C9", new OperationStructure("C9", InstrucionType.I00, 1, "RET", "", "", "PC<-(SP); SP<-SP+2",PCaction.TERMINATES));
		codeMap.put("CA", new OperationStructure("CA", InstrucionType.I20, 3, "JZ", "", "D16", "if Z set,PC<-D16",PCaction.CONTINUATION));
		codeMap.put("CB", new OperationStructure("CB", InstrucionType.I00, 1, "ALT", "", "", "NOP"));
		codeMap.put("CC", new OperationStructure("CC", InstrucionType.I20, 3, "CZ", "", "D16", "if Z,CALL D16", PCaction.CONTINUATION));
		codeMap.put("CD", new OperationStructure("CD", InstrucionType.I20, 3, "CALL", "D16", "", "CALL D16", PCaction.CONTINUATION));
		codeMap.put("CE", new OperationStructure("CE", InstrucionType.I11, 2, "ADI", "A", "D8", "A<- A + data + cy"));
		codeMap.put("CF", new OperationStructure("CF", InstrucionType.I01, 1, "RST", "08H", "", "CALL $8", PCaction.CONTINUATION));
		//
		codeMap.put("D0", new OperationStructure("D0", InstrucionType.I00, 1, "RNC", "", "", "if CY not set, (PC)<-(SP)"));
		codeMap.put("D1", new OperationStructure("D1", InstrucionType.I01, 1, "POP", "DE", "", ""));
		codeMap.put("D2", new OperationStructure("D2", InstrucionType.I20, 3, "JNC", "", "D16", "if CY not set,PC<-D16", PCaction.CONTINUATION));
		codeMap.put("D3", new OperationStructure("D3", InstrucionType.I12, 2, "OUT", "D8", "A", "OUT  (D8),A   I/O")); // Special
		codeMap.put("D4", new OperationStructure("D4", InstrucionType.I20, 3, "CNC", "", "D16", "if NC,CALL D16", PCaction.CONTINUATION));
		codeMap.put("D5", new OperationStructure("D5", InstrucionType.I01, 1, "PUSH", "DE", "", ""));
		codeMap.put("D6", new OperationStructure("D6", InstrucionType.I10, 2, "SUI", "D8", "", "A<-A - byte2"));
		codeMap.put("D7", new OperationStructure("D7", InstrucionType.I01, 1, "RST", "2", "", "CALL $10", PCaction.CONTINUATION));
		codeMap.put("D8", new OperationStructure("D8", InstrucionType.I00, 1, "RC", "", "", "if CY  set,PC<-(SP)"));
		codeMap.put("D9", new OperationStructure("D9", InstrucionType.I00, 1, "RET", "", "", "**PC<-(SP); SP<-SP+2",PCaction.CONTINUATION));
		codeMap.put("DA", new OperationStructure("DA", InstrucionType.I20, 3, "JC", "", "D16", "if CY  set,PC<-D16", PCaction.CONTINUATION));
		codeMap.put("DB", new OperationStructure("DB", InstrucionType.I10, 2, "IN", "A", "D8", "i/O")); // Special ***
		codeMap.put("DC", new OperationStructure("DC", InstrucionType.I20, 3, "CC", "", "D16","if C,CALL D16", PCaction.CONTINUATION));
		codeMap.put("DD", new OperationStructure("DD", InstrucionType.I00, 1, "ALT", "", "", "NOP"));
		codeMap.put("DE", new OperationStructure("DE", InstrucionType.I10, 2, "SBI", "A", "D8", "A<- A - data - cy"));
		codeMap.put("DF", new OperationStructure("DF", InstrucionType.I01, 1, "RST", "3", "", "CALL $18", PCaction.CONTINUATION));
		//
		codeMap.put("E0", new OperationStructure("E0", InstrucionType.I00, 1, "RPO", "", "", "if P is reset, set,PC<-(SP)"));
		codeMap.put("E1", new OperationStructure("E1", InstrucionType.I01, 1, "POP", "HL", "", ""));
		codeMap.put("E2", new OperationStructure("E2", InstrucionType.I20, 3, "JPO", "", "D16", "if P is reset,PC<-D16", PCaction.CONTINUATION));
		codeMap.put("E3", new OperationStructure("E3", InstrucionType.I02, 1, "XTHL", "(SP)", "HL", "L<->(SP);H<->(SP+1)"));
		codeMap.put("E4", new OperationStructure("E4", InstrucionType.I20, 3, "CPO", "", "D16","if P is reset,CALL D16", PCaction.CONTINUATION));
		codeMap.put("E5", new OperationStructure("E5", InstrucionType.I01, 1, "PUSH", "HL", "", ""));
		codeMap.put("E6", new OperationStructure("E6", InstrucionType.I10, 2, "ANI", "D8", "", "A<-A & byte2"));
		codeMap.put("E7", new OperationStructure("E7", InstrucionType.I01, 1, "RST", "4", "", "CALL $20", PCaction.CONTINUATION));
		codeMap.put("E8", new OperationStructure("E8", InstrucionType.I00, 1, "RPE", "", "", "if P is set, set,PC<-(SP)"));
		codeMap.put("E9", new OperationStructure("E9", InstrucionType.I01, 1, "PCHL", "(HL)", "", "PC.hi<-H;PC.lo<-L", PCaction.TOTAL));
		codeMap.put("EA", new OperationStructure("EA", InstrucionType.I20, 3, "JPE", "", "D16", "if P is set,PC<-D16", PCaction.CONTINUATION));
		codeMap.put("EB", new OperationStructure("EB", InstrucionType.I02, 1, "XCHG", "DE", "HL", "H<->D;L<->E")); // Special
		codeMap.put("EC", new OperationStructure("EC", InstrucionType.I20, 3, "CPE", "", "D16","if P is set,CALL D16", PCaction.CONTINUATION));
		codeMap.put("ED", new OperationStructure("ED", InstrucionType.I00, 1, "ALT", "", "", "NOP"));
		codeMap.put("EE", new OperationStructure("EE", InstrucionType.I10, 2, "XRI", "D8", "", "A<- A ^ data"));
		codeMap.put("EF", new OperationStructure("EF", InstrucionType.I01, 1, "RST", "5", "", "CALL $28", PCaction.CONTINUATION));
		//
		codeMap.put("F0", new OperationStructure("F0", InstrucionType.I00, 1, "RP", "", "", "if S rest, set,PC<-(SP)"));
		codeMap.put("F1", new OperationStructure("F1", InstrucionType.I01, 1, "POP", "AF", "","flags<-(SP);A<-(SP+1)"));
		codeMap.put("F2", new OperationStructure("F2", InstrucionType.I20, 3, "JP", "", "D16", "if S reset,PC<-D16", PCaction.CONTINUATION));
		codeMap.put("F3", new OperationStructure("F3", InstrucionType.I00, 1, "DI", "", "", "Disable interrups"));
		codeMap.put("F4", new OperationStructure("F4", InstrucionType.I20, 3, "CP", "", "D16","if S reset ,CALL D16", PCaction.CONTINUATION));
		codeMap.put("F5", new OperationStructure("F5", InstrucionType.I01, 1, "PUSH", "AF", "", "(SP)<-A & cc"));
		codeMap.put("F6", new OperationStructure("F6", InstrucionType.I10, 2, "ORI", "D8", "", "A<-A | byte2"));
		codeMap.put("F7", new OperationStructure("F7", InstrucionType.I01, 1, "RST", "6", "", "CALL $30", PCaction.CONTINUATION));
		codeMap.put("F8", new OperationStructure("F8", InstrucionType.I00, 1, "RM", "", "", "if S set, set,PC<-(SP)"));
		codeMap.put("F9", new OperationStructure("F9", InstrucionType.I02, 1, "SPHL", "SP", "HL", "SP<-HL"));
		codeMap.put("FA", new OperationStructure("FA", InstrucionType.I20, 3, "JM", "", "D16", "if S set,PC<-D16", PCaction.CONTINUATION));
		codeMap.put("FB", new OperationStructure("FB", InstrucionType.I00, 1, "EI", "", "", "Enable interrupts"));
		codeMap.put("FC", new OperationStructure("FC", InstrucionType.I20, 3, "CM", "", "D16","if S set,CALL D16", PCaction.CONTINUATION));
		codeMap.put("FD", new OperationStructure("DD", InstrucionType.I00, 1, "ALT", "", "", "NOP"));
		codeMap.put("FE", new OperationStructure("FE", InstrucionType.I10, 2, "CPI", "D8", "", "A - data"));
		codeMap.put("FF", new OperationStructure("FF", InstrucionType.I01, 1, "RST", "7", "", "CALL $38", PCaction.CONTINUATION));

		}//static

}//class OpCodeMapIntel
