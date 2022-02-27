package dev.punchcafe.gbvng.gen.mbanks.assets;

/**
 * TODO:
 *
 * Read in from a given Music file, strip the first few lines, including the pragma
 * bank, calculate the size, then store body as pure string.
 */
public class BackgroundMusic {

    /**
     * Something like:
     *
     * public BackgroundMusic fromExistingGBTPlayerFile(final File file){
     *      // check first lines of file are what we expect, including pragma bank
     *      // find all data blocks by regex, {}, and then the last _Data block.
     *
     *      collect blocks into array, remove newlines, then calculate size from splitting by comma.
     *      size of the data is then just the number of entries.
     * }
     *
     */
}
