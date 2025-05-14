#!/bin/bash
# Build and push Docker multi-architecture images

echo "Building and pushing multi-architecture Docker image..."
docker buildx build --platform linux/amd64,linux/arm64 \
  -t trunganh0106/domicare-web:v1.5 \
  --push .

echo "Done!"
