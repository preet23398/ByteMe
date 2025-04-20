package myFiles;

import java.util.*;

public class Customer extends User {
    private Map<String, Integer> cart;
    private Map<Integer, Order> orderHistory;
    private Map<String, String> specialRequests;
    private static int orderIDCounter = 1;
    

    public Customer(String username, String password) {
        super(username, password, "Customer");
        this.cart = new HashMap<>();
        this.orderHistory = new HashMap<>();
        this.specialRequests = new HashMap<>();
    }

    public String getCustomerName() {
        return username;
    }

    public void addItemToCart(String itemName, int quantity, String request) {
        if (cart.containsKey(itemName)) {
            int currentQuantity = cart.get(itemName);
            cart.put(itemName, currentQuantity + quantity);
            System.out.println(quantity + " of " + itemName + " added to cart. New quantity: " + cart.get(itemName));
        } else {
            cart.put(itemName, quantity);
            System.out.println("Item(s) added to cart.");
        }
        if (!request.isEmpty()) {
            specialRequests.put(itemName, request);
            System.out.println("Special request added.");
        }
    }

    public Order checkout() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Cannot checkout.");
            return null;
        }
        
        int newOrderId = orderIDCounter++;
        Order newOrder = new Order(newOrderId, getCustomerName(), new HashMap<>(cart), new HashMap<>(specialRequests));
        
        orderHistory.put(newOrderId, newOrder);
        clearCart();
    
        return newOrder;
    }

    public void clearCart() {
        cart.clear();
        specialRequests.clear();
        System.out.println("Your cart and special requests have been cleared.");
    }

    public void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("No orders placed yet.");
            return;
        }
        System.out.println("Order History:");
        for (Map.Entry<Integer, Order> entry : orderHistory.entrySet()) {
            Order order = entry.getValue();
            System.out.println(order);
        }
    }

    public Map<String, Integer> getCart() {
        return cart;
    }

    public Map<Integer, Order> getOrderHistory() {
        return orderHistory;
    }

    public Map<String, String> getSpecialRequests() {
        return specialRequests;
    }
}  

