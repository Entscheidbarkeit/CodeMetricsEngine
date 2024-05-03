package CodeStyle;

import IOSection.Methods;

import java.nio.file.Path;
import java.util.ArrayList;

import IOSection.Utils;

public class CamelCase {
    ArrayList<Methods> methods;
    int countInvalidName;

    public void start(Path path){
        methods = Utils.getMethods();
        runSmallCamelCase();
        print();
    }
    private void runSmallCamelCase(){
        methods.forEach(m->{
            if(!checkSmallCamel(m.getName())) {
                m.calculatePosition();
                m.nameSanitizer();
                m.print(2);
                countInvalidName++;
            }
        });
    }
    private boolean checkSmallCamel(String name) {
        if (name.length() == 0)
            return false;
        if (Character.isUpperCase(name.charAt(0))) {
            return false;
        }
        boolean upperDetected = false;
        for(int i = 0; i< name.length(); i++){
            if(upperDetected){ // if an Uppercase appeared, the next must be lowercase
                if(Character.isUpperCase(name.charAt(i))){
                   return false;
                }
                upperDetected = false;
            }
            if(Character.isUpperCase(name.charAt(i))) {
                upperDetected = true;
            }
        }
        return true;
    }
    public void print(){
        System.out.println("\nthe percentage of invalid method name is: " + ((double)countInvalidName/(double)methods.size()*100) + "%");
    }
}
