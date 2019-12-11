package com.lcs.util;

import java.math.BigDecimal;

/**
 */
public class TaxUtil {
    public static BigDecimal cumulativeTaxation(BigDecimal amount) {
        BigDecimal result = new BigDecimal(0);
        if (amount.compareTo(new BigDecimal(30000)) <= 0) {
            //不超过30000   税率5%
            result = amount.multiply(new BigDecimal(0.05));

        } else if (amount.compareTo(new BigDecimal(30000)) > 0 && amount.compareTo(new BigDecimal(90000)) <= 0) {
            //30000 - 90000  税率10%  - 速算扣除数1500
            result = amount.multiply(new BigDecimal(0.1)).subtract(new BigDecimal(1500));

        } else if (amount.compareTo(new BigDecimal(90000)) > 0 && amount.compareTo(new BigDecimal(300000)) <= 0) {
            //90000 - 300000  税率20%  - 速算扣除数10500
            result = amount.multiply(new BigDecimal(0.2)).subtract(new BigDecimal(10500));

        } else if (amount.compareTo(new BigDecimal(300000)) > 0 && amount.compareTo(new BigDecimal(500000)) <= 0) {
            //300000 - 500000  税率30%  - 速算扣除数40500
            result = amount.multiply(new BigDecimal(0.3)).subtract(new BigDecimal(40500));

        } else if (amount.compareTo(new BigDecimal(500000)) > 0) {
            //大于500000  税率35%  - 速算扣除数65500
            result = amount.multiply(new BigDecimal(0.35)).subtract(new BigDecimal(65500));

        }
        return result;
    }
}
