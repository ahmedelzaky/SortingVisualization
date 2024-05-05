import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;


public class ControlPanel extends JPanel {

    private static final Color BUTTON_COLOR = new Color(59, 89, 182);
    private static final Color BUTTON_HOVER_COLOR = new Color(89, 119, 212);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private final String growthRateButtonText = "Plot Growth Rate";
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private final JSlider delaySlider;
    private boolean isTesting = false;


    public ControlPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.BLACK);

        JPanel sortingButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sortingButtons.setBackground(Color.BLACK);
        String[] sortingAlgorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort",
                "Merge Sort", "Quick Sort"};
        for (String algorithm : sortingAlgorithms) {
            JButton button = createButton(algorithm);
            sortingButtons.add(button);
        }

        JPanel searchingButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchingButtons.setBackground(Color.BLACK);
        String[] searchAlgorithms = {"Linear Search", "Binary Search"};
        for (String algorithm : searchAlgorithms) {
            JButton button = createButton(algorithm);
            searchingButtons.add(button);
        }

        // Adding a TEST button to initiate iterations of sorting.
        JPanel testButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        testButton.setBackground(Color.BLACK);
        JButton test = createButton(growthRateButtonText);
        searchingButtons.add(test);

        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sliderPanel.setBackground(Color.BLACK);
        delaySlider = createDelaySlider();
        sliderPanel.add(new JLabel("Delay:"));
        sliderPanel.add(delaySlider);

        // Adding a dropdown menu for selecting the size of the array.

        add(sortingButtons, BorderLayout.NORTH);
        add(searchingButtons, BorderLayout.CENTER);
        add(sliderPanel, BorderLayout.SOUTH);
    }


    public Choice createDropDown() {


        Choice dropdown = new Choice();
        int[] intArray = {1, 2, 3, 4, 5}; // Your array of integers
        for (int value : intArray) {
            dropdown.add(String.valueOf(value));
        }


        dropdown.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                System.out.println("Selected: " + dropdown.getSelectedItem());
            }
        });

        add(dropdown);

        setSize(250, 100);
        setVisible(true);

        return dropdown;
    }


    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(170, 30));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addMouseListener(Objects.equals(text, growthRateButtonText) ? null : new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        button.addActionListener(e -> {
            Main.reset();
            if (Objects.equals(text, growthRateButtonText)) {
                // Initiating the test.
                if (isTesting) {
                    isTesting = false;
                    enableSlider();
                    button.setBackground(BUTTON_COLOR);
                    Main.sortingVisualization.setDelay(SortingVisualization.DEFAULT_DELAY);
                } else {
                    isTesting = true;
                    disableSlider();
                    button.setBackground(Color.GREEN);
                }

            } else {
                if (text.equals("Linear Search") || text.equals("Binary Search") || isTesting) {
                    Main.controlPanel.disableSlider();

                    if (!isTesting)
                        Main.sortingVisualization.setDelay(100);

                } else {
                    Main.controlPanel.enableSlider();
                }
                Main.sortingVisualization.start(text, isTesting);
            }
        });
        return button;
    }


    private JSlider createDelaySlider() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 5);
        slider.setPreferredSize(new Dimension(200, 50));
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setBackground(Color.BLACK);
        slider.setForeground(Color.WHITE);
        slider.addChangeListener(e -> {
            int delay = slider.getValue();
            Main.sortingVisualization.setDelay(delay);
        });
        slider.setEnabled(false);
        return slider;
    }

    public void disableSlider() {
        delaySlider.setEnabled(false);
    }

    public void enableSlider() {
        delaySlider.setEnabled(true);
    }
}
