package CodeComplexity;

import com.sun.source.tree.MethodTree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Math.log;

public class Methods {
    Path path;

    String className;
    String name;
    int position;

    double maintainabilityIndex;
    CyclomaticComplexity cc;
    HalstedVolume hv;
    LinesOfCode loc;

    MethodTree tree;

    public Methods(Path path,String className,String name,MethodTree methodTree){
        this.path = path;
        this.className = className;
        this.name = name;
        this.tree = methodTree;
        cc = new CyclomaticComplexity();
        hv = new HalstedVolume();
        loc = new LinesOfCode();
    }

    public void runComplexityCheck(){
        nameSanitizer();
        cc.calculate(this.tree);
        loc.calculate(path,tree,name);
        this.position = loc.getPosition();
        hv.calculate(tree);
        maintainabilityIndex = (171-5.2*log(hv.getHalstedVolume()) - 0.23* cc.getValue() -16.2*log(loc.getLines()))/171*100;
    }

    public double getMaintainabilityIndex() {
        return maintainabilityIndex;
    }

    public void print(int mode){
        System.out.println("["+ this.name + "]  of class \"" + this.className + "\" in File \"" + this.path.toString() + "\" : " +this.position);
        // mode 1 print Complexity Info
        if(mode == 1) {
            System.out.println("with Complexity of: " + this.maintainabilityIndex);
        }
        // mode 2 print Code Style Info
        if(mode == 2){
            System.out.println("Name invalid!");
        }
    }

    public void debugPrint(PrintStream out){
        System.setOut(out);
        System.out.println("["+ this.name + "]  of class \"" + this.className + "\" in File \"" + this.path.toString() + "\" : " +this.position);
        System.out.println("with Complexity of: "+ this.maintainabilityIndex);
        System.out.println("with CC of: "+this.cc.getValue());
        System.out.println("with HV of: " + this.hv.getHalstedVolume());
        System.out.println("with LoC of: " + this.loc.getLines()+ " with position of " + this.loc.getPosition());
        System.out.println();
    }
    public void nameSanitizer(){
        if(this.name.equals("<init>")){
            this.name = this.className;
        }
    }

    public String getName() {
        return name;
    }
}
