package com.yu.common.utils;

/**
 * @author chenwei
 * @date 2018/1/3
 */
public class ReflexUtils {


  public static <T> T newClass(Class<? extends T> clazz) {

    T t = null;
    try {
      t = clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return t;
  }
}
