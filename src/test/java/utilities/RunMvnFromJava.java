package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RunMvnFromJava {

    static public String[] runCommand(String[] cmd) throws IOException {

        ArrayList<Object> list = new ArrayList<>();
        //ArrayList list = new ArrayList<>();

        Process proc = Runtime.getRuntime().exec(cmd);

        InputStream istr = proc.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(istr));
        String str;

        while ((str = br.readLine()) != null)
            list.add(str);
        try {
                proc.waitFor();
            }catch(InterruptedException e){
            System.out.println("Process was interupted");
        }

        br.close();

        return (String[])list.toArray(new Object[0]);
        //return (String[])list.toArray(new String[0]);
    }
}
