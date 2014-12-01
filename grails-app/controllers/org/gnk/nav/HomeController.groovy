package org.gnk.nav

import grails.plugins.springsecurity.SpringSecurityService
import org.gnk.cookie.CookieService
import org.gnk.user.User
import org.springframework.security.access.annotation.Secured

import javax.servlet.http.Cookie

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class HomeController {
    CookieService cookieService;
    SpringSecurityService springSecurityService
    def index() {
        User user = session.getAttribute("user")
        if (user == null){
            user = (User) springSecurityService.getCurrentUser()
            user.countConnexion = user.countConnexion + 1
            user.lastConnexion = new Date()
            user.save(failOnError: true)
            session.setAttribute("user", user)
            User currentuser = User.findById(user.id)
            if (currentuser.accountLocked){
                redirect controller: "login", action: "denied"
            }

            Cookie[] cookies = request.getCookies()
            if (cookies){
                Cookie cookie = cookies.find {it.name == "gnk_cookie"}
                if (cookie){
                    String password = user.password
                    String cookieid = user.username + "###" + password
                    Cookie c = new Cookie("prcgn", cookieid)
                    c.setMaxAge(31531600)
                    response.addCookie(c)
                }
            }
        } else {
            if (user.accountLocked){
                redirect controller: "login", action: "denied"
            }
        }
    }
}
