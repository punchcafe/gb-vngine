### Initial limitations:
- No chapter variables (allows us to have hard coded game state)
- No more than one chapter
- Character Limit on string variables
- Character limit on prompts to fit in one line

## Dev notes TODO
- Implement callback pattern for `play_narrative` so it updates gamestate
- Separate `play_narrative` file into `play_narrative` utility functions and `play_narrative_bound`
- Implement get_next_node methods

# Roadmap

## Features
### 0.1.0
- ~Bankable Assets~ ✅
- Predicate user selection branches 🚧
- Saving
- vNgine splash page
### 0.2.0
- JSON Schema for vngine
- RNG schema for XML
- Narrative-injectable variables
- Node variable type
- Next node as $node.node-id
- Player input node
- Rand function
- Prompts as asset IDs


### Future nice to haves
- Animations on backgrounds (using delta format)
- Animation on characters (using delta format)

## Tasks
- Sections refactor:
```
Tree -- all thing around rending the game tree. Nodes, branches and prompts (also contains a "setup" component, which stays
with the service class context but is composed for the setup controller rendering).
Predicate -- Predicate service for collecting all predicates and returning source names for a given expression (model)
Text -- Manages all written text in the narratives. Will be the source of truth for any compressions around commonly used words.

```
- Full documentation 
- Clean up C functions
- Investigate use of `SWITCH_ROM_MBC1(external_asset->bank_number);`. If it needs specifics, can do as Macro def
- Choose bank number `unsigned int` or `unsigned short` based on bank config
- create a testing framework

For testing, consider:
- A logic flow tester, which replaces the gb.h function with mocked alternatives, and runs all provided tests.
- An actual player, which can take snapshots at designated intervals and compare like a snapshot test
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

