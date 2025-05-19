package com.pricecomparator.market.Service.Utilities;

public class MetricConversion {
    public static double KgToGram(int kg) {
        return kg * 1000;
    }
    public static double GramToKg(int gram) {
        return gram / 1000;
    }
    public static double LitreToMl(int litre)
    {
        return litre * 1000;
    }
    public static double MltoLitre(int ml)
    {
        return ml / 1000;
    }
}
