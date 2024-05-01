import CodeComplexity.MaintainablilityIndex;
import IOSection.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class main {
    public static void main(String[] args) {
        // deal with user input, could be a path or directory, with other parameters
        // -help or -h or any other unvalid parameter : print help page
        // -c check the code complexity with the given path
        // -s check code style


        printMenu();
        String choice = Utils.reading(0);

        if(choice.equals("1")){
            printPathMenu();
            String pathString = Utils.reading(1);
            Path path = Utils.pathValidation(pathString);
            if(path == null)
                printNoSuchFile();
            else
                runComplexity(path);
        }
        else if(choice.equals("2")){
            printPathMenu();
            String pathString = Utils.reading(1);
            Path path = Utils.pathValidation(pathString);
            if(path == null)
                printNoSuchFile();
            //runStyle(path0);
            System.out.println("Functionality not yet implemented");
        }
        else{
            return;
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
        mi.print(1);
    }
}
