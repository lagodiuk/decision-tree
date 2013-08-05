/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Yurii Lahodiuk (yura_lagodiuk@ukr.net)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package com.lagodiuk.decisiontree;

public enum Predicate {
	EXISTS {
		@Override
		public boolean eval(Object checkingValue, Object sampleValue) {
			return checkingValue != null;
		}
	},
	EQUAL {
		@Override
		public boolean eval(Object checkingValue, Object sampleValue) {
			if (checkingValue == null) {
				return false;
			}
			return sampleValue.equals(checkingValue);
		}
	},
	GTE {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public boolean eval(Object checkingValue, Object sampleValue) {
			if (checkingValue == null) {
				return false;
			}

			Comparable sample = (Comparable) sampleValue;
			Comparable checking = (Comparable) checkingValue;
			return checking.compareTo(sample) >= 0;
		}
	},
	LTE {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public boolean eval(Object checkingValue, Object sampleValue) {
			if (checkingValue == null) {
				return false;
			}

			Comparable sample = (Comparable) sampleValue;
			Comparable checking = (Comparable) checkingValue;
			return checking.compareTo(sample) <= 0;
		}
	};

	public boolean eval(Object sampleValue, Object checkingValue) {
		throw new UnsupportedOperationException();
	}
}
