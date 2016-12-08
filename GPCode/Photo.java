/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPCode;
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
    
    public Photo(ImageIcon image){
        super(image);
    }
}
