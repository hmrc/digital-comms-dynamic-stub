/*
 * Copyright 2023 HM Revenue & Customs
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

import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, defaultSettings, integrationTestSettings}

val appName = "digital-comms-dynamic-stub"
val hmrcMongoVersion = "1.7.0"
val bootstrapPlayVersion = "8.4.0"
ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "2.13.12"

lazy val appDependencies: Seq[ModuleID] = compile ++ test()

val compile = Seq(
  "uk.gov.hmrc"       %% "bootstrap-backend-play-30" % bootstrapPlayVersion,
  "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30"        % hmrcMongoVersion
)

def test(scope: String = "test, it"): Seq[ModuleID] = Seq(
  "uk.gov.hmrc"       %% "bootstrap-test-play-30"      % bootstrapPlayVersion  % scope,
  "org.scalamock"     %% "scalamock"                   % "5.2.0"               % scope,
  "uk.gov.hmrc.mongo" %% "hmrc-mongo-test-play-30"     % hmrcMongoVersion      % scope
)

lazy val coverageSettings: Seq[Setting[_]] = {
  import scoverage.ScoverageKeys

  val excludedPackages = Seq(
    ".*Reverse.*",
    ".*Routes.*"
  )

  Seq(
    ScoverageKeys.coverageExcludedPackages := excludedPackages.mkString(";"),
    ScoverageKeys.coverageMinimumStmtTotal := 95,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(defaultSettings(): _*)
  .settings(coverageSettings: _*)
  .settings(
    PlayKeys.playDefaultPort := 9175,
    libraryDependencies ++= appDependencies,
    scalacOptions ++= Seq("-Wconf:cat=unused-imports&src=.*routes.*:s"),
    retrieveManaged := true,
    routesImport := Seq.empty,
    routesGenerator := InjectedRoutesGenerator
  )
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(
    IntegrationTest / Keys.fork := false,
    IntegrationTest / unmanagedSourceDirectories := (IntegrationTest / baseDirectory) (base => Seq(base / "it")).value,
    IntegrationTest / parallelExecution := false,
    addTestReportOption(IntegrationTest, "int-test-reports")
  )
