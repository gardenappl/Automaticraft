package goldenapple.automaticraft.util;

import goldenapple.automaticraft.reference.Reference;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;


public class LogHelper {
    public static Logger logger;
    private static Marker marker = MarkerManager.getMarker(Reference.MOD_NAME);

    public static void log(Level logLevel, Object object, Object... formatData){
        logger.log(logLevel, marker, String.format(String.valueOf(object), formatData));
    }

    public static void off(Object object, Object... formatData){
        log(Level.OFF, object, formatData);
    }

    public static void fatal(Object object, Object... formatData){
        log(Level.FATAL, object, formatData);
    }

    public static void error(Object object, Object... formatData){
        log(Level.ERROR, object, formatData);
    }

    public static void warn(Object object, Object... formatData){
        log(Level.WARN, object, formatData);
    }

    public static void info(Object object, Object... formatData){
        log(Level.INFO, object, formatData);
    }

    public static void debug(Object object, Object... formatData){
        log(Level.DEBUG, object, formatData);
    }

    public static void trace(Object object, Object... formatData){
        log(Level.TRACE, object, formatData);
    }

    public static void all(Object object, Object... formatData){
        log(Level.ALL, object, formatData);
    }
}