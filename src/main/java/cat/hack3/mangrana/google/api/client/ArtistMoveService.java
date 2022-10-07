package cat.hack3.mangrana.google.api.client;

import cat.hack3.mangrana.bean.Artist;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import com.google.api.services.drive.model.File;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static cat.hack3.mangrana.utils.Output.log;

public class ArtistMoveService {

    GoogleDriveApiGateway driveApiGateway;

    List<File> artistList;
    String letter;

    public ArtistMoveService() throws IOException {
        this.driveApiGateway = new GoogleDriveApiGateway();
    }

    public Artist moveNextArtistFromFolder(String idOriginFolder, String idDestinationFolder) throws IOException {
        File sourceLetterNonEmptyFolder = getFirstNonEmptyLetterFolder(idOriginFolder);
        letter = sourceLetterNonEmptyFolder.getName();
        File destinationLetterFolder = driveApiGateway.getOrCreateFolderByNameAndParentIdInTeamDrive(letter, idDestinationFolder);
        return moveNextArtist(sourceLetterNonEmptyFolder.getId(), destinationLetterFolder.getId());
    }

    private File getFirstNonEmptyLetterFolder(String idOriginFolder) {
        log("going to lookup the first non-empty letter folder");
        List<File> letterFoldersList = driveApiGateway.getChildrenFolderFromFolderById(idOriginFolder);
        File letterFolder = null;
        while (letterFolder==null) {
            File letterFolderCandidate = letterFoldersList.iterator().next();
            artistList = driveApiGateway.getChildrenFolderFromFolderById(letterFolderCandidate.getId());
            boolean isNotEmpty = Objects.nonNull(artistList) && CollectionUtils.isNotEmpty(artistList) && artistList.size()>0;
            if (isNotEmpty) {
                log("found non folder with name = "+letterFolderCandidate.getName());
                log("containing elements: "+artistList.size());
                letterFolder = letterFolderCandidate;
            }
        }
        return letterFolder;
    }

    private Artist moveNextArtist(String originId, String destinationId) throws IOException {
        File nextArtist = artistList.iterator().next();
        driveApiGateway.moveFolder(nextArtist.getId(), originId, destinationId);
        return new Artist(nextArtist.getName(), letter);
    }

}
