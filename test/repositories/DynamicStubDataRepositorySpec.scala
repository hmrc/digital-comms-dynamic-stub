/*
 * Copyright 2021 HM Revenue & Customs
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
import models.DynamicDataModel
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import play.modules.reactivemongo.ReactiveMongoComponent
import uk.gov.hmrc.mongo.MongoSpecSupport

class DynamicStubDataRepositorySpec extends BaseSpec with MockFactory with MongoSpecSupport {

  val mockMongo: ReactiveMongoComponent = injector.instanceOf[ReactiveMongoComponent]

  val mockRepo: DynamicStubRepository =
    new DynamicStubRepository()(() => mongo(), DynamicDataModel.formats)

  lazy val mockDataRepo: DynamicStubDataRepository = new DynamicStubDataRepository(mockMongo) {
    override lazy val repository: DynamicStubRepository = mockRepo
  }

  val dynamicDataModel: DynamicDataModel = DynamicDataModel("id1", "get", 1, None)
  val dynamicDataJson: JsValue = Json.toJson(dynamicDataModel)

  "DynamicStubDataRepository" should {

    "insert a given document" in {
      val result = await(mockDataRepo.insert(dynamicDataModel))
      result.ok shouldBe true
      result.writeErrors shouldBe Seq()
    }

    "find matching documents given a query" in {
      val result = await(mockDataRepo.find("_id" -> "id1"))
      result shouldBe List(dynamicDataModel)
    }

    "remove all documents from the collection" in {
      val result = await(mockDataRepo.removeAll())
      result.ok shouldBe true
      result.writeErrors shouldBe Seq()
    }

    "contain a DynamicStubRepository" in {
      val dynamicStubRepo = new DynamicStubDataRepository(mockMongo)
      dynamicStubRepo.repository.getClass shouldBe mockRepo.getClass
    }
  }
}
