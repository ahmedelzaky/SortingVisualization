import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SortingVisualization extends JPanel {
    private static final int ARRAY_SIZE = 20;
    private static final int RECT_WIDTH = 20;
    private static final int SPACING = 2;
    private static final int WIDTH = (RECT_WIDTH + SPACING) * ARRAY_SIZE;
    private static final int HEIGHT = 400;
    private static final int DELAY = 50; // Milliseconds

    private final double[] array = new double[ARRAY_SIZE];
    private final Map<Double, Rectangle> rectLabelMap = new HashMap<>();

    public SortingVisualization() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        generateRandomArray();
        initializeRectanglesAndLabels();

        // Start sorting
        bubbleSort();
    }

    private void generateRandomArray() {
        Random rand = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = rand.nextFloat() * 300;
        }
    }

    private void initializeRectanglesAndLabels() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            rectLabelMap.put(
                    array[i],
                    new Rectangle(i * (RECT_WIDTH + SPACING),
                            (int) (HEIGHT - array[i]), RECT_WIDTH,
                            (int) array[i]
                    )
            );
        }
    }

    private void bubbleSort() {
        new Thread(() -> {
            for (int i = 0; i < ARRAY_SIZE - 1; i++) {
                for (int j = 0; j < ARRAY_SIZE - i - 1; j++) {
                    if (array[j] > array[j + 1]) {
                        swap(j, j + 1);
                        repaint();
                        pause();
                        rectLabelMap.get(array[j]).color = Color.BLUE;
                        rectLabelMap.get(array[j + 1]).color = Color.BLUE;
                    }
                }
            }
            for (int i = 0; i < ARRAY_SIZE; i++) {
                rectLabelMap.get(array[i]).color = Color.GREEN;
                repaint();
                pause();
            }
        }).start();
    }

    private void swap(int i, int j) {
        double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        rectLabelMap.get(array[i]).color = Color.RED;
//        rectLabelMap.get(array[j]).color = Color.RED;
        int tempX = rectLabelMap.get(array[i]).x;
        rectLabelMap.get(array[i]).x = rectLabelMap.get(array[j]).x;
        rectLabelMap.get(array[j]).x = tempX;
    }


    private void pause() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Rectangle rectangle : rectLabelMap.values()) {
            g2d.setColor(rectangle.color != null ? rectangle.color : Color.BLUE);
            g2d.fill(rectangle);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sorting Visualization");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new SortingVisualization());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static class Rectangle extends java.awt.Rectangle {
        private Color color;

        Rectangle(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
    }

}
