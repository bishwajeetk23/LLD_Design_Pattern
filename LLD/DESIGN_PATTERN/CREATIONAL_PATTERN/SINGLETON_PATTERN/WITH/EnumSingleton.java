package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.SINGLETON_PATTERN.WITH;

public enum EnumSingleton {
    INSTANCE;
    public static void getInstance(EnumSingleton enumSingleton){
        System.out.println(enumSingleton + "  <>  "  + Thread.currentThread().getName());
    }
}
