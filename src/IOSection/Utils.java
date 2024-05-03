package IOSection;

import com.sun.source.tree.CompilationUnitTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import Compiler.Compiler;

public class Utils {

    private static ArrayList<Methods> methods = new ArrayList<>(); // this is the methods list for all functionalities


    public static boolean pathValidation(String path){ // validate the given path
        try{
        Path pathO = Paths.get(path);
        if(Files.exists(pathO))
            return true;
        else
            return false;
        }catch(Exception e){
            return false;
        }
    }
    public static String reading (int max){ // reading with validation of numbers in range [0,max]
        System.out.println("1. run Code Complexity check for given directory or file");
        System.out.println("2. run Code Style check for given directory or file");
        System.out.println("0. exit");
        System.out.println("Please choose the functionality with number:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String input = reader.readLine();
                input = input.trim();
                for(int i =0; i<max; i++) {
                    if (input.equals(String.valueOf(i))) {
                        return input;
                    }
                }
                System.out.println("invalid input,please retry");
                return reading(max);
            } catch (IOException e) {
                System.out.println("invalid input,please retry");
                return reading(max);
            }
    }
    public static Path readingPath(){ // repeat input procedure until valid path is given
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print(System.getProperty("user.dir")+"\\");
            String input = reader.readLine();
            input = input.trim();
            if(pathValidation(input))
                return Paths.get(input);
            System.out.println("invalid path,please retry");
            return readingPath();
        } catch (IOException e) {
            System.out.println("invalid path,please retry");
            return readingPath();
        }
    }
    public static int runPath(Path path){ // decided if a path is a directory or file , the Arraylist of Methods will be initialized here
        methods = new ArrayList<>();
        if(Files.isDirectory(path)){
            return runDirectory(path);
        }else
            return runFile(path);
    }

    private static int runDirectory(Path path){
        try (Stream<Path> paths = Files.walk(path)){
            System.out.println("processing file...");
            List<Path> pathList = paths.filter(p -> !Files.isDirectory(p)&&p.toString().endsWith(".java")).collect(Collectors.toList());
            if(pathList.isEmpty()){
                return 1; // No java files
            }
            pathList.forEach(p -> {
                System.out.println(p);
            });
            System.out.println();
            Iterable<? extends CompilationUnitTree> ast = Compiler.astGenerator(pathList);
            Compiler.methodRegistration(ast,methods);
        } catch (IOException e) {
            return 2; // Exception
        }
        return 0; // successful
    }
    private static int runFile(Path path){
            if(!path.toString().endsWith(".java"))
                return 1;
            try {
                Iterable<? extends CompilationUnitTree> ast = Compiler.astGenerator(Collections.singletonList(path));
                Compiler.methodRegistration(ast, methods);
            } catch (IOException e) {
                return 2; // Exception
            }
            return 0;
    }

    public static ArrayList<Methods> getMethods() {
        return methods;
    }
}
