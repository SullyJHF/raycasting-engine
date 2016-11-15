import java.awt.image.BufferedImage;

public class Entity {
  private BufferedImage sprite;
  private double posX;
  private double posY;
  private double dirX;
  private double dirY;
  private double width;

  private boolean canWalkThrough = false;

  public Entity(double posX, double posY, double dirX, double dirY, double width) {
    this.posX = posX;
    this.posY = posY;
    this.dirX = dirX;
    this.dirY = dirY;
    this.width = width / 64.0;
  }

  public Entity(BufferedImage sprite, double posX, double posY, double dirX, double dirY, boolean canWalkThrough) {
    this.sprite = sprite;
    this.posX = posX;
    this.posY = posY;
    this.dirX = dirX;
    this.dirY = dirY;
    this.canWalkThrough = canWalkThrough;
  }

  public void action() {
    System.out.println("Action happened on entity at pos: (" + posX + ", " + posY + ")");
  }

  public BufferedImage getSprite() {
    return this.sprite;
  }

  public double getPosX() {
    return this.posX;
  }

  public void setPosX(double posX) {
    this.posX = posX;
  }

  public double getPosY() {
    return this.posY;
  }

  public void setPosY(double posY) {
    this.posY = posY;
  }

  public double getDirX() {
    return this.dirX;
  }

  public void setDirX(double dirX) {
    this.dirX = dirX;
  }

  public double getDirY() {
    return this.dirY;
  }

  public void setDirY(double dirY) {
    this.dirY = dirY;
  }

  public boolean getWalkThrough() {
    return this.canWalkThrough;
  }

  public void setWalkThrough(boolean walkThrough) {
    this.canWalkThrough = walkThrough;
  }

  public double getWidth() {
    return this.width;
  }
}
