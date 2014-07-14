package models

import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._
import play.api.libs.ws._
import scala.concurrent.Future
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits._
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import javax.crypto
import java.net.URI
import org.apache.commons.codec.binary.Base64.encodeBase64

import scala.slick.lifted.TableQuery

object Bitstamp {

	def trades = {}

	def hex(buf:Array[Byte]):String =  {

		buf.map("%02X" format _).mkString

	}

	def authenticate: Map[String,Seq[String]] = {

			val SHA256 = "HmacSHA256";
			val apiKey = "s0Q9GPEc1GbfhdoA1nYvfDI0cQazWoVi"
					val secret = "y6C8nzE3w2AJ607Dm4fcQVKlMZzqjNvV"
					val nonce: Long = System.currentTimeMillis / 1000
					val message = nonce+"109500"+apiKey
					val md = new crypto.spec.SecretKeySpec(secret.getBytes("UTF-8"),SHA256)
			val md2 = java.security.MessageDigest.getInstance("SHA-256");
			val signature = {
					val mac = crypto.Mac.getInstance(SHA256)
							mac.init(md)
							this.hex(mac.doFinal(message.getBytes("UTF-8")))
			}
			Map("key" -> Seq(apiKey),"nonce" -> Seq(nonce.toString),"signature" -> Seq(signature))

	}

	def getBalance:Future[WSResponse] ={
			 WS.url("https://www.bitstamp.net/api/balance/").post(authenticate)
	}
	
	def getTransactions:Future[WSResponse] ={
			 WS.url("https://www.bitstamp.net/api/transactions/").withQueryString("time" -> "minute").get()
	}
	
	
}