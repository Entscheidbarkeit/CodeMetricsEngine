package CodeComplexity;

import com.sun.source.tree.*;
import com.sun.source.util.TreeScanner;

import java.util.HashMap;
import java.util.HashSet;

import static java.lang.Math.log;
/*
operator: arithmetical operator, return, if, (), logical operator, switch, case, assignment operator, modifiers
operands: Literals, Identifiers, Method Invocation
 */
public class HalstedVolume {
    private HashSet<String> oprator; // the length is the distinct number of oprator
    private HashSet<String> oprands;
    private int opratorCount = 0;
    private int oprandCount = 0;

    double halstedVolume;

    public HalstedVolume(){
        this.oprands = new HashSet<>();
        this.oprator = new HashSet<>();
    }
    public double calculate(MethodTree methodTree){
        methodTree.accept(new Scanner(),null);
        this.halstedVolume = (oprandCount+opratorCount)*(log(oprator.size()+oprands.size())/log(2));
        return halstedVolume;
    }

    public double getHalstedVolume() {
        return halstedVolume;
    }

    class Scanner extends TreeScanner<Void,Void>{

        @Override
        public Void scan(Tree tree, Void unused) {
            return super.scan(tree, unused);
        }

        @Override
        public Void visitBinary(BinaryTree node,Void unused){
            oprator.add(node.getKind().toString());
            opratorCount++;
            return super.visitBinary(node,unused);
        }

        @Override
        public Void visitIdentifier(IdentifierTree node, Void unused) {
            oprands.add(node.getName().toString());
            oprandCount++;
            return super.visitIdentifier(node, unused);
        }

        @Override
        public Void visitLiteral(LiteralTree node, Void unused) {
            if(node.getValue()== null) {
                oprands.add("NULL");
                oprandCount++;
            }
            else {
                oprands.add(node.getValue().toString());
                oprandCount++;
            }
            return super.visitLiteral(node, unused);
        }

        @Override
        public Void visitUnary(UnaryTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            return super.visitUnary(node, unused);
        }

        @Override
        public Void visitAssignment(AssignmentTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            return super.visitAssignment(node, unused);
        }

        @Override
        public Void visitModifiers(ModifiersTree node, Void unused) {
            node.getFlags().forEach(mod->{
                oprator.add(mod.name());
                opratorCount++;
            });
            return super.visitModifiers(node, unused);
        }

        @Override
        public Void visitVariable(VariableTree node, Void unused) {
            oprands.add(node.getName().toString());
            oprandCount++;
            return super.visitVariable(node, unused);
        }

        @Override
        public Void visitMethodInvocation(MethodInvocationTree node, Void unused) {
            oprands.add(node.getMethodSelect().toString());
            oprandCount++;
            oprator.add("()");
            opratorCount++;
            return super.visitMethodInvocation(node, unused);
        }

        @Override
        public Void visitIf(IfTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            oprator.add("()");
            opratorCount++;
            return super.visitIf(node, unused);
        }

        @Override
        public Void visitCase(CaseTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            return super.visitCase(node, unused);
        }

        @Override
        public Void visitWhileLoop(WhileLoopTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            oprator.add("()");
            opratorCount++;
            return super.visitWhileLoop(node, unused);
        }

        @Override
        public Void visitForLoop(ForLoopTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            oprator.add("()");
            opratorCount++;
            return super.visitForLoop(node, unused);
        }

        @Override
        public Void visitDoWhileLoop(DoWhileLoopTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            oprator.add("()");
            opratorCount++;
            return super.visitDoWhileLoop(node, unused);
        }

        @Override
        public Void visitArrayAccess(ArrayAccessTree node, Void unused) {
            oprator.add("[]");
            opratorCount++;
            return super.visitArrayAccess(node, unused);
        }

        @Override
        public Void visitTry(TryTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            return super.visitTry(node, unused);
        }

        @Override
        public Void visitCatch(CatchTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            return super.visitCatch(node, unused);
        }

        @Override
        public Void visitReturn(ReturnTree node, Void unused) {
            oprator.add(node.getKind().toString());
            opratorCount++;
            return super.visitReturn(node, unused);
        }
    }
}
