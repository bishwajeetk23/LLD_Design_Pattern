package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.PROTOTYPE_PATTERN.WITHOUT;


interface EmailTemplate{
    public void send(String to);
    public void setContent(String content);
}

class WelcomeMailTemplate implements EmailTemplate{

    private String content;
    private String subject;

    public WelcomeMailTemplate(){
        this.content = "Welcome";
        this.subject = "Welcome subject";
    }

    @Override
    public void send(String to) {
        System.out.println("Sending Welcome mail to "+to+ " content: "+ this.content+" subject: "+this.subject);
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }
    
}


public class Main {
    public static void main(String[] args){
        EmailTemplate e1 = new WelcomeMailTemplate();
        EmailTemplate e2 = new WelcomeMailTemplate();
    }
}
