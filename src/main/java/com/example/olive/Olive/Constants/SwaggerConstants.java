package com.example.olive.Olive.Constants;

public class SwaggerConstants {
    /**
     * Inventory
     * This class contains the Swagger constants related to Inventory APIs.
     */
    public static final class Inventory {
        public static final String TAG_NAME = "Inventory APIs";
        public static final String TAG_DESCRIPTION = "Operations related to Inventory management";
        public static final String POST_SUMMARY = "Add a new inventory item";
        public static final String POST_DESCRIPTION = "This API is responsible to add a new inventory item.";
        public static final String PUT_SUMMARY = "Update Inventory Item";
        public static final String PUT_DESCRIPTION = "This API is responsible to update inventory item.";
        public static final String GET_SUMMARY = "Get Inventory List";
        public static final String GET_DESCRIPTION = "This API returns list of all inventories or inventory by inventoryId.";
        public static final String DELETE_SUMMARY = "Soft Delete Inventory Item";
        public static final String DELETE_DESCRIPTION = "This API soft deletes inventory by inventoryId.";
        public static final String PERMANENT_DELETE_SUMMARY = "Delete Inventory Item";
        public static final String PERMANENT_DELETE_DESCRIPTION = "This API deletes inventory by inventoryId.";
    }
}
