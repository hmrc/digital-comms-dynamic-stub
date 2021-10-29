# digital-comms-dynamic-stub

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/hmrc/digital-comms-dynamic-stub.svg)](https://travis-ci.org/hmrc/digital-comms-dynamic-stub)

## Summary

This stub service is used in acceptance testing of the digital comms solution involving [comms-queue-manager](https://github.com/hmrc/comms-queue-manager).

Generally, when a dynamic stub is called with a url, the stub will check its mongo database for a record with a matching url and return the data it has associated with that url. In the case of this stub, the requests made to the stub will be for the following purposes:-
- to trigger an API1 notification of change approved or rejected (to mimic ETMP/DES behaviour)
- to get API2 details in order to construct an email and / or secure comm message (mimic response from ETMP/DES)
- to post a request for sending an email (mimic response from [comms-email](https://github.com/hmrc/comms-email))
- to post a request for sending a secure comm message (mimic response from [comms-secure-message](https://github.com/hmrc/comms-secure-message))

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE](https://www.java.com/en/download/) to run.

### Running the application

In order to run this microservice, you must have SBT installed. You should then be able to start the application using:

`sbt run`

### License

This code is open source software licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)
