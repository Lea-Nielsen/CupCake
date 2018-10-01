package data;

public class LineItem {

    private Cupcake cupcake;
    private int id, qty;
    private double totalprice;
    private static int lineItemId;

    public LineItem(int qty, Cupcake cupcake) {
        this.cupcake = cupcake;
        this.qty = qty;
        this.totalprice = qty * cupcake.getPrice();
        lineItemId++;
        this.id = lineItemId;
    }
    
    public static int getLineItemId() {
        return lineItemId;
    }

    public static void setLineItemId(int lineItemId) {
        LineItem.lineItemId = lineItemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cupcake getCupcake() {
        return cupcake;
    }

    public void setCupcake(Cupcake cupcake) {
        this.cupcake = cupcake;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }
    
    

}