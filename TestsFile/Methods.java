package CodeComplexity;

import com.sun.source.tree.MethodTree;

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

    public Methods(Path path,String[] className,String name,MethodTree methodTree){
        this.path = path;
        this.className = className;
        this.name = name;
        this.tree = methodTree;
        cc = new CyclomaticComplexity();
        hv = new HalstedVolume();
        loc = new LinesOfCode();
    }

    public void runCOmplexityCheck(){
        cc.calculate(this.tree);
        loc.calculate(path,name);
        this.position = loc.getPosition();
        hv.calculate(tree);
        maintainabilityIndex = 171-5.2*log(hv.getHalstedVolume()) - 0.23* cc.getValue() -16.2*log(loc.getLines());
    }

    public double getMaintainabilityIndex() {
        return maintainabilityIndex;
    }

    public void print(){
        System.out.println(this.name + "of Class" + this.className + "in File" + this.path.toString() + ": " +this.position);
        System.out.println("with Complexity of: "+ this.maintainabilityIndex);
    }
}
