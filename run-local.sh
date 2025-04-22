#!/bin/bash
set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== Starting DomiCare Backend on port 8080 ===${NC}"

# Check if docker-compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Error: docker-compose is not installed.${NC}"
    exit 1
fi

# Start the application using docker-compose
echo -e "${YELLOW}Starting services with docker-compose...${NC}"
docker-compose up -d

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Successfully started DomiCare Backend!${NC}"
    echo -e "${GREEN}The application is now running at: http://localhost:8080${NC}"
    
    # Show running containers
    echo -e "${YELLOW}Running containers:${NC}"
    docker-compose ps
else
    echo -e "${RED}Failed to start services.${NC}"
    exit 1
fi

echo -e "${YELLOW}To stop the application, run: docker-compose down${NC}"