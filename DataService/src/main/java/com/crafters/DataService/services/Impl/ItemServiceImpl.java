package com.crafters.DataService.services.Impl;
import java.util.Date;
import java.util.List;

import com.crafters.DataService.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Component;

import com.crafters.DataService.dtos.CreateItemRequestDTO;
import com.crafters.DataService.dtos.ItemResponse;
import com.crafters.DataService.entities.Item;
import com.crafters.DataService.repositories.ItemRepository;
import com.crafters.DataService.services.ItemService;
/**
 * Implementation of the {@link com.crafters.DataService.services.ItemService} interface.
 * Provides methods for handling operations related to items.
 */
@Component
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;

    public ItemServiceImpl(ItemRepository itemRepository,UserServiceImpl userServiceImpl){
        this.userService=userServiceImpl;
        this.itemRepository=itemRepository;
    }
    /**
     * Creates a new item based on the provided information.
     *
     * @param userId                The user ID associated with the item.
     * @param createItemRequestDTO  The DTO containing information for creating a new item.
     * @return The response containing information about the created item.
     */
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

    public List<ItemResponse> getListOfItemsByUserId(String userId) {
        return itemRepository.findAll(userId).stream().map(item -> new ItemResponse(item)).toList();
    }

    @Override
    public ItemResponse getItemById(String userId, String itemId) {
        Item item = itemRepository.findByIdAndUser_Id(itemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId + " for user: " + userId));
        return new ItemResponse(item);

    }
}
