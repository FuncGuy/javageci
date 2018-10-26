package javax0.geci.tests.fluent;

import javax0.geci.engine.Geci;
import javax0.geci.fluent.Fluent;
import javax0.geci.fluent.FluentBuilder;
import javax0.geci.tutorials.xmlbuilder.XmlBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static javax0.geci.api.Source.maven;

public class TestFluent {


    public static FluentBuilder definition() {
        var t = FluentBuilder.from(SimpleSample.class).start("sample").cloner("copy");
        return t.oneOrMore(t.oneOf(t.one("a").one("b"), t.one("c").one("d"))).oneOf("get", "got");
    }

    public static FluentBuilder regex() {
        var t = FluentBuilder.from(Regex.class).start("pattern").cloner("copy");
        return t.oneOrMore(t.oneOf("terminal", "set", "optional", "zeroOrMore", "oneOrMore", "more")).oneOf("get");
    }

    public static FluentBuilder xml() {
        var t = FluentBuilder.from(XmlBuilder.class).cloner("copy").autoCloseable();
        var tag = t.one("tag").zeroOrMore("attribute").optional("text");
        var xml = t.oneOrMore(tag).one("toString");
        return xml;
    }

    @Test
    public void testXmlBuilder() throws Exception {
        var xml = XmlBuilder.start().tag("alma").attribute("a", "b").text("kakukk");
        try (xml) {
            xml.tag("hamar").close();
        }
        Assertions.assertEquals("", xml.toString());
    }

    @Test
    public void testRegex() {
        var rx = Regex.pattern();
        Pattern pt = Regex.pattern().oneOrMore(Regex.pattern().terminal("a").oneOrMore(rx.terminal("b")).terminal("a")).get();
        Assertions.assertEquals("(?:a(?:b)+a)+", pt.toString());
    }

    @Test
    public void testFluent() throws Exception {
        if (new Geci().source(maven().module("examples").javaSource()).register(new Fluent()).generate()) {
            Assertions.fail("Fluent modified source code. Please compile again.");
        }
    }

}
