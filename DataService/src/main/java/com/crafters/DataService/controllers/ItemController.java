package com.crafters.DataService.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.crafters.DataService.dtos.CreateItemRequestDTO;
import com.crafters.DataService.dtos.ItemResponse;
import com.crafters.DataService.dtos.YearValueDTO;
import com.crafters.DataService.services.Impl.AuthServiceImpl;
import com.crafters.DataService.services.Impl.ItemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * Controller class for handling item-related operations.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    /**
     *
     */
    private AuthServiceImpl authService;
    /**
     *
     */
    private ItemServiceImpl itemService;


    /**
     * Creates list of new items.
     *
     * @param createItemRequestDTOs The DTO containing the information for
     *  creating a new item.
     * @param authentication The authentication object
     * representing the current user.
     * @return The response containing information about the created item.
     */
    @PostMapping("/batch")
    @Operation(summary = "Create new Item",
    security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ItemResponse>> createItems(
        @RequestBody final List<CreateItemRequestDTO> createItemRequestDTOs,
        final Authentication authentication) {
        return ResponseEntity.ok(itemService.createListOfNewItems(
            authService.getUserId(authentication), createItemRequestDTOs));
    }

    /**
     * Creates a new item.
     *
     * @param createItemRequestDTO The DTO containing the information for
     *  creating a new item.
     * @param authentication The authentication object
     * representing the current user.
     * @return The response containing information about the created item.
     */
    @PostMapping("")
    @Operation(summary = "Create new Item",
    security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ItemResponse> createItem(
        @RequestBody final CreateItemRequestDTO createItemRequestDTO,
        final Authentication authentication) {
        return ResponseEntity.ok(itemService.createNewItem(
            authService.getUserId(authentication), createItemRequestDTO));
    }

    /**
     * @param authentication
     * @param filterFlag
     * @param filter
     * @param filterValue
     * @return List<ItemResponse>
     */
    @GetMapping("")
    @Operation(summary = "List Items",
    security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ItemResponse>> getListOfItemsByFilter(
            final Authentication authentication,
            @RequestParam(defaultValue = "false") final Boolean filterFlag,
            @RequestParam(defaultValue = "") final String filter,
            @RequestParam(
                name = "value", defaultValue = "") final String filterValue) {
        if (filterFlag) {
            return ResponseEntity.ok(
                itemService.getListOfItemsByFilterAndUserId(
                    authService.getUserId(authentication), filter,
                    filterValue));
        }
        return  ResponseEntity.ok(itemService.getListOfItemsByUserId(
                authService.getUserId(authentication)));
    }

    /**
     * @param itemId
     * @param authentication
     * @return ItemResponse.
     */
    @GetMapping("/{itemId}")
    @Operation(summary = "Get Item By ID",
    security = @SecurityRequirement(name = "bearerAuth"))
    public final ResponseEntity<ItemResponse> getItemById(
        @PathVariable final String itemId,
        final Authentication authentication) {

        ItemResponse itemResponse = itemService.getItemById(
            authService.getUserId(authentication), itemId);
        return ResponseEntity.ok(itemResponse);
    }

    /**
     * @param authentication
     * @param id
     * @param createItemRequestDTO
     * @return ItemResponse
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update Item By ID",
    security = @SecurityRequirement(name = "bearerAuth"))
    public final ResponseEntity<ItemResponse> updateItem(
        final Authentication authentication,
        @PathVariable final String id,
        @RequestBody final CreateItemRequestDTO createItemRequestDTO) {
        return ResponseEntity.ok(
            itemService.updateItemByUserIdAndItemId(
                authService.getUserId(authentication), id,
                createItemRequestDTO));
    }

    /**
     * @param authentication
     * @param id
     * @param listOfYearValues
     * @return ItemResponse
     */
    @PutMapping("add-new-year-value/{id}")
    @Operation(summary = "Add new year value to Item By ID",
    security = @SecurityRequirement(name = "bearerAuth"))
    public final ResponseEntity<ItemResponse> addNewYearValuesById(
        final Authentication authentication,
        @PathVariable final String id,
        @RequestBody final List<YearValueDTO> listOfYearValues) {
        return ResponseEntity.ok(
            itemService.addNewYearValuesToItemByUserIdAndItemId(
                authService.getUserId(authentication), id, listOfYearValues));
    }

}
