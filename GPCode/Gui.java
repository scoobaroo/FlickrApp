package GPCode;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Scanner;

import java.awt.*;
import java.io.IOException;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Frame;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Object;
import java.net.HttpURLConnection;
import javax.swing.JFrame;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import com.google.gson.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Gui extends JFrame implements ActionListener {
    
    ArrayList <Photo> photoArray;
    Photo deletePhoto;
    private String[] searchTextArray;

    JTextField searchTagField = new JTextField("");
    JTextField numResultsStr = new JTextField("10");
    static JPanel onePanel;
    JScrollPane oneScrollPanel;
    JButton testButton = new JButton("Test");
    JButton searchButton = new JButton("Search");
    JButton saveButton = new JButton("Save");
    JButton deleteButton = new JButton("Delete");
    JButton loadButton = new JButton("Load");
    JButton exitButton = new JButton("Exit");
    
    static int frameWidth = 800;
    static int frameHeight = 600;

    public Gui() {

	// create bottom subpanel with buttons, flow layout
        Photo deletePhoto = new Photo ();
        photoArray = new ArrayList <Photo> ();
        
	JPanel buttonsPanel = new JPanel();
	buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
	// add testButton to bottom subpanel
	buttonsPanel.add(testButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(exitButton);
	// add listener for testButton clicks
	testButton.addActionListener(this);
        loadButton.addActionListener(this);
        saveButton.addActionListener(this);
        deleteButton.addActionListener(this);
        exitButton.addActionListener(this);

	/*
	System.out.println("testButton at " +
			   testButton.getClass().getName() +
			   "@" + Integer.toHexString(hashCode()));
	System.out.println("Components: ");
	Component comp[] = buttonsPanel.getComponents();
	for (int i=0; i<comp.length; i++) {
	    System.out.println(comp[i].getClass().getName() +
			       "@" + Integer.toHexString(hashCode()));
	}
	*/

	// create middle subpanel with 2 text fields and button, border layout
	JPanel textFieldSubPanel = new JPanel(new FlowLayout());
	// create and add label to subpanel
	JLabel tl = new JLabel("Enter search tag:");
	textFieldSubPanel.add(tl);

	// set width of left text field
	searchTagField.setColumns(23);
	// add listener for typing in left text field
	searchTagField.addActionListener(this);
	// add left text field to middle subpanel
	textFieldSubPanel.add(searchTagField);
	// add search button to middle subpanel
	textFieldSubPanel.add(searchButton);
	// add listener for searchButton clicks
	searchButton.addActionListener(this);

	// create and add label to middle subpanel, add to middle subpanel
	JLabel tNum = new JLabel("max search results:");
	numResultsStr.setColumns(2);
	textFieldSubPanel.add(tNum);
	textFieldSubPanel.add(numResultsStr);

	// create and add panel to contain bottom and middle subpanels
	/*
	JPanel textFieldPanel = new JPanel(new BorderLayout());
	textFieldPanel.add(buttonsPanel, BorderLayout.SOUTH);
	textFieldPanel.add(textFieldSubPanel, BorderLayout.NORTH);
	*/
	JPanel textFieldPanel = new JPanel();
	textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));
	textFieldPanel.add(textFieldSubPanel);
	textFieldPanel.add(buttonsPanel);

	// create top panel
	onePanel = new JPanel();
	onePanel.setLayout(new BoxLayout(onePanel, BoxLayout.Y_AXIS));

	// create scrollable panel for top panel
	oneScrollPanel = new JScrollPane(onePanel,
				      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				      JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	oneScrollPanel.setPreferredSize(new Dimension(frameWidth, frameHeight-100));
	setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	// add scrollable panel to main frame
	add(oneScrollPanel);

	// add panel with buttons and textfields to main frame
	add(textFieldPanel);

	// url for image to fetch
        String loc = "http://unixlab.sfsu.edu/~whsu/csc690/P1/TestImages/Test7.png";
        // get image at loc
        Image img = getImageURL(loc);
        String loc2 = "http://unixlab.sfsu.edu/~whsu/csc690/P1/TestImages/Test8.png";
        // get image at loc
        Image img2 = getImageURL(loc2);
        
        // create ImageIcon from image
        // create JLabel from ImageIcon
        // add JLabel to onePanel
        onePanel.add(new JLabel(new ImageIcon(img)));
        onePanel.add(new JLabel(new ImageIcon(img2)));

	onePanel.revalidate();
	onePanel.repaint();
	// connect updated onePanel to oneScrollPanel
	oneScrollPanel.setViewportView(onePanel);
    }
    
    public static void main(String [] args) throws Exception {
	Gui frame = new Gui();
	frame.setTitle("Swing GUI Demo");
	frame.setSize(frameWidth, frameHeight);
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
        // tag to search for
	String key = "SFSUCS413F16Test";
	String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
	// number of results per page
        String request = api + "&per_page=16";
        request += "&format=json&nojsoncallback=1&extras=geo";
        request += "&api_key=" + "f7b135fed95b3221d0bfbb5fd6540a94";

//	 optional search fields
	String userId = "88935360@N05";
	request += "&user_id=" + userId;
	request += "&tags=hydrocephalic";

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
        // get image at loc
        Image photoImg = getImageURL(photoUrl);
        onePanel.add(new JLabel(new ImageIcon(photoImg)));
        onePanel.add(new JButton(new ImageIcon(photoImg)));
	onePanel.revalidate();
	onePanel.repaint();

    }
    
    public void Test(String testText) throws ProtocolException, MalformedURLException, IOException{
        System.out.println("Sending http GET request:");
	System.out.println(testText);

        URL obj = new URL(testText);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
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
        // get image at loc
        Image photoImg = getImageURL(photoUrl);
        Photo photo = new Photo();
        photo.image = photoImg;
        photo.url = photoUrl;
        photoArray.add(photo);
        onePanel.add(new JButton(new ImageIcon(photoImg)));
	onePanel.revalidate();
	onePanel.repaint();
    }
    
    public void Get(String inputSearchText) throws ProtocolException, MalformedURLException, IOException{
                // tag to search for
        searchTextArray = inputSearchText.split("");
        for(int j=0; j<searchTextArray.length ; j++){
            if (searchTextArray[j]==" "){
                searchTextArray[j]="%20";
            }
        }
        String searchText;
        searchText = String.join("", searchTextArray);
        System.out.println(searchText);

	String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
	// number of results per page
        String num = numResultsStr.getText();
        String request = api + "&per_page=" + num;
        request += "&format=json&nojsoncallback=1&extras=geo";
        request += "&api_key=" + "f7b135fed95b3221d0bfbb5fd6540a94";
	request += "&tags="+searchText;

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
	
        // get image at loc
        for(int i=0; i<responseObject.photos.photo.length; i++){
            System.out.println("Photo: " + i);
            int farm = responseObject.photos.photo[i].farm;
            String server = responseObject.photos.photo[i].server;
            String id = responseObject.photos.photo[i].id;
            String secret = responseObject.photos.photo[i].secret;
            String photoUrl = "http://farm"+farm+".static.flickr.com/"+server+"/"+id+"_"+secret+".jpg";
            System.out.println(photoUrl);
            Image photoImg = getImageURL(photoUrl);
   
            
            BufferedImage bufferedImg = (BufferedImage) photoImg;
            double height = bufferedImg.getHeight();
            double width = bufferedImg.getWidth();
            double ratio = 200 / height;
            Image scaledImg = bufferedImg.getScaledInstance((int) (width*ratio), 200,BufferedImage.TYPE_INT_ARGB);
            Photo photo = new Photo();
            photo.image = scaledImg;
            photo.url = photoUrl;
            photoArray.add(photo);
            System.out.println(photo);
            
            onePanel.add(new JButton(new ImageIcon(photoImg)));
        }
        
	onePanel.revalidate();
	onePanel.repaint();
    }
    
    public void Save(){
          try{
            // Create file 
            FileWriter fstream = new FileWriter("../photo_album.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            for(int i = 0; i < photoArray.size(); i++){
                out.write(photoArray.get(i).url + "\n");
                System.out.println(photoArray.get(i).url);
            }
            //Close the output stream
            out.close();
            }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            }
    }
    
    public void Load() throws MalformedURLException, IOException{
        
        // The name of the file to open.
        String fileName = "../photo_album.txt";
        // This will reference one line at a time
        String line = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                Test(line);
            }   
            // Always close files.
            bufferedReader.close();         
        }catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        System.out.println( e.getSource() );
	if (e.getSource() == searchButton) {
	    System.out.println("Search");
            System.out.println("searchTagField: " + searchTagField.getText());
            String searchText = searchTagField.getText();
            try {
                Get(searchText);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	else if (e.getSource() == testButton) {
	    System.out.println("Test");
            System.out.println("testSearchTagField: " + searchTagField.getText());
            String testText = searchTagField.getText();
            try {
                Test(testText);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	else if (e.getSource() == searchTagField) {
	    System.out.println("searchTagField: " + searchTagField.getText());
	}
        else if (e.getSource() == saveButton){
            System.out.println("Save Button Clicked!");
            Save();
        }
        else if (e.getSource() == loadButton){
            System.out.println("Load Button Clicked!");
            try {
                Load();
            } catch (IOException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (e.getSource() == deleteButton){
            System.out.println("Delete Button Clicked!");
            photoArray.remove(deletePhoto);
        }
        else if (e.getSource() == exitButton){
            System.exit(0);
        }
    }

    // get image at URL loc
    static Image getImageURL(String loc) {
        Image img = null;
        try {
            final URL url = new URL(loc);
	    img = ImageIO.read(url);
        } catch (Exception e) {
            System.out.println("Error loading image...");
            return null;
        }
        return img;

    }
    
    public class GetFlickr {
        GetFlickr() {
        }
    
    }
}
