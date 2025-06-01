#!/bin/bash

# Update UserServiceImp.java logs
sed -i '' 's/logger\.info("Creating/logger.info("[User] Creating/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Email already/logger.warn("[User] Email already/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Assigned custom/logger.debug("[User] Assigned custom/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Assigned default/logger.debug("[User] Assigned default/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("User created/logger.info("[User] User created/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Finding user/logger.debug("[User] Finding user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("User not found/logger.warn("[User] User not found/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("User found/logger.debug("[User] User found/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Fetching users/logger.debug("[User] Fetching users/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Found /logger.debug("[User] Found /g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("No user/logger.warn("[User] No user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Creating verification/logger.debug("[User] Creating verification/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Failed to create/logger.warn("[User] Failed to create/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Verification token/logger.debug("[User] Verification token/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Updating email/logger.debug("[User] Updating email/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Failed to update/logger.warn("[User] Failed to update/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Email confirmation/logger.info("[User] Email confirmation/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Fetching user by ID/logger.debug("[User] Fetching user by ID/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Attempting to delete/logger.info("[User] Attempting to delete/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Delete failed/logger.warn("[User] Delete failed/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Cannot delete/logger.warn("[User] Cannot delete/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Deleting /logger.debug("[User] Deleting /g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("User deleted/logger.info("[User] User deleted/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Attempting to reset/logger.info("[User] Attempting to reset/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Password reset failed/logger.warn("[User] Password reset failed/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Password reset/logger.info("[User] Password reset/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Updating user/logger.info("[User] Updating user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("User information/logger.info("[User] User information/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Updating roles/logger.info("[User] Updating roles/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Roles updated/logger.info("[User] Roles updated/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Attempting to delete refresh/logger.debug("[User] Attempting to delete refresh/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Successfully deleted/logger.info("[User] Successfully deleted/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.error("Error deleting/logger.error("[User] Error deleting/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Adding new user/logger.info("[User] Adding new user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Assigning role/logger.debug("[User] Assigning role/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Counting new users/logger.debug("[User] Counting new users/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.info("Total new users/logger.info("[User] Total new users/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java

# Update CategoryServiceImp.java logs
sed -i '' 's/logger\.info("Fetching category/logger.info("[Category] Fetching category/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.error("Category not found/logger.error("[Category] Category not found/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Category fetched/logger.info("[Category] Category fetched/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Adding new category/logger.info("[Category] Adding new category/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Category added/logger.info("[Category] Category added/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.error("Category already exists/logger.error("[Category] Category already exists/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.error("Image not found/logger.error("[Category] Image not found/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Deleting category/logger.info("[Category] Deleting category/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Category deleted/logger.info("[Category] Category deleted/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Deleting /logger.info("[Category] Deleting /g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Updating category/logger.info("[Category] Updating category/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Category updated/logger.info("[Category] Category updated/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Fetching all categories/logger.info("[Category] Fetching all categories/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("No categories found/logger.info("[Category] No categories found/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java
sed -i '' 's/logger\.info("Categories fetched/logger.info("[Category] Categories fetched/g' src/main/java/com/backend/domicare/service/imp/CategoryServiceImp.java

# Update FileServiceImp.java logs
sed -i '' 's/logger\.info("Deleted duplicate file/logger.info("[File] Deleted duplicate file/g' src/main/java/com/backend/domicare/service/imp/FileServiceImp.java
sed -i '' 's/logger\.error("Error deleting duplicate/logger.error("[File] Error deleting duplicate/g' src/main/java/com/backend/domicare/service/imp/FileServiceImp.java
sed -i '' 's/logger\.error("Error uploading file/logger.error("[File] Error uploading file/g' src/main/java/com/backend/domicare/service/imp/FileServiceImp.java
sed -i '' 's/logger\.info("File with public_id/logger.info("[File] File with public_id/g' src/main/java/com/backend/domicare/service/imp/FileServiceImp.java
sed -i '' 's/logger\.info("File with id/logger.info("[File] File with id/g' src/main/java/com/backend/domicare/service/imp/FileServiceImp.java
sed -i '' 's/logger\.error("Error deleting file from/logger.error("[File] Error deleting file from/g' src/main/java/com/backend/domicare/service/imp/FileServiceImp.java

# Update ProductServiceImp.java remaining logs
sed -i '' 's/logger\.info("Product added/logger.info("[Product] Product added/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Updating product/logger.info("[Product] Updating product/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.error("Main image not found/logger.error("[Product] Main image not found/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.error("Landing image not found/logger.error("[Product] Landing image not found/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Product updated/logger.info("[Product] Product updated/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Deleting product/logger.info("[Product] Deleting product/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.error("Product not found/logger.error("[Product] Product not found/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Product deleted/logger.info("[Product] Product deleted/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Fetching product with/logger.info("[Product] Fetching product with/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Product fetched/logger.info("[Product] Product fetched/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Fetching all products/logger.info("[Product] Fetching all products/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Products fetched/logger.info("[Product] Products fetched/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Adding image to product/logger.info("[Product] Adding image to product/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.error("Image not found with/logger.error("[Product] Image not found with/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.warn("Image with URL/logger.warn("[Product] Image with URL/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Image added/logger.info("[Product] Image added/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.info("Fetching products with IDs/logger.info("[Product] Fetching products with IDs/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.error("No products found/logger.error("[Product] No products found/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java

# Check for any remaining log statements that need to be updated
echo "Checking for any remaining log statements that might need to be updated:"
grep -r "logger\." --include="*.java" src/main/java/com/backend/domicare/service/imp/ | grep -v "\[\w*\]"
