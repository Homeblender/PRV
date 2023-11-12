package ru.kolch.prv_1.form;

import lombok.extern.slf4j.Slf4j;
import ru.kolch.prv_1.event.SortingEvent;
import ru.kolch.prv_1.event.SortingEventListener;
import ru.kolch.prv_1.sort.BubbleSorter;
import ru.kolch.prv_1.sort.QuickSorter;
import ru.kolch.prv_1.sort.ShellSorter;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Random;

@Slf4j
public class MyFrame extends JFrame implements SortingEventListener {
    static JList<Integer> shellList = new JList<>();
    static JList<Integer> bubbleList = new JList<>();
    static JList<Integer> quickList = new JList<>();

    private final MyButton button1 = new MyButton("Start sorting");
    private final MyButton button2 = new MyButton(SHOW_VECTOR);
    private final MyButton button3 = new MyButton(SHOW_VECTOR);
    private final MyButton button4 = new MyButton(SHOW_VECTOR);
    private JComboBox<Integer> comboBox;
    private JRadioButton succRb;
    private JRadioButton paralRb;
    private ShellSorter shellSorter;
    private BubbleSorter bubbleSorter;
    private QuickSorter quickSorter;

    static final String COMPARES = "com. = ";
    static final String SWAPS = "sw. = ";
    static final String SECONDS = "sec. = ";
    static final String SHOW_VECTOR = "Show vector";

    private final JLabel comparesLabel1 = new JLabel(COMPARES);
    private final JLabel swapsLabel1 = new JLabel(SWAPS);
    private final JLabel timeLabel1 = new JLabel(SECONDS);
    private final JLabel comparesLabel2 = new JLabel(COMPARES);
    private final JLabel swapsLabel2 = new JLabel(SWAPS);
    private final JLabel timeLabel2 = new JLabel(SECONDS);
    private final JLabel comparesLabel3 = new JLabel(COMPARES);
    private final JLabel swapsLabel3 = new JLabel(SWAPS);
    private final JLabel timeLabel3 = new JLabel(SECONDS);

    private final JTextField comparesField1 = new JTextField(10);
    private final JTextField swapsField1 = new JTextField(10);
    private final JTextField timeField1 = new JTextField(10);
    private final JTextField comparesField2 = new JTextField(10);
    private final JTextField swapsField2 = new JTextField(10);
    private final JTextField timeField2 = new JTextField(10);
    private final JTextField comparesField3 = new JTextField(10);
    private final JTextField swapsField3 = new JTextField(10);
    private final JTextField timeField3 = new JTextField(10);

    JComponent[] tf = {
        comparesField1, swapsField1, timeField1,
        comparesField2, swapsField2, timeField2,
        comparesField3, swapsField3, timeField3,
    };
    JComponent[] labels = {
        comparesLabel1, swapsLabel1, timeLabel1,
        comparesLabel2, swapsLabel2, timeLabel2,
        comparesLabel3, swapsLabel3, timeLabel3,
    };

