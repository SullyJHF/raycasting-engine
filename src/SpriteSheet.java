import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
  private File file;
  private BufferedImage image;

  private int spriteSize;

  private int w, h;

  public SpriteSheet(String filepath) {
    this.file = new File(filepath);
    String fileName = this.file.getName();
    this.spriteSize = Integer.valueOf(fileName.substring(0, fileName.indexOf('x')));
    loadImage();
    this.w = this.image.getWidth();
    this.h = this.image.getHeight();
  }

  private void loadImage() {
    try {
      this.image = ImageIO.read(this.file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public BufferedImage getSprite(int index) {
    if(index < 0) return null;
    int x = (index * this.spriteSize) % this.w;
    int y = (int) Math.floor((index * this.spriteSize) / this.w) * this.spriteSize;
    BufferedImage sprite = this.image.getSubimage(x, y, this.spriteSize, this.spriteSize);
    //    System.out.printf("x: %d y: %d width: %d height %d%n", x, y, this.spriteSize, this.spriteSize);
    return sprite;
  }
}
