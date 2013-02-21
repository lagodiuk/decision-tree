package com.lagodiuk.decisiontree;

public enum Predicate {
	EQUAL {
		@Override
		public boolean eval(Object sampleValue, Object checkingValue) {
			return sampleValue.equals(checkingValue);
		}

		@Override
		public String toString() {
			return "==";
		}
	},
	GTE {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public boolean eval(Object checkingValue, Object sampleValue) {
			Comparable sample = (Comparable) sampleValue;
			Comparable checking = (Comparable) checkingValue;
			return checking.compareTo(sample) >= 0;
		}

		@Override
		public String toString() {
			return ">=";
		}
	},
	LTE {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public boolean eval(Object checkingValue, Object sampleValue) {
			Comparable sample = (Comparable) sampleValue;
			Comparable checking = (Comparable) checkingValue;
			return checking.compareTo(sample) <= 0;
		}

		@Override
		public String toString() {
			return "=<";
		}
	};

	public boolean eval(Object sampleValue, Object checkingValue) {
		throw new UnsupportedOperationException();
	}

}
