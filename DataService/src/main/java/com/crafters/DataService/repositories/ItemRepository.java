package com.crafters.DataService.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.crafters.DataService.entities.Item;
/**
 * Repository interface for performing database operations related to the Item entity.
 */
public interface ItemRepository extends MongoRepository<Item, String>{

    List<Item> findByUser_IdAndIdIn(String userId, List<String> itemIds);
    
    @Query(value = "{'userId':?0,'name':?1}")
    List<Item> findByUser_IDAndName(String userId, String name);
    @Query(value = "{'userId':?0,?1:?2}")
    List<Item> findByUserIdAndFilter(String userId, String filter, String filterValue);
  
    /**
     * Retrieves a list of items based on the specified user ID.
     *
     * @param userId The user ID for which to retrieve items.
     * @return A list of items associated with the specified user ID.
     */
    @Query("{'userId:?0'}")
    List<Item> findAll(String userId);

    List<Item> findByUser_IdAndIdIn(String userId, List<String> itemIds);
    Optional<Item> findByIdAndUser_Id(String itemId, String userId);

}
