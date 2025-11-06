.PHONY: test

run:
	go run main.go
test:
	go test ./...

_build:
	mkdir -p _build

_build/sample.c: _build
	go run main.go -o _build/sample.c -gs app/test/game-state-variables.yaml

_build/sample.gb: _build/sample.c
	${LCC} -o _build/sample.gb _build/sample.c	