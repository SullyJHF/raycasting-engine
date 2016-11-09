import java.awt.Color;

class ColourHandler {
  private int[] cInts = { -16777216, -14865581, -8510125, -16742575, -5549514, -10528945, -4013113, -3608, -65459,
      -23808, -5081, -16718794, -14045697, -8161636, -34904, -13142 };
  private int[][] cRGBs = { { 0, 0, 0 }, { 29, 43, 83 }, { 126, 37, 83 }, { 0, 135, 81 }, { 171, 82, 54 },
      { 95, 87, 79 }, { 194, 195, 199 }, { 255, 241, 232 }, { 255, 0, 77 }, { 255, 163, 0 }, { 255, 236, 39 },
      { 0, 228, 54 }, { 41, 173, 255 }, { 131, 118, 156 }, { 255, 119, 168 }, { 255, 204, 170 } };
  private String[] cStrings = { "BLACK", "DARK_BLUE", "DARK_PURPLE", "DARK_GREEN", "BROWN", "DARK_GRAY", "LIGHT_GRAY",
      "WHITE", "RED", "ORANGE", "YELLOW", "GREEN", "BLUE", "INDIGO", "PINK", "PEACH" };
  private final int INVISIBLE_INT = -65281; // PINK

  public ColourHandler() {
  }

  public Color getColourByName(String name) {
    name = name.toUpperCase();
    for (int i = 0; i < cStrings.length; i++) {
      if (cStrings[i].equals(name)) return new Color(cInts[i]);
    }
    return null;
  }

  public String getColourByInt(int colour) {
    for (int i = 0; i < cInts.length; i++) {
      if (cInts[i] == colour) return cStrings[i];
    }
    return null;
  }

  public Color getColourFromMapTile(int mapTile) {
    if (mapTile == 0) return null;
    mapTile--;
    return new Color(cInts[mapTile]);
  }

  public int getMapTileFromColourRGB(int rgb) {
    if (rgb == INVISIBLE_INT) return 0;
    rgb = findClosestColor(rgb).getRGB();
    for (int i = 0; i < cInts.length; i++) {
      if (cInts[i] == rgb) return i + 1;
    }
    return 0;
  }

  public Color findclosestColor(Color c) {
    return findClosestColor(c.getRGB());
  }

  public Color findClosestColor(int rgb) {
    double smallestD = Double.MAX_VALUE;
    int index = -1;
    int r = (rgb >> 16) & 0xFF;
    int g = (rgb >> 8) & 0xFF;
    int b = rgb & 0xFF;
    for (int i = 0; i < cInts.length; i++) {
      double d = Math.pow(cRGBs[i][0] - r, 2) + Math.pow(cRGBs[i][1] - g, 2) + Math.pow(cRGBs[i][2] - b, 2);
      if (d < smallestD) {
        smallestD = d;
        index = i;
      }
    }
    return new Color(cInts[index]);
  }

  final Color BLACK = new Color(cInts[0]);
  final Color DARK_BLUE = new Color(cInts[1]);
  final Color DARK_PURPLE = new Color(cInts[2]);
  final Color DARK_GREEN = new Color(cInts[3]);
  final Color BROWN = new Color(cInts[4]);
  final Color DARK_GRAY = new Color(cInts[5]);
  final Color LIGHT_GRAY = new Color(cInts[6]);
  final Color WHITE = new Color(cInts[7]);
  final Color RED = new Color(cInts[8]);
  final Color ORANGE = new Color(cInts[9]);
  final Color YELLOW = new Color(cInts[10]);
  final Color GREEN = new Color(cInts[11]);
  final Color BLUE = new Color(cInts[12]);
  final Color INDIGO = new Color(cInts[13]);
  final Color PINK = new Color(cInts[14]);
  final Color PEACH = new Color(cInts[15]);

  public boolean isInvisible(int rgb) {
    if (rgb == INVISIBLE_INT) return true;
    return false;
  }
}
