package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.FACTORY_PATTERN.WITH;
// Problem statement  --> let say we have logistic mode road and air, 
// and logistic service uses send method to send parcel accorind to package mode.

interface Logistic{
    public void send();
}
class Road implements  Logistic{
    @Override
    public void send() {
        System.out.println("Sending via road.!!");        
    }
}
class Air implements  Logistic{

    @Override
    public void send() {
        System.out.println("Sending via air!!");
    }
}

class LogisticFactory{
    public static Logistic mode(String mode){
        if(mode.equalsIgnoreCase("AIR")){
            return new Air();
        }else if (mode.equalsIgnoreCase("ROAD")) {
            return new Road();
        }else{
            return null;
        }
    }
}

class LogisticService{
    public void send(String mode){
       Logistic logistic = LogisticFactory.mode(mode);
       logistic.send();
    }    
}

public class Client {
    public static void main(String[] args) {
        LogisticService service = new LogisticService();
        service.send("Road");
    }
}
