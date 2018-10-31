package javax0.geci.tests.equals;

import javax0.geci.annotations.Geci;

import java.util.Objects;

@Geci("equals subclass='ok' useObjects='true'")
public class ClassNeedingEquals {

    private int anInt;
    private byte aByte;
    private boolean aBoolean;
    private char aChar;
    @Geci("equals exclude='yes'")
    private ClassNeedingEquals huss;
    private short aShort;
    private long aLong;
    private static long saLong;
    private float aFloat;
    private double aDouble;
    @Geci("equals notNull='true'")
    public Object x;
    Character h;


    public float getAFloat() {
        return aFloat;
    }

    //<editor-fold id="equals">
    @javax0.geci.annotations.Generated("equals")
    @Override
    public boolean equals(Object o) {
        if( this == o )return true;
        if (!(o instanceof ClassNeedingEquals)) return false;

        ClassNeedingEquals that = (ClassNeedingEquals)o;
        if( aBoolean != that.aBoolean )return false;
        if( aByte != that.aByte )return false;
        if( aChar != that.aChar )return false;
        if (Double.compare(that.aDouble, aDouble) != 0) return false;
        if (Float.compare(that.aFloat, aFloat) != 0) return false;
        if( aLong != that.aLong )return false;
        if( aShort != that.aShort )return false;
        if( anInt != that.anInt )return false;
        if (!Objects.equals(h,that.h)) return false;
        if (!Objects.equals(x,that.x)) return false;
        return true;
    }
    @javax0.geci.annotations.Generated("equals")
    @Override
    public int hashCode() {
        return Objects.hash(aBoolean,aByte,aChar,aDouble,aFloat,aLong,aShort,anInt,h,x);
        }
    //</editor-fold>
}
