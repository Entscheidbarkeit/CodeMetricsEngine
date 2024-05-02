package Compiler;

import CodeComplexity.Methods;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Compiler {
    public static Iterable<? extends CompilationUnitTree> astGenerator(List<Path> path) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null,null,null);
        Iterable<? extends JavaFileObject> file = fileManager.getJavaFileObjectsFromPaths(path);
        JavacTask task = (JavacTask) compiler.getTask(null,fileManager,null,null,null,file);
        return task.parse();
    }
    public static ArrayList<Methods> methodRegistration(Iterable<? extends CompilationUnitTree> ast, ArrayList<Methods> methods){
        ast.forEach(Unit->{
            Path path = Paths.get(Unit.getSourceFile().toUri());
            Unit.getTypeDecls().forEach(classTree->{ // extract classTree from Unit
                if(classTree instanceof ClassTree){
                    methodsFromInnerClass((ClassTree)classTree,path, methods);
                }
            });
        });
        return methods;
    }
    public static void methodsFromInnerClass(ClassTree classTree,Path path,ArrayList<Methods> methods){
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
                methodsFromInnerClass((ClassTree) methodTree,path,methods); // run recursively if there are innerClasses
            }
        }
    }
}
