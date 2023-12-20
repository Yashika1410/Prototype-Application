package com.crafters.DataService.services.Impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.crafters.DataService.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Component;

import com.crafters.DataService.dtos.CreateItemRequestDTO;
import com.crafters.DataService.dtos.ItemResponse;
import com.crafters.DataService.dtos.YearValueDTO;
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
        return new ItemResponse(getItemByUserIdItemId(userId, itemId));

    }
    @Override
    public ItemResponse updateItemByUserIdAndItemId(String userId, String itemId, CreateItemRequestDTO createItemRequestDTO) {
        Item item = getItemByUserIdItemId(userId, itemId);
        item.setAttributes(createItemRequestDTO.getAttributes().isEmpty()?item.getAttributes():createItemRequestDTO.getAttributes());
        item.setCollectionName(createItemRequestDTO.getCollectionName().isEmpty()?item.getCollectionName():createItemRequestDTO.getCollectionName());
        item.setName(createItemRequestDTO.getName().isEmpty()?item.getName():createItemRequestDTO.getName());
        item.setYearValue(createItemRequestDTO.getYearValue().isEmpty()?item.getYearValue():createItemRequestDTO.getYearValue());
        return new ItemResponse(itemRepository.save(item));
    }

    private Item getItemByUserIdItemId(String userId, String itemId) {
        Item item = itemRepository.findByIdAndUser_Id(itemId, userId)
                .orElseThrow(()-> new EntityNotFoundException( "Item not found by this "+itemId+" id"));
        return item;
    }

    @Override
    public ItemResponse addNewYearValuesToItemByUserIdAndItemId(String userId, String itemId,
            List<YearValueDTO> listOfYearValues) {
                Item item = getItemByUserIdItemId(userId, itemId);
                Map<String,Integer> map = item.getYearValue();
                listOfYearValues.forEach(yearValue->{
                    map.put(yearValue.getYear(), yearValue.getValue());
                });
                item.setYearValue(map);
        return new ItemResponse(itemRepository.save(item));
    }
}
