PROJECT_ROOT="$(dirname $0)/../.."

docker buildx build $PROJECT_ROOT -f developer.dockerfile -t punchcafe/gbvngine-dev:local