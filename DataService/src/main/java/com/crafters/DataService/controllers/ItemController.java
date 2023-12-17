package com.crafters.DataService.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crafters.DataService.dtos.CreateItemRequestDTO;
import com.crafters.DataService.dtos.ItemResponse;
import com.crafters.DataService.services.Impl.AuthServiceImpl;
import com.crafters.DataService.services.Impl.ItemServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    private AuthServiceImpl authService;
    private ItemServiceImpl itemService;

    @PostMapping("")
    @Operation(summary = "Create new Item",
  security = @SecurityRequirement(name = "bearerAuth"))
    public ItemResponse createItem(@RequestBody CreateItemRequestDTO createItemRequestDTO,Authentication authentication) {
        return itemService.CreateNewItem(authService.getUserId(authentication), createItemRequestDTO);
    }
    
    

}
