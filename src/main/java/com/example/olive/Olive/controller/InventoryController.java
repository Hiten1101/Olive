package com.example.olive.Olive.controller;

import com.example.olive.Olive.Constants.SwaggerConstants;
import com.example.olive.Olive.entity.Inventory;
import com.example.olive.Olive.repository.InventoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = SwaggerConstants.Inventory.TAG_NAME, description = SwaggerConstants.Inventory.TAG_DESCRIPTION)
@RestController
@RequestMapping("/api/v3/${spring.application.name}/inventory")
@Slf4j
public class InventoryController {
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Create inventory.
     *
     * @param newInventory - new inventory to create.
     * @return response entity.
     */
    @Operation(
            summary = SwaggerConstants.Inventory.POST_SUMMARY,
            description = SwaggerConstants.Inventory.POST_DESCRIPTION
    )
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
     *
     * @param inventoryId      - updatedInventory id to update.
     * @param updatedInventory - updatedInventory to update.
     * @return response entity.
     */
    @Operation(
            summary = SwaggerConstants.Inventory.PUT_SUMMARY,
            description = SwaggerConstants.Inventory.PUT_DESCRIPTION
    )
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
     *
     * @param inventoryId - inventory id to delete.
     * @return response entity.
     */
    @Operation(
            summary = SwaggerConstants.Inventory.DELETE_SUMMARY,
            description = SwaggerConstants.Inventory.DELETE_DESCRIPTION
    )
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

    /**
     * Delete inventory permanently.
     *
     * @param inventoryId - inventory id to fetch.
     * @return response entity.
     */
    @Operation(
            summary = SwaggerConstants.Inventory.PERMANENT_DELETE_SUMMARY,
            description = SwaggerConstants.Inventory.PERMANENT_DELETE_DESCRIPTION
    )
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
     * List all inventory items or inventory item based on id.
     *
     * @param inventoryId - inventory id to fetch.
     * @return response entity.
     */
    @Operation(
            summary = SwaggerConstants.Inventory.GET_SUMMARY,
            description = SwaggerConstants.Inventory.GET_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inventory.class)),
                            headers = @Header(name = "X-Total-Count", description = "Total number of items")
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<Inventory>> fetchInventoryList(
            @RequestParam(required = false) final UUID inventoryId
    ) {
        List<Inventory> inventoryResponseDtoList = inventoryId != null
                ? inventoryRepository.findById(inventoryId).stream().toList()
                : inventoryRepository.findAll();

        return new ResponseEntity<>(inventoryResponseDtoList, HttpStatus.OK);
    }
}
