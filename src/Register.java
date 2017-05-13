import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Johan007 on 17/5/11.
 */
public class Register {
    HashMap<String, Integer> registers;
    HashMap<String, Boolean> busy;
    HashMap<String, Boolean> stall;
    public Register() {
        this.registers = new HashMap<String, Integer>();
        this.busy = new HashMap<String, Boolean>();
        this.stall = new HashMap<String, Boolean>();
        init();
    }
    public void init() {
        for(int i=0;i<32;i++){
            this.registers.put("R"+i, 0);
            this.busy.put("R"+i, false);
            this.stall.put("R"+i, false);
        }
    }
    public int read(String key) {
        return this.registers.get(key);
    }
    public void write(String key, int value) {
        this.registers.put(key, value);
    }
    public void show() {
        Iterator<String> data = registers.keySet().iterator();
        while(data.hasNext()){
            String key = (String)data.next();
            System.out.println(String.format("Address %s: %d", key, registers.get(key)));
        }
    }
}
