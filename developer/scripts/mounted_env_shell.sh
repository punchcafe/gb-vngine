
MOUNT_DIR="$(dirname $0)/../.."
MOUNT_DIR="$(realpath $MOUNT_DIR)"
echo $MOUNT_DIR

docker run -i --mount type=bind,src=${MOUNT_DIR},dst=/root/src -t punchcafe/gbvngine-dev:local /bin/bash