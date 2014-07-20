package org.gnk.admin

class RequestService {

    Boolean update(String sqlr, String catName, String subCatName, String request) {

        SqlRequestCategory sqlcat = SqlRequestCategory.findByName(catName);
        if (!sqlcat){
            sqlcat = new SqlRequestCategory()
            sqlcat.name = catName;
            sqlcat.sqlRequestSubcategory = new HashSet<SqlRequestSubcategory>()
            sqlcat.save()
        }
        SqlRequestSubcategory sqlsubcat = SqlRequestSubcategory.findByNameAndSqlRequestCategory(subCatName, sqlcat)
        if (!sqlsubcat){
            sqlsubcat = new SqlRequestSubcategory()
            sqlsubcat.name = subCatName
            sqlsubcat.sqlRequestCategory = sqlcat
            sqlcat.sqlRequestSubcategory.add(sqlsubcat)
            sqlsubcat.sqlRequest = new HashSet<SqlRequestSubcategory>();
            sqlsubcat.save()
        }
        SqlRequest sqlrequest = SqlRequest.findByNameAndSqlRequestSubcategory(sqlr, sqlsubcat)
        if (!sqlrequest){
            sqlrequest = new SqlRequest();
            sqlrequest.name = sqlr;
            sqlrequest.sqlRequestSubcategory = sqlsubcat
            sqlsubcat.sqlRequest.add(sqlrequest);
            sqlrequest.sqlrequest = request
            sqlrequest.save()
        }
        return true
    }

    Boolean delete(String catName, String subCatName, String sqlr){
        SqlRequestCategory sqlcat = SqlRequestCategory.findByName(catName);
        SqlRequestSubcategory sqlsubcat = SqlRequestSubcategory.findByNameAndSqlRequestCategory(subCatName, sqlcat)
        SqlRequest sqlrequest = SqlRequest.findByNameAndSqlRequestSubcategory(sqlr, sqlsubcat)
        if (!sqlcat && !sqlsubcat && !sqlrequest){
            return false;
        } else {
            sqlrequest.delete();
            sqlsubcat.sqlRequest.remove(sqlrequest)
            if (sqlsubcat.sqlRequest.size() == 0){
                sqlsubcat.delete();
                sqlcat.sqlRequestSubcategory.remove(sqlsubcat)
            }
            if (sqlcat.sqlRequestSubcategory.size() == 0){
                sqlcat.delete();
            }
            return true;
        }
    }
}
