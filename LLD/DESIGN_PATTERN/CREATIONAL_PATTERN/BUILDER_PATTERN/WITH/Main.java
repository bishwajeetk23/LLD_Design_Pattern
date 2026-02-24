package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.BUILDER_PATTERN.WITH;


class Employee{
    private final String eName;
    private final String eId;
    private final String eEmail;
    private final String city;
    public static class Builder{
        private String eName;
        private String eId;
        private String eEmail;
        private String city;
        public Builder name(String name){
            this.eName = name;
            return this;
        }
        public Builder email(String name){
            this.eEmail = name;
            return this;
        }
        public Builder city(String name){
            this.city = name;
            return this;
        }
        public Builder id(String name){
            this.eId = name;
            return this;
        }
        public Employee build(){
            return new Employee(this);
        }
    }
    private Employee(Builder builder){
        this.city = builder.city;
        this.eName = builder.eName;
        this.eId = builder.eId;
        this.eEmail = builder.eEmail;
    }

    public void printDetail(){
        System.out.println(eName+" name, "+eId+" id," + eEmail+ " email, "+ city+" city");
    }
}


public class Main {
    public static void main(String[] args) {
        Employee bish = new Employee.Builder()
        .city("Bihar")
        .email("bishwajeetk23@gmail.com")
        .id("AMBDI78754B")
        .name("Kumar Bishwajeet")
        .build();
        bish.printDetail();
    }
}
