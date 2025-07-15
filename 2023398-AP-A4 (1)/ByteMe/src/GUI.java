import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import myFiles.*;

public class GUI {

    private static ArrayList<Item> menuItems = new ArrayList<>();
    private static ArrayList<Order> pendingOrders = new ArrayList<>();

    public static void main(String[] args) {
        loadMenuFromFile();
        loadPendingOrdersFromFile();

        JFrame frame = new JFrame("Canteen Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));

        JLabel mainHeading = new JLabel("Welcome to ByteMe!");
        mainHeading.setFont(new Font("Arial", Font.BOLD, 24));
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeading = new JLabel("IIITD's first food ordering system :)");
        subHeading.setFont(new Font("Arial", Font.ITALIC, 16));
        subHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        headingPanel.add(mainHeading);
        headingPanel.add(Box.createVerticalStrut(10));
        headingPanel.add(subHeading);
        headingPanel.add(Box.createVerticalStrut(30));

        JPanel buttonPanel = new JPanel();
        JButton browseMenuButton = new JButton("Browse Menu");

        browseMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMenu(frame);
            }
        });

        JButton viewPendingOrdersButton = new JButton("View Pending Orders");

        viewPendingOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPendingOrders(frame);
            }
        });

        buttonPanel.add(browseMenuButton);
        buttonPanel.add(viewPendingOrdersButton);

        frame.add(headingPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void loadMenuFromFile() {
        try {
            File menuFile = new File("src/menu.txt");

            if (!menuFile.exists()) {
                menuFile.createNewFile();
                System.out.println("menu.txt file created because it did not exist.");
            }

            Scanner fileScanner = new Scanner(menuFile);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                boolean isAvailable = Boolean.parseBoolean(parts[2]);

                menuItems.add(new Item(name, price, isAvailable));
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: The menu.txt file was not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: Unable to create or access the file.");
            e.printStackTrace();
        }
    }

    private static void loadPendingOrdersFromFile() {
        try {
            File ordersFile = new File("src/pendingOrders.txt");

            if (!ordersFile.exists()) {
                ordersFile.createNewFile();
                System.out.println("pendingOrders.txt file created because it did not exist.");
            }

            Scanner fileScanner = new Scanner(ordersFile);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                int orderId = Integer.parseInt(parts[0]);
                String customerName = parts[1];
                String orderStatus = parts[2];
                String items = parts[3];
                String specialRequests = parts[4];

                Map<String, Integer> parsedItems = parseItems(items);
                Map<String, String> parsedSpecialRequests = parseSpecialRequests(specialRequests);

                Order order = new Order(orderId, customerName, parsedItems, parsedSpecialRequests);
                order.setOrderStatus(orderStatus);
                pendingOrders.add(order);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: The pendingOrders.txt file was not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: Unable to create or access the file.");
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> parseItems(String itemsString) {
        Map<String, Integer> items = new HashMap<>();
        if (itemsString != null && !itemsString.isEmpty()) {
            String[] itemArray = itemsString.split(",");
            for (String item : itemArray) {
                String[] itemDetails = item.split(":");
                if (itemDetails.length == 2) {
                    try {
                        String itemName = itemDetails[0].trim();
                        int quantity = Integer.parseInt(itemDetails[1].trim());
                        items.put(itemName, quantity);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing item quantity: " + item);
                    }
                }
            }
        }
        return items;
    }

    private static Map<String, String> parseSpecialRequests(String specialRequestsString) {
        Map<String, String> specialRequests = new HashMap<>();
        if (specialRequestsString != null && !specialRequestsString.isEmpty()) {
            String[] requestArray = specialRequestsString.split(",");
            for (String request : requestArray) {
                String[] requestDetails = request.split(":");
                if (requestDetails.length == 2) {
                    specialRequests.put(requestDetails[0].trim(), requestDetails[1].trim());
                }
            }
        }
        return specialRequests;
    }

    private static void showMenu(JFrame parentFrame) {
        JFrame menuFrame = new JFrame("Browse Menu");
        menuFrame.setSize(600, 400);
        menuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Item Name", "Price", "Availability"};
        Object[][] data = new Object[menuItems.size()][3];

        for (int i = 0; i < menuItems.size(); i++) {
            Item item = menuItems.get(i);
            data[i][0] = item.getName();
            data[i][1] = item.getPrice();
            data[i][2] = item.isAvailable() ? "Available" : "Out of Stock";
        }

        JTable menuTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        menuFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel backPanel = new JPanel();
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
            }
        });
        backPanel.add(backButton);
        menuFrame.add(backPanel, BorderLayout.SOUTH);

        menuFrame.setVisible(true);
    }

    private static void showPendingOrders(JFrame parentFrame) {
        JFrame ordersFrame = new JFrame("Pending Orders");
        ordersFrame.setSize(800, 400);
        ordersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Order ID", "Customer Name", "Status", "Items & Quantity", "Special Requests"};
        Object[][] data = new Object[pendingOrders.size()][5];

        for (int i = 0; i < pendingOrders.size(); i++) {
            Order order = pendingOrders.get(i);
            data[i][0] = order.getOrderId();
            data[i][1] = order.getCustomerName();
            data[i][2] = order.getOrderStatus();

            StringBuilder itemsQuantity = new StringBuilder();
            Map<String, Integer> items = order.getItems();
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                itemsQuantity.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
            }
            if (itemsQuantity.length() > 0) {
                itemsQuantity.setLength(itemsQuantity.length() - 2);
            }
            data[i][3] = itemsQuantity.toString();

            StringBuilder specialRequests = new StringBuilder();
            Map<String, String> requests = order.getSpecialRequests();
            for (Map.Entry<String, String> entry : requests.entrySet()) {
                specialRequests.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
            }
            if (specialRequests.length() > 0) {
                specialRequests.setLength(specialRequests.length() - 2);
            }
            data[i][4] = specialRequests.toString();
        }

        JTable ordersTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        ordersFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel backPanel = new JPanel();
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ordersFrame.dispose();
            }
        });
        backPanel.add(backButton);
        ordersFrame.add(backPanel, BorderLayout.SOUTH);

        ordersFrame.setVisible(true);
    }

}
