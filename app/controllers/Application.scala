package controllers

import play.api._
import play.api.mvc._
import com.tindr.pusher.Pusher

object Application extends Controller {

	def index = Action {
		Ok(views.html.index("Mon premier changement"))
	}

	def trades = Action {
		Ok(views.html.trades("Bitstamp live trades"))
	}


	val pusher = Pusher()
	println("Application started")



}