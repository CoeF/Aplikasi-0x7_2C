package ubaidillah.qowwim.aplikasix07_2c
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context): SQLiteOpenHelper(context,DB_Name,null,DB_Ver) {
    companion object{
        val DB_Name = "prak7"
        val DB_Ver = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val tData = "create table listkalimat(idkal integer primary key autoincrement,nama text not null)"
        val insSample = "insert into listkalimat(nama) values('Manajemen Informatika')"
        db?.execSQL(tData)
        db?.execSQL(insSample)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}