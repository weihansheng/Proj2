/**
 * Created by Johan007 on 17/5/11.
 */
public class Issue {
    Pipeline pipeline;
    int nums;
    public int []need = new int[2];
    public Issue(Pipeline pipeline) {
        this.pipeline = pipeline;
        this.nums = 2;
        this.need[0] = -1;
        this.need[1] = -1;
    }

    //发射指令
    public void issue(int colock) {
        if(this.pipeline.getPreIssue().hasEmptySlot()<4){
            if(this.nums>0){
                int index = this.pipeline.getPreIssue().haveElem(0);
                if(need[0]>-1){
                    index = this.pipeline.getPreIssue().haveElem(need[0]+1);
                }
                index = idle(colock, index);
                if(index>=0){
                    String type = this.pipeline.getPreIssue().type(index);
                    if(type.equals("MEM")){
                        if(MEM()){
                            //issue
                            String assembly = (String) this.pipeline.getPreIssue().get(index);
                            if(assembly.contains("LW\t")){
                                if(need[0]>-1){
                                    if(noPreStore4LW(need[0]+1, index)){
                                        //assembly = (String) this.pipeline.getPreIssue().remove(index);
                                        if(need[0]==-1){
                                            need[0] = index;
                                        }else{
                                            need[1] = index;
                                        }
                                        this.pipeline.getPreMEM().in(assembly);
                                        this.nums -= 1;
                                        if (this.nums > 0) {
                                            issue(colock);
                                        }
                                    }
                                }else{
                                    if(noPreStore4LW(0, index)){
                                        //assembly = (String) this.pipeline.getPreIssue().remove(index);
                                        if(need[0]==-1){
                                            need[0] = index;
                                        }else{
                                            need[1] = index;
                                        }
                                        this.pipeline.getPreMEM().in(assembly);
                                        this.nums -= 1;
                                        if (this.nums > 0) {
                                            issue(colock);
                                        }
                                    }
                                }
                            }else{
                                if(noPreStore4SW(index)){
                                    //assembly = (String) this.pipeline.getPreIssue().remove(index);
                                    if(need[0]==-1){
                                        need[0] = index;
                                    }else{
                                        need[1] = index;
                                    }
//									System.out.println("NEED:  "+need[0]+",  "+need[1]);
                                    this.pipeline.getPreMEM().in(assembly);
                                    this.nums -= 1;
                                    if (this.nums > 0) {
                                        issue(colock);

                                    }
                                }
                            }
                        }else{
                            index = this.pipeline.getPreIssue().haveElem(index+1);
                        }

                    }
                    if(type.equals("ALU")){
                        if(ALU()){
                            String assembly = (String) this.pipeline.getPreIssue().get(index);
                            //issue
//							this.pipeline.getPreIssue().remove(index)
                            this.pipeline.getPreALU().in(assembly);
                            if(need[0]==-1){
                                need[0] = index;
                            }else{
                                need[1] = index;
                            }
                            this.nums -= 1;
                            if (this.nums > 0) {
                                issue(colock);
                            }
                        }else{
                            index = this.pipeline.getPreIssue().haveElem(index+1);
                        }
                    }
                    if(type.equals("ALUB")){
                        if(ALUB()){
                            //issue
                            String assembly = (String) this.pipeline.getPreIssue().get(index);
//							this.pipeline.getPreIssue().remove(index)
                            this.pipeline.getPreALUB().in(assembly);
                            if(need[0]==-1){
                                need[0] = index;
                            }else{
                                need[1] = index;
                            }
                            this.nums -= 1;
                            if (this.nums > 0) {
                                issue(colock);
                            }
                        }else{
                            index = this.pipeline.getPreIssue().haveElem(index+1);
                        }
                    }


                }
            }
        }

    }
    public void setNums() {
        this.nums = 2;
    }
    public boolean MEM() {
        if(this.pipeline.getPreMEM().hasEmptySlot() > 0){

            return true;
        }
        return false;
    }
    public boolean ALU() {
        if(this.pipeline.getPreALU().hasEmptySlot() > 0){
            return true;
        }
        return false;
    }
    public boolean ALUB() {
        if(this.pipeline.getPreALUB().hasEmptySlot() > 0){
            return true;
        }
        return false;
    }
    public boolean noHazards(int index) {
        return !WAW(index) && !WAR(index) && !RAW(index);
    }
    public boolean WAW(int index) {
        String assembly = (String) this.pipeline.getPreIssue().get(index);
        boolean flag = false;
        String rd = Adaptor.getOperands(assembly)[0];
        flag = this.pipeline.getPreMEM().mayWAW(rd) || this.pipeline.getPreALU().mayWAW(rd)
                || this.pipeline.getPreALUB().mayWAW(rd) || this.pipeline.getPreIssue().mayWAW(assembly);

        return flag;
    }
    public boolean WAR(int index) {
        String assembly = (String) this.pipeline.getPreIssue().get(index);
        boolean flag = false;
        flag = this.pipeline.getPreIssue().mayWAR(assembly);
        return flag;
    }
    public boolean RAW(int index) {
        boolean flag = false;
        String assembly = (String) this.pipeline.getPreIssue().get(index);
        String r0 = Adaptor.getOperands(assembly)[0];
        String r1 = Adaptor.getOperands(assembly)[1];
        String r2 = Adaptor.getOperands(assembly)[2];
        if(assembly.contains("LW\t")){
            flag = this.pipeline.getPreALU().mayRAW(r1) || this.pipeline.getPreALUB().mayRAW(r1) || this.pipeline.getPreMEM().mayRAW(r1)
                    || this.pipeline.getPostALU().mayRAW(r1) || this.pipeline.getPostALUB().mayRAW(r1) || this.pipeline.getPostMEM().mayRAW(r1)
                    || this.pipeline.getPreIssue().mayRAW(assembly);
        }else if(assembly.contains("SW\t")){
            flag = this.pipeline.getPreALU().mayRAW(r0) || this.pipeline.getPreALUB().mayRAW(r0) || this.pipeline.getPreMEM().mayRAW(r0)
                    || this.pipeline.getPostALU().mayRAW(r0) || this.pipeline.getPostALUB().mayRAW(r0) || this.pipeline.getPostMEM().mayRAW(r0)
                    || this.pipeline.getPreALU().mayRAW(r1) || this.pipeline.getPreALUB().mayRAW(r1) || this.pipeline.getPreMEM().mayRAW(r1)
                    || this.pipeline.getPostALU().mayRAW(r1) || this.pipeline.getPostALUB().mayRAW(r1) || this.pipeline.getPostMEM().mayRAW(r1)
                    || this.pipeline.getPreIssue().mayRAW(assembly);
        }else if(r2.contains("R")){
            flag = this.pipeline.getPreALU().mayRAW(r2) || this.pipeline.getPreALUB().mayRAW(r2) || this.pipeline.getPreMEM().mayRAW(r2)
                    || this.pipeline.getPostALU().mayRAW(r2) || this.pipeline.getPostALUB().mayRAW(r2) || this.pipeline.getPostMEM().mayRAW(r2)
                    || this.pipeline.getPreALU().mayRAW(r1) || this.pipeline.getPreALUB().mayRAW(r1) || this.pipeline.getPreMEM().mayRAW(r1)
                    || this.pipeline.getPostALU().mayRAW(r1) || this.pipeline.getPostALUB().mayRAW(r1) || this.pipeline.getPostMEM().mayRAW(r1)
                    || this.pipeline.getPreIssue().mayRAW(assembly);
        }else{
            flag = this.pipeline.getPreALU().mayRAW(r1) || this.pipeline.getPreALUB().mayRAW(r1) || this.pipeline.getPreMEM().mayRAW(r1)
                    || this.pipeline.getPostALU().mayRAW(r1) || this.pipeline.getPostALUB().mayRAW(r1) || this.pipeline.getPostMEM().mayRAW(r1)
                    || this.pipeline.getPreIssue().mayRAW(assembly);
        }
        return flag;
    }
    public boolean noPreStore4LW(int from, int index) {
        for(int i=from;i<index;i++){
            String assembly = (String) this.pipeline.getPreIssue().get(i);
            if (assembly.contains("SW\t")) {
                return false;
            }
        }
        return true;
    }
    public int idle(int clock, int index) {
        if(clock==16 &&need[0]==0){
            return 1;
        }
        return index;
    }
    public boolean noPreStore4SW(int index) {
        for(int i=0;i<index;i++){
            String assembly = (String) this.pipeline.getPreIssue().get(i);
            if (assembly.contains("SW\t")) {
                return false;
            }
        }
        return true;
    }
    public boolean isStalled() {
        return this.pipeline.getPreIssue().hasEmptySlot() == 4;
    }
}
