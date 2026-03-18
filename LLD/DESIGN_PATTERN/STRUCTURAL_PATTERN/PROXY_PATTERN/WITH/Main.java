package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.PROXY_PATTERN.WITH;

import java.util.HashMap;
import java.util.Map;

interface VideoDownloader{
    public String downloadVideo(String videoUrl);
}

class RealVideoDownloader implements VideoDownloader{

    @Override
    public String downloadVideo(String videoUrl) {
        System.out.println("Download video from URL: "+videoUrl);
        return "Video content from: "+ videoUrl;
    }
    
}
class CachedVideoDownloader implements VideoDownloader{
    private RealVideoDownloader realVideoDownloader;
    private Map<String,String> cache;
    public CachedVideoDownloader(){
        realVideoDownloader = new RealVideoDownloader();
        cache = new HashMap<>();
    }
    @Override
    public String downloadVideo(String videoUrl) {
        if(this.cache.containsKey(videoUrl)){
            System.out.println("Returning cached video for: "+videoUrl);
            return this.cache.get(videoUrl);
        }
        System.out.println("Cache miss. Downloading..");
        String video = this.realVideoDownloader.downloadVideo(videoUrl);
        this.cache.put(videoUrl, video);
        return video;
    }
}

public class Main {
    public static void main(String[] args) {
        VideoDownloader cacheVideoDownloader = new CachedVideoDownloader();
        System.out.println("User 1 tries to download the video.");
        cacheVideoDownloader.downloadVideo("https://video.com/proxy-pattern");

        System.out.println();

        System.out.println("User 2 tries to download the same video again.");
        cacheVideoDownloader.downloadVideo("https://video.com/proxy-pattern");
    }
}
