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

package services

import base.BaseSpec
import models.SecureCommsServiceRequestModel.formats
import models._
import repositories.SecureMessageRepository
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport

class SecureMessageServiceSpec extends BaseSpec with DefaultPlayMongoRepositorySupport[SecureCommsRequestModel] {

  override lazy val repository = new SecureMessageRepository(mongoComponent)
  lazy val service = new SecureMessageService(mongoComponent)

  "SecureMessageService" should {

    "insert a given document" in {
      val result = await(service.insert(secureCommsModel))
      result.wasAcknowledged() shouldBe true
    }

    "count all documents in the collection" in {
      val result = {
        await(service.insert(secureCommsModel))
        await(service.count())
      }
      result shouldBe 1
    }

    "remove all documents from the collection" in {
      val result = {
        await(service.insert(secureCommsModel))
        await(service.removeAll())
      }
      result shouldBe successDeleteResult
    }

    "contain an SecureMessageRepository" in {
      service.repository.getClass shouldBe repository.getClass
    }
  }
}
