package javax0.geci.equals;

import javax0.geci.api.GeciException;
import javax0.geci.api.Segment;
import javax0.geci.api.Source;
import javax0.geci.tools.AbstractGenerator;
import javax0.geci.tools.CompoundParams;
import javax0.geci.tools.Tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Equals extends AbstractGenerator {
    private static final int NOT_PACKAGE = Modifier.PROTECTED | Modifier.PRIVATE | Modifier.PUBLIC;

    @Override
    public String mnemonic() {
        return "equals";
    }

    @Override
    public void process(Source source, Class<?> klass, CompoundParams global) throws Exception {
        final var gid = global.get("id");
        source.init(gid);
        generateEquals(source, klass, global);
        generateHashCode(source, klass, global);
    }

    private void generateEquals(Source source, Class<?> klass, CompoundParams global) throws Exception {
        final var fields = Tools.getDeclaredFieldsSorted(klass);
        final var gid = global.get("id");
        var segment = source.open(gid);
        var equalsMethod = getEqualsMethod(klass);
        if (equalsMethod == null || Tools.isGenerated(equalsMethod)) {
            segment.write("@javax0.geci.annotations.Generated(\"equals\")");
            segment.write("@Override");
            segment.write_r("public boolean equals(Object o) {");
            segment.write("if( this == o )return true;");
            if (global.is("subclass")) {
                segment.write("if (!(o instanceof %s)) return false;", klass.getSimpleName());
            } else {
                segment.write("if (o == null || getClass() != o.getClass()) return false;");
            }
            segment.newline();
            segment.write("%s that = (%s)o;", klass.getSimpleName(), klass.getSimpleName());
            for (final var field : fields) {
                var local = Tools.getParameters(field, mnemonic());
                var params = new CompoundParams(local, global);
                var primitive = field.getType().isPrimitive();
                if (isNeeded(field, params)) {
                    var name = field.getName();
                    if (primitive) {
                        var type = field.getType();
                        if (field.getType().equals(float.class)) {
                            segment.write("if (Float.compare(that.%s, %s) != 0) return false;", name, name);
                        } else if (field.getType().equals(double.class)) {
                            segment.write("if (Double.compare(that.%s, %s) != 0) return false;", name, name);
                        } else {
                            segment.write("if( %s != that.%s )return false;", name, name);
                        }
                    } else {
                        if (params.is("useObjects")) {
                            segment.write("if (!Objects.equals(%s,that.%s)) return false;", name, name);
                        } else {
                            if (params.is("notNull") ) {
                                segment.write("if (!%s.equals(that.%s)) return false;", name, name);
                            } else {
                                segment.write("if (%s != null ? !%s.equals(that.%s) : that.%s != null) return false;",
                                        name, name, name, name);
                            }
                        }
                    }
                }
            }
            segment.write("return true;");
            segment.write_l("}");
        }
    }

    private Method getEqualsMethod(Class<?> klass) {
        try {
            return klass.getDeclaredMethod("equals", Object.class);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    private Method getHashCodeMethod(Class<?> klass) {
        try {
            return klass.getDeclaredMethod("hashCode");
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    private void generateHashCode(Source source, Class<?> klass, CompoundParams global) throws Exception {
        final var fields = Tools.getDeclaredFieldsSorted(klass);
        final var gid = global.get("id");
        var segment = source.open(gid);
        var hashCodeMethod = getHashCodeMethod(klass);
        var useObjects = global.is("useObjects");
        if (hashCodeMethod == null || Tools.isGenerated(hashCodeMethod)) {
            segment.write("@javax0.geci.annotations.Generated(\"equals\")");
            segment.write("@Override");
            segment.write_r("public int hashCode() {");
            if (useObjects) {
                var fieldNamesCSV = new StringBuilder();
                var separator = Tools.separator(",");
                for (final var field : fields) {
                    var local = Tools.getParameters(field, mnemonic());
                    var params = new CompoundParams(local, global);
                    if (isNeeded(field, params)) {
                        fieldNamesCSV.append(separator.get()).append(field.getName());
                    }
                }
                segment.write_r("return Objects.hash(%s);", fieldNamesCSV);
            } else {
                segment.write("int result = 0;");
                if (Arrays.stream(fields)
                        .filter(f -> !Modifier.isStatic(f.getModifiers()))
                        .map(Field::getType)
                        .anyMatch(c -> c.equals(double.class))) {
                    segment.write("long temp;");
                }
                segment.newline();
                for (final var field : fields) {
                    var local = Tools.getParameters(field, mnemonic());
                    var params = new CompoundParams(local, global);
                    var primitive = field.getType().isPrimitive();
                    if (isNeeded(field, params)) {
                        var name = field.getName();
                        if (primitive) {
                            var type = field.getType();
                            if (field.getType().equals(boolean.class)) {
                                segment.write("result = 31 * result + (%s ? 1 : 0);", name);
                            } else if (field.getType().equals(long.class)) {
                                segment.write("result = 31 * result + (int) (%s ^ (%s >>> 32));", name, name);
                            } else if (field.getType().equals(float.class)) {
                                segment.write("result = 31 * result + (%s != +0.0f ? Float.floatToIntBits(%s) : 0);",
                                        name, name);
                            } else if (field.getType().equals(double.class)) {
                                segment.write("temp = Double.doubleToLongBits(%s);", name);
                                segment.write("result = 31 * result + (int) (temp ^ (temp >>> 32));");
                            } else {
                                segment.write("result = 31 * result + (int) %s;", name);
                            }
                        } else {
                            if (params.is("notNull")) {
                                segment.write("result = 31 * result + %s.hashCode();", name);
                            } else {
                                segment.write("result = 31 * result + (%s != null ? %s.hashCode() : 0);",
                                        name, name);
                            }
                        }
                    }
                }
                segment.write("return result;");
            }
            segment.write_l("}");
        }
    }

    private boolean isNeeded(Field field, CompoundParams params) {
        return params.isNot("exclude") && !Modifier.isStatic(field.getModifiers());
    }
}