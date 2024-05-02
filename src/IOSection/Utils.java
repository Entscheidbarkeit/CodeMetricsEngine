package IOSection;

import CodeComplexity.Methods;
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

    private static ArrayList<Methods> methods = new ArrayList<>();
    public static boolean pathValidation(String path){
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
    public static Path readingPath(){
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
    public static void runPath(Path path){
        methods = new ArrayList<>(); // upon each given Path, the methods will be reregistered from all files
        if(Files.isDirectory(path)){
            runDirectory(path);
        }else
            runFile(path);
    }

    public static String runDirectory(Path path){
        try (Stream<Path> paths = Files.walk(path)){
            System.out.println("processing file...");
            List<Path> pathList = paths.filter(p -> !Files.isDirectory(p)&&p.toString().endsWith(".java")).collect(Collectors.toList());
            pathList.forEach(p -> {
                System.out.println(p);
            });
            System.out.println();
            Iterable<? extends CompilationUnitTree> ast = Compiler.astGenerator(pathList);
            Compiler.methodRegistration(ast,methods);
        } catch (IOException e) {
            return e.getMessage();
        }
        return "Success!";
    }
    public static String runFile(Path path){
        if(!Files.isDirectory(path)) {
            try {
                Iterable<? extends CompilationUnitTree> ast = Compiler.astGenerator(Collections.singletonList(path));
                Compiler.methodRegistration(ast, methods);
            } catch (IOException e) {
                return e.getMessage();
            }
            return "Success!";
        }
        else return "this path is a directory";
    }

    public static ArrayList<Methods> getMethods() {
        return methods;
    }
}
