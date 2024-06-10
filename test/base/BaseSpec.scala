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

package base

import org.apache.pekko.actor.ActorSystem
import com.mongodb.client.result.{DeleteResult, InsertOneResult}
import models.{EmailRequestModel, ExternalRefModel, NameModel, RecipientModel, SecureCommsRequestModel, TaxIdentifierModel}
import org.mongodb.scala.bson.BsonObjectId
import org.mongodb.scala.result.{DeleteResult, InsertOneResult}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.Injector
import play.api.mvc.{AnyContentAsEmpty, ControllerComponents}
import play.api.test.FakeRequest
import play.api.test.Helpers.stubControllerComponents

import scala.concurrent.ExecutionContext

trait BaseSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  implicit val cc: ControllerComponents = stubControllerComponents()

  val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
  val injector: Injector = app.injector
  implicit val ec: ExecutionContext = injector.instanceOf[ExecutionContext]

  implicit val system: ActorSystem = ActorSystem("Sys")

  val successWriteResult: InsertOneResult = InsertOneResult.acknowledged(BsonObjectId())
  val errorWriteResult: InsertOneResult = InsertOneResult.unacknowledged()
  val successDeleteResult: DeleteResult = DeleteResult.acknowledged(1)
  val errorDeleteResult: DeleteResult = DeleteResult.unacknowledged()

  val secureCommsModel: SecureCommsRequestModel = SecureCommsRequestModel(
    ExternalRefModel("anId", "source"),
    RecipientModel(TaxIdentifierModel("AA", "06"), NameModel("first", Some("middle"), Some("last")),
      "dragon@born.tam"),
    "testMessageType",
    "testSubject",
    "testContent"
  )

  val emailModel: EmailRequestModel = EmailRequestModel(Seq("email@test.com"), "ABCD", Map(), force = false)
}
