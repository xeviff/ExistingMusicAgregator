package cat.hack3.mangrana.google.api.client.gateway;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.o7planning.googledrive.example.GoogleDriveUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cat.hack3.mangrana.utils.Output.log;

public class GoogleDriveApiGateway {

    Drive service;
    public GoogleDriveApiGateway() throws IOException {
        service = GoogleDriveUtils.getDriveService();
    }

    public List<File> getChildrenFolderFromFolderById(String id)  {
        List<File> fullFileList = new ArrayList<>();

        String query =
                "trashed=false and "+
                        "mimeType = 'application/vnd.google-apps.folder' and "+
                        "'"+id+"' in parents";

        try {
            FileList fileList = service.files()
                    .list()
                    .setQ(query)
                    .setIncludeItemsFromAllDrives(true)
                    .setSupportsTeamDrives(true)
                    .setFields("nextPageToken, files(id, name)")
                    .setPageSize(100)
                    .setOrderBy("name")
                    .execute();

            fullFileList.addAll(fileList.getFiles());
        } catch (IOException e) {
            log("ERROR during api call");
            e.printStackTrace();
        }

        return fullFileList;
    }

    public File getOrCreateFolderByNameAndParentIdInTeamDrive(String name, String idDestinationFolder) throws IOException {
        Optional<File> childElement = getChildrenFolderFromFolderById(idDestinationFolder).stream()
                .filter(file -> name.equals(file.getName()))
                .findFirst();

        if (childElement.isPresent()) {
            return childElement.get();
        } else {
            return createFolder(name, idDestinationFolder);
        }
    }


    public void moveFolder(String elementId, String currentParentFolderId, String destinationFolderId) throws IOException {
        service.files()
                .update(elementId, null)
                .setAddParents(destinationFolderId)
                .setRemoveParents(currentParentFolderId)
                .setSupportsTeamDrives(true)
                .execute();
    }

    public File createFolder(String name, String parentFolderId) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(name);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setParents(Collections.singletonList(parentFolderId));
        return service
                .files()
                .create(fileMetadata)
                .setSupportsTeamDrives(true)
                .execute();
    }

}
