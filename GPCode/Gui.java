package GPCode;

import com.google.gson.Gson;
import java.util.ArrayList;

import java.awt.*;
import java.io.IOException;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
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
    //create an ArrayList of Photos in photoArray
    private ArrayList <Photo> photoArray;
    //create Photo deletePhoto to remember which Photo is selected
    private Photo deletePhoto;
    //searchTextArray is needed because we need to replace spaces with %20
    private String[] searchTextArray;
    //create select to remember which item is selected
    private int select;

    JTextField searchTagField = new JTextField("");
    JTextField numResultsStr = new JTextField("10");
    static JPanel onePanel;
    JScrollPane oneScrollPanel;
    //create Buttons
    JButton testButton = new JButton("Test");
    JButton searchButton = new JButton("Search");
    JButton saveButton;
    JButton deleteButton = new JButton("Delete");
    JButton loadButton = new JButton("Load");
    JButton exitButton = new JButton("Exit");
    static int frameWidth = 800;
    static int frameHeight = 600;

    public Gui() {
        this.saveButton = new JButton("Save");
        //create a ArrayList of Photos to store images and urls
        //create a Photo of deletePhoto to remember which photo was selcted for deletion
        photoArray = new ArrayList <Photo> ();
        // create bottom subpanel with buttons, flow layout
	JPanel buttonsPanel = new JPanel();
	buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
	// add buttons to bottom subpanel
	buttonsPanel.add(testButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(exitButton);
	// add listener for all button clicks
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
	Gson gson = new Gson();
	String s = response.toString();
	Response responseObject = gson.fromJson(s, Response.class);
        int farm = responseObject.photos.photo[0].farm;
	String server = responseObject.photos.photo[0].server;
	String id = responseObject.photos.photo[0].id;
	String secret = responseObject.photos.photo[0].secret;
	String photoUrl = "http://farm"+farm+".static.flickr.com/"
	    +server+"/"+id+"_"+secret+".jpg";

        // get image at loc
	onePanel.revalidate();
	onePanel.repaint();
    }
    
    public void Test(String testText) throws ProtocolException, MalformedURLException, IOException{
        System.out.println("Fetching image at: "+testText);
        // fetch image at testText
        Image photoImg = getImageURL(testText);            
        //create scaled ImageIcon
        ImageIcon scaledImageIcon = new ImageIcon(getScaledImg(photoImg));
        //create new photo with scaled ImageIcon
        Photo photo = new Photo(scaledImageIcon);
        //set Photo's variables to match input
        photo.image = getScaledImg(photoImg);
        photo.url = testText;
        photo.addActionListener(this);
        //add photo to OnePanel and photoArray
        photoArray.add(photo);
        onePanel.add(photo);
        onePanel.revalidate();
	onePanel.repaint();
    }
    
    public Response Get(String url) throws MalformedURLException, ProtocolException, IOException {
	System.out.println("Get - Sending http GET request:");
	System.out.println(url);
	// open http connection
	URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	// send GET request
        con.setRequestMethod("GET");
	// get response
        int responseCode = con.getResponseCode();
	System.out.println("Response Code : " + responseCode);
	// read and construct response String
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
	Gson gson = new Gson();
	String s = response.toString();
        //create new responseObject from gson
	Response responseObject = gson.fromJson(s, Response.class);
        System.out.println(response);
	System.out.println("# photos = " + responseObject.photos.photo.length);
        return responseObject;
    }
    
    public void Search(String inputSearchText) throws ProtocolException, MalformedURLException, IOException{
        // tag to search for
        //split inputSearchText into array of letters and spaces 
        searchTextArray = inputSearchText.split("");
        //replace spaces with %20
        for(int j=0; j<searchTextArray.length ; j++){
            if (" ".equals(searchTextArray[j])){
                searchTextArray[j]="%20";
            }
        }
        String searchText;
        //create searchText which represents inputSearchText with spaces replaced by %20
        searchText = String.join("", searchTextArray);
        System.out.println("Search String: "+searchText);
	String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
	// number of results per page
        String num = numResultsStr.getText();
        String request = api + "&per_page=" + num;
        request += "&format=json&nojsoncallback=1&extras=geo";
        request += "&api_key=" + "f7b135fed95b3221d0bfbb5fd6540a94";
	request += "&tags="+searchText;
	System.out.println("Search - Sending http GET request:");
	System.out.println(request);
	// open http connection
        Response responseObject = Get(request);
        // get image at loc
        for(int i=0; i<responseObject.photos.photo.length; i++){
            System.out.println("Photo: " + i);
            int farm = responseObject.photos.photo[i].farm;
            String server = responseObject.photos.photo[i].server;
            String id = responseObject.photos.photo[i].id;
            String secret = responseObject.photos.photo[i].secret;
            String photoUrl = "http://farm"+farm+".static.flickr.com/"+server+"/"+id+"_"+secret+".jpg";
            System.out.println(photoUrl);
            //get photoImg at each photoUrl
            Image photoImg = getImageURL(photoUrl);            
            //create ImageIcon with each photoImg
            ImageIcon scaledImageIcon = new ImageIcon(getScaledImg(photoImg));
            //create new Photo object with ImageIcon
            Photo photo = new Photo(scaledImageIcon);
            //set photo variables to reflect input data
            photo.image = getScaledImg(photoImg);
            photo.url = photoUrl;
            //add to photoArray
            photoArray.add(photo);
            System.out.println(photo);
        }
        //Go through photoArray and add to onePanel
        Display();
	onePanel.revalidate();
	onePanel.repaint();
    }
    
    public void Display(){
        //Go through photoArray and add each Photo to onePanel with actionListener
        for(int k=0; k<photoArray.size(); k++){
            Photo photo = photoArray.get(k);
            photo.addActionListener(this);
            onePanel.add(photo);
        }
    }
    
    public Image getScaledImg(Image inputImg){
            //create bufferedImg to manipulate inputImg
            BufferedImage bufferedImg = (BufferedImage) inputImg;
            //create height nad width to store values
            double height = bufferedImg.getHeight();
            double width = bufferedImg.getWidth();
            //create ratio to reflect 200 height aspect ratio
            double ratio = 200 / height;
            //create scaledimg with getScaledInstance
            Image scaledImg = bufferedImg.getScaledInstance((int) (width*ratio), 200,BufferedImage.TYPE_INT_ARGB);
            return scaledImg;
    }
    
    public void Save(){
          try{
            // Create file photo_album.txt
            String fileName = "./photo_album.txt";
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            for(int i = 0; i < photoArray.size(); i++){
                //write line with each photo's URL + a new line
                out.write(photoArray.get(i).url + "\n");
                System.out.println("Saving Image at: " + photoArray.get(i).url);
            }
            System.out.println("Saving to: " + fileName);
            //Close the output stream
            out.close();
            }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            }
    }
    
    public void Load() throws MalformedURLException, IOException{
        // The name of the file to open.
        String fileName = "./photo_album.txt";
        // This will reference one line at a time
        String line = null;
        System.out.println("Loading from: " + fileName);
        try {
            // FileReader reads from photo_album.txt
            FileReader fileReader = new FileReader(fileName);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
            //utilize Test to fetch images at each url stored in photo_album.txt
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
	if (e.getSource() == searchButton) {
            System.out.println("searchTagField: " + searchTagField.getText());
            String searchText = searchTagField.getText();
            try {
            //search for searchText when search button is clicked
                Search(searchText);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	else if (e.getSource() == testButton) {
            System.out.println("Test button clicked-->testSearchTagField: " + searchTagField.getText());
            String testText = searchTagField.getText();
            try {
            //test testText when test is clicked
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
            //save URLS in photoArray to photo_album.txt when save is clicked
            Save();
        }
        else if (e.getSource() == loadButton){
            System.out.println("Load Button Clicked!");
            try {
            //load images from photo_album.txt when clicked
                Load();
            } catch (IOException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (e.getSource() == deleteButton){
            System.out.println("Deleted item: " + select);
            //remove selected item from photoArray
            photoArray.remove(deletePhoto);
            //remove selected item form onePanel
            onePanel.remove(deletePhoto);
            onePanel.revalidate();
            onePanel.repaint();
        }
        else if (e.getSource() == exitButton){
            //exit program when exit is clicked
            System.exit(0);
        }
        for( int a=0; a<onePanel.getComponentCount();a++){
            //loop through each Photo in onePanel to listen for click
            Component photo = onePanel.getComponent(a);
            if(e.getSource() == photo){
                //set select to equal the index of the selected photo
                select = a;
                System.out.println("Selected item at index: "+ a);
                //set deletePhoto to selected photo
                deletePhoto = (Photo) photo;
            }
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
