import play.api._
import play.api.libs.concurrent.Akka
import akka.actor.Props
import play.api.Play.current
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.TimeUnit
import akka.actor.Actor
import models.TestActor

object Global extends GlobalSettings {

	override def onStart(app: Application) {
		val myActor = Akka.system.actorOf(Props[TestActor], name = "myactor")
		Akka.system.scheduler.schedule(FiniteDuration.apply(0, TimeUnit.SECONDS), FiniteDuration.apply(1, TimeUnit.MINUTES), myActor, "transactions")
	} 

}

