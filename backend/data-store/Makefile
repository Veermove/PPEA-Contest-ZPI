# Project Name
BINARY_NAME := data_store

PROTO_FILES := data_store.proto

# Go parameters
GOCMD := go
LOCATED := ./cmd/
DEBUG_EXEC := $(LOCATED)$(BINARY_NAME)
GOBUILD := $(GOCMD) build
GOCLEAN := $(GOCMD) clean
GOGET := $(GOCMD) get

.PHONY: all build clean run install generate help

all: build

build:
	@echo "Building $(BINARY_NAME)"
	$(GOBUILD) -o $(BINARY_NAME) -ldflags "-X main.BuildDate=`date +%Y%m%d-%H%M%S`" $(LOCATED)

generate:
	@echo "Generating $(BINARY_NAME)"
	@echo "Running generator"
	protoc --go_out=./pb --go_opt=paths=source_relative --go-grpc_out=./pb --go-grpc_opt=paths=source_relative --proto_path=../clap/proto/ $(PROTO_FILES)


clean:
	@echo "Cleaning up..."
	$(GOCLEAN)
	rm -f $(BINARY_NAME)
	rm -f $(DEBUG_EXEC)

run:
	@echo "Building and running $(DEBUG_EXEC)"
	$(GOBUILD) -o $(DEBUG_EXEC) -ldflags "-X main.BuildDate=`date +%Y%m%d-%H%M%S`" $(LOCATED)
	$(DEBUG_EXEC) -debug

install:
	@echo "Installing dependencies..."
	$(GOGET)

.PHONY: help
help:
	@echo "Available targets:"
	@echo "  - build: Compile the project."
	@echo "  - generate: Generate features and protobuf files."
	@echo "  - clean: Clean up build artifacts."
	@echo "  - run: Build and run the project."
	@echo "  - install: Install project dependencies."
