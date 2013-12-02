package net.littlelite.utils;

/**
 *
 * Alessio Saltarin General Library Utils
 * package name.alessiosaltarin.utils
 * Copyright Alessio Saltarin 2001-2008
 *
 */
/**
 * Metrical/English System Conversions
 * 
 * @since 01/2003
 * @version 1.0
 */
public class Conversion
{

    private static final float KGLBS = (float) 2.2046;
    private static final float CMINCHES = (float) 0.3937;

    public static int kg2lbs(int kg)
    {
        return Math.round(kg * KGLBS);
    }

    public static int lbs2kg(int lbs)
    {
        return Math.round(lbs / KGLBS);
    }

    public static float kg2lbs(float kg)
    {
        return kg * KGLBS;
    }

    public static float lbs2kg(float lbs)
    {
        return lbs / KGLBS;
    }

    public static int cm2inches(int cm)
    {
        return Math.round(cm * CMINCHES);
    }

    public static int inches2cm(int inches)
    {
        return Math.round(inches / CMINCHES);
    }

    public static float inches2cm(float cm)
    {
        return cm * CMINCHES;
    }

    public static float cm2inches(float inches)
    {
        return inches / CMINCHES;
    }
}
