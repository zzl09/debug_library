package org.stackx.debug;


public class ZToast {
	/*todoing*/
	public static StackTraceElement getCurrentStackTrace() {
		StackTraceElement result = null;
		StackTraceElement[] stackTraceArray = Thread.currentThread()
				.getStackTrace();
		if (stackTraceArray == null) {
			return null;
		}

		for (StackTraceElement stackTrace : stackTraceArray) {
			String className = stackTrace.getClassName();
			try {
				Class tmpClass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				ZLog.e("Toast Error");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
