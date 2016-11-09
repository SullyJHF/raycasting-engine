import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Map {
  private File mapFile;
  private BufferedImage mapImg;

  private ColourHandler ch;

  private int w, h;

  private int[][] mapArray;

  private String mapTitle;

  public Map(String filepath, String mapTitle) {
    this.mapFile = new File(filepath);
    this.mapTitle = mapTitle;
    this.ch = new ColourHandler();
    loadImage();
    createArray();
  }

  private void loadImage() {
    try {
      this.mapImg = ImageIO.read(this.mapFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void createArray() {
    this.w = this.mapImg.getWidth(null);
    this.h = this.mapImg.getHeight(null);
    this.mapArray = new int[this.h][this.w];
    for(int y = 0; y < this.h; y++) {
      for(int x = 0; x < this.w; x++) {
        int rgb = this.mapImg.getRGB(x, y);
        this.mapArray[y][x] = this.ch.getMapTileFromColourRGB(rgb);
      }
    }
  }

  public int[][] getMapArray() {
    return this.mapArray;
  }

  public int getWidth() {
    return this.w;
  }

  public int getHeight() {
    return this.h;
  }

  public String getTitle() {
    return this.mapTitle;
  }

  public void printMap() {
    for(int[] row : this.mapArray) {
      for(int col : row) {
        System.out.print(col + " ");
      }
      System.out.println();
    }
  }
}
