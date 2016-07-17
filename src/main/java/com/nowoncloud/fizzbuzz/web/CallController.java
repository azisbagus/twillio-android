package com.nowoncloud.fizzbuzz.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.nowoncloud.fizzbuzz.domain.FizzBuzzCall;
import com.nowoncloud.fizzbuzz.service.CallService;
import com.nowoncloud.fizzbuzz.service.FizzBuzzGameService;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.verbs.TwiMLException;

@Controller
public class CallController {
	private static final Logger logger = Logger.getLogger(CallController.class);
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	CallService callService;

	@Autowired
	FizzBuzzGameService fizzBuzzGameService;
	
	/**
	 * This method is called when the index page is requested
	 *
	 * @param   Model object
	 * @return  name of the jsp file to be returned
	 *
	 * @author            Manu Mehrotra
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("call", new FizzBuzzCall());
		return "index";
	}
	
	/**
	 * This method is called when the /call page is requested
	 *
	 * @param   Model object
	 * @return  name of the jsp file to be returned
	 *
	 * @author            Manu Mehrotra
	 */
	@RequestMapping(value = "/call", method = RequestMethod.GET)
	public String getCall(@ModelAttribute("call") FizzBuzzCall call) {
		return "index";
	}
	
	
	/**
	 * This method is called when new call details are submitted
	 *
	 * @param   FizzBuzzCall, BindingResult
	 * @return  name of the jsp file to be returned
	 *
	 * @author            Manu Mehrotra
	 */
	@RequestMapping(value = "/call", method = RequestMethod.POST)
	public String submitCall(@Valid @ModelAttribute("call") FizzBuzzCall call, BindingResult bindingResult) throws TwilioRestException {
		logger.info("Got request to schedule call " + call.toString());
		if (bindingResult.hasErrors()) {
            return "index";
        }
		callService.scheduleCall(call);
        return "callSuccess";
	}

	/**
	 * This method is called by twilio to request the initial menu which is to be read out to the user
	 *
	 * @param   HttpServletRequest, HttpServletResponse
	 * @return  TwiML for the menu is written to the response stream
	 *
	 * @author            Manu Mehrotra
	 */
	@RequestMapping(value = "/fizzbuzz", method = RequestMethod.GET)
	public void readGameMenu(HttpServletRequest request, HttpServletResponse response) throws TwiMLException, IOException {
		logger.info("Got request for game menu " + request.toString());
		response.setContentType("application/xml");
		response.getWriter().print(callService.respondWithGameMenu());
	}
	
	/**
	 * This method is called by twilio to provide the number inputted by the user and get the fizzbuzz sequence in response
	 *
	 * @param   HttpServletRequest, HttpServletResponse
	 * @return  TwiML for the fizzbuzz sequence is written to the response stream
	 *
	 * @author            Manu Mehrotra
	 */
	@RequestMapping(value = "/fizzbuzz", method = RequestMethod.POST)
	public void getGameMenuResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, TwiMLException {
		logger.info("Got request for fizzbuzz sequence " + request.toString());
		response.setContentType("application/xml");
		String seq = fizzBuzzGameService.getFizzBuzzSeq(request.getParameter("Digits"));
		response.getWriter().print(callService
				    .respondWithFizzBuzzSequence(
				        request.getParameter("Digits"), seq, 
				        request.getParameter("CallSid")));
	}
	
	
	
	/**
	 * This method is used to redirect users to an error page whenever TwilioRestException happens
	 *
	 * @author            Manu Mehrotra
	 */
	@ExceptionHandler(TwilioRestException.class) 
	public ModelAndView handleTwilioRestException(HttpServletRequest req, Exception exception) {
		logger.error("Request: " + req.getRequestURL() + " raised " + exception);
		ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", exception);
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName("twilioRestException");
	    return mav;
	}
	
}
