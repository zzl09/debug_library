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
