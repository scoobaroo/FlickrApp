//
// File: GetFlickr.java
//
// Instructions:
// 1) replace ****YOUR API KEY HERE**** with your flickr API key
// 2) compile and run
// 3) returns response string with flickr images with tag SFSUCS413F16Test
// 4) parse into JSON object with gson

package GPCode;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import com.google.gson.*;
import com.google.gson.Gson;

public class GetFlickr {
    GetFlickr() {
    }

    public static void main(String [] args) throws Exception {
	// tag to search for
	String key = "SFSUCS413F16Test";
	String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
	// number of results per page
        String request = api + "&per_page=16";
        request += "&format=json&nojsoncallback=1&extras=geo";
        request += "&api_key=" + "f7b135fed95b3221d0bfbb5fd6540a94";

	// optional search fields
	//String userId = "88935360@N05";
	//request += "&user_id=" + userId;
	//request += "&tags=hydrocephalic";

	if (key.length() != 0) {
	    request += "&tags="+key;
	}

	System.out.println("Sending http GET request:");
	System.out.println(request);

	// open http connection
	URL obj = new URL(request);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	// send GET request
        con.setRequestMethod("GET");

	// get response
        int responseCode = con.getResponseCode();

	System.out.println("Response Code : " + responseCode);

	// read and construct response String
        BufferedReader in = new BufferedReader(new InputStreamReader
					       (con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

	System.out.println(response);

	Gson gson = new Gson();
	String s = response.toString();

	Response responseObject = gson.fromJson(s, Response.class);
	System.out.println("# photos = " + responseObject.photos.photo.length);
	System.out.println("Photo 0:");
	int farm = responseObject.photos.photo[0].farm;
	String server = responseObject.photos.photo[0].server;
	String id = responseObject.photos.photo[0].id;
	String secret = responseObject.photos.photo[0].secret;
	String photoUrl = "http://farm"+farm+".static.flickr.com/"
	    +server+"/"+id+"_"+secret+".jpg";
	System.out.println(photoUrl);


    }
}
