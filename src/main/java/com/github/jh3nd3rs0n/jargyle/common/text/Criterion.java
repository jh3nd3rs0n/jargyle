package com.github.jh3nd3rs0n.jargyle.common.text;

public final class Criterion {

	public static Criterion newInstance(
			final CriterionMethod criterionMethod, final String value) {
		return new Criterion(criterionMethod, value);
	}
	
	public static Criterion newInstance(
			final CriterionMethod criterionMethod, 
			final String value,
			final String comment) {
		return new Criterion(criterionMethod, value, comment);
	}
	
	public static Criterion newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"criterion must be in the following format: "
					+ "CRITERION_METHOD:VALUE");
		}
		CriterionMethod criterionMethod = CriterionMethod.valueOfString(
				sElements[0]);
		String value = sElements[1];
		return Criterion.newInstance(criterionMethod, value);
	}
	
	private final String comment;
	private final CriterionMethod criterionMethod;
	private final String value;
	
	private Criterion(final CriterionMethod method, final String val) {
		this(method, val, null);
	}
	
	private Criterion(
			final CriterionMethod method,
			final String val, 
			final String cmmnt) {
		this.comment = cmmnt;
		this.criterionMethod = method;
		this.value = val;
	} 

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Criterion other = (Criterion) obj;
		if (this.criterionMethod != other.criterionMethod) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}
	
	public boolean evaluatesTrue(final String arg) {
		return this.criterionMethod.evaluatesTrue(arg, this.value);
	}

	public String getComment() {
		return this.comment;
	}
	
	public CriterionMethod getCriterionMethod() {
		return this.criterionMethod;
	}
	
	public String getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.criterionMethod == null) ? 0 : this.criterionMethod.hashCode());
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return this.criterionMethod.toString().concat(":").concat(this.value);
	}
	
}
