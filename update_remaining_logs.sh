#!/bin/bash

# Update remaining UserServiceImp.java logs
sed -i '' 's/logger\.debug("Fetching user with email/logger.debug("[User] Fetching user with email/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Update failed - no authenticated/logger.warn("[User] Update failed - no authenticated/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Update failed - user not found with current/logger.warn("[User] Update failed - user not found with current/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Updating information for user/logger.debug("[User] Updating information for user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Updated name for user/logger.debug("[User] Updated name for user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Updated phone for user/logger.debug("[User] Updated phone for user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Updated address for user/logger.debug("[User] Updated address for user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Updated date of birth for user/logger.debug("[User] Updated date of birth for user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Updated gender for user/logger.debug("[User] Updated gender for user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Password update failed - old password/logger.warn("[User] Password update failed - old password/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Updated password for user/logger.debug("[User] Updated password for user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Password update failed - incorrect/logger.warn("[User] Password update failed - incorrect/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Avatar update failed/logger.warn("[User] Avatar update failed/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Updated avatar for user/logger.debug("[User] Updated avatar for user/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Role update failed/logger.warn("[User] Role update failed/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Adding role/logger.debug("[User] Adding role/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("Token deletion failed/logger.warn("[User] Token deletion failed/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("Deleted /logger.debug("[User] Deleted /g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.debug("No refresh tokens found/logger.debug("[User] No refresh tokens found/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java
sed -i '' 's/logger\.warn("User creation by admin failed/logger.warn("[User] User creation by admin failed/g' src/main/java/com/backend/domicare/service/imp/UserServiceImp.java

# Update remaining ProductServiceImp.java logs
sed -i '' 's/logger\.error("Old category not found/logger.error("[Product] Old category not found/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.error("Product with ID: {} not found in old category/logger.error("[Product] Product with ID: {} not found in old category/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java
sed -i '' 's/logger\.error("New category not found/logger.error("[Product] New category not found/g' src/main/java/com/backend/domicare/service/imp/ProductServiceImp.java

# Check for any remaining log statements that need to be updated
echo "Checking for any remaining log statements that might need to be updated:"
grep -r "logger\." --include="*.java" src/main/java/com/backend/domicare/service/imp/ | grep -v "\[\w*\]"
