package com.lagodiuk.decisiontree;

public interface Predicate {

	boolean eval(Object checkingValue, Object sampleValue);

}
