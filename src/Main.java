import javax.swing.*;

public class Main {
    static SortingVisualization sortingVisualization = new SortingVisualization();
    static JFrame frame;
    static ControlPanel controlPanel = new ControlPanel();

    public static void main(String[] args) {
        frame = new JFrame("Sorting Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(controlPanel);
        frame.add(sortingVisualization);
        frame.pack();
        frame.setVisible(true);

    }

    public static void reset() {
        frame.remove(sortingVisualization);
        sortingVisualization = new SortingVisualization();
        frame.add(sortingVisualization);
        frame.revalidate();
        frame.repaint();
    }
}
