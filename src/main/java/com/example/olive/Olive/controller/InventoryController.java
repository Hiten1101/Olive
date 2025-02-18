package com.example.olive.Olive.controller;

import com.example.olive.Olive.entity.Inventory;
import com.example.olive.Olive.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v3/${spring.application.name}/inventory")
@Slf4j
public class InventoryController {
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Create inventory.
     * @return response entity.
     */
    @PostMapping
    public ResponseEntity<Inventory> createInventory(
            @RequestBody final Inventory newInventory
    ) {
        log.info("Creating inventory: {}", newInventory);
        Inventory inventory = new Inventory();
        inventory.setName(newInventory.getName());
        inventory.setDescription(newInventory.getDescription());
        inventory.setPrice(newInventory.getPrice());
        inventory.setQuantity(newInventory.getQuantity());
        inventory.setIsDeleted(false);
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);

        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    /**
     * Update updatedInventory.
     * @param inventoryId - updatedInventory id to update.
     * @param updatedInventory - updatedInventory to update.
     * @return response entity.
     */
    @PutMapping("/{inventoryId}")
    public ResponseEntity<Inventory> updateInventory(
            @PathVariable("inventoryId") final UUID inventoryId,
            @RequestBody final Inventory updatedInventory
    ) {
        log.info("Updating inventory request: {}", updatedInventory);
        Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
        log.info("Existing inventory: {}", inventory);
        if (inventory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        inventory.setName(updatedInventory.getName());
        inventory.setDescription(updatedInventory.getDescription());
        inventory.setPrice(updatedInventory.getPrice());
        inventory.setQuantity(updatedInventory.getQuantity());
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);

        log.info("Updated inventory: {}", inventory);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    /**
     * Delete inventory.
     * @param inventoryId - inventory id to delete.
     * @return response entity.
     */
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<String> deleteInventory(
            @PathVariable("inventoryId") final UUID inventoryId
    ) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
        if (inventory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        inventory.setUpdatedAt(LocalDateTime.now());
        inventory.setIsDeleted(true);
        inventoryRepository.save(inventory);

        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{inventoryId}")
    public ResponseEntity<String> permanentDeleteInventory(
            @PathVariable("inventoryId") final UUID inventoryId
    ) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
        if (inventory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        inventoryRepository.delete(inventory);
        return new ResponseEntity<>("Deleted Permanently", HttpStatus.OK);
    }

    /**
     * List inventory.
     * @return response entity.
     */
    @GetMapping
    public ResponseEntity<List<Inventory>> listInventory(
            @RequestParam(required = false) UUID inventoryId
    ) {
        List<Inventory> inventoryResponseDtoList = inventoryId != null
                ? inventoryRepository.findById(inventoryId).stream().toList()
                : inventoryRepository.findAll();

        return new ResponseEntity<>(inventoryResponseDtoList, HttpStatus.OK);
    }
}
