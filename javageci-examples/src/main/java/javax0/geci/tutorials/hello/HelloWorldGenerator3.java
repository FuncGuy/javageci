package javax0.geci.tutorials.hello;

import javax0.geci.api.GeciException;
import javax0.geci.api.Source;
import javax0.geci.tools.AbstractJavaGenerator;
import javax0.geci.tools.CompoundParams;

public class HelloWorldGenerator3 extends AbstractJavaGenerator {
    public void process(Source source, Class<?> klass, CompoundParams global) throws Exception {
        try {
            final var segment = source.open(global.get("id"));
            final var methodName = global.get("methodName","hello");
            segment.write_r("public static void %s(){",methodName);
            segment.write("System.out.println(\"Hello, World\");");
            segment.write_l("}");
        } catch (Exception e) {
            throw new GeciException(e);
        }
    }

    public String mnemonic() {
        return "HelloWorld3";
    }
}
