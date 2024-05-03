package CodeComplexity;

import com.sun.source.tree.MethodTree;

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

    public int GetPosition() {
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
    public boolean lineVAlidation(Stack<Boolean> stack, String line){ // run this method to check the pair of {},which helps to determine the body of methods

        int indexOfComments = line.length();
        if(line.contains("//")){
            indexOfComments = line.indexOf("//");
        }
        for(int i = 0; i< indexOfComments; i++) {
            if(line.charAt(i)== '/'){
                if(i<line.length()-1){
                    if(line.charAt(i+1)=='*'){
                        inComment = !inComment;
                    }
                }
            }
            if(line.charAt(i)== '*'){
                if(i<line.length()-1){
                    if(line.charAt(i+1)=='/'){
                        inComment = !inComment;
                    }
                }
            }
            if(line.charAt(i)=='"')
                inString = !inString;
            if(!inString&&!inComment) {
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
    public boolean signatureOfMEthod(String line, MethodTree tree, String name) {
        if (line.contains("(" + tree.getParameters().toString() + ")") && line.contains(name)) {
            if (tree.getReturnType() != null) {
                if (line.contains(tree.getReturnType().toString()))
                    return true;
                else return false;
            }
            return true;
        }
        return false;
    }


}
