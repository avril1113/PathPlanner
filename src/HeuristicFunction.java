import java.util.*;

/**
 *  This class implements the A* algorithm with the straight line distance
 *  and fewest links heuristic. The inputs are a initial state,
 *  a goal state and a heuristic value. The output is a list containing
 *  the path from the initial state the goal state.
 */

public class HeuristicFunction {
    private static Queue<City> openList = new LinkedList<>();
    private static HashSet<City> closedList = new HashSet<>();
    private static City newCity;
    private static City destination;
    private static float newCost, newHeuristicValue, newFunctionCost;

    public static List<City> findPath(City startCity, City endCity, boolean considerDistance) {
        destination = endCity;
        addStartCity(startCity, considerDistance);

        while (!openList.isEmpty()) {
            newCity = openList.poll();
            closedList.add(newCity);
            // check if the destination found
            if (newCity.getName().equals(destination.getName())) {
                return getPath(newCity);
            }
            getConnectionCost(considerDistance);
        }
        return null;
    }

    public static List<City> findNext(City currentCity, City endCity, boolean considerDistance) {
        destination = endCity;
        // check if it is start point
        if (currentCity.getPreviousCity() == null) {
            addStartCity(currentCity, considerDistance);
        }

        if (openList.isEmpty()) {
            return null;
        } else {
            newCity = openList.poll();
            closedList.add(newCity);

            getConnectionCost(considerDistance);
            return getPath(openList.element());
        }
    }

    public static void reset() {
        openList.clear();
        closedList.clear();
        for (City city : Main.cities.values()) {
            city.resetCost();
        }
    }

    private static void addStartCity(City startCity, boolean considerDistance) {
        // consider straight line distance or fewest links
        if (considerDistance) {
            startCity.setFunctionCost(0, getDistance(startCity, destination));
        } else {
            startCity.setFunctionCost(0, 1);
        }
        openList.offer(startCity);
    }

    private static void getConnectionCost(boolean considerDistance) {
        // search all the connections
        List adjacentCities = newCity.getConnections();
        for (int i = 0; i < adjacentCities.size(); i++) {
            City adjacentCity = Main.cities.get(adjacentCities.get(i));

            // skip if the city is in excluded list
            if (Main.excludedCities.contains(adjacentCity.getName()))
                continue;

            // consider straight line distance or fewest links
            if (considerDistance) {
                newCost = newCity.getCost() + getDistance(newCity, adjacentCity);
                newHeuristicValue = getDistance(adjacentCity, destination);
            } else {
                newCost = newCity.getCost() + 1;
                newHeuristicValue = 1;
            }
            newFunctionCost = newCost + newHeuristicValue;

            // check whether the city is in open list
            if (openList.contains(adjacentCity)) {
                // check whether the path is shorter or having fewer links
                if (newFunctionCost < adjacentCity.getFunctionCost()) {
                    adjacentCity.setPreviousCity(newCity);
                    adjacentCity.setFunctionCost(newCost, newHeuristicValue);
                }
            } else if (!closedList.contains(adjacentCity)) {
                // add to open list if a city has not been visited yet
                adjacentCity.setPreviousCity(newCity);
                adjacentCity.setFunctionCost(newCost, newHeuristicValue);
                openList.offer(adjacentCity);
            }
        }
    }

    // save the path to a linked list
    private static List<City> getPath(City endCity) {
        LinkedList<City> path = new LinkedList<>();
        City currentCity = endCity;
        do {
            path.addFirst(currentCity);
            currentCity = currentCity.getPreviousCity();
        } while (currentCity != null);

        return path;
    }
    
    // calculates the straight line distance 
    private static float getDistance(City city1, City city2) {
        int x = city1.getxCoord() - city2.getxCoord();
        int y = city1.getyCoord() - city2.getyCoord();
        return (float) Math.hypot(x, y);
    }
}