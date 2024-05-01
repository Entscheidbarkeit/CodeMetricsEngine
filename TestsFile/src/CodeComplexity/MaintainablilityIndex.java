package CodeComplexity;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
            paths.forEach(this::pathDecision);  // recursively run the directory
        } catch (IOException e) {
            return e.getMessage();
        }
        return "Success!";
    }

    public String runFile(Path path){
        try {
            Iterable<? extends CompilationUnitTree> ast = Compiler.astGenerator(path);
            methodRegistration(ast,path);
            for(Methods method: methods){
                method.runComplexityCheck();
            }
        }catch(IOException e){
            return e.getMessage();
        }
        return "Success!";
    }
    public void methodRegistration(Iterable<? extends CompilationUnitTree> ast,Path path){
        ast.forEach(Unit->{ // we process each file individualy, so only one Unit here
            Unit.getTypeDecls().forEach(classTree->{ // extract classTree from Unit
                if(classTree instanceof ClassTree){
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

                    }
                }
            });

        });
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
            methods.stream().limit(3).forEach(Methods::print);
        }
        if(mode == 1){ // debug mode
            methods.stream().forEach(Methods::debugPrint);
        }
    }

}
