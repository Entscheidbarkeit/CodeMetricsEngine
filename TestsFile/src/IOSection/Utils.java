package IOSection;

import CodeComplexity.Methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Utils {
    public static Path pathValidation(String path){
        Path pathO = Paths.get(path);
        if(Files.exists(pathO))
            return pathO;
        else
            return null;
    }
    public static String reading (int validation){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input =  reader.readLine();
            input = input.trim();
            if(validation == 0){ // only number 0,1,2 accepted
                if(!input.equals("1")&&!input.equals("2")&&!input.equals("0")){
                    System.out.println("invalid input,please retry");
                    return reading(validation);
                }
                else return input;
            }
            return input;
        } catch (IOException e) {
            System.out.println("invalid input,please retry");
            return reading(validation);
        }
    }
}
