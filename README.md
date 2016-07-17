fizzBuzzTwillioIVR
==================

This is a Spring 3 MVC WebApp which does the following:<br />
1) Takes in a number to dial <br />
2) Dials the number and gets a number as input from the user <br />
3) Generates a Fizz-Buzz sequence and reads it out to the user. <br />

Users can also schedule calls for later (1min - 60mins) too.

Technologies
======================
<ol>
   <li>Spring 3 MVC</li>
   <li>Hibernate ORM</li>
   <li>MySql database</li>
   <li>Twillio Java SDK</li>
   <li>log4j for logging</li>
   <li>gradle for build system</li>
</ol>

How to Run?
=====================
<ul>
<li> We need gradle version - 1.12 installed to build the project</li>
<li> To generate an eclipse workspace do "gradle eclipse" in the directory which has the build.gralde file</li>
<li> Database configuration and Twillio account configuration need to be done in /src/main/resources/application.properties</li>
<li> sql script for recreating the database on your machine is in /src/main/resources/fizzbuzz_db.sql</li>
<li> To start tomcat and deploy the project on it do "gradle tomcatRunWar"</li>
<li> To build the project do "gradle build"</li>
<li> To clean the project do "gradle clean"</li>
</ul>


Workflow
=====================
<ol>
<li> User enters the call delay and number to call and submits the form.</li>
<li> If the call delay is 0 then Twillio API for making a call are used to call the supplied number.
<p>2.1. Twillio's API returns a call session id which we save in the database with other call related info. </p>
<p>2.2. If the call delay is greater than 0 and less than 60 then call related details are saved in database for scheduler to use. The call's session id is saved at "NA".</p>
<p>2.3. Once the call is connected, twillio contacts FizzBuzz App to get a menu which is read out to user</p>
<p>2.4. User enters a number on their phone</p>
<p>2.5. Twillio supplies this number to FizzBuzz App and a fizzbuzz sequence is returned as response from the FizzBuzz App</p>
<p>2.6. FizzBuzz App updates the database row for this call with the fizzbuzz number provided by twillio. Calls's session id is used to find the row in the database correspoonding to this call.</p>
</li>
<li>If the call delay is greater than 0 then the call details are recorded in the database.<br />
<p>3.1. Scheduler runs periodically every 5 seconds and finds all call details for calls whose delay time has expired (Time to place call on is stored in DB in form of time from unix epoch) and for which the session id is "NA" </p>
<p>3.2. Scheduler places the call using twillio in the manner described above.</p>
<p>3.3. Scheduler updates the record for the call with the call session id and fizzbuzz number</p>
</li>
</ol>


Improvements
======================
<ul>
   <li>Testing... Unit Test...!!!</li>
   <li>Remove some hardcoding at places</li>
   <li>Pass code through findbugs and checkstyle</li>
   <li>Better frontend</li>
   <li>Better exception handling</li>
   <li>mysql table optimizations using querys explain plan</li>
   <li>Database connection pooling</li>
   <li>Better error status's </li>
   <li>Feature in frontend to show already placed calls in paginated form</li>
   <li>improve comments and logging</li>
</ul>
