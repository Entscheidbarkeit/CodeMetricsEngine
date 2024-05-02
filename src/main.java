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
        String choice = Utils.reading(3);

        if(choice.equals("1")){
            printPathMenu();
            Path path = Utils.readingPath();
            if(path == null)
                printNoSuchFile();
            else
                runComplexity(path);
        }
        else if(choice.equals("2")){
            printPathMenu();
            Path path = Utils.readingPath();
            if(path == null)
                printNoSuchFile();
            else runStyle(path);
        }
        else{
            return; // to be added with more functionality
        }
    }
    public static void printNoSuchFile(){
        System.out.println("the designated path do not exist! Please retry");
    }



    public static void printMenu(){
        System.out.println("Welcome to Code Metrics Engine");
        System.out.println("Please choose the functionality with number:");
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
