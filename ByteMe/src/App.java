import myFiles.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class App {
    private static HashMap<String, User> users = new HashMap<>();
    private static ArrayList<Item> menuItems = new ArrayList<>();
    public static List<Order> orderQueue = new ArrayList<>();
    public static List<Order> vipOrderQueue = new ArrayList<>();
    public static List<Order> regularCancelled = new ArrayList<>();
    public static List<Order> vipCancelled = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Byte Me!");

        while (true) {
            System.out.println("Select an option: 1. Login 2. Signup 3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                login(scanner);
            } else if (choice == 2) {
                signup(scanner);
            } else if (choice == 3) {
                System.out.println("Exiting... Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void login(Scanner scanner) {
        System.out.println("Login as: 1. Admin 2. Customer");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();
        String role = roleChoice == 1 ? "Admin" : "Customer";

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.authenticate(username, password, role)) {
            System.out.println("Login successful. Welcome, " + username + "!");
            if (role.equals("Admin")) {
                adminMenu(scanner);
            } else {
                customerMenu(scanner, (Customer) user);
            }
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    private static void signup(Scanner scanner) {
        System.out.println("Signup as: 1. Admin 2. Customer");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();
        String role = roleChoice == 1 ? "Admin" : "Customer";
    
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
    
        User newUser;
        if (role.equals("Admin")) {
            newUser = new Admin(username, password);
        } else {
            newUser = new Customer(username, password);
        }
    
        users.put(username, newUser);
        System.out.println("Signup successful! You can now login.");
    }
    
    private static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Manage Menu");
            System.out.println("2. Manage Orders");
            System.out.println("3. Generate Daily Sales Report");
            System.out.println("4. Logout");
            int adminChoice = scanner.nextInt();
            scanner.nextLine();

            switch (adminChoice) {
                case 1:
                    manageMenu(scanner);
                    break;
                case 2:
                    manageOrders(scanner);
                    break;
                case 3:
                    generateSalesReport(scanner);
                    break;
                case 4:
                    System.out.println("Logging out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void customerMenu(Scanner scanner, Customer customer) {
    while (true) {
        System.out.println("\nCustomer Menu:");
        System.out.println("1. Browse Menu");
        System.out.println("2. Manage Cart");
        System.out.println("3. Order Tracking");
        System.out.println("4. Logout");
        int customerChoice = scanner.nextInt();
        scanner.nextLine();

        switch (customerChoice) {
            case 1:
                browseMenu(scanner);
                break;
            case 2:
                manageCart(scanner, customer);
                break;
            case 3:
                orderTrackingMenu(scanner, customer);
                break;
            case 4:
                System.out.println("Logging out...");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

    private static void manageMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nManage Menu:");
            System.out.println("1. Add New Item");
            System.out.println("2. Update Existing Item");
            System.out.println("3. Remove Item");
            System.out.println("4. Modify Item Price");
            System.out.println("5. Update Item Availability");
            System.out.println("6. Back to Admin Menu");
    
            int menuChoice = scanner.nextInt();
            scanner.nextLine();
    
            switch (menuChoice) {
                case 1:
                    addItem(scanner);
                    break;
                case 2:
                    updateItem(scanner);
                    break;
                case 3:
                    removeItem(scanner);
                    break;
                case 4:
                    modifyPrice(scanner);
                    break;
                case 5:
                    updateAvailability(scanner);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addItem(Scanner scanner) {
        System.out.print("\nEnter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Is the item available? (true/false): ");
        boolean isAvailable = scanner.nextBoolean();
        scanner.nextLine();
    
        Item newItem = new Item(name, price, isAvailable);
        menuItems.add(newItem);
        System.out.println("Item added.");
    }

    private static void updateItem(Scanner scanner) {
        if (menuItems.isEmpty()) {
            System.out.println("The menu is currently empty. No items to update.");
            return;
        }
    
        // Display all items
        System.out.println("\nCurrent Menu Items:");
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i));
        }
    
        // Select an item
        System.out.print("\nEnter the index of the item you wish to update: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
    
        if (itemIndex < 0 || itemIndex >= menuItems.size()) {
            System.out.println("Invalid item number. Returning to admin menu.");
            return;
        }
    
        Item itemToUpdate = menuItems.get(itemIndex);
    
        // Choose attribute to update
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Price");
        System.out.println("3. Availability");
        int updateChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (updateChoice) {
            case 1:
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                itemToUpdate.setName(newName);
                System.out.println("Item name updated successfully.");
                break;
            case 2:
                System.out.print("Enter new price: ");
                double newPrice = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                itemToUpdate.setPrice(newPrice);
                System.out.println("Item price updated successfully.");
                break;
            case 3:
                System.out.print("Is the item available? (true/false): ");
                boolean newAvailability = scanner.nextBoolean();
                scanner.nextLine(); // Consume newline
                itemToUpdate.setAvailable(newAvailability);
                System.out.println("Item availability updated successfully.");
                break;
            default:
                System.out.println("Invalid choice. Returning to admin menu.");
        }
    }
    
    private static void removeItem(Scanner scanner) {
        if (menuItems.isEmpty()) {
            System.out.println("The menu is currently empty. No items to remove.");
            return;
        }
    
        // Display all items
        System.out.println("\nCurrent Menu Items:");
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i));
        }
    
        // Select an item to remove
        System.out.print("Enter the index of the item you wish to remove: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
    
        if (itemIndex < 0 || itemIndex >= menuItems.size()) {
            System.out.println("Invalid item number. Returning to admin menu.");
            return;
        }
    
        // Remove the item from the menu
        menuItems.remove(itemIndex); // Remove the selected item
        System.out.println("Item removed successfully.");
    }
    
    private static void modifyPrice(Scanner scanner) {
        if (menuItems.isEmpty()) {
            System.out.println("The menu is currently empty. No items to modify.");
            return;
        }
    
        // Display all items
        System.out.println("\nCurrent Menu Items:");
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i));
        }
    
        // Select an item to modify
        System.out.print("Enter the index of the item you wish to modify the price for: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
    
        if (itemIndex < 0 || itemIndex >= menuItems.size()) {
            System.out.println("Invalid item number. Returning to admin menu.");
            return;
        }
    
        // Update the price
        System.out.print("Enter the new price for " + menuItems.get(itemIndex).getName() + ": ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
    
        menuItems.get(itemIndex).setPrice(newPrice);
        System.out.println("Price updated successfully for the item.");
    }
    
    private static void updateAvailability(Scanner scanner) {
        if (menuItems.isEmpty()) {
            System.out.println("The menu is currently empty. No items to modify availability.");
            return;
        }
    
        // Display all items
        System.out.println("\nCurrent Menu Items:");
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i));
        }
    
        // Select an item to modify
        System.out.print("Enter the index of the item you wish to update availability for: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
    
        if (itemIndex < 0 || itemIndex >= menuItems.size()) {
            System.out.println("Invalid item number. Returning to admin menu.");
            return;
        }
    
        // Update the availability
        System.out.print("Enter the new availability status for this item (true/false): ");
        boolean newAvailability = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline
    
        menuItems.get(itemIndex).setAvailable(newAvailability);
        System.out.println("Availability updated successfully. ");
    }
    
    public static void browseMenu(Scanner scanner) {
        while (true) {
            if (menuItems.isEmpty()) {
                System.out.println("The menu is currently empty.");
                return;
            }
    
            System.out.println("\nChoose an option:");
            System.out.println("1. View all items");
            System.out.println("2. Search for an item");
            System.out.println("3. Filter by category");
            System.out.println("4. Sort by price");
            System.out.println("5. Go back to main menu");
        
            int choice = scanner.nextInt();
            scanner.nextLine(); 
    
        switch (choice) {
            case 1:
                viewAllItems();
                break;
            case 2:
                searchItem(scanner);
                break;
            case 3:
                filterByPriceOrAvailability(scanner);
                break;
            case 4:
                sortByPrice();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }
    }

    private static void viewAllItems() {
        System.out.println("\nMenu Items:");
        for (Item item : menuItems) {
            System.out.println(item.getName() + " - Rs " + item.getPrice() + " (Available: " + item.isAvailable() + ")");
        }
    }
    
    private static void searchItem(Scanner scanner) {
        System.out.print("\nEnter item name to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        boolean found = false;
    
        for (Item item : menuItems) {
            if (item.getName().toLowerCase().contains(searchTerm)) {
                System.out.println(item.getName() + " - Rs " + item.getPrice() + " (Available: " + item.isAvailable() + ")");
                found = true;
            }
        }
    
        if (!found) {
            System.out.println("No items found matching: " + searchTerm);
        }
    }

    private static void filterByPriceOrAvailability(Scanner scanner) {
        System.out.println("\nChoose filter option:");
        System.out.println("1. Filter by price");
        System.out.println("2. Filter by availability");
        System.out.print("Enter your choice: ");
        int filterChoice = scanner.nextInt();
        scanner.nextLine();
    
        switch (filterChoice) {
            case 1:
                System.out.print("\nEnter price to filter by: ");
                double price = scanner.nextDouble();
                System.out.println("Items at price: Rs " + price);
                boolean priceFound = false;
    
                for (Item item : menuItems) {
                    if (item.getPrice() == price) {
                        System.out.println(item.getName() + " - Rs " + item.getPrice() + " (Available: " + item.isAvailable() + ")");
                        priceFound = true;
                    }
                }
    
                if (!priceFound) {
                    System.out.println("No items found at price: Rs " + price);
                }
                break;
    
            case 2:
                System.out.print("Do you want to filter by availability? (true/false): ");
                String availabilityInput = scanner.nextLine().toLowerCase();
                boolean available = availabilityInput.equals("true");
                System.out.println("Items availability: " + (available ? "Available" : "Not available"));
                boolean availabilityFound = false;
    
                for (Item item : menuItems) {
                    if (item.isAvailable() == available) {
                        System.out.println(item.getName() + " - Rs " + item.getPrice() + " (Available: " + item.isAvailable() + ")");
                        availabilityFound = true;
                    }
                }
    
                if (!availabilityFound) {
                    System.out.println("No items found with availability: " + (available ? "Available" : "Not Available"));
                }
                break;
            
    
            default:
                System.out.println("Invalid filter option. Please try again.");
                break;
        }
    }

    private static void sortByPrice() {
        menuItems.sort(Comparator.comparing(Item::getPrice));
        System.out.println("\nMenu items sorted by price:");
        viewAllItems();
    }

    private static void manageCart(Scanner scanner, Customer customer) {
        while (true) {
            System.out.println("\nManage Cart:");
            System.out.println("1. Add Item to Cart");
            System.out.println("2. Modify Item Quantity");
            System.out.println("3. Remove Item from Cart");
            System.out.println("4. View Total");
            System.out.println("5. Checkout");
            System.out.println("6. Back to Customer Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    System.out.println("\nMenu Items:");
                    int index = 1;
                    for (Item item : menuItems) {
                        System.out.println(index + ". " + item.getName() + " - Rs " + item.getPrice());
                        index++;
                    }
                    System.out.print("\nEnter the index of the item to add to cart: ");
                    int itemIndex = scanner.nextInt();
                    scanner.nextLine();

                    if (itemIndex < 1 || itemIndex > menuItems.size()) {
                        System.out.println("Invalid index. Please try again.");
                        break;
                    }

                    Item selectedItem = menuItems.get(itemIndex - 1);

                    if (selectedItem.isAvailable()) {
                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Any special request for this item? (Leave blank if none): ");
                        String specialRequest = scanner.nextLine();

                        customer.addItemToCart(selectedItem.getName(), quantity, specialRequest);
                    } else {
                        System.out.println(selectedItem.getName() + " cannot be added to cart as it is not available.");
                    }
                    break;
    
                case 2:
                    if (customer.getCart().isEmpty()) {
                        System.out.println("\nYour cart is empty. Cannot modify quantities.");
                    } else {
                        System.out.println("\nCurrent Cart:");
                        int cartIndex = 1; // Start from 1 for user-friendly indexing
                        for (Map.Entry<String, Integer> entry : customer.getCart().entrySet()) {
                            System.out.println(cartIndex + ". " + entry.getKey() + ": " + entry.getValue());
                            cartIndex++;
                        }
                
                        System.out.print("Enter the index of the item you want to modify: ");
                        int itemToModifyIndex = scanner.nextInt();
                        scanner.nextLine();
                
                        if (itemToModifyIndex < 1 || itemToModifyIndex > customer.getCart().size()) {
                            System.out.println("Invalid index. Please try again.");
                            break;
                        }
                
                        String itemToModify = null;
                        cartIndex = 1;
                        for (Map.Entry<String, Integer> entry : customer.getCart().entrySet()) {
                            if (cartIndex == itemToModifyIndex) {
                                itemToModify = entry.getKey();
                                break;
                            }
                            cartIndex++;
                        }
                
                        if (itemToModify == null) {
                            System.out.println("Invalid index. Item not found in cart.");
                        } else {
                            System.out.print("Enter the new quantity for " + itemToModify + ": ");
                            int newQuantity = scanner.nextInt();
                            scanner.nextLine();
                
                            if (newQuantity <= 0) {
                                customer.getCart().remove(itemToModify);
                                System.out.println(itemToModify + " removed from cart.");
                            } else {
                                customer.getCart().put(itemToModify, newQuantity);
                                System.out.println("Quantity updated to " + newQuantity + ".");
                            }
                        }
                    }
                    break;

                case 3:
                    if (customer.getCart().isEmpty()) {
                        System.out.println("Your cart is empty. Cannot remove items.");
                    } else {
                        System.out.println("\nCurrent Cart:");
                        int cartRemoveIndex = 1; // Start from 1 for user-friendly indexing
                        for (Map.Entry<String, Integer> entry : customer.getCart().entrySet()) {
                            System.out.println(cartRemoveIndex + ". " + entry.getKey() + ": " + entry.getValue());
                            cartRemoveIndex++;
                        }

                        System.out.print("Enter the index of the item you want to remove: ");
                        int itemToRemoveIndex = scanner.nextInt();
                        scanner.nextLine();

                        if (itemToRemoveIndex < 1 || itemToRemoveIndex > customer.getCart().size()) {
                            System.out.println("Invalid index. Please try again.");
                            break;
                        }

                        String itemToRemove = null;
                        cartRemoveIndex = 1; // Reset index counter to start from 1
                        for (Map.Entry<String, Integer> entry : customer.getCart().entrySet()) {
                            if (cartRemoveIndex == itemToRemoveIndex) {
                                itemToRemove = entry.getKey();
                                break;
                            }
                        cartRemoveIndex++;
                        }

                        if (itemToRemove != null) {
                            customer.getCart().remove(itemToRemove);
                            System.out.println(itemToRemove + " removed from cart.");
                        } else {
                            System.out.println("Invalid index. Item not found in cart.");
                        }
                    }
                    break;

                case 4:
                    if (customer.getCart().isEmpty()) {
                        System.out.println("Your cart is empty. Total is Rs 0.");
                    } else {
                        double total = 0;
                        for (Map.Entry<String, Integer> entry : customer.getCart().entrySet()) {
                            String itemName = entry.getKey();
                            int itemQuantity = entry.getValue();
                            double itemPrice = menuItems.stream()
                                .filter(item -> item.getName().equals(itemName))
                                .findFirst()
                                .map(Item::getPrice)
                                .orElse(0.0);
                        total += itemPrice * itemQuantity;
                        }
                        System.out.printf("Total price: Rs %.2f\n", total);
                    }
                    break;

                case 5:
                    if (customer.getCart().isEmpty()) {
                        System.out.println("Your cart is empty. Cannot checkout.");
                    } else {
                    Order newOrder = customer.checkout();
                    if (newOrder != null) {
                        System.out.print("Would you like to pay for your order to be considered a VIP order? (yes/no): ");
                        String vipResponse = scanner.nextLine();
                        boolean isVIP = vipResponse.equalsIgnoreCase("yes");
                    if (isVIP) {
                        vipOrderQueue.add(newOrder);
                        System.out.println("VIP Order placed successfully. ");
                    } else {
                        orderQueue.add(newOrder);
                        System.out.println("Regular Order placed successfully. ");
                    }

                    customer.getOrderHistory().put(newOrder.getOrderId(), newOrder);
                    }
                    }
                    break;

                case 6:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private static void orderTrackingMenu(Scanner scanner, Customer customer) {
        while (true) {
            System.out.println("\nOrder Tracking:");
            System.out.println("1. View Order Status");
            System.out.println("2. Cancel Order");
            System.out.println("3. Order History");
            System.out.println("4. Back to Customer Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    viewOrderStatus(customer);
                    break;
                case 2:
                    cancelOrder(scanner, customer);
                    break;
                case 3:
                    customer.viewOrderHistory();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void viewOrderStatus(Customer customer) {
        List<Order> customerOrders = new ArrayList<>(vipOrderQueue);
        customerOrders.addAll(orderQueue);
    
        if (customerOrders.isEmpty()) {
            System.out.println("You have no orders.");
            return;
        }
    
        System.out.println("\nOrder Status:");
        boolean hasOrders = false;
        for (Order order : customerOrders) {
            if (order.getCustomerName().equals(customer.getCustomerName())) {
                System.out.println(order);
                hasOrders = true;
            }
        }
    
        if (!hasOrders) {
            System.out.println("You have no active orders.");
        }
    }
    
    private static void manageOrders(Scanner scanner) {
        while (true) {
            System.out.println("\nOrder Management:");
            System.out.println("1. View Pending Orders");
            System.out.println("2. Update Order Status");
            System.out.println("3. Process Refunds");
            System.out.println("4. Handle Special Requests");
            System.out.println("5. Back to Admin Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    viewPendingOrders();
                    break;
                case 2:
                    updateOrderStatus(scanner);
                    break;
                case 3:
                    processRefunds(scanner);
                    break;
                case 4:
                    handleSpecialRequests();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void viewPendingOrders() {
        if (App.vipOrderQueue.isEmpty() && App.orderQueue.isEmpty()) {
            System.out.println("No pending orders at the moment.");
            return;
        }
    
        System.out.println("Pending Orders:");
    
        // Display VIP orders first
        for (Order order : App.vipOrderQueue) {
            System.out.println("VIP Order from " + order.getCustomerName() + 
                               ". Status: " + order.getOrderStatus() + 
                               " (Order ID: " + order.getOrderId() + ")");
        }
    
        // Display regular orders
        for (Order order : App.orderQueue) {
            System.out.println("Regular Order from " + order.getCustomerName() + 
                               ". Status: " + order.getOrderStatus() + 
                               " (Order ID: " + order.getOrderId() + ")");
        }
    }
    
    private static void cancelOrder(Scanner scanner, Customer customer) {
        List<Order> customerOrders = new ArrayList<>(vipOrderQueue);
        customerOrders.addAll(orderQueue);
        List<Order> userOrders = new ArrayList<>();
    
        for (Order order : customerOrders) {
            if (order.getCustomerName().equals(customer.getCustomerName())) {
                userOrders.add(order);
            }
        }
    
        if (userOrders.isEmpty()) {
            System.out.println("You have no orders to cancel.");
            return;
        }
    
        System.out.println("\nYour current orders:");
        for (int i = 0; i < userOrders.size(); i++) {
            System.out.println((i + 1) + ". " + userOrders.get(i));
        }
    
        System.out.print("Select the order number to cancel: ");
        int orderNumber = scanner.nextInt();
        scanner.nextLine();
    
        if (orderNumber < 1 || orderNumber > userOrders.size()) {
            System.out.println("Invalid order number.");
            return;
        }
    
        Order orderToCancel = userOrders.get(orderNumber - 1);
    
        if (vipOrderQueue.remove(orderToCancel)) {
            vipCancelled.add(orderToCancel);
        } else if (orderQueue.remove(orderToCancel)) {
            regularCancelled.add(orderToCancel);
        }
        
        orderToCancel.setOrderStatus("Refund Initiated");
        System.out.println("Order canceled and status set to 'Refund Initiated'.");
    }
    
    public static boolean removeOrder(Order order) {
        return orderQueue.remove(order); // Returns true if the order was found and removed
    }
    
    private static void updateOrderStatus(Scanner scanner) {
        // Check if there are any orders in the queue (both VIP and regular)
        if (App.vipOrderQueue.isEmpty() && App.orderQueue.isEmpty()) {
            System.out.println("No pending orders to update.");
            return;
        }
    
        // Display current orders with their indices
        System.out.println("\nPending Orders:");
        List<Order> allOrdersList = new ArrayList<>(App.vipOrderQueue); // Start with VIP orders
        allOrdersList.addAll(App.orderQueue); // Add regular orders
    
        for (int i = 0; i < allOrdersList.size(); i++) {
            System.out.println((i + 1) + ". " + allOrdersList.get(i));
        }
    
        // Get the order index from the admin
        System.out.print("Select the order number to update the status (1 to " + allOrdersList.size() + "): ");
        int orderNumber = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character
    
        // Validate the input
        if (orderNumber < 1 || orderNumber > allOrdersList.size()) {
            System.out.println("Invalid order number.");
            return;
        }
    
        Order selectedOrder = allOrdersList.get(orderNumber - 1); // Get the selected order
    
        // Display status options to the admin
        System.out.println("Select the new status for the order:");
        System.out.println("1. Order Received");
        System.out.println("2. Order Being Prepared");
        System.out.println("3. Order Out for Delivery");
        System.out.println("4. Order Delivered and Payment Received");
        System.out.println("5. Refund Initiated");
        System.out.println("6. Refunded");
    
        int statusChoice = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character
    
        // Update the order status based on the choice
        switch (statusChoice) {
            case 1:
                selectedOrder.setOrderStatus("Order Received");
                break;
            case 2:
                selectedOrder.setOrderStatus("Order Being Prepared");
                break;
            case 3:
                selectedOrder.setOrderStatus("Order Out for Delivery");
                break;
            case 4:
                selectedOrder.setOrderStatus("Order Delivered and Payment Received");
                break;
            case 5:
                selectedOrder.setOrderStatus("Refund Initiated");
                break;
            case 6:
                selectedOrder.setOrderStatus("Refunded");
                break;
            default:
                System.out.println("Invalid status choice.");
                return; // Exit the method
        }
    
        // Confirm the update
        System.out.println("Order status updated to: " + selectedOrder.getOrderStatus());
    }
    
    private static void processRefunds(Scanner scanner) {
        if (App.vipOrderQueue.isEmpty() && App.orderQueue.isEmpty() && App.vipCancelled.isEmpty() && App.regularCancelled.isEmpty()) {
            System.out.println("No orders available to process refunds.");
            return;
        }
    
        System.out.println("\nOrders Available for Refund:");
        
        // Collect orders with "Refund Initiated" status from all lists
        List<Order> refundInitiatedOrders = new ArrayList<>();
    
        for (Order order : App.vipOrderQueue) {
            if ("Refund Initiated".equals(order.getOrderStatus())) {
                refundInitiatedOrders.add(order);
            }
        }
        
        for (Order order : App.orderQueue) {
            if ("Refund Initiated".equals(order.getOrderStatus())) {
                refundInitiatedOrders.add(order);
            }
        }
    
        for (Order order : App.vipCancelled) {
            if ("Refund Initiated".equals(order.getOrderStatus())) {
                refundInitiatedOrders.add(order);
            }
        }
    
        for (Order order : App.regularCancelled) {
            if ("Refund Initiated".equals(order.getOrderStatus())) {
                refundInitiatedOrders.add(order);
            }
        }
    
        // Check if there are any "Refund Initiated" orders
        if (refundInitiatedOrders.isEmpty()) {
            System.out.println("No orders with status 'Refund Initiated' found.");
            return;
        }
    
        // Display orders with "Refund Initiated" status
        for (int i = 0; i < refundInitiatedOrders.size(); i++) {
            System.out.println((i + 1) + ". " + refundInitiatedOrders.get(i));
        }
    
        System.out.print("Select the order number to mark as 'Refunded' (1 to " + refundInitiatedOrders.size() + "): ");
        int orderNumber = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character
    
        if (orderNumber < 1 || orderNumber > refundInitiatedOrders.size()) {
            System.out.println("Invalid order number.");
            return;
        }
    
        // Update the selected order status to "Refunded"
        Order selectedOrder = refundInitiatedOrders.get(orderNumber - 1);
        selectedOrder.setOrderStatus("Refunded");
        
        System.out.println("Order status updated to: Refunded");
    }
    
    private static void handleSpecialRequests() {
        System.out.println("\nOrders with Special Requests:");
    
        boolean hasSpecialRequests = false;
        List<Order> allOrdersList = new ArrayList<>(vipOrderQueue);
        allOrdersList.addAll(orderQueue);
    
        for (Order order : allOrdersList) {
            if (!order.getSpecialRequests().isEmpty()) {
                System.out.println(order);
                hasSpecialRequests = true;
            }
        }
    
        if (!hasSpecialRequests) {
            System.out.println("No special requests found in any orders.");
        }
    }
    
    private static void generateSalesReport(Scanner scanner) {
        while (true) {
        System.out.println("\nGenerate Daily Sales Report:");
        System.out.println("1. View Orders of the Day");
        System.out.println("2. View Total Number of Sales");
        System.out.println("3. View Quantity of Items Sold");
        System.out.println("4. Back to Admin Menu");
        
        int reportChoice = scanner.nextInt();
        scanner.nextLine();
    
        switch (reportChoice) {
            case 1:
                viewOrdersOfTheDay();
                break;
            case 2:
                viewTotalSales();
                break;
            case 3:
                viewItemQuantitiesSold();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }}
    }

    private static void viewOrdersOfTheDay() {
        List<Order> ordersOfTheDay = new ArrayList<>();
        
        for (Order order : orderQueue) {
            if (order.getOrderStatus().equals("Order Delivered and Payment Received") || order.getOrderStatus().equals("Refunded")) {
                ordersOfTheDay.add(order);
            }
        }
        
        for (Order order : vipOrderQueue) {
            if (order.getOrderStatus().equals("Order Delivered and Payment Received") || order.getOrderStatus().equals("Refunded")) {
                ordersOfTheDay.add(order);
            }
        }
    
        if (ordersOfTheDay.isEmpty()) {
            System.out.println("No orders delivered or refunded today.");
            return;
        }
    
        System.out.println("\nOrders of the Day:");
        for (Order order : ordersOfTheDay) {
            System.out.println(order);
        }
    }
    
    private static void viewItemQuantitiesSold() {
        Map<String, Integer> itemCounts = new HashMap<>();
    
        for (Order order : orderQueue) {
            if (order.getOrderStatus().equals("Order Delivered and Payment Received")) {
                for (Map.Entry<String, Integer> entry : order.getItems().entrySet()) { 
                    String itemName = entry.getKey();
                    int quantity = entry.getValue();
    
                    itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + quantity);
                }
            }
        }
    
        for (Order order : vipOrderQueue) {
            if (order.getOrderStatus().equals("Order Delivered and Payment Received")) {
                for (Map.Entry<String, Integer> entry : order.getItems().entrySet()) { 
                    String itemName = entry.getKey();
                    int quantity = entry.getValue();
    
                    itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + quantity);
                }
            }
        }
    
        if (itemCounts.isEmpty()) {
            System.out.println("No items sold today.");
            return;
        }
    
        System.out.println("\nQuantity of Items Sold:");
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void viewTotalSales() {
        int deliveredOrdersCount = 0;
    
        for (Order order : orderQueue) {
            if (order.getOrderStatus().equals("Order Delivered and Payment Received")) {
                deliveredOrdersCount++;
            }
        }
    
        for (Order order : vipOrderQueue) {
            if (order.getOrderStatus().equals("Order Delivered and Payment Received")) {
                deliveredOrdersCount++;
            }
        }
    
        System.out.println("Total Number of Delivered Orders: " + deliveredOrdersCount);
    }
     
}
