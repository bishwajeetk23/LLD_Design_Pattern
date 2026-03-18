package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.PROXY_PATTERN.WITHOUT;

class RealVideoDownloader{
    public String downloadVideo(String videoUrl){
        System.out.println("Downloading video from URL: " + videoUrl);
        return "Video content from " + videoUrl;
    }
}

public class Main {
    public static void main(String[] args) {
        RealVideoDownloader video1 = new RealVideoDownloader();
        video1.downloadVideo("videoUrl for video1");
        RealVideoDownloader video2 = new RealVideoDownloader();
        video2.downloadVideo("videoUrl for video1");
    }
}
