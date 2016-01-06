package cardvis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author zachkondak
 */
public class CardVis extends JPanel{
    
    private int imageWidth;
    private int imageHeight;
    private int deckSize;
    private Color[] colors;
    private int[][] simulation;
    private String name;
    
    CardVis(int imageWidth, int imageHeight, int deckSize, Color color1, Color color2, String name) throws DeckSizeError{
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.deckSize = deckSize;
        this.name = name;
        Color[] c = {color1, color2};
        this.simulation = Deck.simulate(deckSize);
        this.colors = c;
        if (simulation.length > imageHeight) throw new DeckSizeError("Number of deck rotations is greater than image height");
        if (deckSize > imageWidth) throw new DeckSizeError("Deck size is greater than image width");
        this.setSize(imageWidth, imageHeight);
    }
    
    CardVis(int[] cardSize, int deckSize, Color color1, Color color2) throws DeckSizeError{
        this(Math.max(1, deckSize * cardSize[0]), Math.max(1, Deck.simulate(deckSize).length * cardSize[1]), deckSize, color1, color2, "Cards");
    }
    
    CardVis(int imageWidth, int imageHeight, int deckSize) throws DeckSizeError{
        this(imageWidth, imageHeight, deckSize, Color.WHITE, Color.BLACK, "Cards");
    }
    
    CardVis(int[] cardSize, int deckSize, Color color1, Color color2, String name) throws DeckSizeError{
        this(Math.max(1, deckSize * cardSize[0]), Math.max(1, Deck.simulate(deckSize).length * cardSize[1]), deckSize, color1, color2, name);
    }
    
    CardVis(int imageWidth, int imageHeight, int deckSize, String name) throws DeckSizeError{
        this(imageWidth, imageHeight, deckSize, Color.WHITE, Color.BLACK, name);
    }
    
    @Override
    public void paint(Graphics g){
      BufferedImage img = createImage();
      
      double changeInR = colors[1].getRed() - colors[0].getRed();
      double changeInG = colors[1].getGreen() - colors[0].getGreen();
      double changeInB = colors[1].getBlue() - colors[0].getBlue();
      
      double dR = Math.min(255.0, changeInR / (double) deckSize);
      double dG = Math.min(255.0, changeInG / (double) deckSize);
      double dB = Math.min(255.0, changeInB / (double) deckSize);
      
      int extraSpaceX = this.imageWidth % deckSize;
      int adjustedWidth = imageWidth - extraSpaceX;
      int cardWidth = adjustedWidth / deckSize;
      
      int rotations = simulation.length;
      int extraSpaceY = this.imageHeight % rotations;
      int adjustedHeight = imageHeight - extraSpaceY;
        int cardHeight = adjustedHeight / rotations;

        for (int row = 0; row < rotations; row++) {
            for (int col = 0; col < deckSize; col++) {
                int red = Math.max(0, Math.min(255, colors[0].getRed() + (int) (simulation[row][col] * dR)));
                int green = Math.max(0, Math.min(255, colors[0].getGreen() + (int) (simulation[row][col] * dG)));
                int blue = Math.max(0, Math.min(255, colors[0].getBlue() + (int) (simulation[row][col] * dB)));
                Color c = new Color(red, green, blue);
                int cardColor = c.hashCode();
                int xOffset = col * cardWidth;
                int yOffset = row * cardHeight;
                for (int x = 0; x < cardWidth; x++) {
                    for (int y = 0; y < cardHeight; y++) {
                        int pixelX = xOffset + x;
                        int pixelY = yOffset + y;
                        img.setRGB(pixelX, pixelY, cardColor);
                    }
                }

                //deal with extra pixels
                if (row == rotations - 1) {
                    for (int x = 0; x < cardWidth; x++) {
                        for (int y = 0; y < extraSpaceY; y++) {
                            int pixelX = xOffset + x;
                            int pixelY = yOffset + y + cardHeight;
                            img.setRGB(pixelX, pixelY, cardColor);
                        }
                    }
                }
                if (col == deckSize - 1) {
                    for (int x = 0; x < extraSpaceX; x++) {
                        for (int y = 0; y < cardHeight; y++) {
                            int pixelX = xOffset + x + cardWidth;
                            int pixelY = yOffset + y;
                            img.setRGB(pixelX, pixelY, cardColor);
                        }
                    }
                }
                if (row == rotations - 1 && col == deckSize - 1) {
                    for (int x = 0; x < extraSpaceX; x++) {
                        for (int y = 0; y < extraSpaceY; y++) {
                            int pixelX = xOffset + x + cardWidth;
                            int pixelY = yOffset + y + cardHeight;
                            img.setRGB(pixelX, pixelY, cardColor);
                        }
                    }
                }
          }
      }
      g.drawImage(img, 0,0,this);
   }

    public BufferedImage createImage() {
        BufferedImage picture = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
        return picture;
    }
    
    public void display(){
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        frame.getContentPane().setPreferredSize(new Dimension(imageWidth, imageHeight));
        frame.pack();
        frame.setTitle(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DeckSizeError {
        
        //Example1: Size of image is calculated based on desired card size
        int[] cardSize = {10, 10};
        new CardVis(cardSize, 54, Color.CYAN, Color.RED, "Example 1 - 54 card deck").display();
        
        //Example2: Size of image is explicitly stated by user
        //new CardVis(520, 90, 52, Color.CYAN, Color.RED, "Example 2 - 52 card deck").display();
    }
    
}
