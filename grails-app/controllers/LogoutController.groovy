import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.gnk.user.User

class LogoutController {

	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {
		// TODO put any pre-logout code here
        User user = session.getAttribute("user")
        User currentuser = User.findById(user.id)
        currentuser.lastConnection = new Date().format("dd-MM-yyyy  HH-mm-ss")
        currentuser.save()
        session.invalidate()
        redirect(controller: "login", action: "index")
        return false
		//redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
	}
}
