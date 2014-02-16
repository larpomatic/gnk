package org.gnk.administration

/**
 * Created with IntelliJ IDEA.
 * User: aurel_000
 * Date: 19/01/14
 * Time: 21:09
 * To change this template use File | Settings | File Templates.
 */
class Query {
    String title;
    String description;
    String query;
    String result;

    public Query(String title, String description, String query)
    {
        this.title = title;
        this.description = description;
        this.query = query;
    }

    public Query(String title, String description, String query, String result)
    {
        this.title = title;
        this.description = description;
        this.query = query;
        this.result = result;
    }
}
