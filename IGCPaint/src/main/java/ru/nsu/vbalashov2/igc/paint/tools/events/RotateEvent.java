package ru.nsu.vbalashov2.igc.paint.tools.events;

public record RotateEvent(int angle) {
    public int initialAngle() {
        return 0;
    }
}
