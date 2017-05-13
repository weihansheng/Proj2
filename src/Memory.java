import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Johan007 on 17/5/11.
 */
public class Memory {
    HashMap<Integer, String> codeSeg;
    HashMap<Integer, Integer> dataSeg;
    int codeAddr, dataAddr;
    public Memory() {
        this.codeSeg = new HashMap<Integer, String>();
        this.dataSeg = new HashMap<Integer, Integer>();
        this.codeAddr = 0;
        this.dataAddr = 0;
    }
    public void init(ArrayList<String> code, int addr1, ArrayList<Integer> data, int addr2) {
        this.codeAddr = addr1;
        this.dataAddr = addr2;
        for (String string : code) {
            codeSeg.put(addr1, string);
            addr1 += 4;
        }
        for (Integer integer : data) {
            dataSeg.put(addr2, integer);
            addr2 += 4;
        }
    }
    public void showCode() {
        Iterator<Integer> code = codeSeg.keySet().iterator();
        while(code.hasNext()){
            int key = (int)code.next();
            System.out.println(String.format("Address %d: %s", key, codeSeg.get(key)));
        }
    }
    public void showData() {
        Iterator<Integer> data = dataSeg.keySet().iterator();
        while(data.hasNext()){
            int key = (int)data.next();
            System.out.println(String.format("Address %d: %s", key, dataSeg.get(key)));
        }
    }
    public boolean write(int addr, int data) {
        boolean isLegal = this.dataSeg.containsKey(addr);
        if(isLegal){
            this.dataSeg.put(addr, data);
        }
        return isLegal;
    }
    public int readData(int addr) {
        return this.dataSeg.get(addr);
    }
    public String readCode(int addr) {
        return this.codeSeg.get(addr);
    }
    public int getDataAddr() {
        return dataAddr;
    }
    public int getCodeAddr() {
        return codeAddr;
    }
    public int getDataSize() {
        return this.dataSeg.size();
    }
    public int getCodeSize() {
        return this.codeSeg.size();
    }
}
