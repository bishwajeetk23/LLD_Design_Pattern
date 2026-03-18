package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.COMPOSITE_PATTERN.WITHOUT;

import java.util.ArrayList;
import java.util.List;

class Product{
    private String name;
    private double price;
    public Product(String name, double price){
        this.name = name;
        this.price = price;
    }
    public void displayDetails(){
        System.out.println("The name of this product is "+this.name+" and its price is "+ this.price);
    }
    public double getPrice(){
        return this.price;
    }
}

class ProductBundle{
    List<Product> productList = new ArrayList<>();
    private String bundleName;
    public ProductBundle(String name){
        this.bundleName = name;
    }
    public void addProduct(Product product){
        this.productList.add(product);
    }
    public void displayDetails(){
        System.out.println("It is a bundle ("+this.bundleName+") of below products:");
        for(Product product: this.productList){
            product.displayDetails();
        }
    }
    public double getPrice(){
        return this.productList.stream().mapToDouble(p -> p.getPrice()).sum();
    }
}


public class client {
    public static void main(String[] args) {
        // Individual items
        Product book = new Product("Book", 500);
        Product headphones = new Product("Headphones", 1500);
        Product charger = new Product("Charger", 800);
        Product pen = new Product("Pen", 20);
        Product notebook = new Product("Notebook", 60);

        // Bundle: Iphone Combo
        ProductBundle iphoneCombo = new ProductBundle("iPhone Combo Pack");
        iphoneCombo.addProduct(headphones);
        iphoneCombo.addProduct(charger);
    
        // Bundle: School Kit
        ProductBundle schoolKit = new ProductBundle("School Kit");
        schoolKit.addProduct(pen);
        schoolKit.addProduct(notebook);

        List<Object>  cart = new ArrayList<>();
        cart.add(book);
        cart.add(iphoneCombo);
        cart.add(schoolKit);

        double total = 0;

        for(Object obj: cart){
            if(obj instanceof Product){
                Product product = (Product)obj;
                product.displayDetails();
                total += product.getPrice();
            }else if(obj instanceof ProductBundle){
                ProductBundle bundle = (ProductBundle) obj;
                bundle.displayDetails();
                total += bundle.getPrice();
            }
        }
        System.out.println("\nTotal Price: ₹" + total);
    }
}