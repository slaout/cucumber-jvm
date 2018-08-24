# Cucumber-JVM Fork to Continue Next Steps for Some Exceptions + Run Scenarios in Parallel
This fork enables two new usages:

* continue execution on failures of some steps;
* run scenarios in parallel, with some synchronization mechanisms.

WARNING: Only the Java language is supported.
WARNING: Only the Spring dependency injection is supported.

## Continue Next Steps for Predefined or All Exceptions
The first purpose of this fork is to be able to continue the execution of the next steps of a scenario when one step fails and we know the next ones still can be executed safely.

This possibility is disabled by default.
It is up to the step definition developer to declare if the failure of a step will not impede the next steps.

This is the case when you have an expensive @Given and/or @When, and you want to execute a lot of @Then checks.
In this case, the failure of one of the checks will let the other independent checks to run as well: no failure is hidden by a previous failure.
The failed step is still marked as failed in the execution result, and the scenario as a whole is failed as well.

To mark a step as being unimportant for the next steps, you need to annotate the step definition with one of the following annotations:

* @ContinueNextStepsFor({AssertionError.class}) : if another exception is thrown, then the next steps are not executed
* @ContinueNextStepsOnException : no matter the exception class, next steps will always be executed

![Screenshot](/fork-purpose.png)

## Run Scenarios in Parallel
The second purpose of this fork is to be able to run scenarios in parallel, with some synchronization mechanisms.

All scenarios are put in a queue, and a thread-pool runs them as fast as possible. +
Compared to executing *features* in parallel, this scenario-based parallelization allows high thread efficiency. +
This is especially the case when you have a lot of fast-to-execute feature files, and one big/slow feature at the end: threads would not be used efficiently with a scenario-based parallelization.

You can also tag scenarios with tags starting with "@synchronized-" to make sure the scenarios with the same synchronized tag name will NOT run in parallel. +
Eg. 3 scenarios with @synchronized-foo will run in a single thread, never in parallel, and 4 other scenarios with @synchronized-bar will run in a serial-fashion in another thread. +
Note: scenarios with @synchronized-foo CAN run in parallel with scenarios tagged with @synchronized-bar: the "foo" and "bar" suffixes are up to you, to place scenarios is the right isolation group. +

A small note about the implementation: sycnrhonized scenarios are put first in the thread-pool queue. +
This is to mitigate or avoid the low thread efficiency discussed above, as some @synchronized-foobar tags could contain a lot of scenarios to be executed in serie by a single thread.

The produced report.json is aggregated as if all scenarios ran in sequential order (but they can have run in any order). +
As a result, parallel run is not supported for IDE JUnit panels: the aggregation can only take place at the very end of the process, to ensure all scenarios are reported in the order they appear in files, not in the execution order.

Note: you can use Spring to inject your dependencies: each scenario of each thread will get its own scopped beans: there is no concurrent access problems to care about.

To run scenarios in parallel, just use the "--threads X" (or "-r X") option when launching the CLI. +
If X is 1, threading is disabled and Cucumber will run like it used to work.

## REALLY IMPORTANT
This works only for Java. This is not implemented for other JVM languages. If you need them, you're encouraged to [compare the code of this branch with cucumber-jvm master branch](https://github.com/cucumber/cucumber-jvm/compare/v1.2.4...slaout:parallel-scenarios-execution-1.2.4) to see how it's implemented (it really only a few lines of code). Feel free to post patches of pull requests to make it work with other languages: I did not have the time not the knowledge to implement the solution for all languages.

## Usage
This project is based on Cucumber-JVM 1.2.4, and all of its artifactId are available in the new groupId "com.github.slaout.fork.info.cukesthreads".

Just include the following dependencies in your project's pom.xml (only the groupId changed compared to the official Cucumber distribution):
```xml
<dependency>
  <groupId>com.github.slaout.fork.info.cukesthreads</groupId>
  <artifactId>cucumber-core</artifactId>
  <version>1.2.4</version>
</dependency>
<dependency>
  <groupId>com.github.slaout.fork.info.cukesthreads</groupId>
  <artifactId>cucumber-java</artifactId>
  <version>1.2.4</version>
</dependency>
<dependency>
  <groupId>com.github.slaout.fork.info.cukesthreads</groupId>
  <artifactId>cucumber-spring</artifactId>
  <version>1.2.4</version>
</dependency>
<dependency>
  <groupId>com.github.slaout.fork.info.cukesthreads</groupId>
  <artifactId>cucumber-junit</artifactId>
  <version>1.2.4</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>4.0.2.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>4.0.2.RELEASE</version>
</dependency>
```

## Recommendations for Using the Annotation
As you know, "with great power comes great responsibility".
Here are some guidelines about using this "new super-power" wisely:
* Add @ContinueNextStepsFor only for @Then steps.
  If you use asserts in @Given, it’s only to check the environment is in a good and known state before starting the test. So if the assert fails, the next steps must not be executed.
  If you use asserts in @When steps, you’re really not supposed to!
* Ask yourself if the assert step is there to prevent execution of next steps.
  Do not use @ContinueNextStepsFor if the answer is yes.
  For instance "When I click ADD TO CART ; Then there is no error ; And I to go my cart" => "there is no error" must not have @ContinueNextStepsFor
* It's OK to add the annotation on "Then the product VisibleProduct is displayed" when checking for visibility of products.
  It's not OK to add the annotation on "Then the product VisibleProduct is displayed" if it's a prerequisite of next steps (because this Then should have been, or is seen as, a Given)
