package ru.kolch.prv_1;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kolch.prv_1.form.MyFrame;

/**
 * Главный класс приложения.
 */
@SpringBootApplication
public class Prv1Application {
    public static void main(String[] args) {
        new MyFrame();
    }


}
