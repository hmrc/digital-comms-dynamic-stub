/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import play.core.PlayVersion
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, defaultSettings, integrationTestSettings}
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "digital-comms-dynamic-stub"

lazy val appDependencies: Seq[ModuleID] = compile ++ test()

val compile = Seq(
  "uk.gov.hmrc"             %% "bootstrap-play-25"          % "5.1.0",
  "uk.gov.hmrc"             %% "simple-reactivemongo"       % "7.22.0-play-25"
)

def test(scope: String = "test, it"): Seq[ModuleID] = Seq(
  "uk.gov.hmrc"             %% "hmrctest"                    % "3.9.0-play-25"         % scope,
  "org.scalatest"           %% "scalatest"                   % "3.0.8"                 % scope,
  "com.typesafe.play"       %% "play-test"                   % PlayVersion.current     % scope,
  "org.pegdown"             %  "pegdown"                     % "1.6.0"                 % scope,
  "org.scalatestplus.play"  %% "scalatestplus-play"          % "2.0.1"                 % scope,
  "org.scalamock"           %% "scalamock-scalatest-support" % "3.6.0"                 % scope,
  "uk.gov.hmrc"             %% "reactivemongo-test"          % "4.16.0-play-25"        % scope
)

lazy val coverageSettings: Seq[Setting[_]] = {
  import scoverage.ScoverageKeys

  val excludedPackages = Seq(
    "<empty>",
    ".*Reverse.*",
    ".*Routes.*",
    ".*standardError*.*",
    ".*govuk_wrapper*.*",
    ".*main_template*.*",
    "uk.gov.hmrc.BuildInfo",
    "app.*",
    "prod.*",
    "config.*",
    "testOnly.*",
    ".*feedback*.*",
    "partials.*"
  )

  Seq(
    ScoverageKeys.coverageExcludedPackages := excludedPackages.mkString(";"),
    ScoverageKeys.coverageMinimum := 95,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] = tests map {
  test => Group(test.name, Seq(test), SubProcess(
    ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name, "-Dlogger.resource=-logback-test.xml"))
  ))
}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(defaultSettings(): _*)
  .settings(publishingSettings: _*)
  .settings(coverageSettings: _*)
  .settings(
    PlayKeys.playDefaultPort := 9175,
    majorVersion := 0,
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    routesGenerator := InjectedRoutesGenerator,
    resolvers ++= Seq(Resolver.jcenterRepo, Resolver.bintrayRepo("hmrc", "releases"))
  )
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(
    Keys.fork in IntegrationTest := false,
    unmanagedSourceDirectories in IntegrationTest := (baseDirectory in IntegrationTest) (base => Seq(base / "it")).value,
    addTestReportOption(IntegrationTest, "int-test-reports"),
    testGrouping in IntegrationTest := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
    parallelExecution in IntegrationTest := false
  )
