/**
 * A Photo class that extends the Java Swing component JButton
 * It allows pictures to be selected in the main GUI
 * @param  url  an absolute URL giving the base location of the image
 * @param  image the image is stored as Image type
 * @return      the image at the specified URL
 * @see         Image
 */

package albumCode;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
/**
 *
 * @author suejanehan
 */
public class Photo extends JButton {
    Image image;
    String url;

    //inherit from JButton class
    public Photo(ImageIcon image){
        super(image);
    }
}
