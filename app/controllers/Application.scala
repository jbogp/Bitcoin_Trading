package controllers

import play.api._
import play.api.mvc._
import models.Bitstamp
import models.DBInteraction

object Application extends Controller {

	def index = Action {
		Ok(views.html.index("Mon premier changement"))
	}

	def trades(unit:String,num:Int) = Action {
		val effectNum:Int = num match{
		  case 0 => unit match {
		    case "minute" => 30
		    case _ => 1
		  }
		  case _ => num
		}
		unit match {
		  case "day" => 	Ok(views.html.trades("Bitstamp live trades",DBInteraction.allTransactions(effectNum,"day").toString,DBInteraction.getMA(effectNum,"day").toString))
		  case "minute" => 	Ok(views.html.trades("Bitstamp live trades",DBInteraction.allTransactions(effectNum,"minute").toString,DBInteraction.getMA(effectNum,"minute").toString))
		  case _ => 	Ok(views.html.trades("Bitstamp live trades",DBInteraction.allTransactions(effectNum,"hour").toString,DBInteraction.getMA(effectNum,"hour").toString))
		}
	}
	
	
	println("Application started")



}