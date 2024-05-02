package CodeStyle;

import CodeComplexity.Methods;

import java.nio.file.Path;
import java.util.ArrayList;

import IOSection.Utils;

public class CamelCase {
    ArrayList<Methods> methods;
    int countInvalidName;

    public void start(Path path){
        Utils.runPath(path);
        methods = Utils.getMethods();
        runSmallCamelCase();
        print();
    }
    public void runSmallCamelCase(){
        methods.forEach(m->{
            if(!checkSmallCamel(m.getName())) {
                m.nameSanitizer();
                m.print(2);
                countInvalidName++;
            }
        });
    }
    public boolean checkSmallCamel(String name) {
        if (name.length() == 0)
            return false;
        if (Character.isUpperCase(name.charAt(0))) {
            return false;
        }
        boolean upperdetected = false;
        for(int i = 0; i< name.length(); i++){
            if(upperdetected){
                if(Character.isUpperCase(name.charAt(i))){
                   return false;
                }
                upperdetected = false;
            }
            if(Character.isUpperCase(name.charAt(i))) {
                upperdetected = true;
            }
        }
        return true;
    }
    public void print(){
        System.out.println("\nthe percentage of invalid method name is: " + ((double)countInvalidName/(double)methods.size()*100) + "%");
    }
}
