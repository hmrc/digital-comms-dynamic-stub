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

package controllers

import mocks.MockDynamicDataRepository
import models.DynamicDataModel
import play.api.libs.json.Json
import play.mvc.Http.Status

class RequestHandlerControllerSpec extends MockDynamicDataRepository {

  lazy val controller = new RequestHandlerController(mockDataRepository)

  lazy val successModel = DynamicDataModel(
    _id = "/test",
    method = "GET",
    status = Status.OK,
    response = None
  )

  lazy val successWithBodyModel = DynamicDataModel(
    _id = "/test",
    method = "GET",
    status = Status.OK,
    response = Some(Json.parse("""{"something" : "hello"}"""))
  )

  "The getRequestHandler method" should {

    "return the status code specified in the model" in {
      lazy val result = controller.getRequestHandler("/test")(request)
      mockFind(List(successModel)).twice()

      status(result) shouldBe Status.OK
    }

    "return the status and body" in {
      lazy val result = controller.getRequestHandler("/test")(request)
      mockFind(List(successWithBodyModel)).twice()

      status(result) shouldBe Status.OK
      await(bodyOf(result)) shouldBe s"${successWithBodyModel.response.get}"
    }

    "return a 404 status when the endpoint cannot be found" in {
      lazy val result = controller.getRequestHandler("/test")(request)
      mockFind(List()).twice()

      status(result) shouldBe Status.NOT_FOUND
    }
  }

  "The postRequestHandler method" should {

    "return a response status when there is no stubbed response body for an incoming POST request" in {
      lazy val result = controller.postRequestHandler("/test")(request)
      mockFind(List(successModel))

      status(result) shouldBe Status.OK
    }

    "return the corresponding response of an incoming POST request" in {
      lazy val result = controller.postRequestHandler("/test")(request)
      mockFind(List(successWithBodyModel))

      await(bodyOf(result)) shouldBe s"${successWithBodyModel.response.get}"
    }

    "return a 404 status if the endpoint specified in the POST request can't be found" in {
      lazy val result = controller.postRequestHandler("/test")(request)
      mockFind(List())

      status(result) shouldBe Status.NOT_FOUND
    }
  }

  "Calling .errorResponseBody" should {

    "return a formatted json body" in {
      val body = Json.obj(
        "status" -> "404",
        "message" -> s"Could not find endpoint in Dynamic Stub matching the URI: url",
        "path" -> "url"
      )
      lazy val result = controller.errorResponseBody("url")

      result shouldBe body
    }
  }
}