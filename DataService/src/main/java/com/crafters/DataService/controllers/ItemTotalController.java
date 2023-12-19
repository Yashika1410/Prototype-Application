package com.crafters.DataService.controllers;

import com.crafters.DataService.dtos.ItemTotalByItemNameResponse;
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
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/itemTotals")
public class ItemTotalController {

    private final AuthServiceImpl authService;
    private final ItemTotalServiceImpl itemTotalService;

    /**
     * @param itemTotalRequestDTO
     * @param authentication
     * @return
     */
    @PostMapping("")
    @Operation(summary = "Create new Item Total",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ItemTotalResponseDTO> createItem(@RequestBody ItemTotalRequestDTO itemTotalRequestDTO, Authentication authentication) {
        return new ResponseEntity<>(itemTotalService.createItemTotal(authService.getUserId(authentication), itemTotalRequestDTO), HttpStatus.CREATED);
    }

    /**
     * @param authentication
     * @param itemName
     * @return
     */
    @GetMapping("/{itemName}")
     @Operation(summary = "Get Total by item Name",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ItemTotalByItemNameResponse getTotalByItemName(Authentication authentication,@RequestParam String itemName) {
        return itemTotalService.getTotalValueByItemNameAndUserId(authService.getUserId(authentication), itemName);
    }
    @GetMapping("find/{itemTotalId}")
    public ResponseEntity<ItemTotalResponseDTO> getItemTotalById(@PathVariable String itemTotalId, Authentication authentication) {
        ItemTotalResponseDTO itemTotalResponse = itemTotalService.getItemTotalById(itemTotalId, authService.getUserId(authentication));
        return ResponseEntity.ok(itemTotalResponse);
    }

}
