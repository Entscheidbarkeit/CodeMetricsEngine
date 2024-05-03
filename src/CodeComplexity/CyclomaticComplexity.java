package CodeComplexity;

import com.sun.source.tree.*;
import com.sun.source.util.TreeScanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* by this Metrics, following points in program are considered:
    &&,||,Catch,While,For,If,Case
 */

public class CyclomaticComplexity {
    private int value = 1;

    public int getValue() {
        return value;
    }

    public int calculate(MethodTree methodTree){
        Scanner scanner = new Scanner();
        methodTree.accept(scanner,null);
        return value;
    }

    class Scanner extends TreeScanner<Void,Void>{
        public Void visitBinary(BinaryTree node,Void unused){
            if(node.getKind() == Tree.Kind.AND)
                value++;
            else if(node.getKind() == Tree.Kind.OR)
                value++;
            return super.visitBinary(node,unused);
        }

        @Override
        public Void visitCatch(CatchTree node, Void unused) {
            value++;
            return super.visitCatch(node, unused);
        }

        @Override
        public Void visitDoWhileLoop(DoWhileLoopTree node, Void unused) {
            value++;
            return super.visitDoWhileLoop(node, unused);
        }

        @Override
        public Void visitForLoop(ForLoopTree node, Void unused) {
            value++;
            return super.visitForLoop(node, unused);
        }

        @Override
        public Void visitWhileLoop(WhileLoopTree node, Void unused) {
            value++;
            return super.visitWhileLoop(node, unused);
        }

        @Override
        public Void visitCase(CaseTree node, Void unused) {
            value++;
            return super.visitCase(node, unused);
        }

        @Override
        public Void visitIf(IfTree node, Void unused) {
            value++;
            return super.visitIf(node, unused);
        }

        @Override
        public Void visitEnhancedForLoop(EnhancedForLoopTree node, Void unused) {
            value++;
            return super.visitEnhancedForLoop(node, unused);
        }
    }

}
