# Java::Geci

Java Generate Code Inline

Javageci is a framework to generate Java code. Code generation programs implemented using Java::Geci can be executed
to generate new source code, modify existing Java source files. This way the programmer can use meta programming to
express code in a shorter and more expressive way than it would be possible in pure Java.

The framework discovers the files that need generated code, provides easy to use API to generate code and
takes care to write the generated code into the source code. The code generating program should focus on the
actual code structure it wants to generate.

## When do you need Java::Geci?

There are several occasions when we need generated code. The simplest such scenarios are also supported by the
IDEs (Eclipse, NetBeans, IntelliJ). They can create setters, getters, constructors, `equals()` and `hashCode()`
methods in different ways. There are two major problems with that solution.

* One is that the code generation is manual, and in case the developer forgets to regenerate the code after an
  influencing change the code becomes outdated.

* The other problem is that the code generation possibilities are not extendable. There is a limited set
  of code that the tools can generate and the developer cannot easily extend these possibilities.

Java::Geci eliminates these two problems. It has an execution mode to check if all code generation is up-to-date and
this can be used as a unit test. If the developer forgot to update some of the generated code after the program
effecting the generated code was changed, the test will fail. 
(As a matter of fact the test also updates the generated code, you only need to start the build phase again.)

Java::Geci also has an extremely simple API supporting code generators so it is extremely simple to create new
code generators that are project specific. You do not even need to package your code generator classes. Just put
them into some of the test packages and execute Java::Geci during the test phase of the build process.

Java::Geci already includes several readily available code generators. These are packaged with the core package
and can generate

* setter and getter
* delegation methods (under development)
* fluent API classes and interfaces
* others will be under development

## How to use Java:Geci

If you look at the test code `TestAccessor.java` in the test module you can see that this is a proof of concept
demonstration sample code:

<!-- USE SNIPPET */TestAccessor -->
```java
package javax0.geci.tests.accessor;

import javax0.geci.accessor.Accessor;
import javax0.geci.engine.Geci;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static javax0.geci.api.Source.maven;

public class TestAccessor {

    @Test
    public void testAccessor() throws Exception {
        if (new Geci().source(maven().module("examples").javaSource()).register(new Accessor()).generate()) {
            Assertions.fail(Geci.FAILED);
        }
    }
}
```

The test runs during the build process and it generates files if that is needed. If files were generated it means that
the code was not up to date and a new compilation has to be done. In this case the `Assertions.fail()` will be invoked
and you have to start the build process again. Second time the code generation will recognize that the code it could
generate is already there and the process will not fail.

`Geci` has a fluent interface.

The method `source()` can be used to specify the directories where source files are. If you have the source files
in different places you have to chain several `source()` invocations one after the other. Every single call to
`source()` can specify several directories. These count as one single directory and the first that exists is used
to discover the files.

The method `register()` can register one or more source code generators. Each of them will be invoked on the code.
 
Finally the method invocation `generate()` wil do the work, read the source files and generate the code.

For further information visit the following documentations

* [Tutorials](TUTORIAL.md)
* [Reference documentation](REFERENCE.md)
* [How to guides](HOWTO.md)
* [Explanations](EXPLANATION.md)
* [Frequently Asked Questions](FAQ.md)

Generators provided with Java::Geci out of the box

* [Setter and getter](ACCESSOR.md)
* [Delegation](DELEGATOR.md)
* [Fluent API](FLUENT.md)
* [equals() and hashCode()](EQUALS.md)
* [object cloner](CLONER.md) (planned)
* [static dependency injection](INJECT.md) (planned)
* [proxy class](PROXY.md) (planned)
* [immutable proxy](IMMUTATOR.md)
