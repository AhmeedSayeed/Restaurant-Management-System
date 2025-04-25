public class Meal {
    private String name;
    private Float price;
    private Float cost;

    public Meal() {
        this.name = "No Name";
        this.price = 0F;
        this.cost = 0F;
    }
    public Meal(String n , Float p , Float c){
        this.name = n;
        this.price = p;
        this.cost = c;
    }

    public void setName(String n)
    {
        name = n;
    }
    public void setPrice(Float p)
    {
        price = p;
    }
    public void setCost(Float c)
    {
        cost = c;
    }

    public String getName()
    {
        return name;
    }
    public Float getPrice()
    {
        return price;
    }
    public Float getCost()
    {
        return cost;
    }

    public void displayMeal(int cnt)
    {
        System.out.printf("%d- %s ............. %.2f$%n",cnt,name,price);
    }
}