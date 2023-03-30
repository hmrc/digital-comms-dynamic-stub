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

package models

import play.api.libs.json.{Format, JsNull, JsObject, Json, OFormat, Writes}
import uk.gov.hmrc.mongo.play.json.formats.MongoJavatimeFormats

import java.time.Instant

case class TaxIdentifierModel(name: String, value: String)

object TaxIdentifierModel {
  implicit val format: OFormat[TaxIdentifierModel] = Json.format[TaxIdentifierModel]
}

case class NameModel(line1: String, line2: Option[String] = None, line3: Option[String] = None)

object NameModel {
  implicit val format: OFormat[NameModel] = Json.format[NameModel]
}

case class RecipientModel(
                           taxIdentifier: TaxIdentifierModel,
                           name: NameModel,
                           email: String
                         )

object RecipientModel {
  implicit val format: OFormat[RecipientModel] = Json.format[RecipientModel]
}

case class ExternalRefModel(id: String, source: String)

object ExternalRefModel {
  implicit val format: OFormat[ExternalRefModel] = Json.format[ExternalRefModel]
}


case class SecureCommsRequestModel(externalRef: ExternalRefModel,
                                   recipient: RecipientModel,
                                   messageType: String,
                                   subject: String,
                                   content: String)

object SecureCommsServiceRequestModel {
  implicit val dateFormat: Format[Instant] = MongoJavatimeFormats.instantFormat

  implicit val formats: Format[SecureCommsRequestModel] = Format(
    Json.reads[SecureCommsRequestModel],
    Writes[SecureCommsRequestModel] { model =>
      JsObject(Json.obj(
        "externalRef" -> model.externalRef,
        "recipient" -> model.recipient,
        "messageType" -> model.messageType,
        "subject" -> model.subject,
        "content" -> model.content,
        "creationTimestamp" -> Instant.now()
      ).fields.filterNot(_._2 == JsNull))
    }
  )
}
