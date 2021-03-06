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
import play.api.libs.json.{JsSuccess, Json}
import uk.gov.hmrc.mongo.MongoSpecSupport

class EmailRepositorySpec extends BaseSpec with MongoSpecSupport {

  "EmailRepository" should {

    val validJson = Json.obj("test " -> "test")

    "format valid json to a JsSuccess" in {
      val result = EmailRepository.rawFormat.reads(validJson)

      result shouldBe JsSuccess(validJson)
    }
  }
}
