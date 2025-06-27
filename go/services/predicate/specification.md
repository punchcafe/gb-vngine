
examples

operators:
`more_than (int, int)`
`less_than (int, int)`
`equals (int, int) | (str, str) | (bool,bool)`
`or (exp, exp)`
`and (exp, exp)`

expression:
`operator`
`bool`
`bool_ref`

types:
```
predicate system type, resolve type
-----------------
expression (...) -> bool delimiter: (\(^\)+\)"
int_ref -> int
int_literal -> int
bool_ref -> bool
bool_literal -> bool 
string_ref -> string delimiter: $[a-z-]+
string_literal -> string, delimiter: "[^"]+"
```

examples:
```
true
true and true
true and ($my_bool_var) // when bool reference
true and ($my_int_var more_than 5)
"hello" equals "goodbye"
("hello" equals "goodbye") or ($word equals "hello")
true equals ($my_int less_than $other_int)
``` 


Alogrithm:
Split by "token":
- brackets
- literals (including strings, making strings a single token)
- operators
- references

All compilation looks at:
- expects operand, operator, operand

