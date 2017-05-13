/**
 * Created by Johan007 on 17/5/11.
 */
public class WriteBack {
    Pipeline pipeline;
    //	public LinkedList<String> MEM = new LinkedList<String>();
//	public LinkedList<String> ALU = new LinkedList<String>();
//	public LinkedList<String> ALUB = new LinkedList<String>();
    public WriteBack(Pipeline pipeline) {

        this.pipeline = pipeline;
    }

    public void writeBack(int clock) {
        if(this.pipeline.getPostMEM().hasEmptySlot()==0){
            //"%s|%s|%d"
            String result = (String)this.pipeline.getPostMEM().out();
            String [] args = result.split("@");
            this.pipeline.getRegister().write(args[1], Integer.parseInt(args[2]));
//			MEM.offer(args[1]);
        }
        if(this.pipeline.getPostALU().hasEmptySlot()==0){
            String result = (String)this.pipeline.getPostALU().out();
            String [] args = result.split("@");
            this.pipeline.getRegister().write(args[1], Integer.parseInt(args[2]));
//			ALU.offer(args[1]);
        }
        if(this.pipeline.getPostALUB().hasEmptySlot()==0){
            String result = (String)this.pipeline.getPostALUB().out();
            String [] args = result.split("@");
            this.pipeline.getRegister().write(args[1], Integer.parseInt(args[2]));
//			ALUB.offer(args[1]);
        }
    }
    public boolean isStalled() {
        return this.pipeline.getPostMEM().hasEmptySlot() == 1 &&
                this.pipeline.getPostALU().hasEmptySlot()==1 && this.pipeline.getPostALUB().hasEmptySlot()==1;
    }
}
