package LLD.DESIGN_PATTERN.BEHAVIOURAL_PATTERN.ITERATOR_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;

class Video{
    private String title;

    public Video(String title){
        System.out.println("New video object is created of title: "+ title);
        this.title = title;
    }

    public String getTitle(){
        System.out.println("Video title is accessed");
        return this.title;
    }
}

interface Iterator{
    public boolean hasNext();
    public Video next();
}

class YoutubePlayListIterator implements Iterator{

    private List<Video> videos;
    private int it;
    public YoutubePlayListIterator(List<Video> videos){
        System.out.println("Youtube Playlist iterator initilize");
        this.videos = videos;
        it = 0;
    }

    @Override
    public boolean hasNext() {
        System.out.println("Checking from iterator that next element is present or not");
        if(this.it<0 || (this.it >= this.videos.size())){
            return false;
        }
        return  true;
    }

    @Override
    public Video next() {
        System.out.println("Getting next element using iterator");
        Video video = this.videos.get(this.it);
        this.it = this.it + 1;
        return video;
    }
    
}
interface Iterable{
    public Iterator createYoutubeIterator();
}
class YoutubePlayList implements Iterable{

    private List<Video> videos;

    public YoutubePlayList(){
        System.out.println("Playlist created");
        this.videos = new ArrayList<>();
    }

    public void addVideo(Video video){
        System.out.println("Adding new Video in playlist");
        this.videos.add(video);
    }

    public List<Video> getVideos(){
        System.out.println("Getting list of videos");
        return this.videos;
    }

    @Override
    public Iterator createYoutubeIterator() {
        return new YoutubePlayListIterator(this.videos);
    }
    
}


public class Main {
    public static void main(String[] args) {
        YoutubePlayList playlist = new YoutubePlayList();
        playlist.addVideo(new Video("Video 1"));
        playlist.addVideo(new Video("Video 2"));
        Iterator iterator = playlist.createYoutubeIterator();
        while (iterator.hasNext()) {
            Video video =  iterator.next();
            System.out.println(video.getTitle() + " <> " + video.hashCode());
        }
        System.out.println("Ending the client code!!");
    }
}

// Iterator pattern decouple traversal and storing and 
// also we need to use stratagy pattern to make it more extensible and thread safe.
// we dont use normal list for thread safty we use CopyOnWriteArrayList