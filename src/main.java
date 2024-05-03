import CodeComplexity.MaintainablilityIndex;
import CodeStyle.CamelCase;
import IOSection.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class main {
    public static void main(String[] args) {
        Utils.printWelcome();
        Utils.printMenu();

        Path path = Utils.choosePath();
        System.out.println(Utils.getMethods().size() + " method(s) detected! ");

        String choice = Utils.reading(4); // number input
        while(!choice.equals("0") ) {
            if (choice.equals("1")) {
                runComplexity(path);
                choice = Utils.reading(4);
            } else if (choice.equals("2")) {
                runStyle(path);
                choice = Utils.reading(4);
            } else if (choice.equals("3")){
                path = Utils.choosePath();
                System.out.println(Utils.getMethods().size() + " method(s) detected! ");
                choice = Utils.reading(4);
            }else {
                return; // to be added with more functionality
            }
        }
        System.out.println("thank you for using Code Metrics Engine!");
    }




    public static void runComplexity(Path path){
        MaintainablilityIndex mi = new MaintainablilityIndex(path);
        mi.start(); // not yet done with sort and information printing
        mi.print(0); // 0 default mode, 1 debug mode, 2 debug mode printing to file
    }

    public static void runStyle(Path path){
        CamelCase camelCase = new CamelCase();
        camelCase.start(path);
    }

}
