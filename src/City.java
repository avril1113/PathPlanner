import java.util.*;

/**
 *  This is a city object class and contains information about the
 *  connections, location and heuristic costs.
 */

public class City {
    private String name;
    private ArrayList<String> connections;
    private int xCoord;
    private int yCoord;
    private City previousCity;
    private float cost;
    private float hValue;
    private float functionCost;

    public City(String name) {
        this.name = name;
        connections = new ArrayList<>();
        previousCity = null;
        cost = 0;
        hValue = 0;
        functionCost = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getConnections() {
        return connections;
    }

    public void addConnections(String city) {
        connections.add(city);
    }

    public int[] getLocation() {
        return new int[]{xCoord, yCoord};
    }

    public void setLocation(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public City getPreviousCity() {
        return previousCity;
    }

    public void setPreviousCity(City previousCity) {
        this.previousCity = previousCity;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getHeuristicEstimate() {
        return hValue;
    }

    public void setHueristiceEstimate(float hValue) {
        this.hValue = hValue;
    }

    public void setFunctionCost(float cost, float hValue) {
        this.cost = cost;
        this.hValue = hValue;
        functionCost = cost + hValue;
    }

    public float getFunctionCost() {
        functionCost = cost + hValue;
        return functionCost;
    }

    public void resetCost() {
        cost = 0;
        hValue = 0;
        functionCost = 0;
        previousCity = null;
    }
}