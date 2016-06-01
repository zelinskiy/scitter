package com.example.app

import org.scalatra._

import scala.util.{Failure, Success, Try}


class PostServlet extends HelloWorldAppStack {


  val myposts:List[Post] = List(new Post("post1", 1), new Post("post2", 2))
  
  
  
  get("/") {
    contentType="text/html"
    jade("/WEB-INF/templates/views/posts.jade",
      "layout" -> "",
      "posts" -> myposts
    )
  }
  
  get("/:id") {
    Try {
      params("id").toInt
    } match {
      case Success(id) => myposts.find(_.Id == id) match {
        case Some(post:Post) => post.Location
        case None => "#"
      }
      case Failure(ex) => pass()
    }
  }

}




class Post(body:String, id: Int){
  val Id = id
  val Body = body
  val Location = "/post/"+ id.toString
}



