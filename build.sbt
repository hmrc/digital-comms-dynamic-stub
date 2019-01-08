import play.core.PlayVersion
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, defaultSettings, integrationTestSettings}
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "digital-comms-dynamic-stub"

lazy val appDependencies: Seq[ModuleID] = compile ++ test()

val compile = Seq(
  "uk.gov.hmrc"             %% "bootstrap-play-25"        % "4.6.0"
)

def test(scope: String = "test, it"): Seq[ModuleID] = Seq(
  "org.scalatest"           %% "scalatest"                % "3.0.5"                 % scope,
  "com.typesafe.play"       %% "play-test"                % PlayVersion.current     % scope,
  "org.pegdown"             %  "pegdown"                  % "1.6.0"                 % scope,
  "org.scalatestplus.play"  %% "scalatestplus-play"       % "2.0.1"                 % scope
)

def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] = tests map {
  test => Group(test.name, Seq(test), SubProcess(
    ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name, "-Dlogger.resource=-logback-test.xml"))
  ))
}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(defaultSettings(): _*)
  .settings(publishingSettings: _*)
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