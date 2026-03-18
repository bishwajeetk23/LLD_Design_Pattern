package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.COMPOSITE_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;

interface CartItem{
    public void displayDetails();
    public double getPrice();
}

class Product implements CartItem{
    private String name;
    private double price;
    public Product(String name, double price){
        this.name = name;
        this.price = price;
    }
    @Override
    public void displayDetails(){
        System.out.println("The name of this product is "+this.name+" and its price is "+ this.price);
    }

    @Override
    public double getPrice(){
        return this.price;
    }
}

class ProductBundle implements CartItem{
    List<CartItem> productList = new ArrayList<>();
    private String bundleName;
    public ProductBundle(String name){
        this.bundleName = name;
    }
    public void addProduct(CartItem product){
        this.productList.add(product);
    }

    @Override
    public void displayDetails(){
        System.out.println("It is a bundle ("+this.bundleName+") of below products:");
        for(CartItem product: this.productList){
            product.displayDetails();
        }
    }

    @Override
    public double getPrice(){
        return this.productList.stream().mapToDouble(((cartItem) ->  cartItem.getPrice())).sum();
    }
}

public class Main {
    public static void main(String[] args) {
        // Individual items
        CartItem book = new Product("Book", 500);
        CartItem headphones = new Product("Headphones", 1500);
        CartItem charger = new Product("Charger", 800);
        CartItem pen = new Product("Pen", 20);
        CartItem notebook = new Product("Notebook", 60);

        // Bundle: Iphone Combo
        ProductBundle iphoneCombo = new ProductBundle("iPhone Combo Pack");
        iphoneCombo.addProduct(headphones);
        iphoneCombo.addProduct(charger);
    
        // Bundle: School Kit
        ProductBundle schoolKit = new ProductBundle("School Kit");
        schoolKit.addProduct(pen);
        schoolKit.addProduct(notebook);

        List<CartItem> cart = new ArrayList<>();
        cart.add(schoolKit);
        cart.add(iphoneCombo);
        cart.add(book);
        double total = cart.stream().mapToDouble(cartItem -> {
            cartItem.displayDetails();
            return cartItem.getPrice();
        }).sum();
        System.out.println("Total price of cart: " + total);
        System.out.println("END of COMPOSITE Design Pattern !");

    }
}
