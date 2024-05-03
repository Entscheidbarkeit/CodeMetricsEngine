package CodeComplexity;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;
import java.util.regex.Pattern;

public class LinesOfCode { // we took the assumption for simplicity that all methods are declared in a line
    private int lines = 0;
    private int position = 0;

    private int totalLines = 0;
    private Stack<Boolean> stack;
    private MethodTree mt;
    private Path pt;
    public LinesOfCode(){
        stack = new Stack<>();
    }

    public int getPosition() {
        return position;
    }

    public int getLines() {
        return lines;
    }

    //for a method, we read all lines from its path and count the lines
    // to detect start of a method we use the parameter list, name, return type from its ast
    // to detect end we use a stack to count pairs of {}. Only when the stack is empty, can we decide the end of the method is reached
    public int calculate(Path path, MethodTree methodTree, String name) {
        pt = path;
        mt = methodTree;
        try {
            Files.lines(path, StandardCharsets.UTF_8).forEach(line->{
                if(signatureOfMethod(line,methodTree,name)){  // the signature found
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
        } catch (Exception e) {
            System.err.println("Error processing file: " + path.toString());
            System.err.println("Please check the file encoding.");
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
                        if(stack.empty())
                            return true;
                    } else {
                        // Handle mismatched closing brace
                        System.err.println("Error: Mismatched closing brace detected.");
                        System.err.println("in File: " + pt.toString()+" with name: " + mt.getName());
                        return false;
                    }
                }
            }
        }
        if(!stack.empty()) // we are still inside a method
            return true;
        else return false;// the method has been processed
    }
    public boolean signatureOfMethod(String line, MethodTree tree, String name) { // decide if a line contains the signature of a method
        if(inComment)
            return false;
        if (line.contains(name)) {
            if(line.contains("//")){ // the name must not be in a comment
                if(line.indexOf(name)>line.indexOf("//"))
                    return false;
            }
            for(VariableTree param : tree.getParameters()){ // if line contains all the parameter
                if(!line.contains(param.getType()+" "+param.getName())){
                    return false;
                }
            }
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
