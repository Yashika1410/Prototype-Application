package com.crafters.DataService.services;

import com.crafters.DataService.dtos.CreateItemRequestDTO;
import com.crafters.DataService.dtos.ItemResponse;
import com.crafters.DataService.dtos.UpdateItemRequestDTO;
import com.crafters.DataService.dtos.YearValueDTO;

import java.util.List;

/**
 * Service interface for handling operations related to items.
 */
public interface ItemService {
    /**
     * Creates a new item based on the provided information.
     *
     * @param userId               The user ID associated with the item.
     * @param createItemRequestDTO The DTO containing information
     *                             for creating a new item.
     * @return The response containing information about the created item.
     */
    ItemResponse createNewItem(
            String userId, CreateItemRequestDTO createItemRequestDTO);

    /**
     * @param userId
     * @param itemId
     * @return ItemResponse
     */
    ItemResponse getItemById(String userId, String itemId);

    /**
     * @param userId
     * @param itemId
     * @param createItemRequestDTO
     * @return ItemResponse
     */
    ItemResponse updateItemByUserIdAndItemId(
            String userId, String itemId,
            CreateItemRequestDTO createItemRequestDTO);

    /**
     * @param userId
     * @param itemId
     * @param listOfYearValues
     * @return ItemResponse
     */
    ItemResponse addNewYearValuesToItemByUserIdAndItemId(
            String userId, String itemId,
            List<YearValueDTO> listOfYearValues);

    /**
     * @param userId
     * @param filter
     * @param filterValue
     * @return ItemResponse
     */
    List<ItemResponse> getListOfItemsByFilterAndUserId(
            String userId, String filter,
            String filterValue);

    String deleteItemById(String id, String userId);

    List<ItemResponse> updateBatchItems(String userId,  List<UpdateItemRequestDTO> updateItemRequestDTOList);
}
