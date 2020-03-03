package com.android.testsq

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.testsq.SQLiteHelper.Companion.Table_Column_2_Email




class AllUsersActivity : AppCompatActivity() {

    lateinit var sqLiteDatabaseObj: SQLiteDatabase
    lateinit var sqLiteHelper: SQLiteHelper
    lateinit var cursor: Cursor
    lateinit var email: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_users_activity)


        val allUsers = getAllUser()


        var additionText1 = findViewById<View>(R.id.list) as TextView
        additionText1.text = allUsers.toString()


    }

    fun getAllUser(): ArrayList<String> {

        val columns = arrayOf(Table_Column_2_Email)

// query the user table

        sqLiteHelper = SQLiteHelper(this)
        sqLiteDatabaseObj = sqLiteHelper.getReadableDatabase()

        // Adding search email query to cursor.
        val sortOrder = "$Table_Column_2_Email ASC"
        cursor = sqLiteDatabaseObj.query(
            SQLiteHelper.TABLE_NAME,
            columns, //columns to return
            null, //columns for the WHERE clause
            null, //The values for the WHERE clause
            null, //group the rows
            null, //filter by row groups
            sortOrder
        )


        val userList = arrayListOf("")
        if (cursor.moveToFirst()) {
            do {

                email = cursor.getString(cursor.getColumnIndex(Table_Column_2_Email))

                userList.add(email)
            } while (cursor.moveToNext())
        }
        cursor.close()
        sqLiteDatabaseObj.close()
        return userList
    }
}