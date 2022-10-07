package cat.hack3.mangrana.music.handler;

import cat.hack3.mangrana.bean.Artist;
import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.ArtistMoveService;
import cat.hack3.mangrana.plex.url.PlexCommandLauncher;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static cat.hack3.mangrana.utils.Output.log;

public class TemporaryFolderAggregator {

    private final ConfigFileLoader configFileLoader;
    private final ArtistMoveService artistMoveService;
    private final PlexCommandLauncher plexCommandLauncher;

    public TemporaryFolderAggregator() throws IOException, IncorrectWorkingReferencesException {
        this.artistMoveService = new ArtistMoveService();
        this.configFileLoader = new ConfigFileLoader();
        this.plexCommandLauncher = new PlexCommandLauncher(configFileLoader);
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        log("Hi, this is the temporary folder aggregator, so here we move artists.");
        new TemporaryFolderAggregator().aggregateNextArtist();
    }

    private void aggregateNextArtist() {
        try {
            Artist movedArtist = moveOneArtist();
            log("successfully moved artist: "+movedArtist.getName());
            waitOneMinute();
            plexCommandLauncher.refreshArtist(movedArtist);
        } catch (Exception e) {
            log("something went wrong with the artist aggregation");
            e.printStackTrace();
        }
    }

    private Artist moveOneArtist() throws IOException {
        return artistMoveService.
                moveNextArtistFromFolder(configFileLoader.getConfig(ORIGIN_FOLDER_ID),
                                         configFileLoader.getConfig(DESTINATION_FOLDER_ID));
    }

    private void waitOneMinute() {
        log("wait 1 minute so the rclone mount realise the change...");
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            log("waiting went wrong");
            Thread.currentThread().interrupt();
        }
    }

}
