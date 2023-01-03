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

package repositories

import models.SecureCommsRequestModel
import play.api.libs.json.Format
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import uk.gov.hmrc.mongo.MongoComponent

import javax.inject.Singleton
import scala.concurrent.ExecutionContext

@Singleton
class SecureMessageRepository(mongo: MongoComponent)
                             (implicit ec: ExecutionContext,
                              formats: Format[SecureCommsRequestModel]) extends PlayMongoRepository[SecureCommsRequestModel](
  mongoComponent = mongo,
  collectionName = "secure-message",
  domainFormat = formats,
  indexes = Seq()
)
