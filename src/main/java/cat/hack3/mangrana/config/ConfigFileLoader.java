package cat.hack3.mangrana.config;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.yml.FakeYmlLoader;

import java.io.File;
import java.util.EnumMap;

public class ConfigFileLoader {

    private static final String CONFIG_FOLDER = "/config";
    private static final String CONFIG_FILE = "MusicAggregatorConfig.yml";

    public enum ProjectConfiguration {
        ORIGIN_FOLDER_ID,
        DESTINATION_FOLDER_ID,
        DESTINATION_FOLDER_PATH,
        PLEX_TOKEN,
        PLEX_URL,
        PLEX_SECTION_REFRESH_URI,
        PLEX_MUSIC_SECTION_ID
    }

    private final EnumMap<ProjectConfiguration, String> configurationsMap;

    public ConfigFileLoader() throws IncorrectWorkingReferencesException {
        File configFile = new File(System.getProperty("user.dir")
                + CONFIG_FOLDER.concat("/").concat(CONFIG_FILE));

        configurationsMap = FakeYmlLoader.getEnumMapFromFile(configFile, ProjectConfiguration.class);
    }

    public String getConfig(ProjectConfiguration key) {
        return configurationsMap.get(key);
    }

}
