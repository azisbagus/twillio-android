package com.nowoncloud.fizzbuzz.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nowoncloud.fizzbuzz.domain.FizzBuzzCall;
import com.nowoncloud.fizzbuzz.repository.CallDao;
import com.nowoncloud.fizzbuzz.scheduler.worker.Worker;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Call;
import com.twilio.sdk.verbs.Gather;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@Service
@PropertySource("classpath:application.properties")
public class FizzBuzzCallServiceImpl implements CallService {
	private static final Logger logger = Logger.getLogger(FizzBuzzCallServiceImpl.class);
	
	private static final int FIZZBUZZ_INPUT_INIT = -1;
	private static final int FIZZBUZZ_INPUT_ERROR = 0;
	
	@Autowired
    Environment env;
	
	@Autowired
	TwilioRestClient twilioRestClient;
	
	@Autowired 
	CallDao callDao;
	
	@Override
	public void makeCall(FizzBuzzCall fizzBuzzCall) throws TwilioRestException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		params.add(new BasicNameValuePair("To", fizzBuzzCall.getToNumber()));
		params.add(new BasicNameValuePair("From", env.getProperty("com.nowoncloud.fizzbuzz.twillio.fromNumber")));
		params.add(new BasicNameValuePair("Url", env.getProperty("com.nowoncloud.fizzbuzz.twillio.getMenu")));
		params.add(new BasicNameValuePair("Method", "GET"));  
		CallFactory callFactory = twilioRestClient.getAccount().getCallFactory(); 
	    Call twillioCall;
		twillioCall = callFactory.create(params);
		fizzBuzzCall.setSessionId(twillioCall.getSid());
	}
	
	@Override
	public void scheduleCall(FizzBuzzCall fizzBuzzCall) throws TwilioRestException {
    	if(fizzBuzzCall.getCallDelay() == 0) {
    		try {
				makeCall(fizzBuzzCall);
			} catch (TwilioRestException e) {
				// TODO have better defined error codes here..
				logger.error(e);
				throw e;
			}
		    fizzBuzzCall.setCallAt((System.currentTimeMillis() / 1000L));
	    } else {
	    	fizzBuzzCall.setSessionId("NA");
			BigInteger currentTime = BigInteger.valueOf((System.currentTimeMillis() / 1000L));
			BigInteger delayToAdd = BigInteger.valueOf(fizzBuzzCall.getCallDelay() * 60);
			BigInteger unixEpochWithCallDelay = currentTime.add(delayToAdd);
			fizzBuzzCall.setCallAt(unixEpochWithCallDelay.longValue());
	    }
    	fizzBuzzCall.setFizzBuzzStartPoint(FIZZBUZZ_INPUT_INIT);
		callDao.createCall(fizzBuzzCall);
	}
	
	@Autowired
	@Qualifier("callWorker")
	private Worker callWorker;
	
	@Scheduled(fixedDelay=5000)
	public void scheduleCall() {  
		// get all calls whose delay has expired
		List<FizzBuzzCall> delayExpiredCalls = callDao.getCallsWithExpiredDelays();
		if(delayExpiredCalls != null && delayExpiredCalls.size() > 0) {
            callWorker.work(delayExpiredCalls);
		}
	 }
	
	@Autowired
	TwiMLResponse twiml;
	
	@Override
	public String respondWithGameMenu() throws TwiMLException {
	    Gather gather = new Gather();
	    Say say = new Say(env.getProperty("com.nowoncloud.fizzbuzz.twillio.callMenuMessage"));
	    say.setVoice(env.getProperty("com.nowoncloud.fizzbuzz.twillio.callVoice"));
        gather.setMethod("POST");
	    gather.setAction(env.getProperty("com.nowoncloud.fizzbuzz.twillio.getfizzbuzz"));
	    gather.append(say);
	    gather.setFinishOnKey("#");
	    Say sayNoInputReceived = new Say(env.getProperty("com.nowoncloud.fizzbuzz.twillio.noInput"));
	    sayNoInputReceived.setVoice(env.getProperty("com.nowoncloud.fizzbuzz.twillio.callVoice"));
	    String retVal;
	    synchronized(twiml) {
	        twiml.append(gather);
	        twiml.append(sayNoInputReceived);
	        retVal = twiml.toXML();
	    }
	    return retVal;
	}


	@Override
	public String respondWithFizzBuzzSequence(String fizzBuzzEndPoint, String fizzBuzzSeq, String callSid) throws TwiMLException {
		Say say;
		logger.info("Request for fizzbuzz sequence - starting point - " + fizzBuzzEndPoint + "call session id" + callSid);
	    if (fizzBuzzSeq != null && fizzBuzzSeq.length() > 0) {
	    	// svae endpoint of fizzbuzz in database
	    	callDao.updateCall(NumberUtils.toInt(fizzBuzzEndPoint), callSid);
	    	// read  fizzbuzz sequence out. Sequence uses space delimiter
	    	String[] wordSlices = fizzBuzzSeq.split(" ");
	    	for (int i = 0; i < wordSlices.length; i++) {
			    say = new Say(wordSlices[i]);
	    		say.setVoice(env.getProperty("com.nowoncloud.fizzbuzz.twillio.callVoice"));
	    		synchronized(twiml) {
	    	    	twiml.append(say);
	    		}
	    	}
	    } else {
	    	// save -1 instead of fizzbuzz end point as an error has occured
	    	callDao.updateCall(FIZZBUZZ_INPUT_ERROR, callSid);
	    	say = new Say("com.nowoncloud.fizzbuzz.twillio.callErrorMessage");
	    	say.setVoice(env.getProperty("com.nowoncloud.fizzbuzz.twillio.callVoice"));
	    	synchronized(twiml) {
    	    	twiml.append(say);
    		}
	    }
	    String retVal;
	    synchronized(twiml) {
	        retVal = twiml.toXML();
	    }
		return retVal;
	}
	
}
