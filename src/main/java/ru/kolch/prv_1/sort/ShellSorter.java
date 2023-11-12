package ru.kolch.prv_1.sort;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.kolch.prv_1.event.SortingEvent;
import ru.kolch.prv_1.event.SortingEventListener;
import ru.kolch.prv_1.sort.abs.Sorter;

import java.util.Random;

/**
 * Сортировка Шелла (наследуется от {@link Sorter}).
 */
@Slf4j
public class ShellSorter extends Sorter {
    int[] array;

    @Setter
    private SortingEventListener listener;

    public ShellSorter(int itemCount, Random rnd) {
        super(itemCount, rnd);
    }


    private void fireSortingCompletedEvent(long compares, long swaps, double executionTime) {
        SortingEvent event = new SortingEvent(this, compares, swaps, executionTime);
        listener.sortingCompleted(event);
    }

    @Override
    public void sort() {
        array = getMassive();
        long start = System.nanoTime();
        int n = array.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = array[i];
                int j;
                for (j = i; j >= gap && array[j - gap] > temp; j -= gap) {
                    array[j] = array[j - gap];
                    setSwaps();
                }
                array[j] = temp;
                setCompares();
            }
        }
        long end = System.nanoTime();
        fireSortingCompletedEvent(getCompares(), getSwaps(), ((end - start) / 1000000000.0));
    }

    @Override
    public void run() {
        log.info("shell_started");
        sort();
    }

}