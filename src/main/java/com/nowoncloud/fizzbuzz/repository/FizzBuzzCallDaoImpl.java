package com.nowoncloud.fizzbuzz.repository;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nowoncloud.fizzbuzz.domain.FizzBuzzCall;

@Repository
@Transactional
public class FizzBuzzCallDaoImpl implements CallDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void createCall(FizzBuzzCall call) {
		getCurrentSession().save(call);	
	}

	//TODO change fizzBuzzStartingPoint to fizzBuzzEndingPoint
	@Override
	public void updateCall(int fizzBuzzStartingPoint, String callSid) {
		Query query = getCurrentSession().createQuery("from FizzBuzzCall where sessionId = :sid");
		query.setParameter("sid", callSid);
		FizzBuzzCall call = (FizzBuzzCall) query.uniqueResult();
		call.setSessionId(callSid);
		call.setFizzBuzzStartPoint(fizzBuzzStartingPoint);
		getCurrentSession().update(call);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FizzBuzzCall> getCallsWithExpiredDelays() {
		return getCurrentSession().createQuery("from FizzBuzzCall where callAt < :currentTime AND sessionId = :sessionId")
				.setParameter("currentTime", (System.currentTimeMillis()/1000L)).setParameter("sessionId", "NA").list();
	}
	
}
