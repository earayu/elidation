package elidation;


/**
 * Created by Administrator on 2016/7/6.
 */
public class Validate {

    private static final String MAX_N_Text = ".{1,N}";  //MaxNText

    private static final String MAX_N_NUMERIC_TEXT = "[0-9]{1,N}"; //MaxNumericText

    private static final String ISO_DATE_TIME = "\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d";  //ISODateTime

    private static final String ISO_DATE = "\\d\\d\\d\\d-\\d\\d-\\d\\d";    //ISODate

    public static String maxNText(Integer n)
    {
        checkNotNull(n);
        String regex = MAX_N_Text.replace("N", String.valueOf(n));
        return regex;
    }

    public static String maxNNumericText(Integer n)
    {
        checkNotNull(n);
        String regex = MAX_N_NUMERIC_TEXT.replace("N", String.valueOf(n));
        return regex;
    }

    public static String isoDate()
    {
        String regex = ISO_DATE;
        return regex;
    }

    public static String isoDateTime()
    {
        String regex = ISO_DATE_TIME;
        return regex;
    }


    public static <T> T checkNotNull(T reference) {
        if(reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }






}
