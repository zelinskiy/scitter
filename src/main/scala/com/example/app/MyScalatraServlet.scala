package com.example.app

import org.scalatra._






class MyScalatraServlet extends HelloWorldAppStack {
  
  

  get("/") {
    <html>
      <body>
        <h1>eeeeeeee, world!</h1>
		<a href="post">posts</a>
		<a href="user">users</a>
      </body>
    </html>
  }
  
  

}
