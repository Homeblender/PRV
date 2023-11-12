package ru.kolch.prv_2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication
public class MandelbrotParal extends JFrame {
    private BufferedImage bufferedImage;
    private final int width = 800;
    private final int height = 600;
    private final float iterations = 100;
    private float scale = 250;
    private float offset = 0;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MandelbrotParal frame = new MandelbrotParal();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public MandelbrotParal() {
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(width, height));

        addMouseWheelListener(new MouseWheelZoomListener());

        Timer timer = new Timer(1000, e -> {
            offset += 0.01f;
            renderMandelbrot();
            repaint();
        });
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

        for (int x = 0; x < width; x++) {
            final int finalX = x;
            threadPool.execute(() -> {
                for (int y = 0; y < height; y++) {
                    int color = calculatePoint((finalX - width / 2f) / scale, (y - height / 2f) / scale);
                    bufferedImage.setRGB(finalX, y, color);
                }
            });
        }

        long endTime = System.nanoTime();

        log.info("Parallel time: " + (endTime - startTime) / 1000000 + " ms");
    }

    public int calculatePoint(float x, float y) {
        float xx = x;
        float yy = y;
        int i = 0;

        for (; i < iterations; i++) {
            float nx = x * x - y * y + xx;
            float ny = 2 * x * y + yy;
            x = nx;
            y = ny;

            if (x * x + y * y > 4) {
                break;
            }
        }

        if (i == iterations) {
            return 0x00000000;
        }

        return Color.HSBtoRGB(i / iterations + offset, 0.5f, 1);
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