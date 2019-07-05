package com.yu.common.base;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper {
  private SharedPreferences sp;

  private SharedPreferencesHelper(SharedPreferences sharedPreferences) {
    this.sp = sharedPreferences;
  }

  public static SharedPreferencesHelper create(SharedPreferences sharedPreferences) {
    return new SharedPreferencesHelper(sharedPreferences);
  }

  public SharedPreferences getSp() {
    return sp;
  }

  public SharedPreferencesHelper putString(String key, @Nullable String value) {
    sp.edit().putString(key, value).commit();
    return this;
  }

  public SharedPreferencesHelper putStringSet(String key, @Nullable Set<String> values) {
    sp.edit().putStringSet(key, values).commit();
    return this;
  }

  public SharedPreferencesHelper putInt(String key, int value) {
    sp.edit().putInt(key, value).commit();
    return this;
  }

  public SharedPreferencesHelper putLong(String key, long value) {
    sp.edit().putLong(key, value).commit();
    return this;
  }

  public SharedPreferencesHelper putFloat(String key, float value) {
    sp.edit().putFloat(key, value).commit();
    return this;
  }

  public SharedPreferencesHelper putBoolean(String key, boolean value) {
    sp.edit().putBoolean(key, value).commit();
    return this;
  }

  public SharedPreferencesHelper remove(String key) {
    sp.edit().remove(key).commit();
    return this;
  }

  public SharedPreferencesHelper clear() {
    sp.edit().clear().commit();
    return this;
  }

  public Map<String, ?> getAll() {
    return sp.getAll();
  }

  @Nullable public String getString(String key, @Nullable String defValue) {
    return sp.getString(key, defValue);
  }

  @Nullable public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
    return sp.getStringSet(key, defValues);
  }

  public int getInt(String key, int defValue) {
    return sp.getInt(key, defValue);
  }

  public long getLong(String key, long defValue) {
    return sp.getLong(key, defValue);
  }

  public float getFloat(String key, float defValue) {
    return sp.getFloat(key, defValue);
  }

  public boolean getBoolean(String key, boolean defValue) {
    return sp.getBoolean(key, defValue);
  }

  public boolean contains(String key) {
    return sp.contains(key);
  }
}
