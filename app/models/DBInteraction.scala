package models


import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.TableQuery
import play.api.libs.json._
import scala.slick.jdbc.{GetResult, StaticQuery => Q}


object DBInteraction {

  	// The query interface for the Suppliers table
	val transactions: TableQuery[Transactions_3min] = TableQuery[Transactions_3min]
	val db = Database.forName("DefaultDS")
  
  	def allTransactions(num:Int,unit:String): JsArray ={
	  
	  		var jsonArr = Json.arr()
	  
	  		db.withSession{ implicit session=>
				
				val timeRef:Long = unit match {
					case "hour" => (System.currentTimeMillis / 1000)-3600*num
					case "day" =>(System.currentTimeMillis / 1000)-86400*num
					case "minute" => (System.currentTimeMillis / 1000)-60*num
					case _ => 0
				}
	
		  transactions.filter(_.date>timeRef.toLong).sortBy(_.date).foreach{
			    case (id, timestamp,price,amount) => {
			      jsonArr = jsonArr :+JsArray(Seq(
			        JsNumber(timestamp*1000),
			        JsNumber(price)
			      ))
			    } 
			  }
			}
			
			jsonArr								
	}
	
  	def getMA(num:Int,unit:String):JsArray={

	  
	  		var jsonArr = Json.arr()
	  		val amFrame = 10
	  
	  		db.withSession{ implicit session=>
				
				val timeRef:Long = unit match {
					case "hour" => (System.currentTimeMillis / 1000)-3600*num
					case "day" =>(System.currentTimeMillis / 1000)-86400*num
					case "minute" => (System.currentTimeMillis / 1000)-60*num
					case _ => 0
				}
	  		
	  	   val trans = transactions.filter(_.date>timeRef.toLong).sortBy(_.date)
	  	   val transPrev = transactions.filter(_.date<=timeRef.toLong).sortBy(_.date.desc).take(amFrame)
	  	   val prevPrices = transPrev.map(_.price).list(session)
		   val listPrices= trans.map(_.price).list(session)
	  	   val listDates = trans.map(_.date).list(session)
	  	   val ma = this.simpleMovingAverage(listPrices, 30)
	  	   val dat = (listDates zip ma)
		   dat.foreach{
	  	     case (timestamp,price) =>
			      jsonArr = jsonArr :+JsArray(Seq(
			        JsNumber(timestamp*1000),
			        JsNumber(price)
			      ))
			  }
			}
  	  		jsonArr
  	  		
										
	}
	
	
	
	def movingSum(values: List[Double], period: Int): List[Double] = period match {
		case 0 => throw new IllegalArgumentException
		case 1 => values
		case 2 => values.sliding(2).map(_.sum).toList
		case odd if odd % 2 == 1 => values zip movingSum(values drop 1, (odd - 1)) map Function.tupled(_+_)
		case even =>val half = even / 2;val partialResult = movingSum(values, half)
		partialResult zip (partialResult drop half) map Function.tupled(_+_)
	}
	
	def simpleMovingAverage(values: List[Double], period: Int): List[Double] = List.fill(period - 1)(values.head) ::: (movingSum(values, period) map (_ / period))


}