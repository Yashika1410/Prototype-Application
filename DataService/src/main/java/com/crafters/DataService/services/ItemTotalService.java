package com.crafters.DataService.services;
import com.crafters.DataService.dtos.ItemTotalByItemNameResponse;
import com.crafters.DataService.dtos.ItemTotalRequestDTO;
import com.crafters.DataService.dtos.ItemTotalResponseDTO;
import com.crafters.DataService.entities.ItemTotal;

import java.util.List;

public interface ItemTotalService {

    public ItemTotalResponseDTO createItemTotal(String userId, ItemTotalRequestDTO itemTotalRequestDTO);
    List<ItemTotalResponseDTO> getAllItemTotal(String userId);

    List<ItemTotalResponseDTO> getItemTotalsByAttribute(String userId, String attributeName, String attributeValue);

    ItemTotalByItemNameResponse getTotalValueByItemNameAndUserId(String userId, String itemName);

    String deleteItemByIdFromItemTotal(String itemTotalId, String itemId);

}
