package com.yu.common.mvp;

import java.lang.reflect.Field;


public class PresenterInjector {


  public static void injectPresenter(PresenterSetter setter) {
    if (setter == null) {
      return;
    }
    Field[] fields = setter.getClass().getDeclaredFields();

    for (Field field : fields) {

      PresenterLifeCycle annotation = field.getAnnotation(PresenterLifeCycle.class);
      if (annotation != null) {
        field.setAccessible(true);
        try {
          Object obj = field.get(setter);
          if (obj != null && obj instanceof Presenter) {
            setter.addPresenter((Presenter) obj);
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }
}