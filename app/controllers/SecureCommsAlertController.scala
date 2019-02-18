/*
 * Copyright 2019 HM Revenue & Customs
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

import java.text.SimpleDateFormat

import javax.inject.{Inject, Singleton}
import models.SecureCommsDataModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.ExecutionContext

@Singleton
class SecureCommsAlertController @Inject()(implicit ec: ExecutionContext) extends BaseController {
  def handleGet(service: String, regNumber: String, communicationId: String): Action[AnyContent] = Action { implicit request =>

    val dateFormat = new SimpleDateFormat("yyyy-mm-dd")
    val dateToReturn = dateFormat.format(DateTime.now())

    val returnData = SecureCommsDataModel(
      dateToReturn,
      "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
        "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p>" +
        "<p>EFFECTIVE DATE OF DE-REGISTRATION|20181227</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
        "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p>" +
        "<p>CUSTOMER EMAIL STATUS|VERIFIED</p><p>NOTIFICATION PREFERENCE|EMAIL</p><p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p>" +
        "<p>FORMAT PREFERENCE|TEXT</p>"
    )

    Ok(Json.toJson(returnData))
  }
}
