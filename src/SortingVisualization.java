import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SortingVisualization extends JPanel {
    private static final int ARRAY_SIZE = 290;
    private static final int RECT_WIDTH = 3;
    private static final int SPACING = 1;
    private static final int WIDTH = ARRAY_SIZE * (RECT_WIDTH + SPACING);
    private static final int HEIGHT = 500;
    private static final int DELAY = 1; // Milliseconds

    private final double[] array = new double[ARRAY_SIZE];
    private final Map<Double, Rectangle> rectLabelMap = new HashMap<>();

    public SortingVisualization() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //add padding to left and right
        setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
        setBackground(Color.BLACK);
        generateRandomArray();
        initializeRectanglesAndLabels();
    }

    private void generateRandomArray() {
        Random rand = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = rand.nextFloat() * 500;
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

    public void startSort(String algorithm) {
        switch (algorithm) {
            case "Bubble Sort":
                bubbleSort();
                break;
            case "Selection Sort":
                selectionSort();
                break;
            case "Insertion Sort":
                insertionSort();
                break;
            case "Merge Sort":
                mergeSort();
                break;
            case "Quick Sort":
                quickSort();
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting algorithm");
        }
    }

    public void bubbleSort() {
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

    public void selectionSort() {
        new Thread(() -> {
            for (int i = 0; i < ARRAY_SIZE - 1; i++) {
                int minIndex = i;
                rectLabelMap.get(array[i]).color = Color.RED;
                repaint();
                for (int j = i + 1; j < ARRAY_SIZE; j++) {
                    rectLabelMap.get(array[j]).color = Color.RED;
                    repaint();
                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                    }
                    pause();
                    rectLabelMap.get(array[j]).color = Color.BLUE;
                    repaint();
                }
                swap(i, minIndex);
                repaint();
                rectLabelMap.get(array[i]).color = Color.BLUE;
                rectLabelMap.get(array[minIndex]).color = Color.BLUE;
            }
            for (int i = 0; i < ARRAY_SIZE; i++) {
                rectLabelMap.get(array[i]).color = Color.GREEN;
                repaint();
                pause();
            }
        }).start();
    }

    public void insertionSort() {
        new Thread(() -> {
            for (int i = 1; i < ARRAY_SIZE; i++) {
                double key = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    rectLabelMap.get(array[j + 1]).color = Color.RED;
                    rectLabelMap.get(array[j + 1]).x = (j + 1) * (RECT_WIDTH + SPACING);
                    j--;
                    repaint();
                    pause();
                    rectLabelMap.get(array[j + 1]).color = Color.blue;
                }
                array[j + 1] = key;
                rectLabelMap.get(array[j + 1]).x = (j + 1) * (RECT_WIDTH + SPACING);
                repaint();
            }
            for (int i = 0; i < ARRAY_SIZE; i++) {
                rectLabelMap.get(array[i]).color = Color.GREEN;
                repaint();
                pause();
            }
        }).start();
    }


    public void mergeSort() {
        new Thread(() -> {
            mergeSort(0, ARRAY_SIZE - 1);
            for (int i = 0; i < ARRAY_SIZE; i++) {
                rectLabelMap.get(array[i]).color = Color.GREEN;
                repaint();
                pause();
            }
        }).start();
    }

    private void mergeSort(int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(l, m);
            for (int i = l; i <= m; i++) {
                rectLabelMap.get(array[i]).color = Color.BLUE;
            }
            repaint();
            mergeSort(m + 1, r);
            for (int i = m + 1; i <= r; i++) {
                rectLabelMap.get(array[i]).color = Color.BLUE;
            }
            repaint();
            merge(l, m, r);
        }
    }

    private void merge(int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        double[] L = new double[n1];
        double[] R = new double[n2];
        for (int i = 0; i < n1; i++) {
            L[i] = array[l + i];
            rectLabelMap.get(L[i]).color = Color.RED;
            repaint();
        }
        for (int i = 0; i < n2; i++) {
            R[i] = array[m + 1 + i];
            rectLabelMap.get(R[i]).color = Color.RED;
            repaint();
        }
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
                rectLabelMap.get(array[k]).color = Color.RED;
                rectLabelMap.get(array[k]).x = k * (RECT_WIDTH + SPACING);
                repaint();
            } else {
                array[k] = R[j];
                j++;
                rectLabelMap.get(array[k]).color = Color.RED;
                rectLabelMap.get(array[k]).x = k * (RECT_WIDTH + SPACING);
                repaint();
            }
            rectLabelMap.get(array[k]).color = Color.RED;
            rectLabelMap.get(array[k]).x = k * (RECT_WIDTH + SPACING);
            k++;
            repaint();
            pause();
        }
        while (i < n1) {
            array[k] = L[i];
            rectLabelMap.get(array[k]).color = Color.RED;
            rectLabelMap.get(array[k]).x = k * (RECT_WIDTH + SPACING);
            i++;
            k++;
            repaint();
            pause();
        }
        while (j < n2) {
            array[k] = R[j];
            rectLabelMap.get(array[k]).color = Color.RED;
            j++;
            k++;
            repaint();
            pause();
        }
    }

    public void quickSort() {
        new Thread(() -> {
            quickSort(0, ARRAY_SIZE - 1);
            for (int i = 0; i < ARRAY_SIZE; i++) {
                rectLabelMap.get(array[i]).color = Color.GREEN;
                repaint();
                pause();
            }
        }).start();
    }

    private void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        double pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                swap(i, j);
                repaint();
                pause();
                rectLabelMap.get(array[i]).color = Color.BLUE;
                rectLabelMap.get(array[j]).color = Color.BLUE;
            }
        }
        swap(i + 1, high);
        return i + 1;
    }


    private void swap(int i, int j) {
        double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        rectLabelMap.get(array[i]).color = Color.RED;
        rectLabelMap.get(array[j]).color = Color.RED;
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

    static class Rectangle extends java.awt.Rectangle {
        private Color color;

        Rectangle(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
    }

}
