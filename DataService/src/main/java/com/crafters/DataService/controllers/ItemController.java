package com.crafters.DataService.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    private AuthServiceImpl authService;
    private ItemServiceImpl itemService;

    /**
     * @param createItemRequestDTO
     * @param authentication
     * @return
     */
    @PostMapping("")
    @Operation(summary = "Create new Item",
  security = @SecurityRequirement(name = "bearerAuth"))
    public ItemResponse createItem(@RequestBody CreateItemRequestDTO createItemRequestDTO,Authentication authentication) {
        return itemService.CreateNewItem(authService.getUserId(authentication), createItemRequestDTO);
    }

    /**
     * @param authentication
     * @param filterFlag
     * @param filter
     * @param filterValue
     * @return
     */
    @GetMapping("")
    @Operation(summary = "List Items",
  security = @SecurityRequirement(name = "bearerAuth"))
    public List<ItemResponse> getListOfItemsByFilter(Authentication authentication,@RequestParam(defaultValue = "false") Boolean filterFlag,@RequestParam(defaultValue = "") String filter,@RequestParam(name = "value",defaultValue = "") String filterValue) {
        if(filterFlag)
        return itemService.getListOfItemsByFilterAndUserId(authService.getUserId(authentication), filter, filterValue);
        return itemService.getListOfItemsByUserId(authService.getUserId(authentication));
    }
    
    

}
