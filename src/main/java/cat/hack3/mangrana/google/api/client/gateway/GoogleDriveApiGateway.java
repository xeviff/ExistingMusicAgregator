package cat.hack3.mangrana.google.api.client.gateway;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.commons.collections4.CollectionUtils;
import org.o7planning.googledrive.example.GoogleDriveUtils;

import java.io.IOException;
import java.util.*;

import static cat.hack3.mangrana.utils.Output.log;

public class GoogleDriveApiGateway {

    Drive service;


    public enum GoogleElementType {FOLDER, VIDEO}

    public GoogleDriveApiGateway() throws IOException {
        service = GoogleDriveUtils.getDriveService();
    }

    public List<File> getSomeChildrenFoldersById(String id)  {
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
                    .setPageSize(3)
                    .setOrderBy("name")
                    .execute();

            fullFileList.addAll(fileList.getFiles());
        } catch (IOException e) {
            log("ERROR during api call");
            e.printStackTrace();
        }

        return fullFileList;
    }
    public void moveFolder(String elementId, String currentParentFolderId, String destinationFolderId) throws IOException {
        service.files()
                .update(elementId, null)
                .setAddParents(destinationFolderId)
                .setRemoveParents(currentParentFolderId)
                .setSupportsTeamDrives(true)
                .execute();
    }

    public void copyFile(String fileId, String destinationFolderId) throws IOException {
        File newFileReference = new File();
        newFileReference.setParents(Collections.singletonList(destinationFolderId));
        service.files()
                .copy(fileId, newFileReference)
                .setSupportsTeamDrives(true)
                .execute();
    }

    public String getParentFolderIdFromFile (String elementId) throws IOException {
        File file = service.files()
                .get(elementId)
                .setSupportsTeamDrives(true)
                .setFields("parents")
                .execute();
        if (CollectionUtils.isNotEmpty(file.getParents()))
            return file.getParents().get(0);
        else throw new NoSuchElementException("parents not found");
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

    private String getTypeFilterQuery(GoogleElementType type) {
        switch (type) {
            case VIDEO:
                return " and mimeType contains 'video'";
            case FOLDER:
                return " and mimeType = 'application/vnd.google-apps.folder'";
            default:
                return "";
        }
    }

}
