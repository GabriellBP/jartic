package jartic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Jartic extends JFrame {

    boolean pressed = false;

    Jartic() {

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                draws.clear();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                System.out.println(pressed);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        new Time().start();

        setSize(1200, 900);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    ArrayList<Draw> draws = new ArrayList<>();

    public class Draw {
        int x, y;

        public Draw(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void paint(Graphics g) {
        for (int i = 1; i < draws.size(); i++) {
            int x1 = draws.get(i).x;
            int y1 = draws.get(i).y;
            int x2 = draws.get(i-1).x;
            int y2 = draws.get(i-1).y;
            g.drawLine(x2, y2, x1, y1);
        }

    }

    public static void main(String[] args) {
        new Jartic();
    }

    public class Time extends Thread {
        public void run() {
            while (true) {
                if (pressed) {
                    try {
                        Point point = getMousePosition();
                        draws.add(new Draw(point.x, point.y));
                    } catch (Exception error) {

                    }
                }
                repaint();
            }
        }
    }
}
