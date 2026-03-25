package LLD.DESIGN_PATTERN.BEHAVIOURAL_PATTERN.ITERATOR_PATTERN.WITHOUT;

import java.util.ArrayList;
import java.util.List;


class Video{
    private String title;

    public Video(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
}

class YoutubePlayList{

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
}




public class Main {
    public static void main(String[] args) {
        YoutubePlayList playlist = new YoutubePlayList();
        playlist.addVideo(new Video("Video 1"));
        playlist.addVideo(new Video("Video 2"));
        
        for(Video video : playlist.getVideos()){
            System.out.println(video.getTitle() + " <>  "+ video.hashCode());
        }
    }
}
