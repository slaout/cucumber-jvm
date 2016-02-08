# Cucumber-JVM Fork to Continue Next Steps for Predefined Exceptions
The whole purpose of this fork is to be able to continue the execution of the next steps of a scenario when one step fails and we know the next ones still can be executed.

This possibility is disabled by default.
It is up to the step definition developer to declare if the step failure will not impede the next steps.

This is the case when you have an expensive @Given and/or @When, and you want to execute a lot of @Then checks.
In this case, the failure of one of the checks will let the other independent checks to run as well: no failure is hidden by a previous failure.
The failed step is still marked as failed in the execution result.

To mark a step as being unimportant for the next steps, you need to annotate the step definition with the following annotation:
@ContinueNextStepsFor({AssertionError.class})
If another exception is thrown, then the next steps are not executed.

![Screenshot](/fork-purpose.png)

REALLY IMPORTANT:
This works only for Java. This is not implemented for other JVM languages. If you need them, you're encouraged to compare the code of this branch with cucumber-jvm master branch to see how it's implemented (it really only a few lines of code). Feel free to post patches of pull requests to make it work with other languages: I did not have the time not the knowledge to implement the solution for all languages.

See also:
* [The thread on Cucumber forum](https://groups.google.com/forum/#!topic/cukes/xTqSyR1qvSc) proposing this solution, with explanations of the main developers on why this is a bad idea and how to refactor the tests to not use this fork. Please read the thread before using this fork: use the fork if you really need it.
* [How to rework your tests to avoid using this fork](https://github.com/cucumber/cucumber-jvm/issues/771) this thread (the comment "dkowis commented on 4 Sep 2014", to be more previse) also explains how to rework your feature files to avoid using this fork. Still want to use this fork? Now, that's fine, you can use it, you've been warned ;-)
* [Another thread](https://groups.google.com/forum/#!searchin/cukes/continue$20after$20failed$20/cukes/OTUjEupNjYk/tZYfMDcy5MkJ) proposing the solution of using ErrorCollector for JUnit. It's another way of doing the several checks thing, even if [JUnit's ErrorCollector is not supported in Cucumber](https://groups.google.com/forum/#!msg/cukes/qMwgAVzWmR0/GSkRUgJ8f4EJ). Remember: ErrorCollector is a JUnit concept, and it will not be supported if you run your scenarios without JUnit (with the CLI, for instance): you will have to collect the errors yourself (in a dedicated step or on @After)
* [A very wobbly solution if you still want to not skip next steps but you do not use this fork](http://stackoverflow.com/questions/6841467/how-can-i-make-cucumber-run-all-the-steps-not-skip-them-even-if-one-of-them-fa) as this makes the overall scenario fail, but it masks the step failure from Cucumber, so all the step results are green, including the ones that aren't right.
* [Another wobbly solution...](http://stackoverflow.com/questions/15298521/continue-running-cucumber-steps-after-a-failure): log the exception in steps, don't throw any exception in steps, but throw it in @After ; as they say: "The only issue would be that, in the report, all the steps would look green but the test case looks failed." That's exactly why you don't want to do this.

Other links where I offered them this fork, for the purose of explaining how this fork will be useful to many people:
* http://cukes.narkive.com/0k4gufhc/cucumber-continuation-of-cucumber-step-even-after-assertion-failure
* https://groups.google.com/forum/#!searchin/cukes/steps$20continue/cukes/odXSUY9cbK0/FdUl1CgrTCwJ
* http://forumsqa.com/question/how-to-continue-the-test-step-execution-even-on-failure-of-a-test-step-in-cucumber/

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
