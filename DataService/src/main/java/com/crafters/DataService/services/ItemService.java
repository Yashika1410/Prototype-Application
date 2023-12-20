package com.crafters.DataService.services;

import java.util.List;

import com.crafters.DataService.dtos.CreateItemRequestDTO;
import com.crafters.DataService.dtos.ItemResponse;
import com.crafters.DataService.dtos.YearValueDTO;
/**
 * Service interface for handling operations related to items.
 */
public interface ItemService {
    /**
     * Creates a new item based on the provided information.
     *
     * @param userId                The user ID associated with the item.
     * @param createItemRequestDTO  The DTO containing information for creating a new item.
     * @return The response containing information about the created item.
     */
    ItemResponse CreateNewItem(String userId,CreateItemRequestDTO createItemRequestDTO);
    ItemResponse getItemById(String userId,String itemId);
    ItemResponse updateItemByUserIdAndItemId(String userId, String itemId, CreateItemRequestDTO createItemRequestDTO);
    ItemResponse addNewYearValuesToItemByUserIdAndItemId(String userId, String itemId,
            List<YearValueDTO> listOfYearValues);
} 