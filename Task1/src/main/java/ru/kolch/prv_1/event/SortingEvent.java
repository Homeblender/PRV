package ru.kolch.prv_1.event;

import java.util.EventObject;

public class SortingEvent extends EventObject {
    private final long compares;
    private final long swaps;
    private final double executionTime;

    public SortingEvent(Object source, long compares, long swaps, double executionTime) {
        super(source);
        this.compares = compares;
        this.swaps = swaps;
        this.executionTime = executionTime;
    }

    public long getCompares() {
        return compares;
    }

    public long getSwaps() {
        return swaps;
    }

    public double getExecutionTime() {
        return executionTime;
    }
}
