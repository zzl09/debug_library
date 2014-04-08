/*
 * Copyright (C) 2014 zzl09
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.stackx.debug;

import android.os.Environment;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ZLog {
    private static final String ANDROID_DEBUG_CLASS_NAME = "android.util.Log";
    private static final String LogFile = "Zlog.log";

    private static StackTraceElement getCurrentStackTrace() {
        StackTraceElement result = null;
        StackTraceElement[] stackTraceArray = Thread.currentThread().getStackTrace();

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
            result = currentStackTrace.getFileName() + "-" + currentStackTrace.getMethodName() + "  (line:" + currentStackTrace.getLineNumber() + ")";
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
                        logMethod.invoke(null, className, String.valueOf(singleTmpMsg));
                    }
                } else if (tmpMsg != null && tmpMsg.getClass().isArray()) {
                    Object[] tmpMsgArray = (Object[]) tmpMsg;
                    for (int i = 0; i < tmpMsgArray.length; i++) {
                        logMethod.invoke(null, className, String.valueOf(tmpMsgArray[i]));
                    }
                } else {
                    logMethod.invoke(null, className, String.valueOf(tmpMsg));
                }
            }
        } catch (Exception e) {
            android.util.Log.e("Debug Model Error", e.toString());
        }

    }

    public static <T> void i(T... msg) {
        doDebug("i", msg);
    }

    public static <T> void v(T... msg) {
        doDebug("v", msg);
    }

    public static <T> void f(T... msg) {
        try {
            String className = getClassMethodName();
            Class<?> logClass = Class.forName(ANDROID_DEBUG_CLASS_NAME);
            Class[] parameterTypes = new Class[]{String.class, String.class};
            for (T tmpMsg : msg) {
                if (tmpMsg instanceof Iterable) {
                    for (Object singleTmpMsg : (Iterable) tmpMsg) {
                        saveToSDCard(LogFile, String.valueOf(singleTmpMsg));
                        //logMethod.invoke(null, className, String.valueOf(singleTmpMsg));
                    }
                } else if (tmpMsg != null && tmpMsg.getClass().isArray()) {
                    Object[] tmpMsgArray = (Object[]) tmpMsg;
                    for (int i = 0; i < tmpMsgArray.length; i++) {
                        saveToSDCard(LogFile, String.valueOf(tmpMsgArray[i]));
                        //logMethod.invoke(null, className, String.valueOf(tmpMsgArray[i]));
                    }
                } else {
                    saveToSDCard(LogFile, String.valueOf(tmpMsg));
                    //logMethod.invoke(null, className, String.valueOf(tmpMsg));
                }
            }
        } catch (Exception e) {
            android.util.Log.e("Debug Model Error", e.toString());
        }
    }

    private static void saveToSDCard(String filename, String content) throws Exception {
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        String handledContent = content + "\r\n";
        String temp = "";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;

        fis = new FileInputStream(file);
        isr = new InputStreamReader(fis);
        br = new BufferedReader(isr);
        StringBuffer buf = new StringBuffer();
        for (int j = 1; (temp = br.readLine()) != null; j++) {
            buf = buf.append(temp);
            buf = buf.append(System.getProperty("line.separator"));
        }
        buf.append(handledContent);
        fos = new FileOutputStream(file);
        pw = new PrintWriter(fos);
        pw.write(buf.toString().toCharArray());
        pw.flush();
        if (pw != null) {
            pw.close();
        }
        if (fos != null) {
            fos.close();
        }
        if (br != null) {
            br.close();
        }
        if (isr != null) {
            isr.close();
        }
        if (fis != null) {
            fis.close();
        }
        //OutputStream out=new FileOutputStream(file);
        //out.write(content.getBytes());
        //out.close();
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