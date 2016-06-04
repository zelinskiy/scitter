package com.example.app

import org.scalatra._
import scala.util.{Failure, Success, Try}
import com.mongodb.casbah.Imports._



class UserServlet extends HelloWorldAppStack {

  val rand = scala.util.Random

 
  get("/") {
    contentType="text/html"
    ssp("/WEB-INF/templates/views/users.ssp",
      "layout" -> "",
      "users" -> Users()
    )
  }
  

  
  get("/:id") {
    contentType="text/html"
    ssp("/WEB-INF/templates/views/user.ssp",
      "layout" -> "",
      "user" -> Users.FindUserById(params("id"))
    )
  }


  post("/create") {
    Users.AddUser(params("username"))
  }



}


object Posts{

  def apply() = AllPosts.toList

  val mongoClient =  MongoClient()
  val postsColl = mongoClient("scitterdb")("posts")

  def AllPosts = for { post <- postsColl }
        yield new Post(
          post("userid").toString,
          post("id").toString,
          post("title").toString, 
          post("body").toString          
        )

}



object Users{

  val rand = scala.util.Random

  def apply() = AllUsers.toList

  val mongoClient =  MongoClient()
  val usersColl = mongoClient("scitterdb")("users")

  def AllUsers = for { user <- usersColl }
        yield new User(
          user("id").toString,
          user("name").toString
        )
  

  def FindUserById(userid:String):User = 
    AllUsers.find(user => user.Id == userid) match{
        case Some(u: User) => u
        case None => null
      }

  def AddUser(username:String){
    val builder = MongoDBObject.newBuilder
    builder += "id" -> rand.nextInt(1000000)
    builder += "name" -> username
    usersColl += builder.result.asDBObject
  }
}



class User(id:String, name:String){

  val Id = id
  val Name = name
  val Location = "/user/" + Id

  def Posts=
    com.example.app.Posts().filter(post => post.UserId == Id)
  

  

}


class Post(userid:String, id:String, title:String, body: String){

  val Id = id
  val UserId = userid  
  val Location = "/post/"+ Id

  val Title = title
  val Body = body

}



