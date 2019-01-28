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

package repositories

import base.BaseSpec
import models.DynamicDataModel
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.{Format, JsValue, Json}
import reactivemongo.api.commands.DefaultWriteResult
import uk.gov.hmrc.mongo.MongoSpecSupport

class DynamicStubDataRepositorySpec extends BaseSpec with MockFactory with MongoSpecSupport {

  val mockFormat: Format[DynamicDataModel] = mock[Format[DynamicDataModel]]
  val mockRepo: DynamicStubRepository =
    new DynamicStubRepository()(() => mongo(), mockFormat, mock[Manifest[DynamicDataModel]])

  lazy val mockDataRepo: DynamicStubDataRepository = new DynamicStubDataRepository {
    override lazy val repository: DynamicStubRepository = mockRepo
  }

  val successWriteResult = DefaultWriteResult(ok = true, n = 1, writeErrors = Seq(), None, None, None)
  val successDeletionResult = DefaultWriteResult(ok = true, n = 0, writeErrors = Seq(), None, None, None)
  val dynamicDataModel = DynamicDataModel("id1", "get", 1, None)
  val dynamicDataJson: JsValue = Json.toJson(dynamicDataModel)

  "DynamicStubDataRepository" should {

    "insert a given document" in {
      (mockFormat.writes(_: DynamicDataModel)).expects(dynamicDataModel).returning(Json.toJson(dynamicDataModel))
      val result = await(mockDataRepo.insert(dynamicDataModel))
      result shouldBe successWriteResult
    }

    "find matching documents given a query" in {
      (mockFormat.reads(_: JsValue)).expects(dynamicDataJson).returning(dynamicDataJson.validate[DynamicDataModel])
      val result = await(mockDataRepo.find("_id" -> "id1"))
      result shouldBe List(dynamicDataModel)
    }

    "remove a document with a specified ID" in {
      val result = await(mockDataRepo.removeById("id1"))
      result shouldBe successWriteResult
    }

    "remove all documents from the collection" in {
      val result = await(mockDataRepo.removeAll())
      result shouldBe successDeletionResult
    }
  }
}
