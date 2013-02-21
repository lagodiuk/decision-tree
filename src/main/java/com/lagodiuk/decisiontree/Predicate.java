package com.lagodiuk.decisiontree;

public enum Predicate {
	EXISTS {
		@Override
		public boolean eval(Object checkingValue, Object sampleValue) {
			return checkingValue != null;
		}

		@Override
		public String getDescription(String attribute, Object sampleValue) {
			return String.format("EXISTS %s", attribute);
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

		@Override
		public String getDescription(String attribute, Object sampleValue) {
			return String.format("%s == %s", attribute, sampleValue);
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

		@Override
		public String getDescription(String attribute, Object sampleValue) {
			return String.format("%s >= %s", attribute, sampleValue);
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

		@Override
		public String getDescription(String attribute, Object sampleValue) {
			return String.format("%s =< %s", attribute, sampleValue);
		}
	};

	public boolean eval(Object sampleValue, Object checkingValue) {
		throw new UnsupportedOperationException();
	}

	public String getDescription(String attribute, Object sampleValue) {
		throw new UnsupportedOperationException();
	}
}
