/*
 * Copyright 2020 HM Revenue & Customs
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
import javax.inject.Inject
import models.DynamicDataModel
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.{Format, JsValue, Json}
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.commands.DefaultWriteResult
import uk.gov.hmrc.mongo.MongoSpecSupport

class DynamicStubDataRepositorySpec @Inject()(reactiveMongoComponent: ReactiveMongoComponent) extends BaseSpec with MockFactory with MongoSpecSupport {

  val mockFormat: Format[DynamicDataModel] = mock[Format[DynamicDataModel]]
  val mockRepo: DynamicStubRepository =
    new DynamicStubRepository()(() => mongo(), mockFormat)

  lazy val mockDataRepo: DynamicStubDataRepository = new DynamicStubDataRepository(reactiveMongoComponent) {
    override lazy val repository: DynamicStubRepository = mockRepo
  }

  val successResult = DefaultWriteResult(ok = true, n = 1, writeErrors = Seq(), None, None, None)
  val dynamicDataModel = DynamicDataModel("id1", "get", 1, None)
  val dynamicDataJson: JsValue = Json.toJson(dynamicDataModel)

  "DynamicStubDataRepository" should {

    "insert a given document" in {
      (mockFormat.writes(_: DynamicDataModel)).expects(dynamicDataModel).returning(Json.toJson(dynamicDataModel))
      val result = await(mockDataRepo.insert(dynamicDataModel))
      result shouldBe successResult
    }

    "find matching documents given a query" in {
      (mockFormat.reads(_: JsValue)).expects(dynamicDataJson).returning(dynamicDataJson.validate[DynamicDataModel])
      val result = await(mockDataRepo.find("_id" -> "id1"))
      result shouldBe List(dynamicDataModel)
    }

    "remove all documents from the collection" in {
      val result = await(mockDataRepo.removeAll())
      result shouldBe successResult
    }

    "contain a DynamicStubRepository" in {
      val dynamicStubRepo = new DynamicStubDataRepository(reactiveMongoComponent)
      dynamicStubRepo.repository.getClass shouldBe mockRepo.getClass
    }
  }
}
