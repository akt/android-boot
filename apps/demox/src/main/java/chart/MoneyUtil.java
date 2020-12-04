package chart;

/**
 * @author wcheng
 */
public final class MoneyUtil {
    /**
     * millisecond per second
     */
    public static final long MSPS = 1000;

    public static long YUANTOFEN(double dbMoney) {
        if (dbMoney >= 0) {
            return (long) (dbMoney * 100 + 0.5);
        } else {
            return (long) (dbMoney * 100 - 0.5);
        }
    }

    public static double FENTOYUAN(long nMoney) {
        return Double.valueOf(FEN2YUANSTR(nMoney));
    }

    public static String FEN2YUANSTR(long nMoney) {
        int sign = 1;
        if (nMoney < 0) {
            sign = -1;
            nMoney *= sign;
        }

        StringBuffer buf = new StringBuffer(20);
        buf.append(nMoney);
        int nLen = buf.length();
        for (int i = 3 - nLen; i > 0 && nLen <= 2; --i) {
            buf.insert(0, "0");
        }
        buf.insert(buf.length() - 2, ".");
        if (sign == -1) {
            buf.insert(0, "-");
        }

        return buf.substring(0, getDecimalLen(buf, 2, false));
    }

    /**
     * 元转分，带符号，例如+100，-100
     *
     * @param nMoney
     * @return
     */
    public static String Fen2YuanStringWithSymbol(long nMoney) {
        String str = FEN2YUANSTR(nMoney);
        if (!str.startsWith("-")) {
            str = "+" + str;
        }
        return str;
    }

    /**
     * Return the famated string. see more {@link #formatDecimal(double, int, boolean)}.
     *
     * @param data:    the double data used to format.
     * @param decByte: the byte number of fractional part.
     * @return the formated string.
     */
    public static String formatDecimal(double data, int decByte) {
        return formatDecimal(data, decByte, false);
    }

    /**
     * Return the formated string with the given data.
     *
     * @param data:         the double data used to format.
     * @param decByte:      the byte number of fractional part.
     * @param isCutEndZero: true to cut the useless ending zero(e.g. 1.00 -> 1), else not.
     * @return the formated string.
     */
    public static String formatDecimal(double data, int decByte, boolean isCutEndZero) {
        String zero = isCutEndZero ? "0" : "0.00";
        if (data == 0)
            return zero;

        //Make negative number unsigned to calculate.
        int nFlat = 1;
        if (data < 0) {
            nFlat = -1;
            data *= nFlat;
        }

        //Rounding here！
        if (decByte > 0) {
            data += 0.5 * Math.pow(0.1, decByte);
        }

        //To handle science number.
        StringBuffer buf = new StringBuffer(64);
        buf.append(data);
        do {
            String e = "E";
            int posE = buf.indexOf(e);
            if (posE < 0)
                break;

            StringBuffer bufTemp = new StringBuffer(64);
            String strMoney = Double.toString(data);
            String temp = strMoney.substring(posE + 1);
            int count = Integer.parseInt(temp);
            if (count < 0) {
                return zero;
            }
            int ptPos = strMoney.indexOf(".");
            int remine = count;
            bufTemp.append(strMoney.substring(0, posE));
            if (ptPos >= 0) {
                bufTemp.deleteCharAt(ptPos);
                remine = posE - (ptPos + 1);
                remine -= count;
            }
            if (remine > 0) {
                bufTemp.insert(bufTemp.length() - remine, '.');
            } else {
                remine *= -1;
                for (int i = 0; i < remine; ++i) {
                    bufTemp.append("0");
                }
            }

            buf = bufTemp;
        } while (false);

        int nDotPos = buf.indexOf(".");
        int nFractionLen = nDotPos + decByte + 1;
        if (!isCutEndZero && decByte > 0 && nDotPos > 0
                && nFractionLen > buf.length()) {
            for (int i = nFractionLen - buf.length(); i > 0; --i) {
                buf.append("0");
            }
        }

        //Make it sign again
        if (nFlat == -1) {
            buf.insert(0, "-");
        }

        return buf.substring(0, getDecimalLen(buf, decByte, isCutEndZero));
    }

    //get useful length(e.g. 1.00 = 1, 1.10 = 1.1).
    private static int getDecimalLen(StringBuffer buf, int decByte, boolean isCutEndZero) {
        if (buf == null || buf.length() <= 1)
            return 0;

        int nDotPos = buf.indexOf(".");
        if (nDotPos < 0)
            return buf.length();

        //Remove the bytes not needed.
        if (decByte > 0) {
            int nFractionLen = nDotPos + decByte + 1;
            if (nFractionLen < buf.length()) {
                buf.delete(nDotPos + decByte + 1, buf.length());
            } else {
                for (int i = nFractionLen - buf.length(); i > 0; --i) {
                    buf.append("0");
                }
            }
        } else {
            return buf.length();
        }

        int nLen = buf.length();
        int count = 0;
        for (int i = nLen - 1; isCutEndZero && i >= nDotPos; --i, ++count) {
            char c = buf.charAt(i);
            if (c != '0') {
                //Also remove the useless dot
                if (c == '.') {
                    ++count;
                }
                break;
            }
        }

        return nLen - count;
    }
}
