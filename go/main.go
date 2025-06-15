package main

import (
	"flag"
	"os"

	"punchcafe.dev/gb-vngine/render"
)

func main() {
	var outputFile = flag.String("o", "game.c", "the output file name.")
	flag.Parse()
	main_code := render.RenderMain()

	os.WriteFile(*outputFile, []byte(main_code), 0644) // todo: undestand this
}
