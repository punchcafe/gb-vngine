package dev.punchcafe.gbvng.gen.mbanks.assets;

import dev.punchcafe.gbvng.gen.mbanks.BankableAssetBase;
import dev.punchcafe.gbvng.gen.mbanks.renderers.AssetVisitor;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 *
 * Read in from a given Music file, strip the first few lines, including the pragma
 * bank, calculate the size, then store body as pure string.
 */
public class BackgroundMusic extends BankableAssetBase {

    private Pattern DATA_AT_FUNCTION = Pattern.compile("(const void __at\\((.+)\\) __bank_.+_Data;)");

    private String id;
    private boolean hasSwappedBankNumber = false;
    @Getter private List<String> body;

    public BackgroundMusic(final File songFile){
        this.id = songFile.getName().substring(0, songFile.getName().length() - 2);
        List<String> allLines;
        try {
            allLines = Files.lines(songFile.toPath()).collect(Collectors.toList());
        } catch (IOException ex){
            throw new RuntimeException();
        }
        final var bodyList = allLines.subList(5, allLines.size());
        final var pattern = Pattern.compile(String.format("const unsigned char %s0\\[] = \\{", this.id));
        if(!pattern.matcher(bodyList.get(0)).matches()){
            System.out.println(bodyList.get(0));
            throw new RuntimeException(String.format("unexpected format for sound format file: %s", bodyList.get(0)));
        }
        this.body = bodyList;
    }

    private String mapAtFunction(final String line){
        final var matcher = DATA_AT_FUNCTION.matcher(line);
        if(matcher.matches()) {
            return matcher.replaceFirst(String.format("$1%d$3", this.assignedBank));
        }
        return line;
    }

    @Override
    public long getSize() {
        final var stringBuilder = new StringBuilder();
        int scope = 0;
        for(final char character :  this.body.stream().collect(joining()).toCharArray()){
            if(character == '{'){
                scope++;
            } else if(character == '}') {
                scope--;
            } else {
                if(scope == 1){
                    stringBuilder.append(character);
                }
            }
        }
        final int numberOfBytes = stringBuilder.toString().split(",").length;

        // TODO: remove and replace with actual accurate representation
        return numberOfBytes * 3;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public <T> T acceptVisitor(AssetVisitor<T> visitor) {
        return visitor.visitBackgroundMusicAsset(this);
    }


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
