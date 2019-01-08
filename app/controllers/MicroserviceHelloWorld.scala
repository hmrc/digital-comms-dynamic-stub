package controllers

import javax.inject.Singleton
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.Future

@Singleton()
class MicroserviceHelloWorld extends BaseController {

	def hello() = Action.async { implicit request =>
		Future.successful(Ok("Hello world"))
	}

}