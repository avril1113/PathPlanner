import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class DrawMap extends JPanel {
    private HashMap<String, City> cities = new HashMap<>();

    public void paint(Graphics g) {
        setSize(850, 850);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black));
        this.cities = Main.cities;
        // cities and connections
        g.setColor(Color.BLACK);
        for (City city :cities.values()) {
            // city locations
            g.drawString(city.getName(), city.getxCoord() - 8, city.getyCoord() - 8);
            g.fillOval(city.getxCoord() - 4, city.getyCoord() - 4, 8, 8);

            // connections between cities
            List adjacentCities = city.getConnections();
            for (int i = 0; i < adjacentCities.size(); i++) {
                City adjacentCity = Main.cities.get(adjacentCities.get(i));
                g.drawLine(city.getxCoord(), city.getyCoord(), adjacentCity.getxCoord(), adjacentCity.getyCoord());
            }
        }

        // excluded cities
        if (Main.excludedCities != null) {
            g.setColor(Color.ORANGE);
            for (String cityName :Main.excludedCities) {
                City city = cities.get(cityName);
                g.fillOval(city.getxCoord() - 4, city.getyCoord() - 4, 8, 8);
            }
        }

        // path
        if (Main.path != null) {
            City start, end;
            g.setColor(Color.RED);
            for (int i = 0; i < Main.path.size() - 1; i++) {
                start = Main.path.get(i);
                end = Main.path.get(i+1);
                g.fillOval(start.getxCoord() - 4, start.getyCoord() - 4, 8, 8);
                g.fillOval(end.getxCoord() - 4, end.getyCoord() - 4, 8, 8);
                g.drawLine(start.getxCoord(), start.getyCoord(), end.getxCoord(), end.getyCoord());
            }
        }
    }
}
