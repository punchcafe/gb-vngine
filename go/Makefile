.PHONY: test run regenerate_app_test

run:
	go run main.go

test:
	go clean -testcache && go test ./...

regenerate_app_test:
	go run main.go -o app/test/expected_source.c \
	-gs app/test/game-state-variables.yaml \
	-c app/test/chapter-sample.yaml

clean:
	rm -rf _build

_build:
	mkdir -p _build

_build/sample.c: _build
	go run main.go -o _build/sample.c -gs app/test/game-state-variables.yaml -c app/test/chapter-sample.yaml

_build/sample.gb: _build/sample.c
	${LCC} -o _build/sample.gb _build/sample.c	