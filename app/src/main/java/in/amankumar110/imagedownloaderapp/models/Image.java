package in.amankumar110.imagedownloaderapp.models;

public class Image {
    private Urls urls;

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public static class Urls {
        private String small;
        private String smallS3;
        private String thumb;
        private String raw;
        private String regular;
        private String full;

        public String getSmall() { return small; }
        public void setSmall(String value) { this.small = value; }

        public String getSmallS3() { return smallS3; }
        public void setSmallS3(String value) { this.smallS3 = value; }

        public String getThumb() { return thumb; }
        public void setThumb(String value) { this.thumb = value; }

        public String getRaw() { return raw; }
        public void setRaw(String value) { this.raw = value; }

        public String getRegular() { return regular; }
        public void setRegular(String value) { this.regular = value; }

        public String getFull() { return full; }
        public void setFull(String value) { this.full = value; }
    }


}


