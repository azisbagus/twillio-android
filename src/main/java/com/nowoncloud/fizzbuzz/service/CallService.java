package com.nowoncloud.fizzbuzz.service;

import com.nowoncloud.fizzbuzz.domain.FizzBuzzCall;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.verbs.TwiMLException;

public interface CallService  {

	public void scheduleCall(FizzBuzzCall call) throws TwilioRestException;

	void scheduleCall();

	String respondWithGameMenu() throws TwiMLException;

	String respondWithFizzBuzzSequence(String fizzBuzzEndPoint, String fizzBuzzStartingPoint, String callSid) throws TwiMLException;

	void makeCall(FizzBuzzCall fizzBuzzCall) throws TwilioRestException;
	
}
