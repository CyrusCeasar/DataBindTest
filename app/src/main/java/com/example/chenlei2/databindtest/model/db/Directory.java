package com.example.chenlei2.databindtest.model.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
@DatabaseTable
public class Directory extends MFile implements Serializable{

    @ForeignCollectionField(eager = true)
    private ForeignCollection<MFile> dirFileList;

}
