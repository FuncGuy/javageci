# Terminology and Definitions

In this document we define certain terms that we use in the
documentation and are not general terms.

## Generator

A _generator_ is a class that implements the `javax0.geci.api.Generator`
interface. Generators usually generate code, but it is not a must. There
are generators that only collect information from the source code and
make it available for other code generators. 

## Segment

A _segment_ is a series of lines in the source code where generated code
is inserted. When a segment already has some code then these will be
overwritten during code generation.

Each segment has an identifier that has to be unique within a single
file.

A _segment_ starts and ends with special lines that are recognized by
segment split helpers.

## Segment Split Helper

A segment split helper is a class that helps the framework to split a
source code into a series of blocks that contain manual code and
segments.

Segment split helpers apply regular expressions and code logic to find
the start and the end of a segment.

## Snippet

The term _snippet_ is used in the document management generators. A
_snippet_ is a piece of source code, which is copied from the source
code and inserted into some documentation file after optional
transformation.

A _snippet_ usually starts ends with special lines that are recognized
by regular expressions.

## Snippet Collector

A snippet collector is a generator that reads the source files and
copies the snippets from the source into a memory structure (called
snippet store). Other document management handling generators later use
this store to insert documentation in formation into documentation files
into segments.

## Target Class

When a generator executes it creates a new class or it injects code into
the source code of an already existing class. The class that the
generator creates or the class into which the generator injects new,
generated code is called _target class_.
