/**
 * Created by Johan007 on 17/5/11.
 */
public class InstructionFetch {
    Pipeline pipeline;
    boolean isStalled;
    public String waitingIns, executedIns;
    public InstructionFetch(Pipeline pipeline) {
        // TODO Auto-generated constructor stub
        this.pipeline = pipeline;
        this.isStalled = false;
        this.waitingIns = "";
        this.executedIns = "";
    }
    public int fetch() {
        int nums = 0;
        int slots = pipeline.getPreIssue().hasEmptySlot();
        String binary = "";
        String assembly = "";
        if (!isStalled) {//fetch
            if (slots == 1) {//fetch 1 instruction
                binary = pipeline.getMemory().readCode(pipeline.getPc());
                assembly = decode(binary);
                if (isBranch(binary)) {
                    processBranch(assembly);
                }else if(isBreak(binary)){
                    this.executedIns = "BREAK";
                    this.isStalled = true;
                    return -1;
                }else if(isNop(binary)){
                    pipeline.pcInc();
                }else {
                    //放到PreIssue
                    pipeline.getPreIssue().in(assembly);
                    pipeline.pcInc();//pc+4
                }
                nums = 1;
            }else if(slots >= 2){//fetch 2 instruction
                binary = pipeline.getMemory().readCode(pipeline.getPc());
                assembly = decode(binary);
                if (isBranch(binary)) {
                    processBranch(assembly);
                    nums = 1;
                    return nums;
                }else if(isBreak(binary)){
                    this.executedIns = "BREAK";
                    this.isStalled = true;
                    return -1;
                }else if(isNop(binary)){
                    pipeline.pcInc();
                }else{
                    pipeline.getPreIssue().in(assembly);
                    pipeline.pcInc();
                }

                binary = pipeline.getMemory().readCode(pipeline.getPc());
                assembly = decode(binary);
                if (isBranch(binary)) {
                    processBranch(assembly);
                    nums = 1;
                    return nums;
                }else if(isBreak(binary)){
                    this.executedIns = "BREAK";
                    this.isStalled = true;
                    return -1;
                }else if(isNop(binary)){
                    pipeline.pcInc();
                }else{
                    pipeline.getPreIssue().in(assembly);
                    pipeline.pcInc();
                }
            }

        }
        return nums;
    }
    public String decode(String binary){
        String assembly = Adaptor.getAssembly(binary);

        return assembly;
    }
    public void setStalled(boolean isStalled) {
        this.isStalled = isStalled;
    }
    public boolean isStalled() {
        return isStalled;
    }
    public boolean isBranch(String binary) {

        return Adaptor.getIsBranch(binary);
    }
    public boolean isBreak(String binary) {
        return Adaptor.isBreak(binary);
    }
    public boolean isNop(String binary) {
        return Adaptor.isNop(binary);
    }
    public void processBranch(String assembly) {
        String [] operands = Adaptor.getOperands(assembly);
        if(assembly.contains("J\t")){
            this.pipeline.setPc(Integer.parseInt(operands[0]));
            this.executedIns = assembly;
            this.waitingIns = "";
        }else if(assembly.contains("JR\t")){
            if (mayRAW(operands[0])) {
                setStalled(true);
                this.waitingIns = assembly;
            }else{
                this.pipeline.setPc(this.pipeline.getRegister().read(operands[0]));
                this.executedIns = assembly;
                this.waitingIns = "";
                setStalled(false);

            }
        }else if(assembly.contains("BEQ\t")){
            if(mayRAW(operands[0])){
                setStalled(true);
                this.waitingIns = assembly;
            }else if(mayRAW(operands[1])){
                setStalled(true);
                this.waitingIns = assembly;
            }else{
                boolean condition = this.pipeline.getRegister().read(operands[0]) == this.pipeline.getRegister().read(operands[1]);
                if(condition){
                    this.pipeline.setPc(this.pipeline.getPc() + Integer.parseInt(operands[2]));
                    this.executedIns = assembly;
                    this.waitingIns = "";
                    pipeline.pcInc();
                }else{
                    this.executedIns = assembly;
                    this.waitingIns = "";
                    pipeline.pcInc();
                }
            }
        }else if(assembly.contains("BLTZ\t")){
            if(mayRAW(operands[0])){
                setStalled(true);
                this.waitingIns = assembly;
            }else{
                if(this.pipeline.getRegister().read(operands[0])<0){
                    this.pipeline.setPc(this.pipeline.getPc() + Integer.parseInt(operands[1]));
                    this.executedIns = assembly;
                    this.waitingIns = "";
                    pipeline.pcInc();
                }else{
                    this.executedIns = assembly;
                    this.waitingIns = "";
                    pipeline.pcInc();
                }
            }
        }else if(assembly.contains("BGTZ\t")){
            if(mayRAW(operands[0])){
                setStalled(true);
                this.waitingIns = assembly;
            }else{
                if(this.pipeline.getRegister().read(operands[0])>0){
                    this.pipeline.setPc(this.pipeline.getPc() + Integer.parseInt(operands[1]));
                    this.executedIns = assembly;
                    this.waitingIns = "";
                    setStalled(false);
                    pipeline.pcInc();
                }else{
                    this.executedIns = assembly;
                    this.waitingIns = "";
                    pipeline.pcInc();
                }
            }
        }else {
            System.out.println("IF->isBranch has a exception!");
        }
    }
    public String getWaitingIns() {
        return this.waitingIns;
    }
    public String getExecutedIns() {
        return this.executedIns;
    }
    public boolean mayRAW(String rd){
        boolean flag = false;

        flag = pipeline.getPreIssue().mayRAW4Branch(rd) || pipeline.getPreMEM().mayRAW(rd) || pipeline.getPreALU().mayRAW(rd)
                || pipeline.getPreALUB().mayRAW(rd) || pipeline.getPostMEM().mayRAW(rd) || pipeline.getPostALU().mayRAW(rd)
                || pipeline.getPostALUB().mayRAW(rd);

        return flag;
    }
}
