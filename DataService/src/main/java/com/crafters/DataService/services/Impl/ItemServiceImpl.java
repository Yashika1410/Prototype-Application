package com.crafters.DataService.services.Impl;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.crafters.DataService.dtos.CreateItemRequestDTO;
import com.crafters.DataService.dtos.ItemResponse;
import com.crafters.DataService.entities.Item;
import com.crafters.DataService.repositories.ItemRepository;
import com.crafters.DataService.services.ItemService;
@Component
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;

    public ItemServiceImpl(ItemRepository itemRepository,UserServiceImpl userServiceImpl){
        this.userService=userServiceImpl;
        this.itemRepository=itemRepository;
    }

    @Override
    public ItemResponse CreateNewItem(String userId, CreateItemRequestDTO createItemRequestDTO) {
        // check if user present or not
        // TODO Auto-generated method stub
        Item item = itemRepository.save(Item.builder().name(createItemRequestDTO.getName())
                        .user(userService.getUserById(userId))
                        .yearValue(createItemRequestDTO.getYearValue())
                        .collectionName(createItemRequestDTO.getCollectionName())
                        .attributes(createItemRequestDTO.getAttributes())
                        .createdAt(new Date(System.currentTimeMillis()))
                        .updatedAt(new Date(System.currentTimeMillis()))
                        .build());
        return new ItemResponse(item);
    }
    
    public List<ItemResponse> getListOfItemsByFilterAndUserId(String userId,String filter,String filterValue){
        return itemRepository.findByUserIdAndFilter(userId, filter, filterValue).stream().map(item -> new ItemResponse(item)).toList();
    }

    public List<ItemResponse> getListOfItemsByUserId(String userId){
        return itemRepository.findAll(userId).stream().map(item -> new ItemResponse(item)).toList();
    }
}
