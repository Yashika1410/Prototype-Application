package com.crafters.DataService.services;

import com.crafters.DataService.dtos.CreateItemRequestDTO;
import com.crafters.DataService.dtos.ItemResponse;

public interface ItemService {

    ItemResponse CreateNewItem(String userId,CreateItemRequestDTO createItemRequestDTO);
    
} 