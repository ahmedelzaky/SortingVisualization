import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SortingVisualization extends JPanel {
    private static final int ARRAY_SIZE = 200;
    private static final int RECT_WIDTH = 5;
    private static final int SPACING = 1;
    private static final int WIDTH = ARRAY_SIZE * (RECT_WIDTH + SPACING);
    private static final int HEIGHT = 500;
    private static int delay = 1; // Milliseconds
    private static final Color DEFUALT_COLOR = Color.BLUE;
    private static final Color SELECTED_COLOR = Color.RED;


    private final double[] array = new double[ARRAY_SIZE];
    private final Map<Double, Rectangle> rectLabelMap = new HashMap<>();

    public SortingVisualization() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        generateRandomArray();
        initializeRectangles();
    }

    private void SortedColor() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            rectLabelMap.get(array[i]).color = Color.GREEN;
            pause();
            repaint();
        }
    }

    private void generateRandomArray() {
        Random rand = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = rand.nextFloat() * 500;
        }
    }

    private void initializeRectangles() {
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

    public void start(String algorithm) {
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
            case "Linear Search":
                linearSearch();
                break;
            case "Binary Search":
                binarySearch();
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting algorithm");
        }
    }

    private void binarySearch() {
        new Thread(() -> {
            Arrays.sort(array);
            initializeRectangles();
            repaint();
            pause(100);
            Random rand = new Random();
            int index = rand.nextInt(ARRAY_SIZE) - 1;
            double key = array[index];
            int low = 0;
            int high = ARRAY_SIZE - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                rectLabelMap.get(array[mid]).color = Color.RED;
                repaint();
                pause(100);
                if (array[mid] == key) {
                    rectLabelMap.get(array[mid]).color = Color.GREEN;
                    repaint();
                    break;
                } else if (array[mid] < key) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }).start();
    }

    private void linearSearch() {
        pause();
        new Thread(() -> {
            Random rand = new Random();
            int index = rand.nextInt(ARRAY_SIZE) - 1;
            double key = array[index];
            for (int i = 0; i < ARRAY_SIZE; i++) {
                rectLabelMap.get(array[i]).color = Color.RED;
                repaint();
                pause(100);
                if (array[i] == key) {
                    rectLabelMap.get(array[i]).color = Color.GREEN;
                    repaint();
                    break;
                }
                rectLabelMap.get(array[i]).color = Color.BLUE;
                repaint();
                pause();
            }
        }).start();
    }


    public void bubbleSort() {
        new Thread(() -> {
            for (int i = 0; i < ARRAY_SIZE - 1; i++) {
                for (int j = 0; j < ARRAY_SIZE - i - 1; j++) {
                    if (array[j] > array[j + 1]) {
                        swap(j, j + 1);
                        repaint();
                        pause();
                        rectLabelMap.get(array[j]).color = DEFUALT_COLOR;
                        rectLabelMap.get(array[j + 1]).color = DEFUALT_COLOR;
                    }
                }
            }
            SortedColor();
        }).start();
    }

    public void selectionSort() {
        new Thread(() -> {
            for (int i = 0; i < ARRAY_SIZE - 1; i++) {
                int minIndex = i;
                rectLabelMap.get(array[i]).color = SELECTED_COLOR;
                repaint();
                for (int j = i + 1; j < ARRAY_SIZE; j++) {
                    rectLabelMap.get(array[j]).color = SELECTED_COLOR;
                    repaint();
                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                    }
                    pause();
                    rectLabelMap.get(array[j]).color = DEFUALT_COLOR;
                    repaint();
                }
                swap(i, minIndex);
                repaint();
                rectLabelMap.get(array[i]).color = DEFUALT_COLOR;
                rectLabelMap.get(array[minIndex]).color = DEFUALT_COLOR;
            }
            SortedColor();
        }).start();
    }

    public void insertionSort() {
        new Thread(() -> {
            for (int i = 1; i < ARRAY_SIZE; i++) {
                double key = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    rectLabelMap.get(array[j + 1]).color = SELECTED_COLOR;
                    rectLabelMap.get(array[j + 1]).x = (j + 1) * (RECT_WIDTH + SPACING);
                    j--;
                    repaint();
                    pause();
                    rectLabelMap.get(array[j + 1]).color = DEFUALT_COLOR;
                }
                array[j + 1] = key;
                rectLabelMap.get(array[j + 1]).x = (j + 1) * (RECT_WIDTH + SPACING);
                repaint();
            }
            SortedColor();
        }).start();
    }


    public void mergeSort() {
        new Thread(() -> {
            mergeSort(0, ARRAY_SIZE - 1);
            SortedColor();
        }).start();
    }

    private void mergeSort(int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(l, m);
            for (int i = l; i <= m; i++) {
                rectLabelMap.get(array[i]).color = DEFUALT_COLOR;
            }
            repaint();
            mergeSort(m + 1, r);
            for (int i = m + 1; i <= r; i++) {
                rectLabelMap.get(array[i]).color = DEFUALT_COLOR;
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
            rectLabelMap.get(L[i]).color = SELECTED_COLOR;
            repaint();
        }
        for (int i = 0; i < n2; i++) {
            R[i] = array[m + 1 + i];
            rectLabelMap.get(R[i]).color = SELECTED_COLOR;
            repaint();
        }
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
                rectLabelMap.get(array[k]).color = SELECTED_COLOR;
                rectLabelMap.get(array[k]).x = k * (RECT_WIDTH + SPACING);
                repaint();
            } else {
                array[k] = R[j];
                j++;
                rectLabelMap.get(array[k]).color = SELECTED_COLOR;
                rectLabelMap.get(array[k]).x = k * (RECT_WIDTH + SPACING);
                repaint();
            }
            rectLabelMap.get(array[k]).color = SELECTED_COLOR;
            rectLabelMap.get(array[k]).x = k * (RECT_WIDTH + SPACING);
            k++;
            repaint();
            pause();
        }
        while (i < n1) {
            array[k] = L[i];
            rectLabelMap.get(array[k]).color = SELECTED_COLOR;
            rectLabelMap.get(array[k]).x = k * (RECT_WIDTH + SPACING);
            i++;
            k++;
            repaint();
            pause();
        }
        while (j < n2) {
            array[k] = R[j];
            rectLabelMap.get(array[k]).color = SELECTED_COLOR;
            j++;
            k++;
            repaint();
            pause();
        }
    }

    public void quickSort() {
        new Thread(() -> {
            quickSort(0, ARRAY_SIZE - 1);
            SortedColor();
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
        rectLabelMap.get(array[high]).color = Color.yellow;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            pause();
            if (i >= 0 && j >= 0) {
                rectLabelMap.get(array[i]).color = DEFUALT_COLOR;
                rectLabelMap.get(array[j]).color = DEFUALT_COLOR;
            }
            repaint();
            if (array[j] < pivot) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        rectLabelMap.get(array[i + 1]).color = DEFUALT_COLOR;
        rectLabelMap.get(array[high]).color = DEFUALT_COLOR;
        return i + 1;
    }


    private void swap(int i, int j) {
        double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        rectLabelMap.get(array[i]).color = SELECTED_COLOR;
        rectLabelMap.get(array[j]).color = SELECTED_COLOR;
        int tempX = rectLabelMap.get(array[i]).x;
        rectLabelMap.get(array[i]).x = rectLabelMap.get(array[j]).x;
        rectLabelMap.get(array[j]).x = tempX;
        repaint();
    }


    private void pause() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }
    }

    private void pause(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Rectangle rectangle : rectLabelMap.values()) {
            g2d.setColor(rectangle.color != null ? rectangle.color : DEFUALT_COLOR);
            g2d.fill(rectangle);
        }
    }

    public void setDelay(int delay) {
        SortingVisualization.delay = delay;
        System.out.println("Delay set to " + delay + " milliseconds");
    }

    static class Rectangle extends java.awt.Rectangle {
        private Color color;

        Rectangle(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
    }

}
