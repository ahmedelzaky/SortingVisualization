import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class SortingVisualization extends JPanel {
    private static final int INIT_ARRAY_SIZE = 100;
    private static  int rectWidth = 10;
    private static final int SPACING = 1;
    private static final int WIDTH = INIT_ARRAY_SIZE * (rectWidth + SPACING);
    private static final int HEIGHT = 500;
    private static int delay = 5; // Milliseconds
    private static final Color DEFUALT_COLOR = new Color(95, 137, 217);
    private static final Color SELECTED_COLOR = new Color(255, 0, 0);
    private static Thread sortingThread;

    private static final int[] sampleSizes = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
    private static final int runTestFor = sampleSizes.length;

    private static final ArrayList<Long> timeTakenInTest = new ArrayList<>();


    private double[] array;
    private ConcurrentHashMap<Double, Rectangle> rectLabelMap;

    public SortingVisualization() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        prepareArray(INIT_ARRAY_SIZE);

    }

    private void prepareArray(int size) {
        array = new double[size];
        rectLabelMap = new ConcurrentHashMap<>();
        generateRandomArray(size);
        initializeRectangles(size);
    }

    private void paintSortedColor(int size) {
        for (int i = 0; i < size; i++) {
            rectLabelMap.get(array[i]).color = Color.GREEN;
            pause();
            repaint();
        }
    }

    private void generateRandomArray(int size) {
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextFloat() * 500;
        }
    }

    private void initializeRectangles(int size) {
        for (int i = 0; i < size; i++) {
            rectLabelMap.put(
                    array[i],
                    new Rectangle(i * (rectWidth + SPACING),
                            (int) (HEIGHT - array[i]), rectWidth,
                            (int) array[i]
                    )
            );
        }
    }

    public void start(String algorithm, boolean test) {
        if (test) {
            setDelay(1);
            runFor(runTestFor, algorithm);
        } else {
            runFor(1, algorithm);

        }


    }


    private void initTest() {
        JOptionPane.showMessageDialog(null, "Default delay will be reduced to improve performance" +
                "and speed up the process of plotting the graph");


    }

    private void matchSortingAlgorithm(String algorithm, int size) {
        switch (algorithm) {
            case "Bubble Sort":
                bubbleSort(size);
                break;
            case "Selection Sort":
                selectionSort(size);
                break;
            case "Insertion Sort":
                insertionSort(size);
                break;
            case "Merge Sort":
                mergeSort(size);
                break;
            case "Quick Sort":
                quickSort(size);
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

    private void runFor(int times, String algorithm) {
        if (times == 1) {
            rectWidth = 10;
            sortingThread = new Thread(() -> threadOperation(algorithm));
            sortingThread.start();
        } else {
            Thread plottingThread = new Thread(() -> {
                for (int i = 0; i < times; i++) {

                    if (i != 0) {
                        prepareArray(sampleSizes[i]);
                    }
                    sortingThread = new Thread(() -> threadOperation(algorithm));
                    sortingThread.start();

                    try {
                        sortingThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    rectWidth = rectWidth == 1 ? rectWidth : rectWidth / 2;

                }

                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("Runtime Growth of " + algorithm + " Algorithm");
                    frame.setContentPane(createChartPanel());
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                });


            });
            plottingThread.start();







        }
    }

    void threadOperation(String algorithm) {

        long start = System.currentTimeMillis();
        matchSortingAlgorithm(algorithm, array.length);
        long end = System.currentTimeMillis();
        if (Thread.currentThread().getId() != sortingThread.getId()) {
            return;
        }
        long timeTaken = end - start;
        System.out.println("Time taken: " + timeTaken +
                " milliseconds. Array size:" + array.length);
        timeTakenInTest.add(timeTaken);
        //JOptionPane.showMessageDialog(null, "Time taken: " + (end - start) + " milliseconds");

    }

    private static JPanel createChartPanel() {
        // Create a dataset
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Series Name");

        // Assuming you have two arrays x[] and y[]
        int[] x = sampleSizes; // Your X data
        long[] y = new long[timeTakenInTest.size()] ; // Your Y data

        for (int i = 0; i < timeTakenInTest.size(); i++) {
            y[i] = timeTakenInTest.get(i);
        }

        // Populate the series with data
        for (int i = 0; i < y.length; i++) {
            series.add(x[i], y[i]);
        }

        dataset.addSeries(series);

        // Create a chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "XY Line Chart", // Chart title
                "X-Axis Label", // X-Axis Label
                "Y-Axis Label", // Y-Axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL,
                true, // Show legend
                true,
                false
        );

        return new ChartPanel(chart);
    }


    // Graph plotting method

    private void binarySearch() {
        Arrays.sort(array);
        initializeRectangles(INIT_ARRAY_SIZE);
        repaint();
        pause(100);
        Random rand = new Random();
        int index = rand.nextInt(INIT_ARRAY_SIZE - 1);
        double key = array[index];
        int low = 0;
        int high = INIT_ARRAY_SIZE - 1;
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
    }

    private void linearSearch() {
        pause();
        Random rand = new Random();
        int index = rand.nextInt(INIT_ARRAY_SIZE) - 1;
        double key = array[index];
        for (int i = 0; i < INIT_ARRAY_SIZE; i++) {
            rectLabelMap.get(array[i]).color = SELECTED_COLOR;
            repaint();
            pause(100);
            if (array[i] == key) {
                rectLabelMap.get(array[i]).color = Color.GREEN;
                repaint();
                break;
            }
            rectLabelMap.get(array[i]).color = DEFUALT_COLOR;
            repaint();
            pause();
        }
    }


    public void bubbleSort(int size) {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    swap(j, j + 1);
                    repaint();
                    pause();
                    rectLabelMap.get(array[j]).color = DEFUALT_COLOR;
                    rectLabelMap.get(array[j + 1]).color = DEFUALT_COLOR;
                }
            }
        }
        paintSortedColor(size);
    }

    public void selectionSort(int size) {
        for (int i = 0; i < size - 1; i++) {
            int minIndex = i;
            rectLabelMap.get(array[i]).color = SELECTED_COLOR;
            repaint();
            for (int j = i + 1; j < size; j++) {
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
        paintSortedColor(size);

    }

    public void insertionSort(int size) {

        for (int i = 1; i < size; i++) {
            double key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                rectLabelMap.get(array[j + 1]).color = SELECTED_COLOR;
                rectLabelMap.get(array[j + 1]).x = (j + 1) * (rectWidth + SPACING);
                j--;
                repaint();
                pause();
                rectLabelMap.get(array[j + 1]).color = DEFUALT_COLOR;
            }
            array[j + 1] = key;
            rectLabelMap.get(array[j + 1]).x = (j + 1) * (rectWidth + SPACING);
            repaint();
        }
        paintSortedColor(size);

    }


    public void mergeSort(int size) {
        mergeSort(0, size - 1);
        paintSortedColor(size);
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
                rectLabelMap.get(array[k]).x = k * (rectWidth + SPACING);
                repaint();
            } else {
                array[k] = R[j];
                j++;
                rectLabelMap.get(array[k]).color = SELECTED_COLOR;
                rectLabelMap.get(array[k]).x = k * (rectWidth + SPACING);
                repaint();
            }
            rectLabelMap.get(array[k]).color = SELECTED_COLOR;
            rectLabelMap.get(array[k]).x = k * (rectWidth + SPACING);
            k++;
            repaint();
            pause();
        }
        while (i < n1) {
            array[k] = L[i];
            rectLabelMap.get(array[k]).color = SELECTED_COLOR;
            rectLabelMap.get(array[k]).x = k * (rectWidth + SPACING);
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

    public void quickSort(int size) {
        quickSort(0, size - 1);
        paintSortedColor(size);
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