* Restrict @ContinueNextStepsFor to ONLY AssertionError.class.
  And in no case to Exception.class (and do not even think about Throwable.class)
* If a step gets an object and asserts on one of its properties, do not add NullPointerException like this: @ContinueNextStepsFor({AssertionError.class, NullPointerException.class})
  Instead, use assertThat(object).isNotNull(); before the assert to execute next steps if the object is not found, and use only @ContinueNextStepsFor(AssertionError.class)

## See Also
* [The thread on Cucumber forum](https://groups.google.com/forum/#!topic/cukes/xTqSyR1qvSc) proposing this solution, with explanations of the main developers on why this is a bad idea and how to refactor the tests to not use this fork. Please read the thread before using this fork: use the fork if you really need it.
* [How to rework your tests to avoid using this fork](https://github.com/cucumber/cucumber-jvm/issues/771) this thread (the comment "dkowis commented on 4 Sep 2014", to be more precise) also explains how to rework your feature files to avoid using this fork. Still want to use this fork? Now, that's fine, you can use it, you've been warned ;-)
* [Another thread](https://groups.google.com/forum/#!searchin/cukes/continue$20after$20failed$20/cukes/OTUjEupNjYk/tZYfMDcy5MkJ) proposing the solution of using ErrorCollector for JUnit. It's another way of doing the several checks thing (but all in one step), even if [JUnit's ErrorCollector is not supported in Cucumber](https://groups.google.com/forum/#!msg/cukes/qMwgAVzWmR0/GSkRUgJ8f4EJ). Remember: ErrorCollector is a JUnit concept, and it will not be supported if you run your scenarios without JUnit (with the CLI, for instance): you will have to collect the errors yourself (in a dedicated step or on @After)
* [A very wobbly solution if you still want to not skip next steps but you do not use this fork](http://stackoverflow.com/questions/6841467/how-can-i-make-cucumber-run-all-the-steps-not-skip-them-even-if-one-of-them-fa) as this makes the overall scenario fail, but it masks the step failure from Cucumber, so all the step results are green, including the ones that aren't right.
* [Another fragile solution...](http://stackoverflow.com/questions/15298521/continue-running-cucumber-steps-after-a-failure): log the exception in steps, don't throw any exception in steps, but throw it in @After ; as they say: "The only issue would be that, in the report, all the steps would look green but the test case looks failed." That's exactly why you don't want to do this.

## Other Links
Links to pages where I offered them this fork, for the purpose of explaining how this fork will be useful to many people:
* http://cukes.narkive.com/0k4gufhc/cucumber-continuation-of-cucumber-step-even-after-assertion-failure
* https://groups.google.com/forum/#!searchin/cukes/steps$20continue/cukes/odXSUY9cbK0/FdUl1CgrTCwJ
* http://forumsqa.com/question/how-to-continue-the-test-step-execution-even-on-failure-of-a-test-step-in-cucumber/

---

# Initial Cucumber-JVM Documentation

[![Build Status](https://secure.travis-ci.org/cucumber/cucumber-jvm.png)](http://travis-ci.org/cucumber/cucumber-jvm)
[![Join the chat at https://gitter.im/cucumber/cucumber-jvm](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/cucumber/cucumber-jvm?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Cucumber-JVM is a pure Java implementation of Cucumber that supports the [most popular programming languages](https://cukes.info/docs/reference/jvm#running) for the JVM.

You can [run](https://cukes.info/docs/reference/jvm#running) it with the tool of your choice.

Cucumber-JVM also integrates with all the popular [Dependency Injection containers](https://cukes.info/docs/reference/java-di).

## Documentation

[Start Here](https://cukes.info/docs).

## Hello World

Check out the simple [cucumber-java-skeleton](https://github.com/cucumber/cucumber-java-skeleton) starter project.

## Downloading / Installation

[Install](https://cukes.info/docs/reference/jvm#installation)

## Bugs and Feature requests

You can register bugs and feature requests in the [Github Issue Tracker](https://github.com/cucumber/cucumber-jvm/issues).

You're most likely going to paste code and output, so familiarise yourself with
[Github Flavored Markdown](http://github.github.com/github-flavored-markdown/) to make sure it remains readable.

*At the very least - use triple backticks*:

<pre>
```java
// Why doesn't this work?
@Given("I have 3 cukes in my (.*)")
public void some_cukes(int howMany, String what) {
    // HALP!
}
```
</pre>

Please consider including the following information if you register a ticket:

* What cucumber-jvm version you're using
* What modules you're using (`cucumber-java`, `cucumber-spring`, `cucumber-groovy` etc)
* What command you ran
* What output you saw
* How it can be reproduced

### How soon will my ticket be fixed?

The best way to have a bug fixed or feature request implemented is to
[fork the cucumber-jvm repo](http://help.github.com/fork-a-repo/) and send a
[pull request](http://help.github.com/send-pull-requests/).
If the pull request has good tests and follows the coding conventions (see below) it has a good chance of
making it into the next release.

If you don't fix the bug yourself (or pay someone to do it for you), the bug might never get fixed. If it is a serious
bug, other people than you might care enough to provide a fix.

In other words, there is no guarantee that a bug or feature request gets fixed. Tickets that are more than 6 months old
are likely to be closed to keep the backlog manageable.

## Contributing fixes

See [CONTRIBUTING.md](https://github.com/cucumber/cucumber-jvm/blob/master/CONTRIBUTING.md)
