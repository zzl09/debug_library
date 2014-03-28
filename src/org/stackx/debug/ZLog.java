package org.stackx.debug;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ZLog {
    private static final String ANDROID_DEBUG_CLASS_NAME = "android.util.Log";

    private static StackTraceElement getCurrentStackTrace() {
        StackTraceElement result = null;
        StackTraceElement[] stackTraceArray = Thread.currentThread()
                .getStackTrace();

        if (stackTraceArray == null) {
            return null;
        }

        for (StackTraceElement stackTrace : stackTraceArray) {
            if (stackTrace.isNativeMethod()) {
                continue;
            }

            if (stackTrace.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (stackTrace.getClassName().equals(ZLog.class.getName())) {
                continue;
            }
            result = stackTrace;
            break;
        }
        return result;
    }

    private static String getClassMethodName() {
        String result = "null";
        StackTraceElement currentStackTrace = getCurrentStackTrace();
        if (currentStackTrace != null) {
            result = currentStackTrace.getFileName() + "-"
                    + currentStackTrace.getMethodName() + "  (line:"
                    + currentStackTrace.getLineNumber() + ")";
        }
        return result;
    }

    private static <T> void doDebug(String debugType, T... msg) {
        try {
            String className = getClassMethodName();
            Class<?> logClass = Class.forName(ANDROID_DEBUG_CLASS_NAME);
            Class[] parameterTypes = new Class[]{String.class, String.class};
            Method logMethod = logClass.getMethod(debugType, parameterTypes);
            for (T tmpMsg : msg) {
                if (tmpMsg instanceof Iterable) {
                    for (Object singleTmpMsg : (Iterable) tmpMsg) {
                        logMethod.invoke(null, className,
                                String.valueOf(singleTmpMsg));
                    }
                } else if (tmpMsg != null && tmpMsg.getClass().isArray()) {
                    Object[] tmpMsgArray = (Object[]) tmpMsg;
                    for (int i = 0; i < tmpMsgArray.length; i++) {
                        logMethod.invoke(null, className,
                                String.valueOf(tmpMsgArray[i]));
                    }
                } else {
                    logMethod.invoke(null, className, String.valueOf(tmpMsg));
                }
            }
        } catch (ClassNotFoundException e) {
            android.util.Log.e("Debug Model Error", e.toString());
        } catch (NoSuchMethodException e) {
            android.util.Log.e("Debug Model Error", e.toString());
        } catch (IllegalAccessException e) {
            android.util.Log.e("Debug Model Error", e.toString());
        } catch (IllegalArgumentException e) {
            android.util.Log.e("Debug Model Error", e.toString());
        } catch (InvocationTargetException e) {
            android.util.Log.e("Debug Model Error", e.toString());
        }

    }

    public static <T> void i(T... msg) {
        doDebug("i", msg);
    }

    public static <T> void v(T... msg) {
        doDebug("v", msg);

    }

    public static <T> void d(T... msg) {
        doDebug("d", msg);

    }

    public static <T> void e(T... msg) {
        doDebug("e", msg);

    }

    public static <T> void w(T... msg) {
        doDebug("w", msg);

    }
}