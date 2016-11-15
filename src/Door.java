import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

public class Door extends Entity {
  private boolean opening = false;
  private Timer t = new Timer(2 * 1000, new StopListener());

  public Door(BufferedImage sprite, double posX, double posY, double dirX, double dirY, boolean canWalkThrough) {
    super(sprite, posX, posY, dirX, dirY, canWalkThrough);
  }

  @Override
  public void action() {
    if (opening) return;
    if (this.getWalkThrough()) {
      startClosing();
    } else {
      startOpening();
    }
  }

  private void startOpening() {
    opening = true;
    t.start();
  }

  private void startClosing() {
    opening = true;
    close();
    opening = false;
  }

  private void open() {
    this.setWalkThrough(true);
  }

  private void close() {
    this.setWalkThrough(false);
  }

  private class StopListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      opening = false;
      open();
      t.stop();
    }
  }

}