package nz.ac.auckland.se206.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoapifyConfig {

  private String apiKey;

  private static GeoapifyConfig instance;

  private GeoapifyConfig() {}

  public String getApiKey() {
    return apiKey;
  }

  public static synchronized GeoapifyConfig readConfig() {
    if (instance == null) {
      File file = new File("geoapify.config");
      try {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        instance = mapper.readValue(file, GeoapifyConfig.class);
      } catch (Exception e) {
        throw new RuntimeException(
            "Unable to read "
                + file.getAbsolutePath()
                + ". Please ensure geoapify.config exists and is valid.",
            e);
      }
    }
    return instance;
  }
}
