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

package services

import base.BaseSpec
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.{JsValue, Json}
import play.modules.reactivemongo.ReactiveMongoComponent
import repositories.SecureMessageRepository
import uk.gov.hmrc.mongo.MongoSpecSupport

class SecureMessageServiceSpec extends BaseSpec with MongoSpecSupport with MockFactory {

  val mockMongo = injector.instanceOf[ReactiveMongoComponent]

  val mockSecureMessageRepo: SecureMessageRepository = new SecureMessageRepository()(() => mongo())
  lazy val mockSecureMessageService: SecureMessageService = new SecureMessageService(mockMongo) {
    override lazy val repository: SecureMessageRepository = mockSecureMessageRepo
  }

  val testJson: JsValue = Json.obj(
    "test" -> "test"
  )

  "SecureMessageService" should {

    "insert a given document" in {
      val result = await(mockSecureMessageService.insert(testJson))
      result shouldBe true
    }

    "remove all documents from the collection" in {
      val result = await(mockSecureMessageService.removeAll())
      result shouldBe true
    }

    "count all documents in the collection" in {
      val result = await(mockSecureMessageService.count())
      result shouldBe 0
    }

    "contain an SecureMessageRepository" in {
      val service = new SecureMessageService(mockMongo)
      service.repository.getClass shouldBe mockSecureMessageRepo.getClass
    }
  }
}
