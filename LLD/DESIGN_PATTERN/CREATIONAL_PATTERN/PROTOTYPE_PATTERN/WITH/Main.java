import java.util.HashMap;
import java.util.Map;

enum MailType{
    WELCOME
}

interface EmailTemplate {
    EmailTemplate copy();
    public void send(String to);
    public void setContent(String content);
}

class WelcomeMailTemplate implements EmailTemplate {
   
    private String content;
    private String subject;

    public WelcomeMailTemplate(){
        this.content = "Welcome mail content";
        this.subject = "Welcom mail subject";
    }

    private WelcomeMailTemplate(WelcomeMailTemplate obj){
        this.content = obj.content;
        this.subject = obj.subject;
    }

    @Override    
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void send(String to) {
        System.out.println("Mail send to "+to+" with content: " + content);
    }

    @Override
    public WelcomeMailTemplate copy(){
        return new WelcomeMailTemplate(this);
    }
}

class EmailRegistory{
    private static Map<MailType,EmailTemplate> templatefactory = new HashMap<>();
    static{
        templatefactory = Map.of(
            MailType.WELCOME, new WelcomeMailTemplate()
        );
    }
    public static EmailTemplate copyTemplate(MailType mailType){
        EmailTemplate template = templatefactory.get(mailType);
        if(template==null){
            throw new IllegalArgumentException(mailType+" this mail type is not supported yet");
        }
        return template.copy();
    }
}

class Main{
    public static void main(String[] args) {
        EmailTemplate welcome = EmailRegistory.copyTemplate(MailType.WELCOME);
        EmailTemplate welcome2 = EmailRegistory.copyTemplate(MailType.WELCOME);
        welcome.setContent("Version 1");
        welcome2.setContent("Version 2");
        welcome2.send("Rahul");
        welcome.send("bishwa");

    }
}