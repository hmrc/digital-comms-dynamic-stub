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

package repositories

import play.api.libs.json.{Format, JsResult, JsSuccess, JsValue}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import scala.concurrent.ExecutionContext


class EmailRepository(mongoComponent: MongoComponent)(implicit ec: ExecutionContext) extends PlayMongoRepository[JsValue](
  mongoComponent = mongoComponent,
  collectionName = "email",
  domainFormat   = EmailRepository.rawFormat,
  indexes        = Seq()
)

object EmailRepository {
  val rawFormat: Format[JsValue] = new Format[JsValue] {
    override def reads(json: JsValue): JsResult[JsValue] = JsSuccess(json)
    override def writes(o: JsValue): JsValue = o
  }
}
