package com.crafters.DataService.utils;

import com.crafters.DataService.entities.Item;
import com.crafters.DataService.entities.ItemTotal;
import com.crafters.DataService.repositories.ItemTotalRepository;
import com.crafters.DataService.services.Impl.ItemTotalServiceImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UpdateItemTotalUtils {

    private final ItemTotalRepository itemTotalRepository;
    private final ItemTotalServiceImpl itemTotalService;

    public UpdateItemTotalUtils(ItemTotalRepository itemTotalRepository, ItemTotalServiceImpl itemTotalService) {
        this.itemTotalRepository = itemTotalRepository;
        this.itemTotalService = itemTotalService;
    }

    public void updateItemTotalSums(String userId) {
        List<ItemTotal> itemTotals = itemTotalRepository.findByUser_Id(userId);

        List<ItemTotal> updatedItemTotals = new ArrayList<>();

        for (ItemTotal itemTotal : itemTotals) {
            List<Item> items = itemTotal.getItems();
            Map<String, Integer> yearSums = itemTotalService.calculateYearSumsOfItems(items);
            itemTotal.setYearTotalValue(yearSums);

            updatedItemTotals.add(itemTotal);
        }

        // Bulk update all the entities
        itemTotalRepository.saveAll(updatedItemTotals);
    }

}
