package nz.ac.auckland.se206.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.JSONArray;
import org.json.JSONObject;

public class reverseGeocoder {

  private String latitude;
  private String longitude;
  private String city;
  private String country;

  public reverseGeocoder() {}

  public String returnCityName() {
    return city;
  }

  public String returnCountryName() {
    return country;
  }

  public void setLatitudeLongitude(double latitude, double longitude) {
    this.latitude = String.valueOf(latitude);
    this.longitude = String.valueOf(longitude);
  }

  public void getLatitudeLongitudeInformation() throws Exception {

    String apiKey = GeoapifyConfig.readConfig().getApiKey();

    if (latitude != null && longitude != null) {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(
                  URI.create(
                      "https://api.geoapify.com/v1/geocode/reverse?lat="
                          + latitude
                          + "&lon="
                          + longitude
                          + "&apiKey="
                          + apiKey))
              .header("Content-Type", "application/json")
              .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

      String jsonOutput = response.body();

      JSONObject object = new JSONObject(jsonOutput);

      JSONArray features = object.getJSONArray("features");

      JSONObject properties = features.getJSONObject(0).getJSONObject("properties");

      city = properties.optString("city", "Unknown city");
      country = properties.optString("country", "Unknown country");
    }
  }
}
