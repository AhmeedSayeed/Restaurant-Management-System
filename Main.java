import java.util.*;

public class Main {

    static class Pair
    {
        Meal meal;
        int count;
        public Pair(Meal m , int c)
        {
            this.meal = m;
            this.count = c;
        }
    }


    static Scanner in = new Scanner(System.in);
    static float totalRevenue = 0f , totalProfit = 0f;
    static int nxtOrder = 1 , cnt = 1;
    static Map<Integer,Order> orders = new HashMap<>();
    static Vector<Meal> menu = new Vector<>();
    static Map<Meal,Integer> mealsCount = new HashMap<>();
    static TreeMap<Pair,Integer> bestSellers = new TreeMap<>((p1, p2) -> {
        if (p1.count != p2.count)
            return Integer.compare(p2.count, p1.count); // Sort count in descending order
        return 1; // Not important
    });

    static void add(Map<Pair, Integer> map, Pair key) {
        map.put(key, map.getOrDefault(key, 0) + 1);
    }

    static public void viewModes()
    {
        System.out.println("------------- Modes -------------");
        System.out.println("1. Owner Mode.\n" + "2. Cashier Mode.\n" + "3. Exit");
        System.out.println("---------------------------------\n");

        int choice = choose(1,3,"Choose from above options (from %d to %d):%n");

        if(choice == 1)
            viewOwnerMode();
        else if(choice == 2)
            viewCashierMode();
        else
            return;
    }

    static public void viewOwnerMode()
    {
        while(true)
        {

            System.out.println("----------- Owner Mode ----------");
            System.out.println("1. Total Revenue.\n" + "2. Total Profit.\n" + "3. Review Orders.\n" + "4. Best seller meals.\n"
                    + "5. Add new meal to Menu.\n" + "6. Remove meal from Menu.\n" + "7. Edit meal info.\n" + "8. Back.");
            System.out.println("---------------------------------\n");

            int choice = choose(1,8,"Choose from above options (from %d to %d):%n");

            if(choice == 1)
                System.out.printf("Total Revenue : %.2f%n",totalRevenue);
            else if(choice == 2)
                System.out.printf("Total Profit : %.2f%n",totalProfit);
            else if(choice == 3)
            {
                System.out.println("----------- Owner Mode ----------");
                System.out.println("Orders List:\n");
                for(Map.Entry<Integer,Order> i : orders.entrySet())
                    i.getValue().displayOrder();
                if(orders.isEmpty())
                    System.out.println("No orders yet!");
                System.out.println("---------------------------------\n");
            }
            else if(choice == 4)
            {
                System.out.println("------- Best Sellers List -------");
                if(mealsCount.isEmpty())
                    System.out.println("No meals were sold!");
                bestSellers.clear();
                for(Map.Entry<Meal,Integer> i : mealsCount.entrySet())
                    add(bestSellers,new Pair(i.getKey(),i.getValue()));
                int rank = 1;
                for(Pair i : bestSellers.keySet())
                    System.out.printf("%d- %s ............ %d meals%n",rank++,i.meal.getName(),i.count);
                System.out.println("---------------------------------\n");
            }
            else if(choice == 5)
            {
                System.out.println("What is the name of the meal?");
                in.nextLine();
                String mealName = in.nextLine();
                Float price , cost;
                System.out.println("What is the price of the meal?");
                price = in.nextFloat();
                System.out.println("What is the cost of the meal?");
                cost = in.nextFloat();
                Meal meal = new Meal(mealName,price,cost);
                menu.add(meal);
            }
            else if(choice == 6)
            {
                if(menu.isEmpty())
                {
                    System.out.println("Menu is empty!\nOwner must add meals to menu.");
                    continue;
                }
                displayMenu(true);
                Integer mealNumber = choose(1, menu.size()+1,"which meal you want to remove (from %d to %d)?%n");
                if(mealNumber == menu.size()+1)
                    continue;
                menu.remove(mealNumber-1);
            }
            else if(choice == 7)
            {
                if(menu.isEmpty())
                {
                    System.out.println("-------------- Menu -------------");
                    System.out.println("Menu is empty!\nOwner must add meals to menu.");
                    System.out.println("---------------------------------\n");
                    continue;
                }
                displayMenu(true);
                Integer mealNumber = choose(1, menu.size()+1,"which meal you want to edit (from %d to %d)?%n");
                if(mealNumber == menu.size()+1)
                    continue;
                while(true)
                {
                    System.out.println("----------- Owner Mode ----------");
                    System.out.println("What do you want to edit?\n" + "1. Meal name.\n" + "2. Meal price\n" + "3. Meal cost\n" + "4. Back");
                    System.out.println("---------------------------------\n");
                    choice = choose(1,4,"Choose from above options (from %d to %d):%n");
                    if(choice == 1)
                    {
                        System.out.println("What is the new name?");
                        in.nextLine();
                        String s = in.nextLine();
                        menu.get(mealNumber-1).setName(s);
                    }
                    else if(choice == 2)
                    {
                        System.out.println("What is the new price?");
                        Float p = in.nextFloat();
                        menu.get(mealNumber-1).setPrice(p);
                    }
                    else if(choice == 3)
                    {
                        System.out.println("What is the new cost?");
                        Float c = in.nextFloat();
                        menu.get(mealNumber-1).setCost(c);
                    }
                    else
                        break;
                }
            }
            else
                break;
        }
        viewModes();
    }

