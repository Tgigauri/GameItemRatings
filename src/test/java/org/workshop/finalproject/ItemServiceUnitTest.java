package org.workshop.finalproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.workshop.finalproject.modules.item.service.ItemService;
import org.workshop.finalproject.modules.user.model.Role;
import org.workshop.finalproject.modules.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceUnitTest {

    private ItemService itemService;

    @BeforeEach
    void setup() {
        itemService = new ItemService(null, null, null);
    }


    @Test
    void checkApprovedSeller_withUnapprovedSeller_shouldThrow() {
        User user = User.builder()
                .role(Role.SELLER)
                .approved(false)
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> itemService.checkApprovedSeller(user));

        assertEquals("Seller not approved by admin yet", ex.getMessage());
    }

    @Test
    void checkApprovedSeller_withApprovedSeller_shouldNotThrow() {
        User user = User.builder()
                .role(Role.SELLER)
                .approved(true)
                .build();

        assertDoesNotThrow(() -> itemService.checkApprovedSeller(user));
    }
}
