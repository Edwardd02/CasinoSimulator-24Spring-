import org.json.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TempleAlertModel {

    private MainMenuModel menuModel;
    private TempleAlertView templeAlertView;
    private String title;
    private String selftext;
    private String author;
    private String formattedESTDateTime;

    public TempleAlertModel(MainMenuModel menu) throws Exception {
        this.menuModel = menu;
        this.templeAlertView = new TempleAlertView(this); // This line sets up the view for temple alerts
    }


    public HttpResponse<String> startGame() throws Exception{
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.reddit.com/r/Temple/new.json?limit=1"))
                .header("User-Agent", "YourApp/0.1 by YourRedditUsername")
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
        // System.out.println(httpResponse.body());
        return httpResponse;
    }

    public void fetchLatestTemplePost() throws Exception {
        HttpResponse<String> httpResponse = startGame(); // Get the latest post JSON response
        JSONObject responseJson = new JSONObject(httpResponse.body());
        JSONObject postJson = responseJson.getJSONObject("data").getJSONArray("children").getJSONObject(0).getJSONObject("data");

        // Extract the necessary details
        title = postJson.getString("title");
        selftext = postJson.optString("selftext", ""); // Use optString to avoid JSONException in case of missing key
        author = postJson.getString("author");
        long createdUtc = postJson.getLong("created_utc");

        // Convert timestamp to EST and format it
        Instant instant = Instant.ofEpochSecond(createdUtc);
        ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
        ZonedDateTime estDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        formattedESTDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(estDateTime);

        // Print the post details in desired format
        /*
        System.out.println("\n\n\n\nTitle: " + title);
        System.out.println("Selftext: " + selftext);
        System.out.println("Author: " + author);
        System.out.println("UTC Timestamp (seconds): " + createdUtc);
        System.out.println("Converted to EST: " + formattedESTDateTime);
         */
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return selftext;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return formattedESTDateTime;
    }

    public void exit() {
        templeAlertView.dispose();
        menuModel.setVisible();
    }
}
