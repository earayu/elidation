package elidation.utils;

/**
 * Created by earay on 2017/2/23.
 */
public class StringUitls {
    public static boolean isEmpty(String str)
    {
        return str==null || str.isEmpty();
    }

    public static boolean isEquals(String str1, String str2)
    {
        if(!isEmpty(str1))
        {
            return str1.equals(str2);
        }
        return false;
    }

    public static boolean isEqualsIgnoreCase(String str1, String str2)
    {
        if(!isEmpty(str1))
        {
            return str1.equalsIgnoreCase(str2);
        }
        return false;
    }
}
