package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.FLYWEIGHT_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Tree{
    private int x;
    private int y;
    private TreeType treeType;
    public Tree(int x, int y, TreeType treeType){
        this.x = x;
        this.y = y;
        this.treeType = treeType;
    }
    public void draw(){
       treeType.draw(x, y);
    }

    public static long getMemoryUsage() {
        return 8 + Integer.BYTES * 2;  // approximate pointer + ints
    }
}

class TreeType{
    String name;
    String type;
    String color;
    public TreeType(String name, String color, String type){
        this.name = name;
        this.type = type;
        this.color = color;
    }

    public void draw(int x, int y){
        System.out.println(name+ " Tree is being drawn with color: "+color+" of type: "+type+" and placed at x: "+x+" y: "+y);
    }
     public static long getMemoryUsage() {
        return 40 * 3;                         
    }
}
class TreeTypefactory{
    private static Map<String,TreeType> cache = new ConcurrentHashMap<>();

    public static TreeType getTreeType(String name, String color, String type){
        String key = name+"*"+color+"*"+type;
        cache.computeIfAbsent(key, (k)-> new TreeType(name, color, type));
        return cache.get(key);
    }
    public static long getTotalFlyweightMemory() {
        return cache.size() * TreeType.getMemoryUsage();
    }
}
class Forest{
    private List<Tree> trees;
    public Forest(){
        this.trees = new ArrayList<>();
    }
    public void plantTree(int x, int y, String name, String color, String type){
        trees.add(new Tree(x, y, TreeTypefactory.getTreeType(name, color, type)));
    }
    public void draw(){
        int counter = 0;
        for(Tree tree: trees){
            if(counter<10)tree.draw();
            counter++;
        }
    }
    public long calculateMemoryUsage() {
        long contextMemory = trees.size() * Tree.getMemoryUsage();
        long flyweightMemory = TreeTypefactory.getTotalFlyweightMemory();
        return contextMemory + flyweightMemory;
    }
}

public class Main {

    public static void main(String[] args) {
        Forest forest = new Forest();
        for(int i=0;i<1000000;i++){
            forest.plantTree(100+i, 200+i*10, "mango", "red", "oak");
        }
        long totalMemory = forest.calculateMemoryUsage();
        System.out.println("Memory in MB: " + (totalMemory / (1024.0 * 1024.0)) + " MB");
        System.out.println("Planted 1 million trees.");
    }
}
