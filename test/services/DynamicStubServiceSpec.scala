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

package services

import base.BaseSpec
import models.DynamicDataModel
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import repositories.DynamicStubRepository
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport

class DynamicStubServiceSpec extends BaseSpec with DefaultPlayMongoRepositorySupport[DynamicDataModel] {

  override lazy val repository = new DynamicStubRepository(mongoComponent)
  lazy val service = new DynamicStubService(mongoComponent)

  val dynamicDataModel: DynamicDataModel = DynamicDataModel("id1", "get", 1, None)

  "DynamicStubDataRepository" should {

    "insert a given document" in {
      val result = await(service.insert(dynamicDataModel))
      result.wasAcknowledged() shouldBe true
    }

    "find matching documents given a query" in {
      val result = {
        await(service.insert(dynamicDataModel))
        await(service.find(Seq("_id" -> "id1")))
      }
      result shouldBe List(dynamicDataModel)
    }

    "remove all documents from the collection" in {
      val result = {
        await(service.insert(dynamicDataModel))
        await(service.removeAll())
      }
      result shouldBe successDeleteResult
    }

    "contain a DynamicStubRepository" in {
      service.repository.getClass shouldBe repository.getClass
    }
  }
}
