package com.crafters.DataService.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.crafters.DataService.entities.Item;
import com.crafters.DataService.entities.ItemTotal;

public interface ItemTotalRepository extends MongoRepository<ItemTotal, String> {
    List<Item> findByUser(String userId);
}