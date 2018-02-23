import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 *  This class takes files as input, stores the data in a list. Let users
 *  input the parameters and output the path from the initial city to the goal city.
 */

public class Main {
    private static File connectionFile;
    private static File locationFile;
    private static boolean file1Extracted = false;
    private static boolean file2Extracted = false;
    private static boolean firstRun = true;
    public static String startPoint;
    public static String endPoint;
    public static String currentPoint;
    public static boolean straightDistance = true;
    public static HashMap<String, City> cities = new HashMap<>();
    public static Vector selection = new Vector();
    public static List<String> excludedCities = new ArrayList<>();
    public static LinkedList<City> path;
    public static JFrame frame = new JFrame("Project 1");
    public static JFrame frame2 = new JFrame("Project 1");

    public static void main(String[] args) {
        // let users enter inputs
        readFile();
    }

    private static void readFile() {
        // choose connection and location file
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button1 = new JButton("Select Connection File");
        JButton button2 = new JButton("Select Location File");
        JButton button3 = new JButton("Next");
        JFileChooser fileChooser = new JFileChooser();

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    connectionFile = fileChooser.getSelectedFile();
                    System.out.println(connectionFile.getName());
                    // read files and save information
                    extractInfo(connectionFile, true);
                    file1Extracted = true;
                }
            }
        });
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    locationFile = fileChooser.getSelectedFile();
                    System.out.println(locationFile.getName());
                    // read files and save information
                    extractInfo(locationFile, false);
                    file2Extracted = true;
                }
            }
        });
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (file1Extracted && file2Extracted) {
                    frame.dispose();
                    newWindow();
                }
            }
        });

        // show gui
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.pack();
        frame.setVisible(true);
    }

    private static void newWindow() {
        DrawMap map = new DrawMap();
        // choose a start point and a end point
        JComboBox sComboBox = new JComboBox(selection);
        JComboBox eComboBox = new JComboBox(selection);
        startPoint = String.valueOf(sComboBox.getSelectedItem());
        endPoint = String.valueOf(eComboBox.getSelectedItem());

        sComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startPoint = String.valueOf(sComboBox.getSelectedItem());
                firstRun = true;
                System.out.println(startPoint);
            }
        });
        eComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endPoint = String.valueOf(eComboBox.getSelectedItem());
                firstRun = true;
                System.out.println(endPoint);
            }
        });

        // choose excluded cities
        JList jList = new JList(selection);
        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jList.setVisibleRowCount(4);
        jList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                excludedCities.clear();
                int[] selectedIdx = jList.getSelectedIndices();
                for (int i = 0; i < selectedIdx.length; i++) {
                    excludedCities.add((String)selection.get(selectedIdx[i]));
                }

                map.repaint();
            }
        });

        // add scroller to the list of excluded cities
        JScrollPane listScroller = new JScrollPane();
        listScroller.setViewportView(jList);

        // choose which heuristic
        JRadioButton radio1 = new JRadioButton("Straight Line Distance", true);
        JRadioButton radio2 = new JRadioButton("Fewest Links");
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(radio1);
        radioGroup.add(radio2);
        radio1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                straightDistance = true;
            }
        });
        radio2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                straightDistance = false;
            }
        });

        // calculation
        JButton button1 = new JButton("Calculate");
        JButton button2 = new JButton("Step by Step Calculate");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!startPoint.equals(endPoint)) {
                    System.out.println("calculate");
                    City startCity = cities.get(startPoint);
                    City endCity = cities.get(endPoint);
                    path = (LinkedList<City>) HeuristicFunction.findPath(startCity, endCity, straightDistance);
                    if (path != null) {
                        for (int i = 0; i < path.size(); i++) {
                            City city = path.get(i);
                            System.out.print(city.getName() + " " + city.getCost() + "  ");
                        }
                    }
                    System.out.println();
                    map.repaint();
                    HeuristicFunction.reset();
                } else {
                    JOptionPane.showMessageDialog(frame,"The end point cannot be the start point. Please Try Again.");
                }
            }
        });
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!startPoint.equals(endPoint)) {
                    if (firstRun) {
                        currentPoint = startPoint;
                        firstRun = false;
                        HeuristicFunction.reset();
                    }

                    City endCity = cities.get(endPoint);
                    City currentCity = cities.get(currentPoint);

                    if (!currentPoint.equals(endPoint)) {
                        path = (LinkedList<City>) HeuristicFunction.findNext(currentCity, endCity, straightDistance);
                        currentPoint = path.getLast().getName();
                        if (path != null) {
                            for (int i = 0; i < path.size(); i++) {
                                City city = path.get(i);
                                System.out.print(city.getName() + " " + city.getCost() + "  ");
                            }
                        }
                        System.out.println();
                        map.repaint();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,"The end point cannot be the start point. Please Try Again.");
                }
            }
        });

        // organize layout
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(sComboBox);
        jPanel.add(eComboBox);
        jPanel.add(listScroller);
        jPanel.add(radio1);
        jPanel.add(radio2);
        jPanel.add(button1);
        jPanel.add(button2);



        // show gui
        frame2.setPreferredSize(new Dimension(1000, 1300));
        frame2.add(jPanel);
        frame2.add(map);
        frame2.setLayout(new GridLayout(8, 1));
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.pack();
        frame2.setVisible(true);
    }

    private static void extractInfo(File file, boolean isConnection) {
        String line;
        City city;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                line = reader.readLine();
                // keep reading until "END"
                if (!line.equals("END")) {
                    String[] strings = line.split("[ ]+");
                    // check whether the city exists, create a new one if not
                    city = getCity(strings[0]);

                    if (isConnection) {
                        // save the connections of current city
                        for (int i = 0; i < Integer.parseInt(strings[1]); i++) {
                            city.addConnections(strings[i+2]);
                        }
                    } else {
                        // save the location of current city
                        city.setLocation(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                    }

                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame,"File path is incorrect. Please Try Again.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,"File path is incorrect. Please Try Again.");
        }
    }

    private static City getCity(String name) {
        City city;
        if (cities.containsKey(name)) {
            city = cities.get(name);
        } else {
            city = new City(name);
            cities.put(name, city);
            selection.add(name);
        }
        return city;
    }
}