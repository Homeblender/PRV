package ru.kolch.prv_1.event;

import java.util.EventListener;

public interface SortingEventListener extends EventListener {
    void sortingCompleted(SortingEvent event);
}
