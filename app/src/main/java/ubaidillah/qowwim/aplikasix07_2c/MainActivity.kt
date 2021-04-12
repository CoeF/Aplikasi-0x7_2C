package ubaidillah.qowwim.aplikasix07_2c

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var db: SQLiteDatabase
    lateinit var adapter: ListAdapter
    lateinit var v : View
    lateinit var builder: AlertDialog.Builder
    var idList : String = ""
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnScanQr->{
                intentIntegrator.setBeepEnabled(true).initiateScan()
            }
            R.id.btnGenereateQr->{
                val barCodeEncoder = BarcodeEncoder()
                val bitmap = barCodeEncoder.encodeBitmap(edQrCode.text.toString(),
                BarcodeFormat.QR_CODE,400,400)
//                QrCodeView.setImageBitmap(bitmap)
                if(bitmap!=null){
                    if( edQrCode!= null){
                        QrCodeView.setImageBitmap(bitmap)
                    }else{
                        Toast.makeText(this,"Dibatalkan",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.btnHapus->{
                builder.setTitle("Konfirmasi").setMessage("Yakin akan menghapus data ini?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Ya",btnDeleteList)
                        .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnEdit->{
                builder.setTitle("Konfirmasi").setMessage("Apakah data nya sudah benar ?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Ya",btnUpdateDialog)
                        .setNegativeButton("Tidak",null)
                builder.show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult  = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(intentResult!=null){
            if(intentResult.contents != null){

                edQrCode.setText(intentResult.contents)
                var kata = edQrCode.text
                var cv : ContentValues = ContentValues()
                cv.put("nama",kata.toString())
                db.insert("listkalimat",null,cv)
            }else{
                Toast.makeText(this,"Dibatalkan",Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    val itemClick = AdapterView.OnItemClickListener{ parent, view, position, id ->
        val c: Cursor = parent.adapter.getItem(position) as Cursor
        idList = c.getString(c.getColumnIndex("_id"))
        edQrCode.setText(c.getString(c.getColumnIndex("nama")))
    }
//    fun insertDataList(Kalimat:String){
//        var cv : ContentValues = ContentValues()
//        cv.put("nama",Kalimat)
//        db.insert("listkalimat",null,cv)
//        showDataList()
//        Toast.makeText(this,"Data Berhasil Ditambahkan",Toast.LENGTH_SHORT).show()
//    }
    lateinit var intentIntegrator: IntentIntegrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intentIntegrator = IntentIntegrator(this)
        btnGenereateQr.setOnClickListener (this)
        btnScanQr.setOnClickListener (this)

        btnHapus.setOnClickListener(this)
        btnEdit.setOnClickListener(this)
        builder = AlertDialog.Builder(this)
        lsViewData.setOnItemClickListener(itemClick)
        db = DBOpenHelper(this).writableDatabase

    }
    fun showDataList() {
        val cursor : Cursor =db.query("listkalimat", arrayOf("nama","idkal as _id"),
                null,null,null,null,"_id asc")
        adapter = SimpleCursorAdapter(this,R.layout.data_list,cursor,
                arrayOf("_id","nama"), intArrayOf(R.id.txId, R.id.txKalimat), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        lsViewData.adapter = adapter

    }
    fun deleteDataList(idkalimat: String){
        db.delete("listkalimat","idkal = $idkalimat", null)
        showDataList()
    }
    val btnDeleteList = DialogInterface.OnClickListener{ dialog, which ->
        deleteDataList(idList)
        edQrCode.setText("")
    }
    fun updateDataList(idkalimat: String ,idKal: String){
        var cv : ContentValues = ContentValues()
        cv.put("nama",idkalimat)
        db.update("listkalimat",cv,"idkal = $idKal",null)
        showDataList()
    }
    val btnUpdateDialog = DialogInterface.OnClickListener{ dialog, which ->
        updateDataList(edQrCode.text.toString(),idList)
        edQrCode.setText("")
    }
    override fun onStart() {
        super.onStart()
        showDataList()
//        insertDataList(edQrCode.toString())
    }
}