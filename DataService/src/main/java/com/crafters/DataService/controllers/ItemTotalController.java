package com.crafters.DataService.controllers;

import com.crafters.DataService.dtos.ItemTotalRequestDTO;
import com.crafters.DataService.dtos.ItemTotalResponseDTO;
import com.crafters.DataService.services.Impl.AuthServiceImpl;
import com.crafters.DataService.services.Impl.ItemTotalServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/itemTotals")
public class ItemTotalController {

    private final AuthServiceImpl authService;
    private final ItemTotalServiceImpl itemTotalService;

    @PostMapping("")
    @Operation(summary = "Create new Item",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ItemTotalResponseDTO> createItem(@RequestBody ItemTotalRequestDTO itemTotalRequestDTO, Authentication authentication) {
        return new ResponseEntity<>(itemTotalService.createItemTotal(authService.getUserId(authentication), itemTotalRequestDTO), HttpStatus.CREATED);
    }


}
