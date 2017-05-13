/**
 * Created by Johan007 on 17/5/11.
 */
public class Instruction {
    // Classes
    public static final String SPECIAL = "000000";
    public static final String J = "000010";
    public static final String BEQ = "000100";
    public static final String BLTZ = "000001";
    public static final String BGTZ = "000111";
    public static final String SW = "101011";
    public static final String LW = "100011";
    public static final String MUL = "011100";

    // SPECIAL
    public static final String JR = "001000";
    public static final String ADD = "100000";
    public static final String SUB = "100010";
    public static final String BREAK = "001101";
    public static final String SLL = "000000";
    public static final String SRL = "000010";
    public static final String SRA = "000011";
    public static final String NOP = "000000";
    public static final String AND = "100100";
    public static final String NOR = "100111";
    public static final String SLT = "101010";

    public int address;
    public static int length;
    String bits;
    String sMSB, sOperation, sOperand1, sOperand2, sResult, sImm, sFunction;

    String format;

    String rs, rt, rd, base;
    int imm;

    boolean isBranch;

    public Instruction(String bits, int address) {
        this.bits = bits;
        this.sMSB = this.bits.substring(0, 1);
        this.sOperation = this.bits.substring(0, 6);
        this.sOperand1 = this.bits.substring(6, 11);
        this.sOperand2 = this.bits.substring(11, 16);
        this.sResult = this.bits.substring(16, 21);
        this.sImm = this.bits.substring(21, 26);
        this.sFunction = this.bits.substring(26, 32);
        this.address = address;
        length = this.bits.length() / 8;
        this.isBranch = false;

        if (this.sOperation.equals(Instruction.SPECIAL)) {
            if (this.sFunction.equals(Instruction.JR)) {
                // this.format = "JR rs";
                this.format = String.format("JR\tR%s", Integer.valueOf(this.sOperand1, 2));
                this.isBranch = true;
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
            } else if (this.sFunction.equals(Instruction.ADD)) {
                // this.format = "ADD rd, rs, rt"
                this.format = String.format("ADD\tR%d, R%d, R%d", Integer.valueOf(this.sResult, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sOperand2, 2));
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
                this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rd = "R" + Integer.valueOf(this.sResult, 2);

            } else if (this.sFunction.equals(Instruction.SUB)) {
                // this.format = "SUB rd, rs, rt";
                this.format = String.format("SUB\tR%d, R%d, R%d", Integer.valueOf(this.sResult, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sOperand2, 2));
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
                this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rd = "R" + Integer.valueOf(this.sResult, 2);

            } else if (this.sFunction.equals(Instruction.BREAK)) {

                this.format = "BREAK";

            } else if (this.sFunction.equals(Instruction.SLL)) {// if(rd==rt==00000)NOP,else SLL
                if (this.sOperand2.equals("00000") && this.sResult.equals("00000")) {

                    this.format = "NOP";

                } else {
                    // this.format = "SLL\trd, rt, sa";
                    this.format = String.format("SLL\tR%d, R%d, #%d", Integer.valueOf(this.sResult, 2),
                            Integer.valueOf(this.sOperand2, 2), Integer.valueOf(this.sImm, 2));
                    this.imm = Integer.valueOf(this.sImm, 2);
                    this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
                    this.rd = "R" + Integer.valueOf(this.sResult, 2);
                }
            } else if (this.sFunction.equals(Instruction.SRL)) {
                // this.format = "SRL rd, rt, sa";
                this.format = String.format("SRL\tR%d, R%d, #%d", Integer.valueOf(this.sResult, 2),
                        Integer.valueOf(this.sOperand2, 2), Integer.valueOf(this.sImm, 2));
                this.imm = Integer.valueOf(this.sImm, 2);
                this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rd = "R" + Integer.valueOf(this.sResult, 2);

            } else if (this.sFunction.equals(Instruction.SRA)) {
                // this.format = "SRA rd, rt, sa";
                this.format = String.format("SRA\tR%d, R%d, #%d", Integer.valueOf(this.sResult, 2),
                        Integer.valueOf(this.sOperand2, 2), Integer.valueOf(this.sImm, 2));
                this.imm = Integer.valueOf(this.sImm, 2);
                this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rd = "R" + Integer.valueOf(this.sResult, 2);

            } else if (this.sFunction.equals(Instruction.AND)) {
                // this.format = "AND rd, rs, rt"
                this.format = String.format("AND\tR%d, R%d, R%d", Integer.valueOf(this.sResult, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sOperand2, 2));
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
                this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rd = "R" + Integer.valueOf(this.sResult, 2);

            } else if (this.sFunction.equals(Instruction.NOR)) {
                // this.format = "NOR rd, rs, rt"
                this.format = String.format("NOR\tR%d, R%d, R%d", Integer.valueOf(this.sResult, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sOperand2, 2));
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
                this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rd = "R" + Integer.valueOf(this.sResult, 2);

            } else if (this.sFunction.equals(Instruction.SLT)) {
                // this.format = "SLT rd, rs, rt"
                this.format = String.format("SLT\tR%d, R%d, R%d", Integer.valueOf(this.sResult, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sOperand2, 2));
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
                this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rd = "R" + Integer.valueOf(this.sResult, 2);

            }
        } else if (this.sOperation.equals(Instruction.J)) {
            // this.format = "J target";
            this.format = String.format("J\t#%d",
                    Integer.valueOf(this.sOperand1 + this.sOperand2 + this.sResult + this.sImm + this.sFunction + "00", 2));
            this.isBranch = true;
            this.imm = Integer.valueOf(this.sOperand1 + this.sOperand2 + this.sResult + this.sImm + this.sFunction + "00", 2);
        } else if (this.sOperation.equals(Instruction.BEQ)) {
            // this.format = "BEQ rs, rt, offset";
            this.format = String.format("BEQ\tR%d, R%d, #%d", Integer.valueOf(this.sOperand1, 2),
                    Integer.valueOf(this.sOperand2, 2),
                    Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction + "00", 32), 2));
            this.isBranch = true;
            this.imm = Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction + "00", 32), 2);
            this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
            this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
        } else if (this.sOperation.equals(Instruction.BLTZ)) {
            // this.format = "BLTZ rs, offset";
            this.format = String.format("BLTZ\tR%d, #%d", Integer.valueOf(this.sOperand1, 2),
                    Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction + "00", 32), 2));
            this.isBranch = true;
            this.imm = Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction + "00", 32), 2);
            this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
        } else if (this.sOperation.equals(Instruction.BGTZ)) {
            // this.format = "BGTZ rs, offset";
            this.format = String.format("BGTZ\tR%d, #%d", Integer.valueOf(this.sOperand1, 2),
                    Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction + "00", 32), 2));
            this.isBranch = true;
            this.imm = Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction + "00", 32), 2);
            this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
        } else if (this.sOperation.equals(Instruction.SW)) {
            // this.format = "SW rt, offset(base)";
            this.format = String.format("SW\tR%d, %d(R%d)", Integer.valueOf(this.sOperand2, 2),
                    Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction, 32), 2), Integer.valueOf(this.sOperand1, 2));
            this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
            this.imm = Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction, 32), 2);
            this.base = "R" + Integer.valueOf(this.sOperand1, 2);
        } else if (this.sOperation.equals(Instruction.LW)) {
            // this.format = "LW rt, offset(base)";
            this.format = String.format("LW\tR%d, %d(R%d)", Integer.valueOf(this.sOperand2, 2),
                    Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction, 32), 2), Integer.valueOf(this.sOperand1, 2));
            this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
            this.imm = Integer.valueOf(this.signExtend(this.sResult + this.sImm + this.sFunction, 32), 2);
            this.base = "R" + Integer.valueOf(this.sOperand1, 2);
        } else if (this.sOperation.equals(Instruction.MUL)) {
            // this.format = "MUL rd, rs, rt";
            this.format = String.format("MUL\tR%d, R%d, R%d", Integer.valueOf(this.sResult, 2),
                    Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sOperand2, 2));
            this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
            this.rt = "R" + Integer.valueOf(this.sOperand2, 2);
            this.rd = "R" + Integer.valueOf(this.sResult, 2);
        }
        // category 2
        else if (this.sMSB.equals("1")) {
            if (this.sOperation.equals("110000")) {// ADD
                // this.format = "ADD rd, rs, #imm";
                this.format = String.format("ADD\tR%d, R%d, #%d", Integer.valueOf(this.sOperand2, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2));
                this.imm = Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2);
                this.rd = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
            } else if (this.sOperation.equals("110001")) {// SUB
                // this.format = "SUB rd, rs, #imm";
                this.format = String.format("SUB\tR%d, R%d, #%d", Integer.valueOf(this.sOperand2, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2));
                this.imm = Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2);
                this.rd = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
            } else if (this.sOperation.equals("100001")) {// MUL
                // this.format = "MUL rd, rs, rt";
                this.format = String.format("MUL\tR%d, R%d, #%d", Integer.valueOf(this.sOperand2, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2));
                this.imm = Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2);
                this.rd = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
            } else if (this.sOperation.equals("110010")) {// AND
                // this.format = "AND rd, rs, rt";
                this.format = String.format("AND\tR%d, R%d, #%d", Integer.valueOf(this.sOperand2, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2));
                this.imm = Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2);
                this.rd = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
            } else if (this.sOperation.equals("110011")) {// NOR
                // this.format = "NOR rd, rs, rt";
                this.format = String.format("NOR\tR%d, R%d, #%d", Integer.valueOf(this.sOperand2, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2));
                this.imm = Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2);
                this.rd = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
            } else if (this.sOperation.equals("110101")) {// SLT
                // this.format = "SLT rd, rs, r";
                this.format = String.format("SLT\tR%d, R%d, #%d", Integer.valueOf(this.sOperand2, 2),
                        Integer.valueOf(this.sOperand1, 2), Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2));
                this.imm = Integer.valueOf(this.sResult + this.sImm + this.sFunction, 2);
                this.rd = "R" + Integer.valueOf(this.sOperand2, 2);
                this.rs = "R" + Integer.valueOf(this.sOperand1, 2);
            }
        }
    }

    public String getFormat() {
        return this.format;
    }

    public String signExtend(String string, int length) {
        char[] chars = new char[length];
        int size = string.length();
        for (int i = 0; i < size; i++) {
            chars[length - 1 - i] = string.charAt(size - 1 - i);
        }
        for (int i = size; i < length; i++) {
            chars[i - size] = string.charAt(0);
        }
        return new String(chars);
    }

    public String unSignExtend(String string, int length) {
        char[] chars = new char[length];
        int size = string.length();
        for (int i = 0; i < size; i++) {
            chars[length - 1 - i] = string.charAt(size - 1 - i);
        }
        for (int i = size; i < length; i++) {
            chars[i - size] = '0';
        }
        return new String(chars);
    }
}
