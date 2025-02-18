package com.example.olive.Olive.service;

import com.example.olive.Olive.entity.Inventory;
import com.example.olive.Olive.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final RedisService redisService;

    public Inventory addInventory(final Inventory newInventory) {
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
        log.info("Inventory created: {}", newInventory);
        redisService.saveToRedis(
                String.valueOf(inventory.getId()),
                "Redis stored value for inventory item name: " + inventory.getName(),
                10
        );

        return inventory;
    }

    public Inventory editInventory(
            final UUID inventoryId,
            final Inventory updatedInventory
    ) {
        try {
            log.info("Updating inventory request: {}", updatedInventory);
            Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
            log.info("Existing inventory: {}", inventory);
            if (inventory == null) {
                throw new RuntimeException();
            }

            inventory.setName(updatedInventory.getName());
            inventory.setDescription(updatedInventory.getDescription());
            inventory.setPrice(updatedInventory.getPrice());
            inventory.setQuantity(updatedInventory.getQuantity());
            inventory.setUpdatedAt(LocalDateTime.now());
            inventoryRepository.save(inventory);
            log.info("Updated inventory: {}", inventory);

            return inventory;
        } catch (Exception e) {
            log.error("Error updating inventory: {}", e.getMessage());
            return null;
        }
    }

    public Inventory deleteInventory(final UUID inventoryId) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
            if (inventory == null) {
                throw new RuntimeException();
            }

            inventory.setUpdatedAt(LocalDateTime.now());
            inventory.setIsDeleted(true);
            inventoryRepository.save(inventory);

            return inventory;
        } catch (Exception e) {
            log.error("Error deleting inventory: {}", e.getMessage());
            return null;
        }
    }

    public void permanentDeleteInventory(final UUID inventoryId) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
            if (inventory == null) {
                throw new RuntimeException();
            }
            redisService.deleteFromRedis(String.valueOf(inventory.getId()));

            inventoryRepository.delete(inventory);
        } catch (Exception e) {
            log.error("Error permanently deleting inventory: {}", e.getMessage());
        }
    }

    public List<Inventory> fetchInventoryList(final UUID inventoryId) {
        List<Inventory> inventories = inventoryId != null
                ? inventoryRepository.findById(inventoryId).stream().toList()
                : inventoryRepository.findAll();

        for (Inventory inventory : inventories) {
            log.info("Value from redis => {}",
                    redisService.getFromRedis(String.valueOf(inventory.getId())));
        }

        return inventories;
    }
}
