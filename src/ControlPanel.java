import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class ControlPanel extends JPanel {

    private static final Color BUTTON_COLOR = new Color(59, 89, 182);
    private static final Color BUTTON_HOVER_COLOR = new Color(89, 119, 212);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private final String growthRateButtonText = "Plot Growth Rate";
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    public static JSlider delaySlider;
    private boolean isTesting = false;


    public ControlPanel() {
        

        setLayout(new BorderLayout(10, 10));
        setBackground(SortingVisualization.THEME_COLOR);

        JPanel sortingButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sortingButtons.setBackground(SortingVisualization.THEME_COLOR);
        String[] sortingAlgorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort",
                "Merge Sort", "Quick Sort"};
        for (String algorithm : sortingAlgorithms) {
            JButton button = createButton(algorithm);
            sortingButtons.add(button);
        }

        JPanel searchingButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchingButtons.setBackground(SortingVisualization.THEME_COLOR);
        String[] searchAlgorithms = {"Linear Search", "Binary Search"};
        for (String algorithm : searchAlgorithms) {
            JButton button = createButton(algorithm);
            searchingButtons.add(button);
        }

        JPanel plotGrowthRate = new JPanel(new FlowLayout(FlowLayout.CENTER));
        plotGrowthRate.setBackground(SortingVisualization.THEME_COLOR);
        JButton test = createButton(growthRateButtonText);
        searchingButtons.add(test);

        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sliderPanel.setBackground(SortingVisualization.THEME_COLOR);
        delaySlider = createDelaySlider();
        sliderPanel.add(new JLabel("Delay:"));
        sliderPanel.add(delaySlider);

        add(sortingButtons, BorderLayout.NORTH);
        add(searchingButtons, BorderLayout.CENTER);
        add(sliderPanel, BorderLayout.SOUTH);
    }

    @Deprecated
    public Choice createDropDown() {


        Choice dropdown = new Choice();
        int[] intArray = {1, 2, 3, 4, 5}; // Your array of integers
        for (int value : intArray) {
            dropdown.add(String.valueOf(value));
        }


        dropdown.addItemListener(e -> System.out.println("Selected: " + dropdown.getSelectedItem()));

        add(dropdown);

        setSize(250, 100);
        setVisible(true);

        return dropdown;
    }

    /**
     * Creates a JButton with the specified text and add action listener to it depending on the text.
     *
     * @param text The text to be displayed on the button.
     * @return JButton with the specified text.
     */
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
                // The sorting visualization is started with the selected algorithm.
                Main.sortingVisualization.start(text, isTesting);
            }
        });
        return button;
    }

    /**
     * Creates a JSlider to control the delay of the sorting visualization.
     *
     * @return JSlider to control the delay of the sorting visualization.
     */
    private JSlider createDelaySlider() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 5);
        slider.setPreferredSize(new Dimension(200, 50));
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setBackground(SortingVisualization.THEME_COLOR);
        slider.setForeground(SortingVisualization.THEME_COLOR);
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
