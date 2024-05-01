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


public class MaintainablilityIndex{

    Path path;

    ArrayList<Methods> methods;

    public MaintainablilityIndex(Path path){
        this.path = path;
        methods = new ArrayList<>();
    }

    public void pathDecision(Path path){
        if(Files.isDirectory(path)){
            runDirectory(path);
        }else
            runFile(path);
    }

    public String runDirectory(Path path){
        try (Stream<Path> paths = Files.walk(path)){
            paths.filter(p -> !p.equals(path)).forEach(p -> {
                System.out.println(p.toString());
                runFile(p);
            });
        } catch (IOException e) {
            return e.getMessage();
        }
        return "Success!";
    }
    int startPoint = 0;
    public String runFile(Path path){
        if(!Files.isDirectory(path)) {
            try {
                Iterable<? extends CompilationUnitTree> ast = Compiler.astGenerator(path);
                methodRegistration(ast, path);
                for (int i = startPoint; i < methods.size(); i++) {
                    methods.get(i).runComplexityCheck();
                }
                startPoint = methods.size();
            } catch (IOException e) {
                return e.getMessage();
            }
            return "Success!";
        }
        else return "this path is a directory";
    }
    public void methodRegistration(Iterable<? extends CompilationUnitTree> ast,Path path){
        ast.forEach(Unit->{ // we process each file individualy, so only one Unit here
            Unit.getTypeDecls().forEach(classTree->{ // extract classTree from Unit
                if(classTree instanceof ClassTree){
                    methodsFromInnerClass((ClassTree)classTree,path);
                }
            });

        });
    }
    public void methodsFromInnerClass(ClassTree classTree,Path path){
        String className = ((ClassTree) classTree).getSimpleName().toString();
        for(Tree methodTree:((ClassTree) classTree).getMembers()){  // extract methodTree from classTree
            if(methodTree instanceof MethodTree){
                methods.add(new Methods(
                                path,
                                className, // class Name
                                ((MethodTree)methodTree).getName().toString(), //  method Name
                                (MethodTree) methodTree // tree of this method
                        )
                );
            }
            if(methodTree instanceof ClassTree){
                methodsFromInnerClass((ClassTree) methodTree,path); // run recursively if there are innerClasses
            }
        }
    }

    public void start(){
        pathDecision(this.path);
    }

    public void sort(){
        methods.sort(Comparator.comparingDouble(Methods::getMaintainabilityIndex));
    }

    public void print(int mode){
        if(mode == 0) { // normal mode
            System.out.println("the 3 methods with highest complexity:");
            this.sort();
            methods.stream().limit(3).forEach(Methods::print);
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
