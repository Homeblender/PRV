package ru.kolch.prv_1.form;

import javax.swing.JLabel;
import java.awt.Font;

/**
 * Класс лейбла.
 */
public class MyLabel extends JLabel {
    MyLabel(String text) {
        super(text);
        this.setText(text);
        this.setFont(new Font("Calibri", Font.PLAIN, 16));
    }
}