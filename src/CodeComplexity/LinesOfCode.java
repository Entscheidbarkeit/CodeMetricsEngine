package CodeComplexity;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;
import java.util.regex.Pattern;

public class LinesOfCode { // we took the assumption for simplicity that all methods are declared in a line
    private int lines = 0;
    private int position = 0;

    private int totalLines = 0;
    private Stack<Boolean> stack;
    public LinesOfCode(){
        stack = new Stack<>();
    }

    public int getPosition() {
        return position;
    }

    public int getLines() {
        return lines;
    }

    public int calculate(Path path, MethodTree methodTree, String name) {

        try {
            Files.lines(path).forEach(line->{
                if(signatureOfMethod(line,methodTree,name)){
                    lineValidation(stack,line);
                    lines++;
                    position = totalLines+1;
                }
                else if(!stack.empty()){
                    lineValidation(stack,line);
                    lines++;
                }
                totalLines++;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
    private boolean inString = false; // determine if we are inside a String
    private boolean inComment = false;

    private boolean inChar = false;
    public boolean lineValidation(Stack<Boolean> stack, String line){ // run this method to check the pair of {},which helps to determine the body of methods


        for(int i = 0; i< line.length(); i++) {
            if(!inString&&!inComment){
                if(line.charAt(i)=='\'')
                    inChar = !inChar;
            }
            if(!inComment&&!inChar) {
                if (line.charAt(i) == '"')
                    inString = !inString;
            }
            if(!inString&&!inChar) { // outside a String, decide if we are in a comment by // or /* */
                if (line.charAt(i) == '/') {
                    if (i < line.length() - 1) {
                        if (line.charAt(i + 1) == '*') {
                            inComment = !inComment;
                        }
                        if(line.charAt(i+1) == '/'){ // comment at the end of line, stop processing
                            return true;
                        }
                    }
                }
                if (inComment) {
                    if (line.charAt(i) == '*') {
                        if (i < line.length() - 1) {
                            if (line.charAt(i + 1) == '/') {
                                inComment = !inComment;
                            }
                        }
                    }
                }
            }
            if(!inString&&!inComment&&!inChar) {
                if (line.charAt(i) == '{') {
                    stack.push(true);
                }
                else if (line.charAt(i) == '}') {
                    if (!stack.isEmpty()) {
                        stack.pop();
                    } else {
                        // Handle mismatched closing brace
                        System.err.println("Error: Mismatched closing brace detected.");
                        return false;
                    }
                }
            }
        }
        if(!stack.empty()) // we are still inside a method
            return true;
        else return false;// the method has been processed
    }
    public boolean signatureOfMethod(String line, MethodTree tree, String name) {

        for(VariableTree param : tree.getParameters()){
            if(!line.contains(param.getType()+" "+param.getName())){
                return false;
            }
        }
        if (line.contains(name)) {
            if (tree.getReturnType() != null) {
                if (line.contains(tree.getReturnType().toString()))
                    return true;
                else return false;
            }
            if(line.contains("class")){
                if(line.indexOf("class")>line.indexOf(name))
                    return true;
                return false;
            }
            return true;
        }
        return false;
    }


}
