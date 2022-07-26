package cat.hack3.mangrana.config;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static cat.hack3.mangrana.utils.Output.log;

public class ConfigFileLoader {

    private static final String CONFIG_FOLDER = "/config";
    private static final String CONFIG_FILE = "MusicAgregatorConfig.yml";
    private static final String MUSIC_TD_KEY = "music_team_drive_id";
    private static final String ORIGIN_FOLDER_KEY = "origin_folder_id";
    private static final String DESTINATION_FOLDER_KEY = "destination_folder_id";

    private final String musicTDid;
    private final String originFolderId;
    private final String destinationFolderId;

    public ConfigFileLoader() throws IncorrectWorkingReferencesException {
        log("Loading values from the config file...");
        try {
            YamlMapping config = Yaml.createYamlInput(
                    new File(getConfigFolder().concat("/").concat(CONFIG_FILE)))
                    .readYamlMapping();

            musicTDid = Optional.ofNullable(
                     config.string(MUSIC_TD_KEY))
                     .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the musicTDid :(") );
            originFolderId = Optional.ofNullable(
                     config.string(ORIGIN_FOLDER_KEY))
                     .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the origin folder id :(") );
            destinationFolderId = Optional.ofNullable(
                            config.string(DESTINATION_FOLDER_KEY))
                    .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the destination folder id :(") );
        } catch (IOException e) {
            throw new IncorrectWorkingReferencesException("couldn't find the config file :(");
        }
    }

    private String getConfigFolder(){
        return CONFIG_FOLDER;
    }

    public String getMusicTDid() {
        return musicTDid;
    }

    public String getOriginFolderId() {
        return originFolderId;
    }

    public String getDestinationFolderId() {
        return destinationFolderId;
    }
}
