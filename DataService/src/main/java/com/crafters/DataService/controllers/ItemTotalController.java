package com.crafters.DataService.controllers;

import com.crafters.DataService.dtos.ItemTotalByItemNameResponse;
import com.crafters.DataService.dtos.ItemTotalRequestDTO;
import com.crafters.DataService.dtos.ItemTotalResponseDTO;
import com.crafters.DataService.dtos.ItemTotalUpdateRequestDTO;
import com.crafters.DataService.services.Impl.AuthServiceImpl;
import com.crafters.DataService.services.Impl.ItemTotalServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<ItemTotalResponseDTO> createTotalItem(
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

    @DeleteMapping("/{itemTotalId}")
    @Operation(summary = "Delete Total by itemTotal id",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> deleteItemTotalById(
            final Authentication authentication,
            @PathVariable final String itemTotalId) {
        return new ResponseEntity<String>(itemTotalService.deleteItemTotalById(itemTotalId,
                authService.getUserId(authentication)), HttpStatus.NO_CONTENT);

    }

    /**
     * @param itemId
     * @param itemTotalUpdateRequestDTO
     * @param authentication
     * @return ItemTotalResponse
     */
    @PutMapping("/{itemId}")
    @Operation(summary = " Update Total by itemTotal id",
            security = @SecurityRequirement(name = "bearerAuth"))
    public final ResponseEntity<ItemTotalResponseDTO> addNewYearTotal(
            @PathVariable final String itemId,
            @RequestBody final
            ItemTotalUpdateRequestDTO itemTotalUpdateRequestDTO,
            final Authentication authentication) {
                System.out.println("Hi" + itemId);
        return ResponseEntity.ok(itemTotalService.updateTotalItems(
                        authService.getUserId(authentication),
                        itemId, itemTotalUpdateRequestDTO));
    }

}
