package com.example.liao10;

public class Toolkit {

    public static final char CHARACTER_COMMENT = '#';
    public static final char CHARACTER_DIVIDER = '=';

    public static int split(String str, char c, String strs[], int size)
    {

        if(str.isEmpty() || size < 1)
        {
            return -1;
        }

        str = str + c;

        String tmp = "";
        int len = str.length();
        int cnt = 0;
        for(int i = 0; i < len; i ++)
        {
            if(str.charAt(i) == c)
            {
                    strs[cnt ++] = tmp;
                    tmp = "";
                    if(cnt >= size)
                    {
                            return size;
                    }
            }
            else
            {
                tmp = tmp + str.charAt(i);
            }
        }

        return cnt;
    }

    // Rot13 encryption algorithm
    public static String rot13_encrypt(String iString)
    {

        //System.out.println(iString);

        if(iString.isEmpty())
        {
            return "";
        }

        int len = iString.length();
        String oString = "";
        for(int i = 0; i < len ; i ++)
        {
            oString = oString + (char)(iString.charAt(i) + 13);
        }
        return oString;
    }

    // Rot13 decryption algorithm
    public static String rot13_decrypt(String iString)
    {
        if(iString.isEmpty())
        {
            return "";
        }

        int len = iString.length();
        String oString = "";
        for(int i = 0; i < len ; i ++)
        {
            oString = oString + (char)(iString.charAt(i) - 13);
        }
        return oString;
    }

}
