import myFiles.Customer;
import myFiles.Item;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
public class CustomerTest {
    public Customer customer;
    public Item unavailableItem;
    public CustomerTest(){
        customer = new Customer("test_user", "123");
        unavailableItem = new Item("pizza", 50, false);
        App.menuItems=new ArrayList<>();

        App.menuItems.add(unavailableItem);
    }
    @Test
    public void testCustomer() {
        customer.addItemToCart(unavailableItem,5," ");
        assertFalse(customer.getCart().containsKey(unavailableItem.getName()),"no");
    }
}