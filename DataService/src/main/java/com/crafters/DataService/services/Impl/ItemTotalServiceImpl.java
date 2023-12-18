package com.crafters.DataService.services.Impl;

import com.crafters.DataService.dtos.ItemTotalRequestDTO;
import com.crafters.DataService.dtos.ItemTotalResponseDTO;
import com.crafters.DataService.entities.Attribute;
import com.crafters.DataService.entities.Item;
import com.crafters.DataService.entities.ItemTotal;
import com.crafters.DataService.repositories.ItemRepository;
import com.crafters.DataService.repositories.ItemTotalRepository;
import com.crafters.DataService.services.ItemTotalService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemTotalServiceImpl implements ItemTotalService {
    private final ItemTotalRepository itemTotalRepository;
    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;


    public ItemTotalServiceImpl(ItemTotalRepository itemTotalRepository, ItemRepository itemRepository, UserServiceImpl userService) {
        this.itemTotalRepository = itemTotalRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    public List<ItemTotalResponseDTO> getItems(String userId, String attributeName, String attributeValue){
        if (attributeName != null && attributeValue != null) {
          return getItemTotalsByAttribute(
                    userId, attributeName, attributeValue);
        } else {
            return getAllItemTotal(userId);
        }
    }
    public ItemTotalResponseDTO createItemTotal(String userId, ItemTotalRequestDTO itemTotalRequestDTO) {
        //TODO check if user present or not
        List<String> itemIds = itemTotalRequestDTO.getItemIds();
        Attribute attribute = itemTotalRequestDTO.getAttribute();

        List<Item> items = itemRepository.findByUser_IdAndIdIn(userId, itemIds);
        List<Item> filteredItems = new ArrayList<>();
        Map<String, Integer> yearSums = itemTotalRequestDTO.getYearTotalValue();

        items.stream()
                .filter(item ->
                        String.valueOf(item.getAttributes().get(attribute.getAttributeName()))
                                .equalsIgnoreCase(attribute.getAttributeValue()))
                .forEach(filteredItems::add);

        if (itemTotalRequestDTO.getYearTotalValue().isEmpty()) {
            if (!allItemsHaveSameKeys(items)) {
                throw new IllegalStateException("All attributes must be of the same type");
            }

            yearSums = filteredItems.stream()
                    .flatMap(item -> item.getYearValue().entrySet().stream())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            Integer::sum
                    ));
        }

        ItemTotal itemTotal = itemTotalRepository
                .save(ItemTotal.builder()
                        .name(itemTotalRequestDTO.getName()).attribute(itemTotalRequestDTO.getAttribute())
                        .user(userService.getUserById(userId))
                        .items(filteredItems).yearTotalValue(yearSums)
                        .createdAt(new Date(System.currentTimeMillis()))
                        .updatedAt(new Date(System.currentTimeMillis()))
                        .build());

        return new ItemTotalResponseDTO(itemTotal);
    }

    private boolean allItemsHaveSameKeys(List<Item> items) {
        if (items.isEmpty()) {
            return true;
        }
        Set<String> referenceKeys = items.get(0).getYearValue().keySet();
        return items.stream()
                .allMatch(item -> item.getYearValue().keySet().equals(referenceKeys));
    }

    @Override
    public List<ItemTotalResponseDTO> getAllItemTotal(String userId) {
        List<ItemTotal> itemTotalsList = itemTotalRepository.findAll();
        return itemTotalsList.stream()
                .map(ItemTotalResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemTotalResponseDTO> getItemTotalsByAttribute(String userId, String attributeName, String attributeValue) {
        List<ItemTotal> itemTotalsList = itemTotalRepository.findByUser_Id(userId);
        return itemTotalsList.stream()
                .filter(itemTotal -> {
                    // Filter based on the specified attribute name and value
                    Attribute attribute = itemTotal.getAttribute();
                    return attribute != null &&
                            attributeName.equalsIgnoreCase(attribute.getAttributeName()) &&
                            attributeValue.equalsIgnoreCase(attribute.getAttributeValue());
                })
                .map(ItemTotalResponseDTO::new)
                .collect(Collectors.toList());
    }
}
