import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


public class SortingVisualization extends JPanel {
    public static final int DEFAULT_DELAY = 5;
    public static final int SEARCH_DELAY = 100;
    private static final int PLOTTING_SORTING_DELAY = 0;
    public static final int PLOTTING_SEARCH_DELAY = 1;

    private static final int INIT_ARRAY_SIZE = 100;
    private static final int SPACING = 1;
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 500;
    private static int rectWidth = WIDTH / INIT_ARRAY_SIZE - SPACING;
    private static int delay = DEFAULT_DELAY;


    private static final Color DEFUALT_COLOR = new Color(95, 137, 217);
    private static final Color SELECTED_COLOR = new Color(255, 0, 0);
    public static final Color THEME_COLOR = Color.BLACK;
    private static Thread sortingThread, plottingThread;
    private int[] sampleSizes = generateSeries(10, true, 2, 15);


    private final static ArrayList<Long> timeTakenInTest = new ArrayList<>();


    private double[] array;
    private ConcurrentHashMap<Double, Rectangle> rectLabelMap;

    public SortingVisualization() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(THEME_COLOR);
        prepareArray(INIT_ARRAY_SIZE);

    }

    /**
     * this method Creates an array of random numbers
     * and initializes the rectangles
     *
     * @param size the size of the array
     */
    private void prepareArray(int size) {
        array = new double[size];
        rectLabelMap = new ConcurrentHashMap<>();
        generateRandomArray(size);
        setOptimalRectWidth();
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

    /**
     * this method initializes the rectangles
     *
     * @param size the size of the array
     */
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

    /**
     * this method generates a series of sizes
     *
     * @param initial     the initial size of the series
     * @param isGeometric if the series is geometric or arithmetic
     * @param factor      the factor by which the size of the series increases
     * @param size        the size of the series
     * @return an array of sizes
     */
    int[] generateSeries(int initial, boolean isGeometric, double factor, int size) {
        int[] series = new int[size];
        double lastValue = initial;
        if (isGeometric) {
            for (int i = 0; i < size; i++) {
                series[i] = (int) lastValue;
                lastValue = lastValue * factor;
            }
        } else {
            for (int i = 0; i < size; i++) {
                series[i] = (int) (initial + i * factor);
            }
        }
        printArray(series);
        return series;


    }

    void printArray(int[] arr) {
        for (int j : arr) {
            System.out.print(j + " ");
        }
        System.out.println();
    }

    /**
     * this method sets the optimal width of the rectangles
     */
    private void setOptimalRectWidth() {
        rectWidth = Math.max((WIDTH / array.length - SPACING), 1);
    }

    /**
     * this method starts the algorithm
     *
     * @param algorithm            the algorithm to be run
     * @param isPlottingGrowthRate if the algorithm is to be tested
     */
    public void start(String algorithm, boolean isPlottingGrowthRate) {
        fineTuneDelay(algorithm, isPlottingGrowthRate);
        fineTuneSampleSize(algorithm, isPlottingGrowthRate);
        if (isPlottingGrowthRate) {
            runFor(runTestFor(), algorithm);
        } else {
            runFor(1, algorithm);
        }


    }

    int runTestFor() {
        return sampleSizes.length;
    }

    void fineTuneDelay(String algorithm, boolean isPlottingGrowthRate) {
        if (isPlottingGrowthRate) {
            if (algorithm.equals("Linear Search") || algorithm.equals("Binary Search")) {
                setDelay(PLOTTING_SEARCH_DELAY);
            } else {
                setDelay(PLOTTING_SORTING_DELAY);
            }
        } else {
            setDelay(ControlPanel.delaySlider.getValue());
            if (algorithm.equals(("Linear Search")) || algorithm.equals("Binary Search"))
                setDelay(SEARCH_DELAY);

        }

    }

    void fineTuneSampleSize(String algorithm, boolean isPlottingGrowthRate) {
        if (isPlottingGrowthRate) {
            switch (algorithm) {
                case "Bubble Sort":
                case "Selection Sort":
                case "Insertion Sort":
                    sampleSizes = generateSeries(100, false, 64, 20);
                    break;
                case "Merge Sort":
                case "Quick Sort":
                    sampleSizes = generateSeries(100, false, 128, 45);
                    break;
                case "Linear Search":
                    sampleSizes = generateSeries(100, false, 2, 30);
                    break;
                case "Binary Search":
                    sampleSizes = generateSeries(100, false, 1024, 60);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting algorithm");
            }
            prepareArray(sampleSizes[0]);
        } else {
            prepareArray(INIT_ARRAY_SIZE);
        }
    }

    /**
     * gets the algorithm to be run and the size of the array
     *
     * @param algorithm the algorithm to be run
     * @param size      the size of the array
     */
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
                linearSearch(size);
                break;
            case "Binary Search":
                binarySearch(size);
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting algorithm");
        }

    }

    /**
     * this method runs the algorithm for a given number of times
     *
     * @param times     the number of times the algorithm is to be run
     * @param algorithm the algorithm to be run
     */
    private void runFor(int times, String algorithm) {
        if (times == 1) {
            setOptimalRectWidth();
            sortingThread = new Thread(() -> threadOperation(algorithm, true));
            sortingThread.start();
        } else {
            plottingThread = new Thread(() -> {
                for (int i = 0; i < times; i++) {

                    if (i != 0) {
                        prepareArray(sampleSizes[i]);
                    }
                    sortingThread = new Thread(() -> threadOperation(algorithm, false));
                    sortingThread.start();

                    try {
                        sortingThread.join();
                    } catch (InterruptedException e) {
                        e.fillInStackTrace();
                    }

                    //rectWidth = rectWidth == 1 ? rectWidth : rectWidth / 2;
                    setOptimalRectWidth();
                    if (Thread.currentThread().getId() != plottingThread.getId()) {
                        return;
                    }
                }


                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("Runtime Growth of " + algorithm + " Algorithm");
                    frame.setContentPane(createChartPanel(algorithm));
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                });


            });
            plottingThread.start();


        }
    }

    /**
     * this method runs the algorithm in a separate thread
     *
     * @param algorithm the algorithm to be run
     * @param show      if the time taken is to be shown
     */
    void threadOperation(String algorithm, boolean show) {

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

        System.out.println("Array sorted: " + isArraySorted(array));

        if (show)
            JOptionPane.showMessageDialog(null, "Time taken: " + (end - start) + " milliseconds");

    }

    /**
     * this method creates a JPanel containing the chart of time Complexity of the algorithm
     *
     * @param algorithm the algorithm to be run
     * @return a JPanel containing the chart
     */
    private JPanel createChartPanel(String algorithm) {
        // Create a dataset
        XYSeriesCollection dataset = new XYSeriesCollection();

        // First series
        XYSeries actualRuntime = new XYSeries(algorithm + " Algorithm Actual Runtime");
        // Assuming you have two arrays x[] and y[]
        int[] x = sampleSizes; // Your X data
        int[] y = new int[timeTakenInTest.size()]; // Your Y data for the first series

        for (int i = 0; i < timeTakenInTest.size(); i++) {
            y[i] = Math.toIntExact(timeTakenInTest.get(i));
        }

        // Populate the first series with data
        try {
            for (int i = 0; i < x.length; i++) {
                actualRuntime.add(x[i], y[i]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }

        dataset.addSeries(actualRuntime);

        // Second series
        XYSeries theoreticalRuntime;
        // Populate the second series with data
        // For now let's hard code it to merge sort (nlogn)

        double timeAverage = average(y, "n");
        switch (algorithm) {
            case "Bubble Sort":
            case "Selection Sort":
            case "Insertion Sort":
                theoreticalRuntime = new XYSeries(algorithm + " Algorithm Theoretical Runtime " + "(n^2)");
                for (int j : x) {
                    double proportion = timeAverage / average(x, "n^2");
                    theoreticalRuntime.add(j, (int) (proportion * j * j));
                }
                break;
            case "Merge Sort":
            case "Quick Sort":
                theoreticalRuntime = new XYSeries(algorithm + " Algorithm Theoretical Runtime " + "(nlogn)");
                for (int j : x) {
                    double proportion = timeAverage / average(x, "nlogn");
                    theoreticalRuntime.add(j, (int) (proportion * j * Math.log(j)));
                }
                break;
            case "Linear Search":
                theoreticalRuntime = new XYSeries(algorithm + " Algorithm Theoretical Runtime " + "(n)");
                for (int j : x) {
                    double proportion = timeAverage / average(x, "n");
                    theoreticalRuntime.add(j, (int) (proportion * j));
                }
                break;
            case "Binary Search":
                theoreticalRuntime = new XYSeries(algorithm + " Algorithm Theoretical Runtime " + "(logn)");
                for (int j : x) {
                    double proportion = timeAverage / average(x, "logn");
                    theoreticalRuntime.add(j, (int) (proportion * Math.log(j)));
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting algorithm");
        }


        dataset.addSeries(theoreticalRuntime);

        // Create a chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Size VS. Time Plot", // Chart title
                "Sample Size", // X-Axis Label
                "Time Taken", // Y-Axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL,
                true, // Show legend
                true,
                false
        );

        // Customize the renderer
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED); // First series color
        renderer.setSeriesPaint(1, Color.BLUE); // Second series color
        plot.setRenderer(renderer);

        timeTakenInTest.clear();

        return new ChartPanel(chart);
    }

    /**
     * this method calculates the average of an array
     *
     * @param array the array to be averaged
     * @param type  the type of the array
     * @return the average of the array
     */
    private static double average(int[] array, String type) {
        int sum = 0;
        for (int l : array) {
            // types are "n", "n^2", "nlogn", "logn"
            switch (type) {
                case "n":
                    sum += l;
                    break;
                case "n^2":
                    sum += l * l;
                    break;
                case "nlogn":
                    sum += (int) (l * Math.log(l));
                    break;
                case "logn":
                    sum += (int) (Math.log(l));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid type");
            }
        }

        return (double) sum / array.length;
    }

    private void binarySearch(int size) {
        Arrays.sort(array);
        initializeRectangles(size);
        repaint();
        pause();
        Random rand = new Random();
        int index = rand.nextInt(size - 1);
        double key = array[index];
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            rectLabelMap.get(array[mid]).color = Color.RED;
            repaint();
            pause();
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

    private void linearSearch(int size) {
        try {
            pause();
            Random rand = new Random();
            int index = rand.nextInt(size) - 1;
            double key = array[index];
            for (int i = 0; i < size; i++) {
                rectLabelMap.get(array[i]).color = SELECTED_COLOR;
                repaint();
                pause();
                if (array[i] == key) {
                    rectLabelMap.get(array[i]).color = Color.GREEN;
                    repaint();
                    break;
                }
                repaint();
                pause();
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {

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
                    if (i != minIndex)
                        rectLabelMap.get(array[minIndex]).color = DEFUALT_COLOR;
                    minIndex = j;
                    rectLabelMap.get(array[minIndex]).color = Color.yellow;
                    repaint();
                }
                pause();
                if (j != minIndex)
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
            } else {
                array[k] = R[j];
                j++;
            }
            rectLabelMap.get(array[k]).color = SELECTED_COLOR;
            rectLabelMap.get(array[k]).x = k * (rectWidth + SPACING);
            repaint();
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
//        int tempX = rectLabelMap.get(array[i]).x;
        rectLabelMap.get(array[i]).x = i * (rectWidth + SPACING);
        rectLabelMap.get(array[j]).x = j * (rectWidth + SPACING);
        repaint();
    }


    private void pause() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }
    }

    /**
     * this method paints the rectangles
     *
     * @param g the graphics object
     */
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

    private boolean isArraySorted(double[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    static class Rectangle extends java.awt.Rectangle {
        private Color color;

        Rectangle(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
    }

}



