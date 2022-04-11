/*
 * Copyright 2022 HM Revenue & Customs
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

package controllers

import models.DynamicDataModel
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import play.api.libs.json.{JsValue, Json}
import services.DynamicStubService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class RequestHandlerController @Inject()(dataRepository: DynamicStubService)(
                                        implicit ec: ExecutionContext,
                                        cc: ControllerComponents) extends BackendController(cc) {

  def getRequestHandler(url: String): Action[AnyContent] = Action.async { implicit request =>

    val dataNotUsingQueryStringParameters =
      dataRepository.find(Seq("_id" -> s"""${request.uri.takeWhile(_ != '?')}""", "method" -> "GET"))
    val dataUsingQueryStringParameters =
      dataRepository.find(Seq("_id" -> request.uri, "method" -> "GET"))

    def getResult(data: Seq[DynamicDataModel]): Result = data match {
      case head :: _ if head.response.nonEmpty => Status(head.status)(head.response.get) //return status and body
      case head :: _ => Status(head.status) //Only return status, no body.
      case _ => NotFound(errorResponseBody(request.uri))
    }

    for {
      dataBasedOnUrlPath <- dataNotUsingQueryStringParameters
      dataBasedOnCompleteUri <- dataUsingQueryStringParameters
    } yield {
      if (dataBasedOnCompleteUri.nonEmpty) getResult(dataBasedOnCompleteUri) else getResult(dataBasedOnUrlPath)
    }
  }

  def postRequestHandler(url: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.find(Seq("_id" -> s"""${request.uri.takeWhile(_ != '?')}""", "method" -> "POST")).map {
      case head :: _ if head.response.nonEmpty => Status(head.status)(head.response.get) //return status and body
      case head :: _ => Status(head.status) //Only return status, no body.
      case _ => NotFound(errorResponseBody(request.uri))
    }
  }

  def errorResponseBody(path: String): JsValue = {
    Json.obj(
      "status" -> "404",
      "message" -> s"Could not find endpoint in Dynamic Stub matching the URI: $path",
      "path" -> path
    )
  }
}
