package models

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.{ProvenShape, ForeignKeyQuery}

// A Suppliers table with 6 columns: id, name, street, city, state, zip
class Transactions_3min(tag: Tag) extends Table[(Option[Int], Long, Double, Double)](tag, "Transactions_3min") {

  // This is the primary key column:
  def id: Column[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def date: Column[Long] = column[Long]("date")
  def price: Column[Double] = column[Double]("price")
  def amount: Column[Double] = column[Double]("amount")
  
  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[(Option[Int], Long, Double, Double)] = (id.?, date, price, amount)
  

}
