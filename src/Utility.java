/*
 * */
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
public abstract class Utility {
    public Utility() {}

    public static void drawCenteredCircle(Graphics g, int x, int y, int r, Color c) {
        g.setColor(c);
        g.drawOval(x-r,y-r,r*2,r*2);
    }

    public static void drawCenteredTextBox(Graphics g, int x, int y, String str, Color txtColor) {
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth(str);
        int height = metrics.getAscent() + metrics.getDescent();
        int newX = x- (width/2);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(newX,y-height,width,height);
        g.setColor(txtColor);
        g.drawString(str,newX,y);
    }

    public static boolean pointInCircle(int x, int y, int r, int testX, int testY) {
        int rSquared = r*r;
        return (x-testX) * (x-testX) + (y-testY) * (y-testY) <= rSquared;
    }
}