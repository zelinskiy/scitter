package com.example.app

import org.scalatra._
import scala.util.{Failure, Success, Try}
import com.mongodb.casbah.Imports._



class PostServlet extends HelloWorldAppStack {

  val rand = scala.util.Random


  def myposts = Posts()
  val mongoColl = Posts.postsColl


  post("/insert") {
    val builder = MongoDBObject.newBuilder
    builder += "userid" -> params("userid")
    builder += "id" -> rand.nextInt(1000000)
    builder += "title" -> params("title")
    builder += "body" -> params("body")    
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







