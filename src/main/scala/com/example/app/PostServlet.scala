package com.example.app

import org.scalatra._
import scala.util.{Failure, Success, Try}
import com.mongodb.casbah.Imports._



class PostServlet extends HelloWorldAppStack {

  val rand = scala.util.Random

  val mongoClient =  MongoClient()
  val mongoColl = mongoClient("scitterdb")("posts")

  def myposts = for { post <- mongoColl }
        yield new Post(
          post("title").toString, 
          post("body").toString,
          post("id").toString
        )

  
  
  get("/utils/all") {
    mongoColl.find()
  }

  post("/insert") {
    val builder = MongoDBObject.newBuilder
    builder += "title" -> params("title")
    builder += "body" -> params("body")
    builder += "id" -> rand.nextInt(10000)
    mongoColl += builder.result.asDBObject
  }

  
  get("/") {
    contentType="text/html"
    ssp("/WEB-INF/templates/views/posts.ssp",
      "layout" -> "",
      "posts" -> myposts
    )
  }
  
  get("/:id") {
    myposts.find(post => post.Id == params("id")) match{
      case Some(p: Post) => p.Title + " | " + p.Body
      case None => params("id") + " not found"
    }
  }


  post("/remove/:id") {
    myposts.find(post => post.Id == params("id")) match{
      case Some(p: Post) => mongoColl.findAndRemove(MongoDBObject("id" -> p.Id.toInt))
      case None => params("id") + " not found"
    }
    
  }
 


}




class Post(title:String, body: String, id:String){

  val Id = id
  val Title = title
  val Body = body

  val Location = "/post/"+ Id
}



