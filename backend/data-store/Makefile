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
	@echo "[INFO] Building $(BINARY_NAME)"
	$(GOBUILD) -o $(BINARY_NAME) -ldflags "-X main.BuildDate=`date +%Y%m%d-%H%M%S`" $(LOCATED)

generate:
	@echo "[INFO] Generating $(BINARY_NAME)"
	@echo "[INFO] Running generator"
	protoc --go_out=./pb --go_opt=paths=source_relative --go-grpc_out=./pb --go-grpc_opt=paths=source_relative --proto_path=../clap/proto/ $(PROTO_FILES)
	@echo "[INFO] Running sqlc"
	sqlc generate


clean:
	@echo "[INFO] Cleaning up..."
	$(GOCLEAN)
	rm -f $(BINARY_NAME)
	rm -f $(DEBUG_EXEC)

run:
	@echo "[INFO] Building and running $(DEBUG_EXEC)"
	$(GOBUILD) -o $(DEBUG_EXEC) -ldflags "-X main.BuildDate=`date +%Y%m%d-%H%M%S`" $(LOCATED)
	$(DEBUG_EXEC) -debug

run-dict:
	@echo "[INFO] Building and running $(DEBUG_EXEC)"
	$(GOBUILD) -o $(DEBUG_EXEC) -ldflags "-X main.BuildDate=`date +%Y%m%d-%H%M%S`" $(LOCATED)
	$(DEBUG_EXEC) -debug -initdict

install:
	@echo "[INFO] Installing dependencies..."
	$(GOGET)

.PHONY: help
help:
	@echo "[INFO] Available targets:"
	@echo "[INFO]   - build: Compile the project."
	@echo "[INFO]   - generate: Generate features and protobuf files."
	@echo "[INFO]   - clean: Clean up build artifacts."
	@echo "[INFO]   - run: Build and run the project."
	@echo "[INFO]   - install: Install project dependencies."
