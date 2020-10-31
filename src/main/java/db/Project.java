package db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "project")
public class Project {
    @DatabaseField(id = true)
    public int id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String url;

    public Project(){

    }
}

