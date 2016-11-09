import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Surface extends JPanel implements ActionListener {
  private boolean debugText = false;
  private Graphics g;
  private Timer t;
  private ColourHandler ch = new ColourHandler();
  private double goalFps = 60;
  private int frameTimeMs = (int) (1000 / goalFps);
  private final int INITIAL_DELAY = 0;
  private double runningTime = 0;
  private double oldRunningTime = 0;
  private String fps = "";

  private final int SCALE = 3;
  private final int SCREEN_HEIGHT = 200;
  private final int SCREEN_WIDTH = 320;

  private Map m;
  private int[][] worldMap;
  private int mapWidth;
  private int mapHeight;

  // Game world variables
  double posX, posY; // posX is actually number of rows down you are and posY is number of columns across
  double dirX, dirY;
  double planeX, planeY;
  double moveSpeed, rotSpeed;

  double time;
  double oldTime;

  public Surface() {
    initTimer();
    setSurfaceSize();
    initGameWorld();
  }

  public void start() {
    startTimer();
  }

  private void initTimer() {
    this.t = new Timer(frameTimeMs, this);
    this.t.setInitialDelay(INITIAL_DELAY);
  }

  private void setSurfaceSize() {
    setPreferredSize(new Dimension(SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE));
  }

  private void initGameWorld() {
    this.m = new Map("res/00Dungeon_of_the_Damned.png", "Test map");
    this.worldMap = m.getMapArray();
    this.mapWidth = m.getWidth();
    this.mapHeight = m.getHeight();

    setStartPosition();
  }

  private void startTimer() {
    this.time = System.nanoTime();
    this.oldTime = time;
    this.t.start();
  }

  private void setStartPosition() {
    this.posX = 62;
    this.posY = 1;

    this.dirX = -1;
    this.dirY = 0;

    this.planeY = 0.66;
    this.planeX = 0;

  }

  private void tick(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();

    // draw sky and ground
    g2d.setColor(ch.BLUE.brighter());
    g2d.fillRect(0, 0, SCREEN_WIDTH * SCALE, (SCREEN_HEIGHT / 2) * SCALE);
    g2d.setColor(ch.BLACK.brighter());
    g2d.fillRect(0, (SCREEN_HEIGHT / 2) * SCALE, SCREEN_WIDTH * SCALE, (SCREEN_HEIGHT / 2) * SCALE);

    // Loop through each column on the screen
    for (int x = 0; x < SCREEN_WIDTH; x++) {
      double cameraX = 2 * x / (double) SCREEN_WIDTH - 1;
      double rayPosX = posX;
      double rayPosY = posY;
      double rayDirX = dirX + planeX * cameraX;
      double rayDirY = dirY + planeY * cameraX;

      int mapX = (int) rayPosX;
      int mapY = (int) rayPosY;

      double sideDistX, sideDistY;

      double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
      double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
      double perpWallDist;

      int stepX, stepY;

      int hit = 0;
      int side = -1;

      if (rayDirX < 0) {
        stepX = -1;
        sideDistX = (rayPosX - mapX) * deltaDistX;
      } else {
        stepX = 1;
        sideDistX = (mapX + 1.0 - rayPosX) * deltaDistX;
      }

      if (rayDirY < 0) {
        stepY = -1;
        sideDistY = (rayPosY - mapY) * deltaDistY;
      } else {
        stepY = 1;
        sideDistY = (mapY + 1.0 - rayPosY) * deltaDistY;
      }

      while (hit == 0) {
        if (sideDistX < sideDistY) {
          sideDistX += deltaDistX;
          mapX += stepX;
          side = 0;
        } else {
          sideDistY += deltaDistY;
          mapY += stepY;
          side = 1;
        }
        if (mapX >= mapWidth) hit = -1;
        if (mapY >= mapHeight) hit = -1;
        if (hit == -1) {
          break;
        }
        if (hit != -1 && worldMap[mapX][mapY] > 0) hit = 1;
      }

      if (hit == -1) {
        continue;
      }

      if (side == 0) {
        perpWallDist = (mapX - rayPosX + (1 - stepX) / 2) / rayDirX;
      } else {
        perpWallDist = (mapY - rayPosY + (1 - stepY) / 2) / rayDirY;
      }

      int lineHeight = (int) (SCREEN_HEIGHT / perpWallDist);
      int drawStart = -lineHeight / 2 + SCREEN_HEIGHT / 2;
      if (drawStart < 0) drawStart = 0;
      int drawEnd = lineHeight / 2 + SCREEN_HEIGHT / 2;
      if (drawEnd >= SCREEN_HEIGHT) drawEnd = SCREEN_HEIGHT - 1;

      Color c;
      c = ch.getColourFromMapTile(worldMap[mapX][mapY]);

      if (side == 1) {
        c = c.darker();
      }

      // draw the line
      g2d.setColor(c);
      g2d.fillRect(x * SCALE, drawStart * SCALE, SCALE, (drawEnd - drawStart) * SCALE);
    }

    oldTime = time;
    time = System.nanoTime();
    double timeDiff = time - oldTime;
    double frameTime = timeDiff / 1000000000.0;

    moveSpeed = frameTime * 5.0;
    rotSpeed = frameTime * 3.0;
    if (Main.rotLeft) spinLeft();
    if (Main.rotRight) spinRight();
    if (Main.up) moveForward();
    if (Main.down) moveBackwards();
    if (Main.left) strafe(false);
    if (Main.right) strafe(true);

    if(debugText) {
      g2d.setColor(Color.BLACK);
      g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, 20));
      oldRunningTime = runningTime;
      runningTime += timeDiff / 1000000000.0;
      if (Math.floor(runningTime) != Math.floor(oldRunningTime)) {
        updateFps((int) (1.0 / frameTime));
      }
      g2d.drawString(this.fps, 0, 18);
      g2d.drawString("dirX: " + String.valueOf(Math.round(dirX * 100.0) / 100.0), 0, 50);
      g2d.drawString("dirY: " + String.valueOf(Math.round(dirY * 100.0) / 100.0), 0, 75);
      double newDirX = Math.toDegrees(Math.acos(Math.round(dirX * 100.0) / 100.0));
      double newDirY = Math.toDegrees(Math.acos(Math.round(dirY * 100.0) / 100.0));
      g2d.drawString("newDirX: " + String.valueOf(Math.round(newDirX * 100.0) / 100.0), 0, 100);
      g2d.drawString("newDirY: " + String.valueOf(Math.round(newDirY * 100.0) / 100.0), 0, 125);

      g2d.drawString("posX: " + String.valueOf(Math.round(posX * 100.0) / 100.0), 100, 50);
      g2d.drawString("posY: " + String.valueOf(Math.round(posY * 100.0) / 100.0), 100, 75);
    }

    g2d.dispose();
  }

  private void updateFps(int fps) {
    this.fps = String.valueOf(fps) + " FPS";
  }

  private void spinLeft() {
    double oldDirX = dirX;
    dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
    dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
    double oldPlaneX = planeX;
    planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
    planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
  }

  private void moveForward() {
    if (worldMap[(int) (posX + dirX * moveSpeed)][(int) posY] == 0) posX += dirX * moveSpeed;
    if (worldMap[(int) posX][(int) (posY + dirY * moveSpeed)] == 0) posY += dirY * moveSpeed;
  }

  private void strafe(boolean direction) {
    // true = right
    int d = direction ? -1 : 1;
    double oldDirX = dirX;
    double newDirX = dirX * Math.cos(d * Math.PI / 2) - dirY * Math.sin(d * Math.PI / 2);
    double newDirY = oldDirX * Math.sin(d * Math.PI / 2) + dirY * Math.cos(d * Math.PI / 2);
    if (worldMap[(int) (posX + newDirX * moveSpeed)][(int) posY] == 0) posX += newDirX * moveSpeed;
    if (worldMap[(int) posX][(int) (posY + newDirY * moveSpeed)] == 0) posY += newDirY * moveSpeed;
  }

  private void spinRight() {
    double oldDirX = dirX;
    dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
    dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
    double oldPlaneX = planeX;
    planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
    planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
  }

  private void moveBackwards() {
    if (worldMap[(int) (posX - dirX * moveSpeed)][(int) posY] == 0) posX -= dirX * moveSpeed;
    if (worldMap[(int) posX][(int) (posY - dirY * moveSpeed)] == 0) posY -= dirY * moveSpeed;
  }

  public String getTitle() {
    return m.getTitle();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    tick(g);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // repaint calls paintComponent so in turn calls tick(g);
    repaint();
  }
}