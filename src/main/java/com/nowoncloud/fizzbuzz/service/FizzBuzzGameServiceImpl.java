package com.nowoncloud.fizzbuzz.service;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FizzBuzzGameServiceImpl implements FizzBuzzGameService {
	private static final Logger logger = Logger.getLogger(FizzBuzzGameServiceImpl.class);
	@Override
	/**
	 * This method returns a fizzbuzz sequence upto fizzBuzzEndPoint
	 *
	 * @param   String fizzBuzzEndPoint
	 * @return  String fizzBuzzSequence
	 *
	 * @author            Manu Mehrotra
	 */
	public String getFizzBuzzSeq(String fizzBuzzEndPoint) {
		StringBuilder retVal = new StringBuilder();
		int startingNumber = NumberUtils.toInt(fizzBuzzEndPoint, -1);
		if (startingNumber < 0 || startingNumber > 999) {
			logger.warn("Input is out of range. Input specified is: "+ fizzBuzzEndPoint);
			return retVal.toString();
		}
	 for (int i = 1; i <= NumberUtils.toInt(fizzBuzzEndPoint, -1); i++) {
            if (i % 15 == 0) {
            	retVal.append("FizzBuzz");
            } else if (i % 3 == 0) {
            	retVal.append("Fizz");
            } else if (i % 5 == 0) {
            	retVal.append("Buzz");
            } else {
            	retVal.append(i);
            }
            retVal.append(" ");
            logger.info("generated fizzbuzz sequence is : " + retVal.toString());
        }
	 return retVal.toString();
	}
}
