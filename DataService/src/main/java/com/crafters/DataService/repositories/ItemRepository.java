package com.crafters.DataService.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.crafters.DataService.entities.Item;
public interface ItemRepository extends MongoRepository<Item, String>{

    @Query("{'userId':?0}")
    List<Item> findAll(String userId);

    List<Item> findByUser_IdAndIdIn(String userId, List<String> itemIds);
    
    @Query(value = "{'userId':?0,'name':?1}")
    List<Item> findByUser_IDAndName(String userId, String name);
    @Query(value = "{'userId':?0,?1:?2}")
    List<Item> findByUserIdAndFilter(String userId, String filter, String filterValue);
    
}
