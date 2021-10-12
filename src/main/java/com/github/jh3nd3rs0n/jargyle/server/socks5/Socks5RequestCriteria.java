package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestCriteria {

	private static final Socks5RequestCriteria EMPTY_INSTANCE = 
			new Socks5RequestCriteria(Collections.emptyList());
	
	public static Socks5RequestCriteria getEmptyInstance() {
		return EMPTY_INSTANCE;
	}
	
	public static Socks5RequestCriteria newInstance(
			final List<Socks5RequestCriterion> socks5ReqCriteria) {
		return new Socks5RequestCriteria(socks5ReqCriteria);
	}
	
	public static Socks5RequestCriteria newInstance(
			final Socks5RequestCriterion... socks5ReqCriteria) {
		return newInstance(Arrays.asList(socks5ReqCriteria));
	}
	
	private final List<Socks5RequestCriterion> socks5RequestCriteria;
	
	private Socks5RequestCriteria(
			final List<Socks5RequestCriterion> socks5ReqCriteria) {
		this.socks5RequestCriteria = new ArrayList<Socks5RequestCriterion>(
				socks5ReqCriteria);
	}
	
	public Socks5RequestCriterion anyEvaluatesTrue(
			final String clientAddress,
			final Socks5Request socks5Req) {
		for (Socks5RequestCriterion socks5RequestCriterion 
				: this.socks5RequestCriteria) {
			if (socks5RequestCriterion.evaluatesTrue(
					clientAddress, socks5Req)) {
				return socks5RequestCriterion;
			}
		}
		return null;
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
		Socks5RequestCriteria other = (Socks5RequestCriteria) obj;
		if (this.socks5RequestCriteria == null) {
			if (other.socks5RequestCriteria != null) {
				return false;
			}
		} else if (!this.socks5RequestCriteria.equals(other.socks5RequestCriteria)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.socks5RequestCriteria == null) ? 0 : this.socks5RequestCriteria.hashCode());
		return result;
	}

	public List<Socks5RequestCriterion> toList() {
		return Collections.unmodifiableList(this.socks5RequestCriteria);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socks5RequestCriteria=")
			.append(this.socks5RequestCriteria)
			.append("]");
		return builder.toString();
	}
	
}
