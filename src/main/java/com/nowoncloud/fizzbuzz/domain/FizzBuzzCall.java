package com.nowoncloud.fizzbuzz.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.nowoncloud.fizzbuzz.validator.PhoneNumber;

@Entity
@Table(name = "fizzbuzz_call")
public class FizzBuzzCall implements Serializable {
	
	private static final long serialVersionUID = -3492210817289678799L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="call_id", nullable = false)
	private int id;
	
	@Column(name="session_id", nullable = false)
	private String sessionId;
	
	@PhoneNumber
	@Column(name="to_number", nullable = false)
	private String toNumber;
	
	@Transient
	@NumberFormat(style = Style.NUMBER)
	@Range(min = 0, max = 60)
	private int callDelay;
	
	//TODO change fizzbuzz_start_point to fizzbuzz_end_point
	@Column(name="fizzbuzz_start_point", nullable = false)
	private int fizzBuzzStartPoint;
	
    @Column(name = "call_at", nullable = false)
    private long callAt;
	
	
	public long getCallAt() {
		return callAt;
	}

	public void setCallAt(long callAt) {
		this.callAt = callAt;
	}

	public int getFizzBuzzStartPoint() {
		return fizzBuzzStartPoint;
	}

	public void setFizzBuzzStartPoint(int fizzBuzzStartPoint) {
		this.fizzBuzzStartPoint = fizzBuzzStartPoint;
	}
	
	public int getCallDelay() {
		return callDelay;
	}
	
	public void setCallDelay(int callDelay) {
		this.callDelay = callDelay;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getToNumber() {
		return toNumber;
	}
	
	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}
	
}
