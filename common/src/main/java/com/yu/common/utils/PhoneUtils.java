package com.yu.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {

  /**
   * 需求:满足11位， 第一位为数字1，第二位可以为3、5、7、8中的一个，后面可以是0～9的数字，有9位
   */
  public static boolean isPhoneLegal(String str) {
    String regExp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    Pattern p = Pattern.compile(regExp);
    Matcher m = p.matcher(str);
    return m.matches();
  }

}
