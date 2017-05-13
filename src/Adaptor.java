/**
 * Created by Johan007 on 17/5/11.
 */
public class Adaptor {
    public static String getAssembly(String binary) {
        return new Instruction(binary, 0).getFormat();
    }
    public static boolean getIsBranch(String binary) {
        return new Instruction(binary, 0).isBranch;
    }
    public static boolean isBreak(String binary) {
        if(new Instruction(binary, 0).getFormat().contains("BREAK")){
            return true;
        }
        return false;
    }
    public static boolean isNop(String binary) {
        if(new Instruction(binary, 0).getFormat().contains("NOP")){
            return true;
        }
        return false;
    }
    public static String[] getOperands(String assembly) {
        String [] array = assembly.split(" ");
        String [] result = new String[array.length];
        result[0] = array[0].split("\t")[1].replace(",", "").replace("#", "");
        for(int i=1;i<array.length;i++){
            result[i] = array[i].replace(",", "").replace("#", "");
        }
        if (assembly.contains("LW\t")||assembly.contains("SW\t")) {
            result[1] = result[1].replace("(", " ").replace(")", "");
            String[]temp = new String[3];
            temp[0] = result[0];
            temp[1] = result[1].split(" ")[1];
            temp[2] = result[1].split(" ")[0];
            result = temp;
        }
        return result;

    }
}
