package com.example.app

import org.scalatra._
import scala.util.{Failure, Success, Try}
import com.mongodb.casbah.Imports._



class PostServlet extends HelloWorldAppStack {

  val rand = scala.util.Random


  def myposts = Posts()
  val mongoColl = Posts.postsColl



  post("/insert") {
    val newPost = new Post(
      params("userid"),
      rand.nextInt(1000000).toString,
      "tweet",
      params("title"),
      params("body")
      )

    if(newPost.UserId == Users.CurrentUser.Id){
      mongoColl += newPost.AsMongoObject
    } 
  }


  post("/retweet") {

    val oldPost = Posts.FindPostById(params("postid"))

    val newPost = new Post(
      Users.CurrentUser.Id,
      rand.nextInt(1000000).toString,
      "retweet",
      oldPost.Title,
      oldPost.Body
      )

    if(newPost.UserId == Users.CurrentUser.Id){
      mongoColl += newPost.AsMongoObject
    } 
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
      case Some(p: Post) => mongoColl.findAndRemove(MongoDBObject("id" -> p.Id))
      case None => params("id") + " not found"
    }
    
  }
 


}