    static public void viewCashierMode()
    {
        while(true)
        {
            System.out.println("---------- Cashier Mode ---------");
            System.out.println("1. Add Order.\n" + "2. Edit Order.\n" + "3. Delete Order.\n" + "4. Back.");
            System.out.println("---------------------------------\n");
            int choice = choose(1,4,"Choose from above options (from %d to %d):%n");
            if((choice == 2 || choice == 3) && orders.isEmpty())
            {
                System.out.println("No orders yet!\nYou must add order first.");
                continue;
            }
            if(choice == 1)
            {
                if(menu.isEmpty())
                {
                    System.out.println("Menu is empty!\nOwner must add meals to menu.");
                    break;
                }
                Order order = new Order(nxtOrder++);
                Float lstOrderPrice = 0f , lstOrderProfit = 0f;
                while(true)
                {
                    displayMenu(true);
                    choice = choose(1,menu.size()+1,"Select an item from menu (from %d to %d):%n");
                    if(choice != cnt)
                    {
                        int count = choose(1,1000,"How many meal you want?%n");
                        order.editMeal(menu.get(choice-1),count + order.getMealNumber(menu.get(choice-1)));
                        mealsCount.put(menu.get(choice-1),count + ((mealsCount.containsKey(menu.get(choice-1))) ? mealsCount.get(menu.get(choice-1)) : 0));
                        totalRevenue -= lstOrderPrice;
                        totalProfit -= lstOrderProfit;
                        totalRevenue += order.getTotalPrice();
                        totalProfit += order.getTotalProfit();
                        lstOrderPrice = order.getTotalPrice();
                        lstOrderProfit = order.getTotalProfit();
                        orders.put(order.getCode(),order);
                        System.out.println("The current order:");
                        orders.get(nxtOrder-1).displayOrder();
                    }
                    else
                        break;
                }
                viewCashierMode();
            }
            else if(choice == 2)
            {
                while(true)
                {
                    System.out.println("---------- Orders List ----------\n");
                    for(Map.Entry<Integer,Order> i : orders.entrySet())
                        i.getValue().displayOrder();
                    System.out.printf("%d- Back%n",orders.size()+1);
                    System.out.println("---------------------------------\n");
                    int orderCode = choose(1,orders.size()+1,"what is the code of the order (from %d to %d)?%n");
                    if(orderCode == orders.size()+1)
                        break;
                    orders.get(orderCode).displayOrder();
                    totalProfit -= orders.get(orderCode).getTotalProfit();
                    totalRevenue -= orders.get(orderCode).getTotalPrice();
                    System.out.println("---------- Cashier Mode ---------");
                    System.out.println("What do you want?\n" + "1. Add new meal.\n" + "2. Delete Meal.\n"
                            + "3. Edit a number of meal.\n" + "4. Back.");
                    System.out.println("---------------------------------\n");
                    choice = choose(1,4,"Choose from above options (from %d to %d):%n");
                    if(choice == 4)
                        break;
                    if(choice == 1)
                    {
                        displayMenu(false);
                        int mealNumber = choose(1,menu.size(),"Select an item from the menu (from %d to %d):%n") - 1;
                        int count = choose(1,1000,"How many meal you want?%n");
                        orders.get(orderCode).editMeal(menu.get(mealNumber),count + orders.get(orderCode).getMealNumber(menu.get(mealNumber)));
                        mealsCount.put(menu.get(mealNumber),count + ((mealsCount.containsKey(menu.get(mealNumber))) ? mealsCount.get(menu.get(mealNumber)) : 0));
                    }
                    else if(choice == 2)
                    {
                        orders.get(orderCode).displayOrder();
                        int mealNumber = choose(1,orders.get(orderCode).getMeals().size(),"Select an item from the order (from %d to %d):%n") - 1;
                        Meal m = new Meal();
                        int c = 1;
                        for(Map.Entry<Meal,Integer> i : orders.get(orderCode).getMeals().entrySet())
                        {
                            if(mealNumber + 1 == c++)
                            {
                                m = i.getKey();
                                break;
                            }
                        }
                        int lst = mealsCount.get(m);
                        mealsCount.remove(m);
                        mealsCount.put(m , lst - orders.get(orderCode).getMealNumber(menu.get(mealNumber)));
                        if(mealsCount.get(m) <= 0)
                            mealsCount.remove(m);
                        orders.get(orderCode).deleteMeal(menu.get(mealNumber));
                        if(orders.get(orderCode).getMeals().isEmpty())
                            orders.remove(orderCode);
                    }
                    else
                    {
                        orders.get(orderCode).displayOrder();
                        int mealNumber = choose(1,orders.get(orderCode).getMeals().size(),"Select an item from the order (from %d to %d):%n") - 1;
                        Meal m = new Meal();
                        int c = 1;
                        for(Map.Entry<Meal,Integer> i : orders.get(orderCode).getMeals().entrySet())
                        {
                            if(mealNumber + 1 == c++)
                            {
                                m = i.getKey();
                                break;
                            }
                        }
                        int count = choose(1,1000,"How many meal you want?%n");
                        int lst = orders.get(orderCode).getMealNumber(m) , lst2 = mealsCount.get(m);
                        orders.get(orderCode).editMeal(m,count);
                        mealsCount.remove(m);
                        mealsCount.put(m , lst2 - (lst - count));
                    }
                    if(!orders.containsKey(nxtOrder))
                        System.out.println("Order has been deleted!");
                    else
                    {
                        totalProfit += orders.get(orderCode).getTotalProfit();
                        totalRevenue += orders.get(orderCode).getTotalPrice();
                        System.out.println("Order after editing:\n");
                        orders.get(orderCode).displayOrder();
                    }
                }
                viewCashierMode();
            }
            else if(choice == 3)
            {
                System.out.println("---------- Orders List ----------\n");
                for(Map.Entry<Integer,Order> i : orders.entrySet())
                    i.getValue().displayOrder();
                System.out.printf("%d- Back%n",orders.size()+1);
                System.out.println("---------------------------------\n");
                int orderCode = choose(1,orders.size()+1,"what is the code of the order (from %d to %d)?%n");
                if(orderCode == orders.size()+1)
                    break;
                totalProfit -= orders.get(orderCode).getTotalProfit();
                totalRevenue -= orders.get(orderCode).getTotalPrice();
                for(Map.Entry<Meal,Integer> i : orders.get(orderCode).getMeals().entrySet())
                {
                    mealsCount.put(i.getKey() , ((mealsCount.containsKey(i.getKey())) ? mealsCount.get(i.getKey()) : 0) - orders.get(orderCode).getMealNumber(i.getKey()));
                    if(mealsCount.get(i.getKey()) <= 0)
                        mealsCount.remove(i.getKey());
                }
                orders.remove(orderCode);
            }
            else
                break;
        }
        viewModes();
    }

    static public int choose(int st , int en , String s)
    {
        int choice;
        while(true)
        {
            System.out.printf(s,st,en);
            choice = in.nextInt();
            if(choice >= st && choice <= en)
                break;
        }
        return choice;
    }

    static public void displayMenu(Boolean back)
    {
        cnt = 1;
        System.out.println("-------------- Menu -------------");
        for(Meal i : menu)
        {
            i.displayMeal(cnt++);
        }
        if(back == true)
            System.out.printf("%d- Back%n",cnt);
        System.out.println("---------------------------------\n");
    }

    public static void main (String args[]) {
        System.out.println("--- Welcome to our Restaurant ---\n");
        viewModes();
        System.out.println("Thanks for using my system.");
    }
}