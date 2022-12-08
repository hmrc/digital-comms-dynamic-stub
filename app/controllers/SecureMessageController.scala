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

import models.SecureCommsRequestModel
import models.SecureCommsServiceRequestModel.format
import play.api.libs.json.JsValue
import play.api.mvc._
import services.SecureMessageService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton()
class SecureMessageController @Inject()(secureMessageService: SecureMessageService)
                                       (implicit val ec: ExecutionContext,
                                        cc: ControllerComponents) extends BackendController(cc) {

  def insert: Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[SecureCommsRequestModel](model =>
      secureMessageService.insert(model) map {
        case result if result.wasAcknowledged()  => Created
        case _ => InternalServerError
      }
    )
  }

  def remove: Action[AnyContent] = Action.async {
    secureMessageService.removeAll().map {
      case result if result.wasAcknowledged() => Ok
      case _ => InternalServerError
    }
  }

  def count: Action[AnyContent] = Action.async {
    secureMessageService.count().map(x => Ok(x.toString))
  }
}
