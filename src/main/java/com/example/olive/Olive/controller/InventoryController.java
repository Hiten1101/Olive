package com.example.olive.Olive.controller;

import com.example.olive.Olive.constants.SwaggerConstants;
import com.example.olive.Olive.entity.Inventory;
import com.example.olive.Olive.service.InventoryService;
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

import java.util.List;
import java.util.UUID;

@Tag(name = SwaggerConstants.Inventory.TAG_NAME, description = SwaggerConstants.Inventory.TAG_DESCRIPTION)
@RestController
@RequestMapping("/api/v3/${spring.application.name}/inventory")
@Slf4j
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

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
        Inventory inventory = inventoryService.addInventory(newInventory);

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
        Inventory inventory = inventoryService.editInventory(inventoryId, updatedInventory);

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
    public ResponseEntity<Inventory> deleteInventory(
            @PathVariable("inventoryId") final UUID inventoryId
    ) {
        Inventory inventory = inventoryService.deleteInventory(inventoryId);

        return new ResponseEntity<>(inventory, HttpStatus.OK);
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
        inventoryService.permanentDeleteInventory(inventoryId);

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
        List<Inventory> inventoryResponseDtoList = inventoryService.fetchInventoryList(inventoryId);

        return new ResponseEntity<>(inventoryResponseDtoList, HttpStatus.OK);
    }
}
