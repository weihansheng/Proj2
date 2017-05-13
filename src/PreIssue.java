import java.util.LinkedList;

public class PreIssue implements Buffers{
	Pipeline pipeline;
	//双向列表 作为List使用时,一般采用add / get方法来 压入/获取对象;作为Queue使用时,才会采用 offer/poll/take等方法
	LinkedList<String> buffer;
	String [] inBuffer = new String[4];
	String [] outBuffer = new String[4];
	int in, out;
	public PreIssue(Pipeline pipeline) {
		this.buffer = new LinkedList<String>();
		this.pipeline = pipeline;
		this.in = 0;
		this.out = 0;
	}
	public int hasEmptySlot() {
		
		return 4 - this.buffer.size();
	}

	public boolean in(Object object) {
		
		inBuffer[in] = (String)object;
		in++;
		return false;
	}

	public Object out() {
		outBuffer[out] = buffer.peek();
		out++;
		return buffer.peek();//返回此列表的头元素，或null，如果此列表为空
	}
	public Object get(int index) {
		return this.buffer.get(index);
	}
	public Object remove(int index) {
		return this.buffer.remove(index);
	}
	public String type(int index) {
		String assembly = this.buffer.get(index);
		String type = "";
		
		if (assembly.contains("LW\t")||assembly.contains("SW\t")) {
			//MEM\
			type = "MEM";
		}else if(assembly.contains("SLL\t")||assembly.contains("SRL\t")||assembly.contains("SRA\t")||assembly.contains("MUL\t")){
			//ALUB
			type = "ALUB";
		}else{
			//ALU
			type = "ALU";
		}
		return type;
	}
	public int haveElem(int from) {
		for(int i=from;i<this.buffer.size();i++){
			if(pipeline.getIssue().noHazards(i)){
				return i;
			}
		}
		return -1;
	}
	public boolean mayWAW(String assembly) {
		for(int i=0;i<buffer.size();i++){
			if (buffer.get(i).equals(assembly)) {
				break;
			}
			String s1 = Adaptor.getOperands(buffer.get(i))[0];
			String s2 = Adaptor.getOperands(assembly)[0];
			if (buffer.get(i).contains("SW\t")||assembly.contains("SW\t")) {
				return false;
			}
			if (s1.equals(s2)) {
				return true;
			}
		}
		return false;
	}
	public boolean mayWAR(String assembly) {
		for(int i=0;i<buffer.size();i++){
			if(buffer.get(i).equals(assembly)){
				break;
			}
			if(assembly.contains("SW\t")){
				return false;
			}
			String [] operands = Adaptor.getOperands(buffer.get(i));
			
			String s2 = Adaptor.getOperands(assembly)[0];
			if(buffer.get(i).contains("LW\t")){
				if(operands[1].equals(s2)){
					return true;
				}
			}else if(buffer.get(i).contains("SW\t")){
				if(operands[0].equals(s2)||operands[1].equals(s2)){
					return true;
				}
			}else{
				if (operands[1].equals(s2)||operands[2].equals(s2)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean mayRAW(String assembly) {
		for(int i=0;i<buffer.size();i++){
			if(buffer.get(i).equals(assembly)){
				break;
			}
			if(buffer.contains("SW\t")){
				continue;
			}
			String s1 = Adaptor.getOperands(buffer.get(i))[0];
			String s20 = Adaptor.getOperands(assembly)[0];
			String s21 = Adaptor.getOperands(assembly)[1];
			String s22 = Adaptor.getOperands(assembly)[2];
			if(assembly.contains("LW\t")){
				if (s21.equals(s1)) {
					return true;
				}
			}else if(assembly.contains("SW\t")){
				if (s20.equals(s1)||s21.equals(s1)){
					return true;
				}
			}else{
				if(s21.equals(s1)||s22.equals(s1)){
					return true;
				}
			}
		}
		return false;
	}
	public boolean mayRAW4Branch(String r) {
		for(int i=0;i<buffer.size();i++ ){
			if(buffer.get(i).contains("SW\t")){
				continue;
			}
			if(Adaptor.getOperands(buffer.get(i))[0].equals(r)){
				return true;
			}
		}
		return false;
	}
	public int size() {
		return buffer.size();
	}
	public void flush() {
		for(int i=out-1;i>-1;i--){
			buffer.remove(outBuffer[i]);
		}
		for(int i=0;i<in;i++){
			buffer.offer(inBuffer[i]);//作为List使用时,一般采用add / get方法来 压入/获取对象;作为Queue使用时,才会采用 offer/poll/take等方法
		}
		in = 0;
		out = 0;
	}
}
