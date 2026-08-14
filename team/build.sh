#!/bin/bash

set -e

UPSTREAM_REGISTRY="dbeaver"
BUILD_REGISTRY="dbeaver-rev"
VERSION_TAG="26.1.0"
JDK_VERSION="21"
DBEAVER_IMAGES=(
  "cloudbeaver-dc:$VERSION_TAG:domain-controller:build"
  "cloudbeaver-qm:$VERSION_TAG:query-manager:build"
  "cloudbeaver-rm:$VERSION_TAG:resource-manager:build"
  "cloudbeaver-te:$VERSION_TAG:cloudbeaver:build"
  "cloudbeaver-tm:$VERSION_TAG:task-manager:build"
  "cloudbeaver-nginx:$VERSION_TAG:nginx:tag"
  "cloudbeaver-haproxy:$VERSION_TAG:haproxy:tag"
  "cloudbeaver-kafka:3.9:kafka:tag"
  "cloudbeaver-postgres:14:postgres:tag"
)

function remove_existing_images() {
  for item in "${DBEAVER_IMAGES[@]}"; do
    IFS=: read -r image tag _ _ <<<"$item"
    docker image rm -f "${BUILD_REGISTRY}/${image}:${tag}" || true
  done
}

function download_openjdk() {
  SITE_OPENJDK="https://jdk.java.net/archive/"
  JDK=${1:-"jdk21"}
  PLATFORM=${2:-"linux-x64"}
  DESTDIR=${3:-"$JDK-$PLATFORM"}
  if [ -d "$DESTDIR" ]; then
    echo "$DESTDIR already exists. Skipping download."
    return
  fi
  echo "Grabing OpenJDK $JDK for platform $PLATFORM from $SITE_OPENJDK"
  SITE_OPENJDK_CONTENT=$(curl -s "$SITE_OPENJDK")
  OPENJDK_URLS=$(echo "$SITE_OPENJDK_CONTENT" | grep -Eo "(http|https)://[a-zA-Z0-9./?=_-]*(\.tar\.gz)")
  DOWNLOAD=$(echo "$OPENJDK_URLS" | grep "$PLATFORM" | grep "$JDK" | head -n 1)
  SITE_VERSION=$(echo "$DOWNLOAD" | sed -e 's#.*jdk-\(.*\)_.*#\1#' | sed -e 's#^\(.*\)_.*#\1#')
  echo "Downloading OpenJDK v$SITE_VERSION / $PLATFORM. Please wait..."
  FILENAME=$(basename "$DOWNLOAD")
  if [ -f "$FILENAME" ]; then
    echo "$FILENAME already exists. Skipping download."
  else
    curl -o "$FILENAME" "$DOWNLOAD"
  fi
  mkdir -p "$DESTDIR"
  tar -xf "$FILENAME" -C "$DESTDIR/" --strip=1
}

function build_multi_platform_image() {
  base_image=$1
  target_image=$2
  product=$3
  docker buildx build -t "$target_image" \
    --platform linux/amd64,linux/arm64 \
    --build-arg BASE_IMAGE="$base_image" \
    --build-arg PRODUCT="$product" \
    --build-arg JDK="jdk$JDK_VERSION" \
    . --push
}

function tag_multi_platform_image() {
  base_image=$1
  target_image=$2
  docker buildx imagetools create -t "$target_image" "$base_image"
}

function build_image() {
  base_image=$1
  target_image=$2
  product=$3
  docker buildx build -t "$target_image" \
    --build-arg BASE_IMAGE="$base_image" \
    --build-arg PRODUCT="$product" \
    --build-arg JDK="jdk$JDK_VERSION" \
    . # --push
}

function tag_image() {
  base_image=$1
  target_image=$2
  docker tag "$base_image" "$target_image"
}

echo "Removing existing Docker images..."
remove_existing_images

echo "Downloading OpenJDK..."
# download_openjdk "jdk$JDK_VERSION" "linux-aarch64" "jdk$JDK_VERSION/linux/arm64"
download_openjdk "jdk$JDK_VERSION" "linux-x64" "jdk$JDK_VERSION/linux/amd64"

echo "Pulling upstream Docker images..."
for item in "${DBEAVER_IMAGES[@]}"; do
    IFS=: read -r image tag _ _ <<< "$item"
    echo "Pulling Docker image: ${UPSTREAM_REGISTRY}/${image}:${tag}"
    docker pull "${UPSTREAM_REGISTRY}/${image}:${tag}"
done

echo "Building or tagging Docker images..."
for item in "${DBEAVER_IMAGES[@]}"; do
  IFS=: read -r image tag product build <<<"$item"
  if [ "$build" == "build" ]; then
    echo "Building Docker image: ${BUILD_REGISTRY}/${image}:${tag} for product: $product"
    # build_multi_platform_image "${UPSTREAM_REGISTRY}/${image}:${tag}" "${BUILD_REGISTRY}/${image}:${tag}" "$product"
    build_image "${UPSTREAM_REGISTRY}/${image}:${tag}" "${BUILD_REGISTRY}/${image}:${tag}" "$product"
  else
    echo "Skipping build for Docker image: ${BUILD_REGISTRY}/${image}:${tag} (no build required)"
    # tag_multi_platform_image "${UPSTREAM_REGISTRY}/${image}:${tag}" "${BUILD_REGISTRY}/${image}:${tag}"
    tag_image "${UPSTREAM_REGISTRY}/${image}:${tag}" "${BUILD_REGISTRY}/${image}:${tag}"
  fi
done
