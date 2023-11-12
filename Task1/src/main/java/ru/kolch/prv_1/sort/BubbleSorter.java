package ru.kolch.prv_1.sort;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.kolch.prv_1.event.SortingEvent;
import ru.kolch.prv_1.event.SortingEventListener;
import ru.kolch.prv_1.sort.abs.Sorter;

import java.util.Random;

/**
 * Сортировка пузырьком (наследуется от {@link Sorter}).
 */
@Slf4j
public class BubbleSorter extends Sorter {
    int[] array;

    @Setter
    private SortingEventListener listener;

    public BubbleSorter(int itemCount, Random rnd) {
        super(itemCount, rnd);
    }

    private void fireSortingCompletedEvent(long compares, long swaps, double executionTime) {
        SortingEvent event = new SortingEvent(this, compares, swaps, executionTime);
        listener.sortingCompleted(event);
    }

    @Override
    public void sort() {
        array = getMassive();
        int temp = 0;

        long start = System.nanoTime();

        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[j - 1] > array[j]) {
                    setSwaps();
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
                setCompares();
            }
        }
        long end = System.nanoTime();
        fireSortingCompletedEvent(getCompares(), getSwaps(), (end - start) / 1000000000.0);
    }

    @Override
    public void run() {
        log.info("bubble_started");
        sort();
    }
}