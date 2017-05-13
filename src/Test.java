/**
 * Created by Johan007 on 17/5/11.
 */
public class Test {
    public static void main(String[] args) {
        String [] array = Adaptor.getOperands("J\t#188");
//		String [] array = "ADD\tR4, R2, R1".split(" ");
        for(String string : array){
            System.out.println(string);
        }
        boolean flag = WAR("ADD	R4, R2, R1");
        System.out.println(flag);
    }

    public static boolean WAR(String assembly1) {
        boolean flag = false;
        String r0 = Adaptor.getOperands(assembly1)[0];
        String r1 = Adaptor.getOperands(assembly1)[1];
        String r2 = Adaptor.getOperands(assembly1)[2];
        if (assembly1.contains("LW\t")) {
            flag = mayWAR(r1);
        }else if(assembly1.contains("SW\t")){
            flag = mayWAR(r1) || mayWAR(r0);
        }else if(r2.contains("R")){
            flag = mayWAR(r1) || mayWAR(r2);
        }else{
            flag = mayWAR(r1);
        }

        return flag;
    }
    public static boolean mayWAR(String rd) {

        if (Adaptor.getOperands("ADD	R2, R0, #1")[0].equals(rd)) {
            return true;
        }
        return false;

    }
}
