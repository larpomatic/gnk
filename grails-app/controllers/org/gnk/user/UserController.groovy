package org.gnk.user

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.gnk.admin.right
import org.gnk.rights.RightsService
import org.springframework.security.access.annotation.Secured

import java.text.DateFormat
import java.text.SimpleDateFormat;
import java.util.Date

import javax.servlet.http.Cookie
import java.util.logging.SimpleFormatter

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class UserController {
    RightsService rightsService
    UserService userService

    def
    index() {
        render 'Secure access only'
    }

    def profil() {

        User user = session.getAttribute("user")

        if (!user) {
            params.setProperty("redirectaction", "profil")
            params.setProperty("redirectcontroller", "user")
            redirect(controller: "adminUser", action: "checkcookie", params: [actionredirect: "profil", controllerredirect: "user"])
            return false
        } else {
            int disabled = 0
            User currentuser = User.findById(user.id)
            int rightuser = currentuser.gright
            if (rightsService.hasRight(user.gright, right.RIGHTSHOW.value()) && !rightsService.hasRight(user.gright, right.RIGHTMODIF.value())) {
                disabled = 1;
            }
            List<Boolean> lb = userService.instperm(rightuser)
            Date date = currentuser.lastConnexion
            DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yyyy")
            String dateDesign = dateFormat.format(date).toString()
            [currentuser: currentuser, lb: lb, date: dateDesign, disabled: disabled]
        }
    }

    def modifperm() {
        User user = session.getAttribute("user")

        if (!user) {
            params.setProperty("redirectaction", "profil")
            params.setProperty("redirectcontroller", "user")
            redirect(controller: "adminUser", action: "checkcookie", params: [actionredirect: "profil", controllerredirect: "user"])
            return false
        } else {
            List<Boolean> lb = []
            user = session.getAttribute('user')
            User currentuser = User.findById(user.id)

            for (int i = 0; i < 24; i++) {
                StringBuilder s = new StringBuilder()
                s.append("checkbox")
                s.append(i)
                String checkb = s.toString()
                GrailsParameterMap param = getParams()
                if (param.get(checkb) != null) {
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
    }

    def modifyProfil() {
        User user = session.getAttribute("user")
        User currentuser = User.findById(user.id)
        String newpassword = params.passwordChanged
        String confirmpassword = params.passwordChangedConfirm
        if (confirmpassword)
            if (newpassword && newpassword.size() > 3 && confirmpassword && confirmpassword.equals(newpassword) && !newpassword.equals(user.password)) {
                currentuser.password = newpassword
                flash.success = "Votre mot de passe a bien été modifié"
            } else {
                if (newpassword.size() <= 3) {
                    flash.error = "Mot de passe trop court (mininum 4 caractères)"
                } else {

                    flash.error = "Votre Mot de passe et sa confirmation sont différents"
                }
            }
        else {
            if (params.firstnamemodif && params.firstnamemodif != currentuser.firstname
                    || params.lastnamemodif && params.lastnamemodif != currentuser.lastname
                    || params.usernamemodif && params.usernamemodif != currentuser.username) {
                if (params.firstnamemodif)
                    currentuser.firstname = params.firstnamemodif
                if (params.lastnamemodif)
                    currentuser.lastname = params.lastnamemodif
                if (params.usernamemodif)
                    currentuser.username = params.usernamemodif
                flash.success = "Vos modifications ont  a bien été prises en compte"
            } else {
                flash.error = "erreur champ vide ou invalid";
            }
        }
        currentuser.save(failOnError: true)
        redirect(action: "profil")
    }
}
