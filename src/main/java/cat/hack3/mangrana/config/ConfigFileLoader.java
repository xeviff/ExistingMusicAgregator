package cat.hack3.mangrana.config;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static cat.hack3.mangrana.utils.Output.log;

public class ConfigFileLoader {

    private static final String CONFIG_FOLDER = "/config";
    private static final String CONFIG_FILE = "MusicAggregatorConfig.yml";

    public enum ProjectConfiguration {
        MUSIC_TEAM_DRIVE_ID,
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
        log("Loading values from the config file...");
        try {
            YamlMapping config = Yaml.createYamlInput(
                    new File(getConfigFolder().concat("/").concat(CONFIG_FILE)))
                    .readYamlMapping();

            configurationsMap = new EnumMap<>(ProjectConfiguration.class);
            Arrays.stream(ProjectConfiguration.values())
                    .forEach(projectConfiguration -> addConfigToMap(config, projectConfiguration));

        } catch (IOException e) {
            throw new IncorrectWorkingReferencesException("couldn't find the config file :(");
        }
    }

    private void addConfigToMap (YamlMapping config, ProjectConfiguration configKey) {
        try {
            String configValue = Optional.ofNullable(
                    config.string(configKey.name().toLowerCase()))
                    .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the configuration: "+configKey.name()) );
            configurationsMap.put(configKey, configValue);
        } catch (IncorrectWorkingReferencesException e) {
            e.printStackTrace();
        }
    }

    public String getConfig(ProjectConfiguration key) {
        return configurationsMap.get(key);
    }

    private String getConfigFolder(){
        return System.getProperty("user.dir") + CONFIG_FOLDER;
    }

}
