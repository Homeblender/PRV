package ru.kolch.prv_3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

@Slf4j
@SpringBootApplication
public class MandelbrotSucc extends JFrame implements ActionListener {
    private final BufferedImage bufferedImage;
    private static final Integer WIDTH = 800;
    private static final Integer HEIGHT = 600;
    private static final Float ITERATIONS = 100F;
    private Float scale = 250F;
    private static float offset = 0;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MandelbrotSucc frame = new MandelbrotSucc();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public MandelbrotSucc() {
        bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addMouseWheelListener(new MandelbrotSucc.MouseWheelZoomListener());

        Timer timer = new Timer(1000, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(bufferedImage, 0, 0, null);
    }

    public void zoomIn() {
        scale *= 1.2F;
    }

    public void zoomOut() {
        scale /= 1.2F;
    }

    public void renderMandelbrot() {
        long startTime = System.nanoTime();

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int color = calculatePoint((x - WIDTH / 2f) / scale, (y - HEIGHT / 2f) / scale);
                bufferedImage.setRGB(x, y, color);
            }
        }

        long endTime = System.nanoTime();

        log.info("Successively time: " + (endTime - startTime) / 1000000 + " ms");
    }

    public int calculatePoint(float x, float y) {
        float xx = x;
        float yy = y;
        int i = 0;

        for (; i < ITERATIONS; i++) {
            float nx = x * x - y * y + xx;
            float ny = 2 * x * y + yy;
            x = nx;
            y = ny;

            if (x * x + y * y > 4) {
                break;
            }
        }

        if (i == ITERATIONS) {
            return 0x00000000;
        }

        return Color.HSBtoRGB(i / ITERATIONS + offset, 0.5f, 1);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        offset += 0.01f;
        renderMandelbrot();
        repaint();
    }

    private class MouseWheelZoomListener implements MouseWheelListener {
        public void mouseWheelMoved(MouseWheelEvent e) {
            int spins = e.getWheelRotation();
            if (spins < 0) {
                zoomIn();
            } else {
                zoomOut();
            }
            renderMandelbrot();
            repaint();
        }
    }
}
