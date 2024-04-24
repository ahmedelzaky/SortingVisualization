# Sorting Visualization

This application visualizes various sorting algorithms using Swing in Java. It allows users to select different sorting algorithms and visualize their sorting process in real-time.

## Features

- Visualize sorting algorithms: Bubble Sort, Selection Sort, Insertion Sort, Merge Sort, Quick Sort.
- Real-time visualization of sorting process.
- Simple and intuitive user interface.

## Requirements

- Java Development Kit (JDK) 11 or later.
- Swing library.

## How to Run

1. Clone the repository or download the source code.
2. Open the project in your Java IDE.
3. Compile and run the `Main.java` file.
4. Once the application window opens, you can select a sorting algorithm from the control panel and observe its visualization in the main panel.

## Controls

- The control panel allows you to select different sorting algorithms.
- Click on the buttons corresponding to the sorting algorithms to start their visualization.

## Example

```java
public class Main {
    public static void main(String[] args) {
        // Start the application
        javax.swing.SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Sorting Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(new ControlFrame());
        frame.add(new SortingVisualization());
        frame.pack();
        frame.setVisible(true);
    }
}
```

