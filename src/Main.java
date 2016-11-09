import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Main extends JFrame implements KeyListener {
  public static boolean left = false;
  public static boolean up = false;
  public static boolean right = false;
  public static boolean down = false;
  public static boolean rotLeft = false;
  public static boolean rotRight = false;

  public Main() {
    initUI();
  }

  private void initUI() {
    Surface s = new Surface();
    add(s);

    pack();

    setTitle(s.getTitle());
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addKeyListener(this);
    s.start();
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        Main m = new Main();
        m.setVisible(true);
      }
    });
  }

  @Override
  public void keyPressed(KeyEvent e) {
    /*
     * w = 87
     * a = 65
     * s = 83
     * d = 68
     *
     * up = 38
     * left = 37
     * down = 39
     * right = 40
     */
    if (e.getKeyCode() == 65) left = true;
    if (e.getKeyCode() == 87) up = true;
    if (e.getKeyCode() == 68) right = true;
    if (e.getKeyCode() == 83) down = true;

    if (e.getKeyCode() == 37) rotLeft = true;
    if (e.getKeyCode() == 39) rotRight = true;
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == 65) left = false;
    if (e.getKeyCode() == 87) up = false;
    if (e.getKeyCode() == 68) right = false;
    if (e.getKeyCode() == 83) down = false;

    if (e.getKeyCode() == 37) rotLeft = false;
    if (e.getKeyCode() == 39) rotRight = false;
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
}
