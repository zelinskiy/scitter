package com.example.app

import org.scalatra._
import scala.util.{Failure, Success, Try}
import com.mongodb.casbah.Imports._



class UserServlet extends HelloWorldAppStack {

  val rand = scala.util.Random

  post("/login") {
    Users.Login(params("username"), params("password"))
  }

  post("/logout") {
    Users.CurrentUser = Users.DefaultUser
  }

 
  get("/") {
    contentType="text/html"
    ssp("/WEB-INF/templates/views/users.ssp",
      "layout" -> "",
      "users" -> Users(),
      "me" -> Users.CurrentUser
    )
  }
  

  
  get("/:id") {
    contentType="text/html"
    ssp("/WEB-INF/templates/views/user.ssp",
      "layout" -> "",
      "user" -> Users.FindUserById(params("id")),
      "me" -> Users.CurrentUser
    )
  }


  post("/create") {
    Users.AddUser(params("username"), params("password"))
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
          post("posttype").toString,
          post("title").toString, 
          post("body").toString          
        )

  def FindPostById(postid:String):Post = 
    AllPosts.find(po => po.Id == postid) match{
        case Some(p: Post) => p
        case None => null
      }

}



object Users{

  val rand = scala.util.Random

  val DefaultUser = new User("-1", "Guest", "228")

  def apply() = AllUsers.toList

  val mongoClient =  MongoClient()
  val usersColl = mongoClient("scitterdb")("users")


  var CurrentUser = DefaultUser




  def AllUsers = for { user <- usersColl }
        yield new User(
          user("id").toString,
          user("name").toString,
          user("password").toString
        )
  

  def FindUserById(userid:String):User = 
    AllUsers.find(user => user.Id == userid) match{
        case Some(u: User) => u
        case None => null
      }

  def AddUser(username:String, password:String){
    val builder = MongoDBObject.newBuilder
    builder += "id" -> rand.nextInt(1000000)
    builder += "name" -> username
    builder += "password" -> password
    usersColl += builder.result.asDBObject
  }

  def Login(username:String, password:String){
    AllUsers
    .find(user => 
      user.Name == username 
      && user.Password == password) 
    match{
        case Some(u: User) => CurrentUser = u
        case None => null
      }
  }

}



class User(id:String, name:String, password:String){

  val Id = id
  val Name = name
  val Password = password
  val Location = "/user/" + Id




  def Posts=
    com.example.app.Posts().filter(post => post.UserId == Id)
  

  

}





class Post(userid:String, id:String, posttype:String, title:String, body: String){

  val Id = id
  val UserId = userid  
  val Location = "/post/"+ Id

  val Title = title
  val Body = body

  val Type = posttype 





  def AsMongoObject:DBObject = { 
    val builder = MongoDBObject.newBuilder
    builder += "userid" -> UserId
    builder += "id" -> Id
    builder += "posttype" -> posttype
    builder += "title" -> Title
    builder += "body" -> Body
    builder.result.asDBObject
  }

}



