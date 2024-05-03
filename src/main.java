import CodeComplexity.MaintainablilityIndex;
import CodeStyle.CamelCase;
import IOSection.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class main {
    public static void main(String[] args) {
        printMenu();
        printPathMenu();

        Path path = Utils.readingPath(); // path validation and file validation
        while(true) {
            int result;
            result = Utils.runPath(path);
            if (result == 1) {
                System.out.println("this file or directory dose not contain any of the java files");
                path = Utils.readingPath();
            }
            else if (result == 2) {
                System.out.println("an Exception was created during processing files");
                path = Utils.readingPath();
            }
            else {
                if(Utils.getMethods().size() == 0) {
                    System.out.println("There are no methods detected, please choose another path");
                    path = Utils.readingPath();
                }
                else break;
            }
        }
        System.out.println(Utils.getMethods().size() + " method(s) detected! ");


        String choice = Utils.reading(3); // number input
        if (choice.equals("1")) {
            runComplexity(path);
        } else if (choice.equals("2")) {
            runStyle(path);
        } else {
            return; // to be added with more functionality
        }
    }



    public static void printMenu(){
        System.out.println("Welcome to Code Metrics Engine");
        System.out.println("Following functionalities are available:");
        System.out.println("1. run Code Complexity check for given directory or file");
        System.out.println("2. run Code Style check for given directory or file");
        System.out.println("0. exit");
    }
    public static void printPathMenu(){
        System.out.println("Please give the path:");
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
