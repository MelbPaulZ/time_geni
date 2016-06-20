package unimelb.org.itime.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by yinchuandong on 20/06/2016.
 */

@Table(name = "user")
public class User {

    @Id(column = "id")
    private String id;
    private String username;
    private String password;
}
