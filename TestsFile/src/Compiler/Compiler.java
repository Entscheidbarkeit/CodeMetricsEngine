package Compiler;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class Compiler {
    public static Iterable<? extends CompilationUnitTree> astGenerator(Path path) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null,null,null);
        Iterable<? extends JavaFileObject> file = fileManager.getJavaFileObjectsFromPaths(Collections.singletonList(path));

        JavacTask task = (JavacTask) compiler.getTask(null,fileManager,null,null,null,file);
        return task.parse();
    }
}
