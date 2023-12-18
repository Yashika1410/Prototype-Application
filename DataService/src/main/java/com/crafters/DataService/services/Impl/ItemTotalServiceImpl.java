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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //TODO check if user present or not
        List<String> itemIds = itemTotalRequestDTO.getItemIds();
        Attribute attribute = itemTotalRequestDTO.getAttribute();

        List<Item> items = itemRepository.findByUser_IdAndIdIn(userId, itemIds);

        Map<String, Integer> yearSums = new HashMap<>();

        for (Item item : items) {
            if (item.getCollectionName().equals(itemTotalRequestDTO.getName())) {
                Map<String, Integer> yearValue = item.getYearValue();

                for (Map.Entry<String, Integer> entry : yearValue.entrySet()) {
                    String year = entry.getKey();
                    int value = entry.getValue();

                    int currentSum = yearSums.getOrDefault(year, 0);
                    int newSum = currentSum + value;
                    yearSums.put(year, newSum);
                }
            }
        }
        ItemTotal itemTotal = itemTotalRepository.save(ItemTotal.builder().name(itemTotalRequestDTO.getName()).user(userService.getUserById(userId)).items(items).yearTotalValue(yearSums).createdAt(new Date(System.currentTimeMillis())).updatedAt(new Date(System.currentTimeMillis())).build());

        return new ItemTotalResponseDTO(itemTotal);
    }
}
