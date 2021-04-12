package ubaidillah.qowwim.aplikasix07_2c

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnScanQr->{
                intentIntegrator.setBeepEnabled(true).initiateScan()
            }
            R.id.btnGenereateQr->{
                val barCodeEncoder = BarcodeEncoder()
                val bitmap = barCodeEncoder.encodeBitmap(edQrCode.text.toString(),
                BarcodeFormat.QR_CODE,400,400)
                QrCodeView.setImageBitmap(bitmap)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult : IntentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(intentResult!=null){
            if(intentResult.contents != null){
                edQrCode.setText(intentResult.contents)
            }else{
                Toast.makeText(this,"Dibatalkan",Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    lateinit var intentIntegrator: IntentIntegrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intentIntegrator = IntentIntegrator(this)
        btnGenereateQr.setOnClickListener (this)
        btnScanQr.setOnClickListener (this)
    }
}