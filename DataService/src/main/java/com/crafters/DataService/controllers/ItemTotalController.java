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
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/itemTotals")
public class ItemTotalController {

    /**
     *
     */
    private final AuthServiceImpl authService;
    /**
     *
     */
    private final ItemTotalServiceImpl itemTotalService;

    /**
     * @param itemTotalRequestDTO
     * @param authentication
     * @return ResponseEntity
     */
    @PostMapping("")
    @Operation(summary = "Create new Item Total",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ItemTotalResponseDTO> createItem(
        @RequestBody final ItemTotalRequestDTO itemTotalRequestDTO,
        final Authentication authentication) {
        return new ResponseEntity<>(
                itemTotalService.createItemTotal(
                    authService.getUserId(authentication), itemTotalRequestDTO),
                HttpStatus.CREATED);
    }

    /**
     * @param attributeName
     * @param attributeValue
     * @param authentication
     * @return ResponseEntity
     */
    @GetMapping("")
    @Operation(
        summary = "List of Itemtotal on basis of attributes name and values",
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ItemTotalResponseDTO>> getAllItemTotals(
            @RequestParam(required = false) final String attributeName,
            @RequestParam(required = false) final String attributeValue,
            final Authentication authentication) {

        return new ResponseEntity<>(
                itemTotalService.getItems(
                    authService.getUserId(authentication),
                    attributeName, attributeValue),
                HttpStatus.OK);
    }

    /**
     * @param authentication
     * @param itemName
     * @return ItemTotalByItemName
     */
    @GetMapping("/{itemName}")
    @Operation(summary = "Get Total by item Name",
    security = @SecurityRequirement(name = "bearerAuth"))
    public ItemTotalByItemNameResponse getTotalByItemName(
        final Authentication authentication,
        @RequestParam final String itemName) {
        return itemTotalService.getTotalValueByItemNameAndUserId(
            authService.getUserId(authentication), itemName);

    }

}
