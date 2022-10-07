package cat.hack3.mangrana.bean;

public class Artist {
    String name;
    String letterFolder;

    public Artist(String name, String letterFolder) {
        this.name = name;
        this.letterFolder = letterFolder;
    }

    public String getName() {
        return name;
    }

    public String getLetterFolder() {
        return letterFolder;
    }
}
