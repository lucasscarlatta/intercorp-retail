package com.example.demo.utils;

import java.text.DecimalFormat;

public class Util {

    public static Double decimalTwoDigits(double decimalNumber) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        return Double.parseDouble(df.format(decimalNumber));
    }
}
