package ru.nsu.vbalashov2.igc.paint.tools;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import ru.nsu.vbalashov2.igc.paint.tools.events.ColorEvent;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Line implements Tool {

    private Color color = Color.BLACK;
    private Point start = new Point(0, 0);
    private boolean startSelected = false;

    public Line(EventBus eventBus) {
        eventBus.register(this);
    }

    @Override
    public void use(BufferedImage image, int x, int y) {
        if (!startSelected) {
            start = new Point(x, y);
            startSelected = true;
        } else {
            drawBresenhamLine(start.x, start.y, x, y, image, color);
            startSelected = false;
        }
    }

    private static void drawBresenhamLine(int xStart, int yStart, int xEnd, int yEnd, BufferedImage image, Color color) {
        int dx = xEnd - xStart;
        int dy = yEnd - yStart;

        int incrementX = sign(dx);
        int incrementY = sign(dy);

        dx = Math.abs(dx);
        dy = Math.abs(dy);

        int stepX;
        int stepY;
        int dShort;
        int dLong;

        if (dx > dy) {
            stepX = incrementX;
            stepY = 0;
            dShort = dy;
            dLong = dx;
        } else {
            stepX = 0;
            stepY = incrementY;
            dShort = dx;
            dLong = dy;
        }

        int x = xStart;
        int y = yStart;
        int err = -dLong;

        if (inBounds(image, x, y)) {
            image.setRGB(x, y, color.getRGB());
        }

        for (int i = 0; i < dLong; ++i) {
            err += 2 * dShort;
            if (err > 0) {
                err -= 2 * dLong;
                x += incrementX;
                y += incrementY;
            } else {
                x += stepX;
                y += stepY;
            }
            if (!inBounds(image, x, y)) {
                continue;
            }
            image.setRGB(x, y, color.getRGB());
        }
    }

    private static int sign(int x) {
        return Integer.compare(x, 0);
    }

    private static boolean inBounds(BufferedImage image, int x, int y) {
        return 0 <= x && x <= image.getWidth() &&
                0 <= y && y <= image.getHeight();
    }

    public void drawLine(int xStart, int yStart, int xEnd, int yEnd, BufferedImage image, Color color) {
        drawBresenhamLine(xStart, yStart, xEnd, yEnd, image, color);
    }

    @Subscribe
    private void handleColorEvent(ColorEvent colorEvent) {
        color = colorEvent.color();
    }

    @Subscribe
    private void handlePaintToolEvent(PaintTool paintTool) {
        startSelected = false;
    }
}
