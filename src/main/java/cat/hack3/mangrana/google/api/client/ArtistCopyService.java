package cat.hack3.mangrana.google.api.client;

import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import static cat.hack3.mangrana.utils.Output.log;

public class ArtistCopyService {

    GoogleDriveApiGateway driveApiGateway;

    public ArtistCopyService() throws IOException {
        this.driveApiGateway = new GoogleDriveApiGateway();
    }

    public String moveNextArtistFromFolder(String idOriginFolder, String idDestinationFolder) throws IOException {
        log(MessageFormat.format("going to move artist folder from {0} to {1}", idOriginFolder, idDestinationFolder));
        List<File> artists = driveApiGateway.getFirstChildFromFolderById(idOriginFolder);
        return moveNextArtist(artists, idOriginFolder, idDestinationFolder);
    }

    private String moveNextArtist(List<File> artists, String originId, String destinationId) throws IOException {
        File file = artists.get(0);
        driveApiGateway.moveFolder(file.getId(), originId, destinationId);
        return file.getName();
    }

}
