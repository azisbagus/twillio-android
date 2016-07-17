package com.nowoncloud.fizzbuzz.repository;

import java.util.List;

import com.nowoncloud.fizzbuzz.domain.FizzBuzzCall;

public interface CallDao {

	public void createCall(FizzBuzzCall call);
	
	public void updateCall(int fizzBuzzEndPoint, String callSid);

	public List<FizzBuzzCall> getCallsWithExpiredDelays();
	
}
