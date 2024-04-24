import javax.swing.*;
import java.awt.*;


public class ControlPanel extends JPanel {

    private static final Color BUTTON_COLOR = new Color(59, 89, 182);
    private static final Color BUTTON_HOVER_COLOR = new Color(89, 119, 212);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private final JSlider delaySlider;

    public ControlPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.BLACK);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);
        String[] algorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort",
                "Merge Sort", "Quick Sort", "Linear Search", "Binary Search"};
        for (String algorithm : algorithms) {
            JButton button = createButton(algorithm);
            buttonPanel.add(button);
        }

        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sliderPanel.setBackground(Color.BLACK);
        delaySlider = createDelaySlider();
        sliderPanel.add(new JLabel("Delay:"));
        sliderPanel.add(delaySlider);

        add(buttonPanel, BorderLayout.NORTH);
        add(sliderPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 30));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        button.addActionListener(e -> {
            if (text.equals("Linear Search") || text.equals("Binary Search")) {
                Main.controlPanel.disableSlider();
            } else {
                Main.controlPanel.enableSlider();
            }
            Main.reset();
            Main.sortingVisualization.start(text);
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
