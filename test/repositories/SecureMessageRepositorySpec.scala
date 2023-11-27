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

import base.BaseSpec
import common.Constants
import models.SecureCommsRequestModel
import models.SecureCommsServiceRequestModel.formats
import org.mongodb.scala.bson.{BsonInt32, BsonString}
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport

class SecureMessageRepositorySpec extends BaseSpec with DefaultPlayMongoRepositorySupport[SecureCommsRequestModel] {

  override lazy val repository = new SecureMessageRepository(mongoComponent)

  "The SecureMessageRepository" should {

    "have a TTL index on the creationTimestamp field, with an expiry time set by the Constants object" in {
      val indexes = {
        await(repository.ensureIndexes())
        await(repository.collection.listIndexes().toFuture())
      }
      val ttlIndex = indexes.find(_.get("name").contains(BsonString("expiry")))

      ttlIndex.get("key").toString shouldBe """{"creationTimestamp": 1}"""
      ttlIndex.get("expireAfterSeconds") shouldBe BsonInt32(Constants.timeToLiveInSeconds)
    }
  }
}
