package myFiles;

import java.util.Map;
import java.util.Objects;

public class Order {
    private int orderId;
    private String customerName;
    private Map<String, Integer> items;
    private Map<String, String> specialRequests;
    private String status;

    public Order(int orderId, String customerName, Map<String, Integer> items, Map<String, String> specialRequests) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = items;
        this.specialRequests = specialRequests;
        this.status = "Order Received";
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public String getOrderStatus() {
        return status;
    }

    public void setOrderStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder itemDetails = new StringBuilder("Order ID: " + orderId + "\nStatus: " + status + "\nItems:\n");
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            itemDetails.append("- ").append(entry.getKey()).append(" (Quantity: ").append(entry.getValue()).append(")");
            if (specialRequests.containsKey(entry.getKey())) {
                itemDetails.append(" [Special Request: ").append(specialRequests.get(entry.getKey())).append("]");
            }
            itemDetails.append("\n");
        }
        return itemDetails.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return orderId == order.orderId &&
               Objects.equals(customerName, order.customerName) &&
               Objects.equals(items, order.items) &&
               Objects.equals(specialRequests, order.specialRequests) &&
               Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customerName, items, specialRequests, status);
    }

    public Map<String, String> getSpecialRequests() {
        return specialRequests; // Getter for special requests
    }
}
