package cat.hack3.mangrana.music.handler;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.ArtistCopyService;
import cat.hack3.mangrana.plex.url.PlexCommandLauncher;

import java.io.IOException;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.DESTINATION_FOLDER_ID;
import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.ORIGIN_FOLDER_ID;

public class TemporaryFolderAggregator {

    private final ConfigFileLoader configFileLoader;
    private final ArtistCopyService artistCopyService;

    private final PlexCommandLauncher plexCommandLauncher;

    public TemporaryFolderAggregator() throws IncorrectWorkingReferencesException, IOException {
        this.artistCopyService = new ArtistCopyService();
        this.configFileLoader = new ConfigFileLoader();
        this.plexCommandLauncher = new PlexCommandLauncher(configFileLoader);
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        new TemporaryFolderAggregator().aggregateNextArtist();
    }

    private void aggregateNextArtist() throws IOException {
        String artistName = moveOneArtist();
        plexCommandLauncher.refreshArtist(artistName);
    }

    private String moveOneArtist() throws IOException {
        return artistCopyService.
                moveNextArtistFromFolder(configFileLoader.getConfig(ORIGIN_FOLDER_ID),
                                         configFileLoader.getConfig(DESTINATION_FOLDER_ID));
    }

}
