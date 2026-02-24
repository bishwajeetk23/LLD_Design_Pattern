package LLD.DESIGN_PATTERN.BEHAVIOURAL_PATTERN.OBSERVER_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;

interface Subscriber{
    public void update(String content);
}

class EmailSubscriber implements Subscriber{
    private String email;
    public EmailSubscriber(String email){
        this.email = email;
    }

    @Override
    public void update(String content) {
        System.out.println("Email sent to " + this.email + ": New video uploaded - " + content);
    }
}

class MobileSubscriber implements Subscriber{
    private String username;
    public MobileSubscriber(String username){
        this.username = username;
    }
    @Override
    public void update(String content) {
        System.out.println("In-app notification for " + this.username + ": New video - " + content);
    }
}


interface Channel{
    public void subscribe(Subscriber subscribe);
    public void unSubscribe(Subscriber subscribe);
    public void notifySubscribers(String videoTitle);
}

class YoutubeChannel implements Channel{
    private final List<Subscriber> subscriber = new ArrayList<>();
    private String channelName;

    public YoutubeChannel(String channelName){
        this.channelName = channelName;
    }

    @Override
    public void subscribe(Subscriber subscribe) {
        subscriber.add(subscribe);
        System.out.println("You have subscribed to "+this.channelName);
    }

    @Override
    public void unSubscribe(Subscriber subscribe) {
        subscriber.remove(subscribe);
    }

    @Override
    public void notifySubscribers(String videoTitle) {
        for(Subscriber subscriberObj: this.subscriber){
            subscriberObj.update(videoTitle);
        }
    }

    public void uploadVideo(String videoTitle) {
        System.out.println(this.channelName + " uploaded: " + videoTitle + "\n");
        notifySubscribers(videoTitle);
    }
}


public class Main {
    public static void main(String[] args) {
        Subscriber mobile = new MobileSubscriber("bishwajeetk23");
        YoutubeChannel dbk = new YoutubeChannel("DangerBoyKing");
        dbk.subscribe(new EmailSubscriber("bishwa@gmail.com"));
        dbk.uploadVideo("ClashOfClan");
        dbk.subscribe(mobile);
        dbk.uploadVideo("Attack on titans");
        dbk.unSubscribe(mobile);
        dbk.uploadVideo("Java Interview Prep...");
    }
}
