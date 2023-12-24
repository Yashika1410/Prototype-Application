package com.crafters.DataService.services.Impl;

import com.crafters.DataService.dtos.ItemTotalByItemNameResponse;
import com.crafters.DataService.dtos.ItemTotalRequestDTO;
import com.crafters.DataService.dtos.ItemTotalResponseDTO;
import com.crafters.DataService.dtos.ItemTotalUpdateRequestDTO;
import com.crafters.DataService.entities.Attribute;
import com.crafters.DataService.entities.Item;
import com.crafters.DataService.entities.ItemTotal;
import com.crafters.DataService.exceptions.EntityNotFoundException;
import com.crafters.DataService.repositories.ItemRepository;
import com.crafters.DataService.repositories.ItemTotalRepository;
import com.crafters.DataService.services.ItemTotalService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemTotalServiceImpl implements ItemTotalService {
    /**
     *
     */
    private static final int HUNDRED = 100;
    /**
     *
     */
    private final ItemTotalRepository itemTotalRepository;
    /**
     *
     */
    private final ItemRepository itemRepository;
    /**
     *
     */
    private final UserServiceImpl userService;


    /**
     * @param itemTotalRepo
     * @param itemRepo
     * @param userSer
     */
    public ItemTotalServiceImpl(
            final ItemTotalRepository itemTotalRepo,
            final ItemRepository itemRepo,
            final UserServiceImpl userSer) {
        this.itemTotalRepository = itemTotalRepo;
        this.itemRepository = itemRepo;
        this.userService = userSer;
    }

    /**
     * @param userId
     * @param attributeName
     * @param attributeValue
     * @return ItemTotals responses
     */
    public final List<ItemTotalResponseDTO> getItems(
            final String userId, final String attributeName,
            final String attributeValue) {
        if (attributeName != null && attributeValue != null) {
            return getItemTotalsByAttribute(
                    userId, attributeName, attributeValue);
        } else {
            return getAllItemTotal(userId);
        }
    }

    @Override
    public final ItemTotalResponseDTO createItemTotal(final String userId,
                                                      final ItemTotalRequestDTO itemTotalRequestDTO) {

        List<String> itemIds = itemTotalRequestDTO.getItemIds();
        Attribute attribute = itemTotalRequestDTO.getAttribute();
        if (itemTotalRequestDTO.getItemIds().isEmpty()) {
            throw new IllegalArgumentException("Invalid Item Ids");
        }
        List<Item> items = itemRepository.findByUser_IdAndIdIn(userId, itemIds);
        List<Item> filteredItems = filterItemsByAttribute(items, attribute);
        Map<String, Integer> yearSums = calculateYearSums(
                itemTotalRequestDTO.getYearTotalValue(), filteredItems);

        ItemTotal itemTotal = itemTotalRepository
                .save(ItemTotal.builder()
                        .name(itemTotalRequestDTO.getName())
                        .user(userService.getUserById(userId))
                        .attribute(itemTotalRequestDTO.getAttribute())
                        .items(filteredItems)
                        .rowType(itemTotalRequestDTO.getRowType())
                        .yearTotalValue(yearSums)
                        .createdAt(new Date(System.currentTimeMillis()))
                        .updatedAt(new Date(System.currentTimeMillis()))
                        .build());

        createRelationWIthItems(filteredItems, itemTotal);
        return new ItemTotalResponseDTO(itemTotal);
    }

    public final void updateAndDeleteItem(final ItemTotal itemTotal, final String itemIdToDelete) {
        List<Item> items = itemTotal.getItems();
        if (items.removeIf(item -> item.getId().equals(itemIdToDelete))) {
            if (items.isEmpty()) {
                itemTotalRepository.delete(itemTotal);
            } else {
                Map<String, Integer> yearSums = calculateYearSumsOfItems(items);
                itemTotal.setYearTotalValue(yearSums);
                itemTotalRepository.save(itemTotal);
            }
        }
    }

    public final Map<String, Integer> calculateYearSumsOfItems(final List<Item> items) {
        if (!allItemsHaveSameKeys(items)) {
            throw new IllegalStateException(
                    "All attributes must be of the same type");
        }
        return items.stream()
                .flatMap(item -> item.getYearValue().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum));
    }

    private List<Item> filterItemsByAttribute(
            final List<Item> items, final Attribute attribute) {
        return items.stream()
                .filter(item -> (attribute.getAttributeName().equalsIgnoreCase(item.getName()) &&
                        attribute.getAttributeValue().equalsIgnoreCase(item.getCollectionName())) ||
                        String.valueOf(item.getAttributes().get(attribute.getAttributeName()))
                                .equalsIgnoreCase(attribute.getAttributeValue()))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> calculateYearSums(
            final Map<String, Integer> yearTotalValue,
            final List<Item> filteredItems) {
        if (yearTotalValue.isEmpty()) {
            if (!allItemsHaveSameKeys(filteredItems)) {
                throw new IllegalStateException(
                        "All attributes must be of the same type");
            }

            return filteredItems.stream()
                    .flatMap(item -> item.getYearValue().entrySet().stream())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            Integer::sum));
        } else {
            return yearTotalValue;
        }

    }

    private boolean allItemsHaveSameKeys(final List<Item> items) {
        if (items.isEmpty()) {
            return true;
        }
        Set<String> referenceKeys = items.get(0).getYearValue().keySet();
        return items.stream()
                .allMatch(
                        item -> item.getYearValue().keySet().equals(referenceKeys));
    }

    @Override
    public final List<ItemTotalResponseDTO> getAllItemTotal(
            final String userId) {
        List<ItemTotal> itemTotalsList = itemTotalRepository.findAll();
        return itemTotalsList.stream()
                .map(ItemTotalResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public final List<ItemTotalResponseDTO> getItemTotalsByAttribute(
            final String userId, final String attributeName,
            final String attributeValue) {
        List<ItemTotal> itemTotalsList = itemTotalRepository.findByUser_Id(
                userId);
        return itemTotalsList.stream()
                .filter(itemTotal -> {
                    // Filter based on the specified attribute name and value
                    Attribute attribute = itemTotal.getAttribute();
                    return attribute != null
                            && attributeName.equalsIgnoreCase(
                                    attribute.getAttributeName())
                            && attributeValue.equalsIgnoreCase(
                                    attribute.getAttributeValue());
                })
                .map(ItemTotalResponseDTO::new)
                .collect(Collectors.toList());
    }

    private void createRelationWIthItems(final List<Item> filteredItems,
            final ItemTotal itemTotal) {
        for (Item item : filteredItems) {
            if (item.getItemTotals() == null) {
                item.setItemTotals(new ArrayList<>());
            }
            item.getItemTotals().add(itemTotal);
        }
        itemRepository.saveAll(filteredItems);
    }

    @Override
    public final ItemTotalByItemNameResponse getTotalValueByItemNameAndUserId(
            final String userId,
            final String itemName) {
        List<ItemTotal> itemTotalList = itemTotalRepository.findByItemsIn(
                itemRepository.findByUser_IDAndName(userId, itemName));
        return ItemTotalByItemNameResponse.builder()
                .collectionName(itemTotalList.get(0).getName())
                .yearValue(itemTotalList.stream().flatMap(
                        itemTotal -> itemTotal.getYearTotalValue().entrySet().stream()).collect(
                                Collectors.toMap(
                                        Map.Entry::getKey, Map.Entry::getValue, Integer::sum)))
                .name(itemName).build();

    }

    /**
     * @param itemTotal
     * @param yearValueDTOs
     * @return ItemTotal
     */
    public Map<String, Integer> addNewYearValuesById(
            final ItemTotal itemTotal,
            final Map<String, Integer> yearValueDTOs) {
        Map<String, Long> ratioMap = new HashMap<>();
        String year = itemTotal.getItems().get(
                (0)).getYearValue().keySet().iterator().next();
        System.out.println(year);
        itemTotal.getItems().forEach(i -> {
            ratioMap.put(i.getId(), Long.valueOf(
                    i.getYearValue().get(year)
                     * HUNDRED / itemTotal.getYearTotalValue().get(year)));

        });
        System.out.println(ratioMap.toString());
        ratioMap.forEach((k, v) -> {
            Item item = itemRepository.findById(k)
                    .orElseThrow(
                            () -> new EntityNotFoundException(
                                    "item not found by this Id: " + k));
            Map<String, Integer> map = item.getYearValue();
            yearValueDTOs.forEach((key, value) -> {
                map.put(
                        key, Math.round(
                                v * value) / HUNDRED);
            });
            item.setYearValue(map);
            itemRepository.save(item);
        });
        Map<String, Integer> updatedYearValue = itemTotal.getYearTotalValue();
        yearValueDTOs.forEach((key, value) -> {
            updatedYearValue.put(key, value);
        });
        return updatedYearValue;
    }

    public final String deleteItemTotalById(final String itemTotalId, final String userId) {
        System.out.println("id------------------------------------------" + itemTotalId);
        System.out.println("userId------------------------------------------" + userId);
        ItemTotal itemTotal = itemTotalRepository.findByIdAndUser_Id(itemTotalId, userId).orElseThrow(() -> {
            throw new EntityNotFoundException("ItemTotal Not found by given ID");
        });
        itemTotal.getItems().stream().forEach((item) -> {
            deleteItemTotalFromItem(item, itemTotalId);
        });
        itemTotalRepository.deleteById(itemTotalId);
        return "itemTotal Deleted";
    }

    public final void deleteItemTotalFromItem(final Item item, final String itemTotalId) {
        List<ItemTotal> itemTotals = item.getItemTotals();
        item.setItemTotals(itemTotals.stream().filter(itemTotal -> !itemTotal.getId().equals(itemTotalId))
                .collect(Collectors.toList()));
        itemRepository.save(item);
    }

    public final String deleteItemByIdFromItemTotal(final String itemTotalId, final String itemId) {

        ItemTotal itemTotal = itemTotalRepository.findById(itemTotalId)
                .orElseThrow(() -> new EntityNotFoundException("ItemTotal with given ID not found"));

        removeItemFromList(itemTotal.getItems(), itemId);

        itemTotalRepository.save(itemTotal);

        return "Deleted";
    }

    private void removeItemFromList(final List<Item> itemList, final String itemId) {
        itemList.removeIf(item -> item.getId().equals(itemId));
    }

    /**
     * @param userId
     * @param itemId
     * @param itemTotalUpdateRequestDTO
     * @return jj.
     */
    public ItemTotalResponseDTO updateTotalItems(
            final String userId, final String itemId,
            final ItemTotalUpdateRequestDTO itemTotalUpdateRequestDTO) {
                System.out.println("Hi");
            ItemTotal itemTotal = itemTotalRepository.findByIdAndUser_Id(
                itemId, userId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Item total not found by this "
                                + itemId + " id"));
            if (!itemTotalUpdateRequestDTO.getAttribute()
            .getAttributeValue()
            .equals(itemTotal.getAttribute().getAttributeValue())){
                itemTotal.getAttribute()
                    .setAttributeValue(itemTotalUpdateRequestDTO
                    .getAttribute().getAttributeValue());
                }
            if (!itemTotal.getName()
            .equals(itemTotalUpdateRequestDTO.getName())) {
                itemTotal.setName(
                    itemTotalUpdateRequestDTO.getName());
                }
            Map<String, Integer> newYearValue = new HashMap<>();
            itemTotalUpdateRequestDTO.getYearTotalValue().forEach(
                (key, value) -> {
                if (!itemTotal.getYearTotalValue()
                .containsKey(key)) {
                    newYearValue.put(key, value);
                } else if (!itemTotal.getYearTotalValue()
                .get(key).equals(value)) {
                        newYearValue.put(key, value);
                    }
            });
            if (!newYearValue.isEmpty()) {
                itemTotal.setYearTotalValue(
                    addNewYearValuesById(itemTotal, newYearValue));
            }
        return new ItemTotalResponseDTO(itemTotalRepository.save(itemTotal));
    }
}
