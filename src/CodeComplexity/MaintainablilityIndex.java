package CodeComplexity;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;
import Compiler.Compiler;
import com.sun.source.tree.Tree;

import IOSection.Utils;


public class MaintainablilityIndex{

    Path path;

    ArrayList<Methods> methods;

    public MaintainablilityIndex(Path path){
        this.path = path;
    }



    public void start(){
        Utils.runPath(this.path); // travers the given path
        this.methods = Utils.getMethods(); // get the methods from all files in the path from Util
        methods.forEach(Methods::runComplexityCheck);
    }


    public void print(int mode){
        if(mode == 0) { // normal mode
            System.out.println("3 methods with highest complexity(Worst Maintainability):");
            methods.sort(Comparator.comparingDouble(Methods::getMaintainabilityIndex));
            methods.stream().limit(3).forEach(m-> m.print(1));
        }
        if(mode == 1){ // debug mode

            methods.stream().forEach(m->m.debugPrint(System.out));
        }
        if(mode == 2){ // debug mode, printing to File
            Path log = Paths.get("log/complexity.txt");
            System.out.println("files on path:  " + log.toAbsolutePath().toString());
            try {
                if(!Files.exists(log)) {
                    Files.createDirectories(log.getParent());
                    Files.createFile(log);
                }
                PrintStream newOut =new PrintStream(new FileOutputStream(log.toString()));
                methods.stream().forEach(m->m.debugPrint(newOut));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
