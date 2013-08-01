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
