package com.crafters.DataService.services;

import com.crafters.DataService.dtos.ItemTotalByItemNameResponse;
import com.crafters.DataService.dtos.ItemTotalRequestDTO;
import com.crafters.DataService.dtos.ItemTotalResponseDTO;

public interface ItemTotalService {
        public ItemTotalResponseDTO createItemTotal(String userId, ItemTotalRequestDTO itemTotalRequestDTO);
        public ItemTotalByItemNameResponse getTotalValueByItemNameAndUserId(String userId,String itemName);
}
