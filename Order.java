import java.util.HashMap;
import java.util.Map;

public class Order {
    private Integer orderNumber;
    private Float totalPrice , totalCost;
    private Map<Meal,Integer> meals;

    public Order()
    {
        this.orderNumber = null;
        this.totalPrice = this.totalCost = 0F;
        meals = new HashMap<>();
    }
    public Order(Integer c)
    {
        this.orderNumber = c;
        this.totalPrice = this.totalCost = 0F;
        meals = new HashMap<>();
    }

    public void setcode(Integer c)
    {
        orderNumber = c;
    }
    public void setMeals(Map <Meal,Integer> m)
    {
        meals = m;
        for(Map.Entry<Meal, Integer> i : meals.entrySet())
        {
            totalPrice += i.getKey().getPrice() * i.getValue();
            totalCost += i.getKey().getCost() * i.getValue();
        }
    }

    public void editMeal(Meal m , Integer count)
    {
        totalPrice += m.getPrice() * (count - ((meals.containsKey(m)) ? meals.get(m) : 0));
        totalCost += m.getCost() * (count - ((meals.containsKey(m)) ? meals.get(m) : 0));
        meals.remove(m);
        if(count > 0)
            meals.put(m,count);
    }
    public void deleteMeal(Meal m)
    {
        if(!meals.containsKey(m))
            return;
        totalPrice -= m.getPrice() * meals.get(m);
        totalCost -= m.getCost() * meals.get(m);
        meals.remove(m);
    }

    public Integer getCode()
    {
        return orderNumber;
    }
    public Float getTotalPrice()
    {
        return totalPrice;
    }
    public Map <Meal,Integer> getMeals()
    {
        return meals;
    }
    public Float getTotalProfit()
    {
        return totalPrice - totalCost;
    }
    public Integer getMealNumber(Meal m)
    {
        return ((meals.containsKey(m)) ? meals.get(m) : 0);
    }

    public void displayOrder()
    {
        int cnt = 1;
        System.out.println("Order #" + orderNumber);
        System.out.println("---------------------------------");
        System.out.println("   Num  Price              Meal");
        for(Map.Entry<Meal, Integer> i : meals.entrySet())
        {
            System.out.printf("%d- ",cnt++);
            System.out.printf("x%d   %.2f  ............ %s%n",i.getValue(),i.getKey().getPrice(),i.getKey().getName());
        }
        System.out.printf("Total price = %.2f%n",totalPrice);
        System.out.println("---------------------------------\n");
    }
}