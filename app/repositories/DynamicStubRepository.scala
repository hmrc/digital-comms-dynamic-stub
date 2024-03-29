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

import common.Constants
import models.DynamicDataModel
import org.mongodb.scala.model.{IndexModel, IndexOptions, Indexes}
import play.api.libs.json.Format
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import scala.concurrent.ExecutionContext

@Singleton
class DynamicStubRepository(mongo: MongoComponent)
                           (implicit ec: ExecutionContext,
                            formats: Format[DynamicDataModel]) extends PlayMongoRepository[DynamicDataModel](
  mongoComponent = mongo,
  collectionName = "data",
  domainFormat = formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("creationTimestamp"),
    IndexOptions().name("expiry").expireAfter(Constants.timeToLiveInSeconds, TimeUnit.SECONDS)
  )),
  replaceIndexes = true
)
