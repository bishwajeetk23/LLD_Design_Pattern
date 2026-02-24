package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.DECORATOR_PATTERN.WITH;

interface Pizza{
    public double getCost();
    public String description();
}

class MargaritaPizza implements Pizza{

    @Override
    public double getCost() {
        return 100;
    }

    @Override
    public String description() {
        return "Concrete class which implements directly Pizza interface...";
    }
    
}

abstract class PizzaDecorator implements Pizza{
    protected Pizza pizza;

    public PizzaDecorator(Pizza pizza) {
        this.pizza = pizza;
    }
}

class Olive extends PizzaDecorator{
    public Olive(Pizza pizza){
        super(pizza);
    }
    @Override
    public double getCost() {
        return pizza.getCost() + 40;
    }

    @Override
    public String description() {
        return pizza.description() +  " Adding olive to pizza... ";
    }
    
}

class CheezePizza extends PizzaDecorator{
    public CheezePizza(Pizza pizza){
        super(pizza);
    }
    @Override
    public double getCost() {
        return this.pizza.getCost() + 60;
    }

    @Override
    public String description() {
        return pizza.description()+" Adding extra cheese to pizza... ";
    }
    
}


public class Main {
    public static void main(String[] args) {
        Pizza extraCheesePizza = new CheezePizza(new MargaritaPizza());
        System.out.println(extraCheesePizza.getCost());
        System.out.println(extraCheesePizza.description());
        Pizza olivecheeesePizza = new Olive(extraCheesePizza);
        System.out.println(olivecheeesePizza.getCost());
        System.out.println(olivecheeesePizza.description());
    }
}
