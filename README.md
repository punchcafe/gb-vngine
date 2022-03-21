### Initial limitations:
- No chapter variables (allows us to have hard coded game state)
- No more than one chapter
- Character Limit on string variables
- Character limit on prompts to fit in one line


# Roadmap

## Features
### 0.1.0
- Bankable Assets
- Predicate user selection branches
- Saving
- vNgine splash page
### 0.2.0
- Narrative-injectable variables
- Node variable type
- Next node as $node.node-id
- Player input node
- Prompts as asset IDs

## Tasks
- Full documentation 
- Clean up C functions
- Investigate use of `SWITCH_ROM_MBC1(external_asset->bank_number);`. If it needs specifics, can do as Macro def
- Choose bank number `unsigned int` or `unsigned short` based on bank config
## Refactors
#### `SourceName`
- Add `SourceName` type, and add to all classes currently using string.
- Have `SourceName` deriving functions for regular strings
potentially:
```java
/**
The source name for something. Typically this can be seen as the 'source of truth' name, and whatever is 
produced by it's toString() method will be the way to reference it within C code.
**/
class SourceName {
    private final SourceName prefix;
    private final SourceName suffix;
    private final String name;
    
    // static methods for generating

    public String toString(){
        return Stream.of(prefix, name, suffix)
        .filter(Objects::nonNull)
        .collect(joining("_");
    }
}
```

