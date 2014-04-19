package org.gnk.nav

import org.gnk.user.User

class HomeController {

    def index() {
        User  user = session.getAttribute("user")
        if(!user){
            redirect(controller: "login" , action: "index")
            return false
        }
   }
}
