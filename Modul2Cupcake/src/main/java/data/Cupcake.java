package data;

public class Cupcake {

    private String top, bottom, name;
    private int id;
    private double price;

    public Cupcake(double price, String top, String bottom) {
        this.name = bottom + " with " + top;
        this.price = price;
        this.top = top;
        this.bottom = bottom;
    }
    
    public Cupcake(int id, String name, double price, String top, String bottom) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.top = top;
        this.bottom = bottom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
}
