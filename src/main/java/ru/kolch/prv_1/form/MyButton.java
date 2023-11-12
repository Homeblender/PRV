package ru.kolch.prv_1.form;

import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Класс кнопки.
 */
public class MyButton extends JButton {
    MyButton(String text) {
        super(text);
        this.setText(text);
        this.setPreferredSize(new Dimension(180, 30));
        this.setFont(new Font("Calibri", Font.BOLD, 16));
        this.setEnabled(false);
    }
}