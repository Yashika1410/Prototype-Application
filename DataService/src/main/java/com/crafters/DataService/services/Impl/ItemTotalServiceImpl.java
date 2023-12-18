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

    public ItemTotalResponseDTO createItemTotal(String userId, ItemTotalRequestDTO itemTotalRequestDTO) {

        List<String> itemIds = itemTotalRequestDTO.getItemIds();
        Attribute attribute = itemTotalRequestDTO.getAttribute();
        if (itemTotalRequestDTO.getItemIds().isEmpty()) {
            throw new IllegalArgumentException("Invalid Item Ids");
        }
        List<Item> items = itemRepository.findByUser_IdAndIdIn(userId, itemIds);
        List<Item> filteredItems = filterItemsByAttribute(items, attribute);
        Map<String, Integer> yearSums = calculateYearSums(itemTotalRequestDTO.getYearTotalValue(), filteredItems);

        ItemTotal itemTotal = itemTotalRepository
                .save(ItemTotal.builder()
                        .name(itemTotalRequestDTO.getName())
                        .user(userService.getUserById(userId))
                        .attribute(itemTotalRequestDTO.getAttribute())
                        .items(filteredItems)
                        .yearTotalValue(yearSums)
                        .createdAt(new Date(System.currentTimeMillis()))
                        .updatedAt(new Date(System.currentTimeMillis()))
                        .build());

        createRelationWIthItems(filteredItems, itemTotal);
        return new ItemTotalResponseDTO(itemTotal);
    }

    private List<Item> filterItemsByAttribute(List<Item> items, Attribute attribute) {
        return items.stream()
                .filter(item -> String.valueOf(item.getAttributes().get(attribute.getAttributeName()))
                        .equalsIgnoreCase(attribute.getAttributeValue()))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> calculateYearSums(Map<String, Integer> yearTotalValue, List<Item> filteredItems) {
        if (yearTotalValue.isEmpty()) {
            if (!allItemsHaveSameKeys(filteredItems)) {
                throw new IllegalStateException("All attributes must be of the same type");
            }

            return filteredItems.stream()
                    .flatMap(item -> item.getYearValue().entrySet().stream())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            Integer::sum
                    ));
        } else {
            return yearTotalValue;
        }
    }

    private boolean allItemsHaveSameKeys(List<Item> items) {
        if (items.isEmpty()) {
            return true;
        }
        Set<String> referenceKeys = items.get(0).getYearValue().keySet();
        return items.stream()
                .allMatch(item -> item.getYearValue().keySet().equals(referenceKeys));
    }

    private void createRelationWIthItems(List<Item> filteredItems, ItemTotal itemTotal) {
        for (Item item : filteredItems) {
            if (item.getItemTotals() == null) {
                item.setItemTotals(new ArrayList<>());
            }
            item.getItemTotals().add(itemTotal);
        }
        itemRepository.saveAll(filteredItems);
    }
}