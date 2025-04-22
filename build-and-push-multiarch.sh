#!/bin/bash
set -e

# Configuration
DOCKER_REGISTRY="docker.io"  # Change to your registry (Docker Hub, ECR, etc.)
IMAGE_NAME="trunganh0106/domicare-web"
IMAGE_TAG="latest"
PLATFORMS="linux/amd64,linux/arm64"  # Common platforms - x86_64 and ARM64

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== Building Multi-Architecture Docker Image for DomiCare Backend ===${NC}"

# Check if the JAR file exists
if [ ! -f "target/domicare-0.0.1-SNAPSHOT.jar" ]; then
    echo -e "${RED}Error: JAR file not found. Building the application first...${NC}"
    
    # Build the application with Maven
    ./mvnw clean package -DskipTests
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}Maven build failed. Please fix the errors and try again.${NC}"
        exit 1
    fi
fi

# Setup buildx builder if it doesn't exist
builder_name="multiarch-builder"

if ! docker buildx inspect $builder_name &>/dev/null; then
    echo -e "${YELLOW}Creating new Docker buildx builder: $builder_name${NC}"
    docker buildx create --name $builder_name --driver docker-container --bootstrap
else
    echo -e "${GREEN}Using existing Docker buildx builder: $builder_name${NC}"
    docker buildx use $builder_name
fi

# Login to Docker registry if credentials are provided
if [ -n "$DOCKER_USERNAME" ] && [ -n "$DOCKER_PASSWORD" ]; then
    echo -e "${YELLOW}Logging into Docker registry...${NC}"
    echo "$DOCKER_PASSWORD" | docker login $DOCKER_REGISTRY -u $DOCKER_USERNAME --password-stdin
fi

# Full image name with registry
FULL_IMAGE_NAME="$DOCKER_REGISTRY/$IMAGE_NAME:$IMAGE_TAG"

echo -e "${YELLOW}Building and pushing image: $FULL_IMAGE_NAME for platforms: $PLATFORMS${NC}"

# Build and push the multi-architecture image
docker buildx build \
    --platform $PLATFORMS \
    --tag $FULL_IMAGE_NAME \
    --push \
    .

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Successfully built and pushed multi-architecture image: $FULL_IMAGE_NAME${NC}"
    
    # Show the image details
    echo -e "${YELLOW}Image details:${NC}"
    docker buildx imagetools inspect $FULL_IMAGE_NAME
else
    echo -e "${RED}Failed to build and push the image.${NC}"
    exit 1
fi

echo -e "${GREEN}=== Process completed successfully ===${NC}"