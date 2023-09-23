
NAME := pgstore

DOCKER := docker
DOCKER_RUN := $(DOCKER) run
DOCKER_BUILD := $(DOCKER) build


.PHONY: build run stop rm clean help

build:
	@echo "Building $(NAME)"
	$(DOCKER_BUILD) -t $(NAME) .

run:
	@echo "Building and running $(NAME)"
	$(DOCKER_BUILD) -t $(NAME) .
	$(DOCKER_RUN) --name $(NAME) -d -p 5431:5432 $(NAME)

stop:
	@echo "Stopping $(NAME)"
	$(DOCKER) stop $(NAME)

rm:
	@echo "Removing $(NAME)"
	$(DOCKER) rm $(NAME)

clean: stop rm

help:
	@echo "Available targets:"
	@echo "  - build: Build the container."
	@echo "  - run: Build and run the container."
	@echo "  - stop: Stop the container."
	@echo "  - rm: Remove the container."
	@echo "  - clean: Stop and remove the container."
