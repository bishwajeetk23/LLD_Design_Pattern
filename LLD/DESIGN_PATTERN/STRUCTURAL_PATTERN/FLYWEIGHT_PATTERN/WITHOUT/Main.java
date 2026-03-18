package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.FLYWEIGHT_PATTERN.WITHOUT;

import java.util.ArrayList;
import java.util.List;

// Without flyweight, every tree stores both intrinsic and extrinsic state.
class Tree {
    private static final long STRING_MEMORY_USAGE = 40;

    private int x;
    private int y;
    private String name;
    private String color;
    private String type;

    public Tree(int x, int y, String name, String color, String type) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.color = color;
        this.type = type;
    }

    public void draw() {
        System.out.println(
                name + " Tree is being drawn with color: " + color
                        + " of type: " + type
                        + " and placed at x: " + x + " y: " + y
        );
    }

    public static long getMemoryUsage() {
        return 8 + Integer.BYTES * 2 + (STRING_MEMORY_USAGE * 3);
    }
}

class Forest {
    private final List<Tree> trees;

    public Forest() {
        this.trees = new ArrayList<>();
    }

    public void plantTree(int x, int y, String name, String color, String type) {
        trees.add(new Tree(x, y, name, color, type));
    }

    public void draw() {
        int counter = 0;
        for (Tree tree : trees) {
            if (counter < 10) {
                tree.draw();
            }
            counter++;
        }
    }

    public long calculateMemoryUsage() {
        return trees.size() * Tree.getMemoryUsage();
    }
}

public class Main {

    public static void main(String[] args) {
        Forest forest = new Forest();

        for (int i = 0; i < 1000000; i++) {
            forest.plantTree(100 + i, 200 + i * 10, "mango", "red", "oak");
        }

        long totalMemory = forest.calculateMemoryUsage();
        System.out.println("Memory in MB: " + (totalMemory / (1024.0 * 1024.0)) + " MB");
        System.out.println("Planted 1 million trees.");
    }
}
