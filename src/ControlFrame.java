import javax.swing.*;
import java.awt.*;

public class ControlFrame extends JPanel {

    public ControlFrame() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Sorting Algorithm:"));
        String[] algorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort"};

        for (String algorithm : algorithms) {
            JButton button = new JButton(algorithm);
            button.addActionListener(e -> {
                setVisible(true);
                Main.reset();
                Main.sortingVisualization.startSort(algorithm);
            });
            panel.add(button);
        }
        setBackground(Color.BLACK);
        add(panel);
        setVisible(true);
    }

}
