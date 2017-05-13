import java.util.LinkedList;

public class PreALU implements Buffers{
	LinkedList<String> buffer;
	String [] inBuffer = new String[4];
	String [] outBuffer = new String[4];
	int in, out;
	public PreALU() {
		this.buffer = new LinkedList<String>();
		this.in = 0;
		this.out = 0;
	}
	@Override
	public int hasEmptySlot() {
		
		return 2 - this.buffer.size();
	}

	@Override
	public boolean in(Object object) {
		inBuffer[in] = (String)object;
		in++;
		return false;
	}

	@Override
	public Object out() {
		outBuffer[out] = buffer.peek();
		out++;
		return buffer.peek();
	}
	public int getOut() {
		return out;
	}
	public Object get(int index) {
		return this.buffer.get(index);
	}
	public Object remove(int index) {
		return this.buffer.remove(index);
	}
	public boolean mayWAW(String rd) {
		for (String string : buffer) {
			if(Adaptor.getOperands(string)[0].equals(rd)){
				return true;
			}
		}
		return false;
	}
	public boolean mayRAW(String rd) {
		for (String string : buffer){
			if (Adaptor.getOperands(string)[0].equals(rd)) {
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
			buffer.offer(inBuffer[i]);
		}
		in = 0;
		out = 0;
	}
}
