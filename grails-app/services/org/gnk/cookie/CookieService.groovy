package org.gnk.cookie

class CookieService {

    String cookieusern(String encoded){
        String username = ""
        for (int i = 0; i < encoded.length() && encoded[i] != '#'; i++){
            username = username + encoded[i]
        }
        return  username
    }
    String cookiepassword(String encoded){
        String password = ""
        int tmp = 0;
        for (int i = 0; i < encoded.length(); i++){
            if (tmp == 3){
                password = password + encoded[i]
            }
            if (encoded[i] == "#"){
                tmp++
            }
        }
        return password
    }

    Boolean isAuth(String password, String cpass){
        if (password.equals(cpass))
            return true
        return false
    }
}
