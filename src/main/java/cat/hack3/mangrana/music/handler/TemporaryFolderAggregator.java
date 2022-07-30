package cat.hack3.mangrana.music.handler;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.ArtistCopyService;

import java.io.IOException;

public class TemporaryFolderAggregator {

    ConfigFileLoader configFileLoader;
    ArtistCopyService artistCopyService;

    public TemporaryFolderAggregator() throws IncorrectWorkingReferencesException {
        this.configFileLoader = new ConfigFileLoader();
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        new TemporaryFolderAggregator().moveOneArtist();
    }

    private void moveOneArtist() throws IOException {
        artistCopyService.
                moveNextArtistFromFolder(configFileLoader.getOriginFolderId(),
                                         configFileLoader.getDestinationFolderId());
    }

}
