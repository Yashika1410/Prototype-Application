package com.crafters.DataService.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.crafters.DataService.entities.Item;
public interface ItemRepository extends MongoRepository<Item, String>{

    @Query("{'userId:?0'}")
    List<Item> findAll(String userId);
    
}
