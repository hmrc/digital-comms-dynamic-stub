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

import javax.inject.Inject
import models.DynamicDataModel
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import repositories.DynamicStubDataRepository
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.{ExecutionContext, Future}

class SetupDataController @Inject()(dataRepository: DynamicStubDataRepository)(
                                    implicit val ec: ExecutionContext,
                                    cc: ControllerComponents) extends BackendController(cc) {

  val addData: Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[DynamicDataModel](json => json.method.toUpperCase match {
      case "GET" | "POST" => addStubDataToDB(json)
      case x => Future.successful(BadRequest(s"The method: $x is currently unsupported"))
    }).recover {
      case ex => InternalServerError("Unexpected exception returned when adding data to stub")
    }
  }

  private def addStubDataToDB(json: DynamicDataModel): Future[Result] =
    dataRepository.insert(json).map {
      case result if result.ok => Ok(s"The following JSON was added to the stub: \n\n${Json.toJson(json)}")
      case _ => InternalServerError("Failed to add data to Stub.")
  }

  val removeAll: Action[AnyContent] = Action.async {
    dataRepository.removeAll().map {
      case result if result.ok => Ok("Removed All Stubbed Data")
      case _ => InternalServerError("Unexpected Error Clearing MongoDB.")
    }
  }
}
