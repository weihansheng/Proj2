import java.util.LinkedList;

public class PostALUB implements Buffers{
	LinkedList<String> buffer;
	String [] inBuffer = new String[4];
	String [] outBuffer = new String[4];
	int in, out;
	public PostALUB() {
		this.buffer = new LinkedList<String>();
		this.in = 0;
		this.out = 0;
	}
	
	@Override
	public int hasEmptySlot() {
		
		return 1 - buffer.size();
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
	public Object get(int index) {
		return buffer.get(index);
	}
	public int size() {
		return buffer.size();
	}
	public boolean mayRAW(String rd) {
		for (String string : buffer) {
			if(Adaptor.getOperands(string)[0].equals(rd)){
				return true;
			}
		}
		return false;
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
