# Cucumber JVM Fork to Continue Next Steps for Predefined Exceptions
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
