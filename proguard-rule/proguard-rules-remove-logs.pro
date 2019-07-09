# --------------------------------------------------------------------
# REMOVE all Log messages except warnings and errors
# --------------------------------------------------------------------
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
}

#-assumenosideeffects class android.util.Log {
#   public static boolean isLoggable(java.lang.String,int);
#   public static int v(...);
#   public static int i(...);
#   public static int w(...);
#	  public static int d(...);
#	  public static int e(...);
#}