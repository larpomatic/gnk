package org.gnk.user

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap


class UserController {
    UserService userService

    def index() {
        render 'Secure access only'
    }

    def profil(){

        User user = session.getAttribute("user")
        User currentuser = User.findById(user.id)
        int rightuser = currentuser.gright
        List<Boolean> lb = userService.instperm(rightuser)

        [currentuser : currentuser , lb : lb]
    }

    def modifperm(){
        List<Boolean> lb = []
        User user = session.getAttribute('user')
        User currentuser = User.findById(user.id)

        for (int i = 0; i < 24; i++)
        {
            StringBuilder s = new StringBuilder()
            s.append("checkbox")
            s.append(i)
            String checkb = s.toString()
            GrailsParameterMap param = getParams()
            if (param.get(checkb) != null)
            {
                lb.add(true)
            } else {
                lb.add(false)
            }
        }
        currentuser.gright = userService.changeright(lb)
        user.gright = userService.changeright(lb)
        currentuser.save(failOnError: true)
        session.setAttribute("user", user)
        redirect(controller: "user", action: "profil")
    }

    def modifyProfil(){
        User user = session.getAttribute("user")
        User currentuser = User.findById(user.id)

        if (params.lastnamemodif != currentuser.lastname){
            currentuser.lastname = params.lastnamemodif
            currentuser.save()
        }
        redirect( action: "profil")
    }
}
