package ru.kolch.prv_1.sort.abs;

import lombok.Getter;

import java.util.Random;


public abstract class Sorter implements Runnable {

    private final int itemCount;

    @Getter
    private int[] massive;
    private final Random rnd;

    @Getter
    private long compares = 0;

    @Getter
    private long swaps = 0;

    protected Sorter(int itemCount, Random rnd) {
        this.itemCount = itemCount;
        this.rnd = rnd;
        fillArray();
    }

    protected void setCompares() {
        compares++;
    }

    protected void setSwaps() {
        swaps++;
    }

    protected abstract void sort();

    private void fillArray() {
        massive = new int[itemCount];
        for (int i = 0; i < itemCount; i++) {
            massive[i] = rnd.nextInt(itemCount);
        }
    }

}