    public MyFrame() {
        SwingUtilities.invokeLater(() -> {
            JComponent[] components = {
                new MyLabel("Size of each vector:"),
                new MyLabel("Shell sort:"),
                new MyLabel("Bubble sort:"),
                new MyLabel("Quick sort:"),
            };
            JComponent[] buttons = { button1, button2, button3, button4};

            comboBox = new JComboBox<>(new Integer[]{10, 100, 10000, 50000, 100000});
            comboBox.setPreferredSize(new Dimension(180, 50));
            comboBox.setSelectedIndex(-1);

            succRb = new JRadioButton("Successively");
            succRb.setPreferredSize(new Dimension(180, 30));
            succRb.setFont(new Font("Calibri", Font.BOLD, 16));
            succRb.setSelected(true);
            paralRb = new JRadioButton("Parallely");
            paralRb.setPreferredSize(new Dimension(180, 30));
            paralRb.setFont(new Font("Calibri", Font.BOLD, 16));
            ButtonGroup group = new ButtonGroup();
            group.add(succRb);
            group.add(paralRb);

            setConfigure();
            //frame
            JComponent[] panels = {
                new MyPanel(12, 10, 185, 500),
                new MyPanel(209, 10, 185, 500),
                new MyPanel(406, 10, 185, 500),
                new MyPanel(603, 10, 185, 500),
                new MyPanel(12, 520, 776, 60),
            };
            for (int i = 0; i < panels.length - 1; i++) {
                panels[i].add(components[i]);
            }
            panels[0].add(comboBox);
            panels[0].add(succRb);
            panels[0].add(paralRb);



            JScrollPane sp1 = new JScrollPane(shellList);
            sp1.setPreferredSize(new Dimension(180, 300));
            JScrollPane sp2 = new JScrollPane(bubbleList);
            sp2.setPreferredSize(new Dimension(180, 300));
            JScrollPane sp3 = new JScrollPane(quickList);
            sp3.setPreferredSize(new Dimension(180, 300));
            panels[1].add(sp1);
            panels[2].add(sp2);
            panels[3].add(sp3);

            for (int i = 0; i < panels.length - 1; i++) {
                panels[i].add(buttons[i]);
            }
            for (JComponent t : tf) {
                t.setBorder(null);
                t.setOpaque(false);
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i == 0) {
                        panels[j + 1].add(labels[j * 3]);
                        panels[j + 1].add(tf[j * 3]);
                    } else if (i == 1) {
                        panels[j + 1].add(labels[j * 3 + 1]);
                        panels[j + 1].add(tf[j * 3 + 1]);
                    } else {
                        panels[j + 1].add(labels[j * 3 + 2]);
                        panels[j + 1].add(tf[j * 3 + 2]);
                    }
                }
            }

            comboBox.addActionListener(e -> button1.setEnabled(true));
            MyLabel totalTime = new MyLabel("Total time (sec): ");
            panels[4].add(totalTime);

            button1.addActionListener(e -> {
                for (JComponent t : tf) {
                    ((JTextField) t).setText("");
                }
                int itemCount = Integer.parseInt(String.valueOf(comboBox.getSelectedItem()));
                Random rnd2 = new Random();
                int k = rnd2.nextInt(10);
                shellSorter = new ShellSorter(itemCount, new Random(k));
                shellSorter.setListener(this);
                bubbleSorter = new BubbleSorter(itemCount, new Random(k));
                bubbleSorter.setListener(this);
                quickSorter = new QuickSorter(itemCount, new Random(k));
                quickSorter.setListener(this);
                if (succRb.isSelected()) {
                    long start = System.nanoTime();
                    shellSorter.sort();
                    bubbleSorter.sort();
                    quickSorter.sort();
                    long end = System.nanoTime();
                    totalTime.setText("Total time (sec): " + ((end - start) / 1000000000.0));
                } else if (paralRb.isSelected()) {

                    Thread thread1 = new Thread(shellSorter);
                    Thread thread2 = new Thread(bubbleSorter);
                    Thread thread3 = new Thread(quickSorter);
                    long start = System.nanoTime();
                    thread1.start();
                    thread2.start();
                    thread3.start();
                    long end = System.nanoTime();
                    totalTime.setText("Total time (sec): " + ((end - start) / 1000000000.0));
                }
            });

            for (JComponent panel : panels) {
                this.add(panel);
            }
        });
    }

    void setConfigure() {
        this.setTitle("Sorter");
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = 800;
        int windowHeight = 600;
        this.setBounds((screenSize.width - windowWidth) / 2,
                (screenSize.height - windowHeight) / 2, windowWidth, windowHeight);
    }

    void updateSorting1Results(long compares, long swaps, double executionTime) {
        SwingUtilities.invokeLater(() -> {
            comparesField1.setText(String.valueOf(compares));
            swapsField1.setText(String.valueOf(swaps));
            timeField1.setText(String.valueOf(executionTime));
        });
        log.info("shell_stoped");
    }

    void updateSorting2Results(long compares, long swaps, double executionTime) {
        SwingUtilities.invokeLater(() -> {
            comparesField2.setText(String.valueOf(compares));
            swapsField2.setText(String.valueOf(swaps));
            timeField2.setText(String.valueOf(executionTime));
        });
        log.info("bubble_stoped");
    }

    void updateSorting3Results(long compares, long swaps, double executionTime) {
        SwingUtilities.invokeLater(() -> {
            comparesField3.setText(String.valueOf(compares));
            swapsField3.setText(String.valueOf(swaps));
            timeField3.setText(String.valueOf(executionTime));
        });
        log.info("quick_stoped");
    }

    @Override
    public void sortingCompleted(SortingEvent event) {
        if (event.getSource() == shellSorter) {
            updateSorting1Results(event.getCompares(), event.getSwaps(), event.getExecutionTime());
        } else if (event.getSource() == bubbleSorter) {
            updateSorting2Results(event.getCompares(), event.getSwaps(), event.getExecutionTime());
        } else if (event.getSource() == quickSorter) {
            updateSorting3Results(event.getCompares(), event.getSwaps(), event.getExecutionTime());
        }
    }
}
