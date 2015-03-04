package org.gnk.adminUser

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.gnk.admin.right
import org.gnk.cookie.CookieService
import org.gnk.gn.GnHasUser
import org.gnk.rights.RightsService
import org.gnk.selectintrigue.Plot
import org.gnk.user.SecRole
import org.gnk.user.User
import org.gnk.user.UserSecRole
import org.gnk.user.UserService
import groovy.sql.Sql
import sun.security.jca.GetInstance

import javax.servlet.http.Cookie

class AdminUserController {
    UserService userService
    CookieService cookieService
    RightsService  rightsService

    def checkcookie() {

        Cookie[] cookies = request.getCookies()
        if (cookies) {
            Cookie cookie = cookies.find { it.name == "prcgn" }
            if (cookie) {
                String username = cookieService.cookieusern(cookie.value)
                User currentuser = User.findByUsername(username)
                if (cookieService.isAuth(currentuser.password, cookieService.cookiepassword(cookie.value)))
                    session.setAttribute("user", currentuser)
                else
                    redirect(controller: "login", action: "denied")
            } else {
                redirect(controller: "login", action: "denied")
            }
        } else {
            redirect(controller: "login", action: "denied")
        }
        String actionredir = params.get("actionredirect")
        String controllerredir = params.get("controllerredirect")
        redirect(controller: controllerredir, action: actionredir)
    }

    def list() {

        User user = session.getAttribute("user")

        if (!user) {
            params.setProperty("redirectaction", "profil")
            params.setProperty("redirectcontroller", "user")
            redirect(controller: "adminUser", action: "checkcookie", params: [actionredirect: "list", controllerredirect: "adminUser"])
            return false
        } else {
            GrailsParameterMap param = params
            def name = param.usersearch
            if ("".equals(name) || null == name) {
                List<User> users = User.list()
                [users: users]
            } else {
                def c = User.createCriteria()
                List<User> users = c.list {
                    or {
                        ilike("username", "%" + name + "%")
                        ilike("firstname", "%" + name + "%")
                        ilike("lastname", "%" + name + "%")
                    }
                }
                [users: users]
            }
        }
    }

    def edit(long id) {
        User user
        if (id != null && id != 0) {
            user = User.findById(id)
            session.setAttribute("id_user_check", id)
        } else {
            if (!session.getAttribute("id_user_check")) {
                redirect(action: "list")
                return false
            } else {
                int iduser = session.getAttribute("id_user_check")
                user = User.findById(iduser)
            }
        }

        int rightuser = user.gright
        String newpassword = params.passwordChanged
        if (newpassword && newpassword.size() > 5)
            user.password = newpassword
        List<Boolean> lb = userService.instperm(rightuser)
        int disabled = 0
        User currentuser = (User) session.getAttribute("user")
        if (rightsService.hasRight(currentuser.gright, right.RIGHTSHOW.value()) && !rightsService.hasRight(currentuser.gright, right.RIGHTMODIF.value())) {
            disabled = 1;
        }
        [user: user, lb: lb, disabled : disabled]
    }

    def changeperm(long id) {

        List<Boolean> lb = []
        User user = User.findById(id)

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
        if (user.gright != userService.changeright(lb)) {
            flash.success = "Le niveau de droit à bien été mis à jour"
        } else {
            flash.error = "Le niveau de droit est identique";
        }
        user.gright = userService.changeright(lb)
        user.save(failOnError: true)
        redirect(controller: "adminUser", action: "edit")
    }

    def lock(long id) {
        User user = User.findById(id)
        if (user.accountLocked == true) {
            user.accountLocked = false
        } else {
            user.accountLocked = true
        }

        redirect controller: "adminUser", action: "list"
    }

    def statistic(long id) {
        User user = User.findById(id)
        if (!id) {
            redirect(action: "list")
            return false
        }
        List<Plot> plots = Plot.list()
        int countPublicPlot = 0;
        int countPrivatePlot = 0;
        int countDraftPlot = 0;
        for (int i = 0; i < plots.size(); i++) {
            if (plots[i].user.id == user.id) {
                if (plots[i].isPublic) {
                    countPublicPlot++
                } else {
                    if (plots[i].isDraft) {
                        countDraftPlot++
                    } else {
                        countPrivatePlot++
                    }
                }
            }
        }
        int count = GnHasUser.findAllByUser(user).size();
        [user: user, countPublicPlot: countPublicPlot, countPrivatePlot: countPrivatePlot, countDraftPlot: countDraftPlot, count : count]
    }

    def modifyUser(long id) {
        User user = User.findById(id)
        String newpassword = params.passwordChanged
        String confirmpassword = params.passwordChangedConfirm
        if (confirmpassword && newpassword)
            if (newpassword && newpassword.size() > 3 && confirmpassword && confirmpassword.equals(newpassword)) {
                user.password = newpassword
                flash.success = "Votre mot de passe a bien été modifié"
            } else {
                if (newpassword.size() <= 3) {
                    flash.error = "Mot de passe trop court (mininum 3 caractères)"
                }
                else
                {
                    flash.error = "Votre Mot de passe et sa confirmation sont différents"
                }
            }
        else {
            if (params.firstnamemodif && params.firstnamemodif != user.firstname
                    || params.lastnamemodif && params.lastnamemodif != user.lastname
                    || params.usernamemodif && params.usernamemodif != user.username) {
                if (params.firstnamemodif)
                    user.firstname = params.firstnamemodif
                if (params.lastnamemodif)
                    user.lastname = params.lastnamemodif
                if (params.usernamemodif)
                    user.username = params.usernamemodif
                flash.success = "Vos modifications ont bien été prises en compte"
            } else {
                flash.error = "erreur le champ est vide / invalide / identique";
            }
        }
        user.save(failOnError: true)
        redirect(action: "edit", params: "user : ${user.id}")
    }

    def deleteUser(int id) {
        User user = User.findById(id)
        User newUser = User.findByUsername("admin@gnk.com")
        ArrayList<Plot> plots = Plot.findAllByUser(user)
        for (Plot p : plots) {
            p.user = newUser
            p.save(flush: true)
        }

        UserSecRole userSecRoles = UserSecRole.findAllByUser(user)
        for (UserSecRole userSecRole : userSecRoles) {
            userSecRole.delete()
        }
        user.delete();

        redirect(action: "list")
    }

    def createUser() {
        String username = params.username
        String firstname = params.firstname
        String lastname = params.lastname
        String password = params.password
        String passwordRepeat = params.passwordRepeat

        if (username && firstname && lastname && password && passwordRepeat && !User.findByUsername(username)) {

            User nUser = new User()
            nUser.username = username
            nUser.firstname = firstname
            nUser.lastname = lastname
            nUser.password = password
            nUser.enabled = true
            nUser.gright = 0
            nUser.countConnexion = 0
            nUser.lastConnexion = new Date()
            nUser.save(failOnError: true)
            SecRole userRole = SecRole.findByAuthority("ROLE_USER") ?: new SecRole(authority: "ROLE_USER").save(failOnError: true)
            if (!nUser.getAuthorities().contains(userRole)) {
                UserSecRole.create(nUser, userRole, true)
         }
            redirect(action: "list")
        }
    }
}
