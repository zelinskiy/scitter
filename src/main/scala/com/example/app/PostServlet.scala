package com.example.app

import org.scalatra._

class PostServlet extends HelloWorldAppStack {

  get("/") {
    <html>
      <body>
        <h1>Еееееее Скалка</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

}
