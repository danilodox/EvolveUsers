package com.danilo.evolveusers.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.danilo.evolveusers.data.model.User
import com.danilo.evolveusers.util.convertDate
import com.danilo.evolveusers.util.convertDate.parseStringToTimestamp
import java.sql.Timestamp

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "users.db"
        private const val TBL_USERS = "tbl_users"
        private const val NOME = "nome"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
        private const val FOTO = "foto"
        private const val ENDERECO = "endereco"
        private const val EMAIL = "email"
        private const val DATANASCIMENTO = "dataNascimento"
        private const val SEXO = "sexo"
        private const val CPFCNPJ = "cpfCnpj"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblUsers = ("CREATE TABLE " + TBL_USERS + "("
                + "$NOME TEXT,"
                + "$USERNAME TEXT,"
                + "$PASSWORD TEXT,"
                + "$FOTO TEXT,"
                + "$ENDERECO TEXT,"
                + "$EMAIL TEXT,"
                + "$DATANASCIMENTO TEXT,"
                + "$SEXO TEXT,"
                + "$CPFCNPJ TEXT PRIMARY KEY"
                + ")")

        db?.execSQL(createTblUsers)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //db?.execSQL("drop table if exists Userdata")
        db?.execSQL("drop table if exists $TBL_USERS")
        onCreate(db)
    }

    //Colocar o model como parametro
    fun insertData(userModel : User): Result {
        val db = this.writableDatabase
        val contentValues = ContentValues()




        return try {
            contentValues.put(NOME, userModel.nome)
            contentValues.put(USERNAME, userModel.username)
            contentValues.put(PASSWORD, userModel.password)
            contentValues.put(FOTO, userModel.foto)
            contentValues.put(ENDERECO, userModel.endereco)
            contentValues.put(EMAIL, userModel.email)
            contentValues.put(DATANASCIMENTO,  convertDate.formatToSQLiteDate(userModel.dataNascimento) )
            contentValues.put(SEXO, userModel.sexo)
            contentValues.put(CPFCNPJ, userModel.cpfCnpj)


            val result = db.insert(TBL_USERS, null, contentValues)
            if (result == (-1).toLong()) {
                Result.Failure("Insert failed")
            } else {
                Result.Success("Insert successful")
            }
        } catch (e: Exception) {
            // Exceção durante a inserção
            Result.Failure("Error inserting data: ${e.message}")
        } finally {
            db.close()
        }
    }


    fun getAllUsers(): ArrayList<User> {
        val db = this.readableDatabase
        val userList: ArrayList<User> = ArrayList()
        val selectQuery = "select * from $TBL_USERS"

        val cursor : Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var nome: String
        var username: String
        var password: String
        var foto: String
        var endereco: String
        var email: String
        var dataNascimento: Timestamp
        var sexo: String
        var cpfCnpj: String

        if (cursor.moveToFirst()) {
            do {

                nome = cursor.getString(cursor.getColumnIndex("nome"))
                username = cursor.getString(cursor.getColumnIndex("username"))
                password = cursor.getString(cursor.getColumnIndex("password"))
                foto = cursor.getString(cursor.getColumnIndex("foto"))
                endereco = cursor.getString(cursor.getColumnIndex("endereco"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                //dataNascimento = Timestamp(cursor.getString(cursor.getColumnIndex("dataNascimento")).toLong() * 1000)
                dataNascimento = parseStringToTimestamp(cursor.getString(cursor.getColumnIndex("dataNascimento")))!!
                sexo = cursor.getString(cursor.getColumnIndex("sexo"))
                cpfCnpj = cursor.getString(cursor.getColumnIndex("cpfCnpj"))
                val user = User(nome, username, password, foto, endereco, email, dataNascimento, sexo, cpfCnpj)

                userList.add(user)
            } while (cursor.moveToNext())

        }
        cursor.close()
        return userList

    }

    fun updateUser(userModel: User): Result {
        val db = this.writableDatabase

        return try {


            val contentValues = ContentValues()
                // Verificar se cada campo no userModel foi modificado e, em caso afirmativo, adicioná-lo ao ContentValues

            if (userModel.nome.isNotEmpty()) contentValues.put(NOME, userModel.nome)
            if (userModel.username.isNotEmpty()) contentValues.put(USERNAME, userModel.username)
            if (userModel.password.isNotEmpty()) contentValues.put(PASSWORD, userModel.password)
            if (userModel.foto.isNotEmpty()) contentValues.put(FOTO, userModel.foto)
            if (userModel.endereco.isNotEmpty()) contentValues.put(ENDERECO, userModel.endereco)
            if (userModel.email.isNotEmpty()) contentValues.put(EMAIL, userModel.email)
            if (userModel.dataNascimento != null) contentValues.put(DATANASCIMENTO, convertDate.formatToSQLiteDate(userModel.dataNascimento))
            if (userModel.sexo.isNotEmpty()) contentValues.put(SEXO, userModel.sexo)
            //Não incluit o CPFCNPJ pq é a chave primária


              /*  {
                put(NOME, userModel.nome)
                put(USERNAME, userModel.username)
                put(PASSWORD, userModel.password)
                put(FOTO, userModel.foto)
                put(ENDERECO, userModel.endereco)
                put(EMAIL, userModel.email)
                put(DATANASCIMENTO,  convertDate.formatToSQLiteDate(userModel.dataNascimento) )
                put(SEXO, userModel.sexo)


               // put(CPFCNPJ, userModel.cpfCnpj)

            }*/

            val result = db.update(TBL_USERS, contentValues, "cpfCnpj=?", arrayOf(userModel.cpfCnpj))
            if (result == 1) {
                Result.Success("Update successful")
            } else {
                Result.Failure("Update failed")
            }
        } catch (e: Exception) {
            // Exceção durante a inserção
            Result.Failure("Error updating data: ${e.message}")
        } finally {
            db.close()
        }
    }

    fun getUser(cpfCnpj: String): User? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TBL_USERS WHERE $CPFCNPJ = ?"
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, arrayOf(cpfCnpj))
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return null
        }

        var user: User? = null

        if (cursor.moveToFirst()) {
            val nome = cursor.getString(cursor.getColumnIndex(NOME))
            val username = cursor.getString(cursor.getColumnIndex(USERNAME))
            val password = cursor.getString(cursor.getColumnIndex(PASSWORD))
            val foto = cursor.getString(cursor.getColumnIndex(FOTO))
            val endereco = cursor.getString(cursor.getColumnIndex(ENDERECO))
            val email = cursor.getString(cursor.getColumnIndex(EMAIL))
            val dataNascimento = parseStringToTimestamp(cursor.getString(cursor.getColumnIndex(DATANASCIMENTO)))!!
            val sexo = cursor.getString(cursor.getColumnIndex(SEXO))
            val cpfCnpj = cursor.getString(cursor.getColumnIndex(CPFCNPJ))

            user = User(nome, username, password, foto, endereco, email, dataNascimento, sexo, cpfCnpj)
        }

        cursor.close()
        return user
    }


    fun checkUserPass(userName: String, password: String): Boolean {
        val db = this.writableDatabase
        //val query = "select * from Userdata where username= '$userName' and password= '$password'"
        val query = "SELECT * FROM $TBL_USERS WHERE $CPFCNPJ = ?"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    private fun isUserExists(cpfCnpj: String): Boolean {
        val db = this.readableDatabase
        //val query = "SELECT * FROM Userdata WHERE username= '$username'"
        val query = "SELECT * FROM $TBL_USERS WHERE $CPFCNPJ = '$cpfCnpj'"
        val cursor = db.rawQuery(query, null)
        val userExists = cursor.count > 0
        cursor.close()
        db.close()
        return userExists
    }


}