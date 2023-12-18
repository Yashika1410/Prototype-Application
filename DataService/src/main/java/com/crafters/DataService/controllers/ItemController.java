package com.crafters.DataService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crafters.DataService.dtos.CreateItemRequestDTO;
import com.crafters.DataService.dtos.ItemResponse;
import com.crafters.DataService.services.Impl.AuthServiceImpl;
import com.crafters.DataService.services.Impl.ItemServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;

/**
 * Controller class for handling item-related operations.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    private AuthServiceImpl authService;
    private ItemServiceImpl itemService;
    /**
     * Creates a new item.
     *
     * @param createItemRequestDTO The DTO containing the information for creating a new item.
     * @param authentication       The authentication object representing the current user.
     * @return The response containing information about the created item.
     */
    @PostMapping("")
    @Operation(summary = "Create new Item",
  security = @SecurityRequirement(name = "bearerAuth"))
    public ItemResponse createItem(@RequestBody CreateItemRequestDTO createItemRequestDTO,Authentication authentication) {
        return itemService.CreateNewItem(authService.getUserId(authentication), createItemRequestDTO);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable String itemId,Authentication authentication) {
        // Call the service to get the item by ID
        ItemResponse itemResponse = itemService.getItemById(authService.getUserId(authentication), itemId);
        return ResponseEntity.ok(itemResponse);
    }

}
