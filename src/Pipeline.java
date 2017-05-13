/**
 * Created by Johan007 on 17/5/11.
 */
public class Pipeline {
    //units
    InstructionFetch insFetch;
    Issue issue;
    Executor executor;
    WriteBack writeBack;
    Memory memory = new Memory();
    Register register = new Register();
    int pc = 0;

    //buffers
    PreIssue preIssue;
    PreMEM preMEM = new PreMEM();
    PreALU preALU = new PreALU();
    PreALUB preALUB = new PreALUB();
    PostMEM postMEM = new PostMEM();
    PostALU postALU = new PostALU();
    PostALUB postALUB = new PostALUB();
    public Pipeline() {
        insFetch = new InstructionFetch(this);
        issue = new Issue(this);
        executor = new Executor(this);
        writeBack = new WriteBack(this);
        preIssue = new PreIssue(this);
    }

    public InstructionFetch getInsFetch() {
        return insFetch;
    }
    public void setInsFetch(InstructionFetch insFetch) {
        this.insFetch = insFetch;
    }
    public Issue getIssue() {
        return issue;
    }
    public void setIssue(Issue issue) {
        this.issue = issue;
    }
    public Executor getExecutor() {
        return executor;
    }
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
    public WriteBack getWriteBack() {
        return writeBack;
    }
    public void setWriteBack(WriteBack writeBack) {
        this.writeBack = writeBack;
    }
    public Memory getMemory() {
        return memory;
    }
    public void setMemory(Memory memory) {
        this.memory = memory;
    }
    public Register getRegister() {
        return register;
    }
    public void setRegister(Register register) {
        this.register = register;
    }
    public int getPc() {
        return pc;
    }
    public void setPc(int pc) {
        this.pc = pc;
    }
    public PreIssue getPreIssue() {
        return preIssue;
    }
    public void setPreIssue(PreIssue preIssue) {
        this.preIssue = preIssue;
    }
    public PreMEM getPreMEM() {
        return preMEM;
    }
    public void setPreMEM(PreMEM preMEM) {
        this.preMEM = preMEM;
    }
    public PreALU getPreALU() {
        return preALU;
    }
    public void setPreALU(PreALU preALU) {
        this.preALU = preALU;
    }
    public PreALUB getPreALUB() {
        return preALUB;
    }
    public void setPreALUB(PreALUB preALUB) {
        this.preALUB = preALUB;
    }
    public PostMEM getPostMEM() {
        return postMEM;
    }
    public void setPostMEM(PostMEM postMEM) {
        this.postMEM = postMEM;
    }
    public PostALU getPostALU() {
        return postALU;
    }
    public void setPostALU(PostALU postALU) {
        this.postALU = postALU;
    }
    public PostALUB getPostALUB() {
        return postALUB;
    }
    public void setPostALUB(PostALUB postALUB) {
        this.postALUB = postALUB;
    }
    public void pcInc() {
        pc = pc + 4;
    }
}
