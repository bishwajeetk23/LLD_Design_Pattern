package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.BRIDGE_PATTERN.WIHTOUT;

interface PlayQuality{
    public void play(String title);
}

class MobileHDPlayer implements PlayQuality{
    @Override
    public void play(String title) {
        System.out.println("Mobile Player: playing "+title+ " in HD");
    }
}

class WebHDPlayer implements PlayQuality{

    @Override
    public void play(String title) {
        System.out.println("Web Player: playing " + title + " Playing in HD");
    }
}

class SmartTVUltraHDPlayer implements PlayQuality {
    @Override
    public void play(String title) {
        System.out.println("Smart TV: Playing " + title + " in ultra HD");
    }
}

class Web4KPlayer implements PlayQuality {
    @Override
    public void play(String title) {
        // Web player plays in 4K
        System.out.println("Web Player: Playing " + title + " in 4K");
    }
}

public class Mian {
    public static void main(String[] args) {
        // This design pattern deals with the problem of class explosion and also decouple abstraction and implementation i.e, High level part with low level part.
        // For example here, Player type can be considered as high level and its quality can be treated as low level part and many combination can happen which will lead to explosion of classes.
        
        PlayQuality player = new WebHDPlayer();
        player.play("Interstellar");
    }
}
