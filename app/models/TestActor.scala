package models


import akka.actor.Actor
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import play.api.libs.ws.WSResponse
import play.api.libs.json.JsObject
import scala.slick.driver.MySQLDriver.simple._
import play.api.libs.json.JsValue
import play.api.libs.json.Reads
import play.libs.F.Tuple
import play.api.libs.json._

class TestActor extends Actor{
  
	// The query interface for the Suppliers table
	val transactions: TableQuery[Transactions_3min] = TableQuery[Transactions_3min]
	val db = Database.forName("DefaultDS")
	



	def handleResponse(response:Future[WSResponse]) = {
		val d:Double = 2
			response.onComplete {
				case Success(response)=> response.json.as[List[JsValue]].map{i=>
					db.withSession { implicit session =>
				  		transactions += (None, (i \ "date").as[String].toLong,(i \ "price").as[String].toDouble,(i \ "amount").as[String].toDouble)
					}
				}
				case Failure(response)=> println("echec"+response.getMessage())
			}
		}
	
		def receive = {
		case "balance" => this.handleResponse(Bitstamp.getBalance)
		case "transactions" => this.handleResponse(Bitstamp.getTransactions)
		case _ => println("fee")
	}

}
