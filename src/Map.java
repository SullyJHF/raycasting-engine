import java.awt.Color;
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
    for (int y = 0; y < this.h; y++) {
      for (int x = 0; x < this.w; x++) {
        int rgb = this.mapImg.getRGB(x, y);
        this.mapArray[y][x] = this.ch.getMapTileFromColourRGB(rgb);
      }
    }
  }

  public int[][] getMapArray() {
    return this.mapArray;
  }

  public BufferedImage getMapImage() {
    return this.mapImg;
  }

  public BufferedImage getAlphaMapImage(Color replace) {
    BufferedImage newImg = new BufferedImage(this.w, this.h, BufferedImage.TYPE_INT_ARGB);
    for (int y = 0; y < this.h; y++) {
      for (int x = 0; x < this.w; x++) {
        int rgb = this.mapImg.getRGB(x, y);
        if (ch.isInvisible(rgb)) continue;
        newImg.setRGB(x, y, rgb);
      }
    }
    return newImg;
  }

  public BufferedImage getSubAlphaImage(Color replace, double posX, double posY, double diameter) {
    BufferedImage worldMap = new BufferedImage((int) diameter, (int) diameter, BufferedImage.TYPE_INT_ARGB);
    // get sub section of getAlphaMapImage
    double width = diameter;
    double height = diameter;
    double x = posY - width / 2;
    double y = posX - height / 2;
    if (x < 0) x = 0;
    if (y < 0) y = 0;
    if (x + width > this.w) width = Math.ceil(this.w - x);
    if (y + height > this.h) height = Math.ceil(this.h - y);
    //    System.out.printf("x: %f y: %f width: %f height: %f%n", x, y, width, height);
    BufferedImage newImg = this.getAlphaMapImage(replace).getSubimage((int) x, (int) y, (int) width, (int) height);
    int playerX = (int) (posY - x);
    int playerY = (int) (posX - y);
    //    System.out.printf("playerX: %d playerY: %d%n", playerX, playerY);
    // move it so the map is displayed in the bottom left when we are in the bottom left
    newImg.setRGB(playerX, playerY, Color.WHITE.getRGB());
    worldMap.setData(newImg.getRaster());
    return worldMap;
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
    for (int[] row : this.mapArray) {
      for (int col : row) {
        System.out.print(col + " ");
      }
      System.out.println();
    }
  }
}
