package org.gnk.user


class UserService {

    def serviceMethod() {


        }
     List<Boolean> instperm(int perm){
         List<Boolean> perms = []
        for (int i = 0; i <= 23; i++){
            boolean b = (perm & 1)
            perms.add(i, b)
            perm = perm / 2
        }
        return perms
    }
    int changeright(List<Boolean> perms){
        int resultat = 0;
        int k = 0;
        for (int i = 0 ;  i < perms.size() ; i++){
          int tmp = 0
            if (k == 0 && perms.get(i) == true){
                resultat = 1;
            }
            else{
                if  (perms.get(i) == true){
                    tmp = 1
                    for (int j = 0; j < k; j++){
                        tmp = tmp * 2
                    }
                }
            }
            resultat = resultat + tmp
            k++
        }
        return resultat
    }
}