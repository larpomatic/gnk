package gnk

import org.gnk.admin.right
import org.gnk.rights.RightsService
import org.gnk.user.User

class RightsUserTagLib {
    RightsService rightsService;

    def hasRights = { attrs, body ->
        User user = (User) session.getAttribute("user")
        User currentuser = User.findById(user.id)
        int right = Integer.valueOf(attrs.lvlright)
        if (rightsService.hasRight(currentuser.gright, right))
            out << body()
    }
    def hasNotRights = { attrs, body ->
        User user = (User) session.getAttribute("user")
        User currentuser = User.findById(user.id)
        int right = Integer.valueOf(attrs.lvlright)
        if (!rightsService.hasRight(currentuser.gright, right))
            out << body()
    }

    def plotOwner = { attrs, body ->
        User user = (User) session.getAttribute("user")
        User currentuser = User.findById(user.id)
        int idOwner = Integer.valueOf(attrs.idOwner)
        int right1 = Integer.valueOf(attrs.lvlright)
        int right2 = Integer.valueOf(attrs.lvlrightAdmin)
        if ((currentuser.id == idOwner && rightsService.hasRight(currentuser.gright, right1)) || rightsService.hasRight(currentuser.gright, right2)){
            out << body()
        }
    }
}
