package ru.kolch.prv_1.sort;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.kolch.prv_1.event.SortingEvent;
import ru.kolch.prv_1.event.SortingEventListener;
import ru.kolch.prv_1.sort.abs.Sorter;

import java.util.Random;


/**
 * Быстрая сортировка (наследуется от {@link Sorter}).
 */
@Slf4j
public class QuickSorter extends Sorter {
    int[] array;

    @Setter
    private SortingEventListener listener;

    public QuickSorter(int itemCount, Random rnd) {
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
        quickSort(array, 0, array.length - 1);
        long end = System.nanoTime();
        fireSortingCompletedEvent(getCompares(), getSwaps(), (end - start) / 1000000000.0);
    }

    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(arr, low, high);
            quickSort(arr, low, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                setSwaps();
            }
            setCompares();
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        setSwaps();

        return i + 1;
    }

    @Override
    public void run() {
        log.info("quick_started");
        sort();
    }
}