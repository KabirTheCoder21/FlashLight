package com.example.l2

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.SmsManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.example.l2.R.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var imgView : ImageView
    lateinit var camera : Button
    lateinit var sharebtn : Button
    lateinit var gallery : Button
    lateinit var msgBtn : Button
    lateinit var email : Button
    private var REQUEST_IMAGE_CAPTURE = 100
   private var pick = 43

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        val dial: Button = findViewById(R.id.dial)
        dial.setOnClickListener {
            /*val it = Intent(Intent.ACTION_DIAL)
            it.data = Uri.parse("tel:+916387638554")
            startActivity(it)*/
            val intent: Intent = Uri.parse("tel:6387638554").let { number ->
                Intent(Intent.ACTION_DIAL, number)
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No app is Found : $e", Toast.LENGTH_SHORT).show()
            }

        }

        camera = findViewById<Button>(R.id.camera)
        imgView = findViewById(R.id.imageView)
        camera.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // Display error state to the user.
                Toast.makeText(this, "No app is Found : $e", Toast.LENGTH_SHORT).show()
            }
        }
        sharebtn = findViewById(R.id.shareBtn)
      //  val bitmapDrawable = imgView.drawable as? BitmapDrawable
      //  val bitmap: Bitmap? = bitmapDrawable?.bitmap
       // val path = MediaStore.Images.Media.insertImage(
        //    contentResolver,
       //     bitmap,
       //     "Image i want to share",
       //     null
            //  )
        //val uri = Uri.parse(path)
        sharebtn.setOnClickListener {
           /* val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.data = uri
            shareIntent.type = "image/*"
            startActivity(Intent.createChooser(shareIntent,"Share Image Via : ")q */

            */

            val bitmapDrawable = imgView.drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            shareImageandText(bitmap)

        }
        gallery = findViewById(id.Gallery)
        gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)


            startActivityForResult(intent,pick)
        }
        msgBtn = findViewById(id.message)
        msgBtn.setOnClickListener {
           /* val uri = Uri.parse("smsto:12346556")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", "Here goes your message...")
            startActivity(intent)*/
            val smsManager = SmsManager.getDefault() as SmsManager
            smsManager.sendTextMessage("6387638554", null, "sms message", null, null)
        }
        email = findViewById(id.email)
        email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.data = Uri.parse("mailto:")
            "text/plain".also { intent.type = it }
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("kabirseth2103@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT,"Kabir seth Resume")
            intent.putExtra(Intent.EXTRA_TEXT,"This is my resume")
            try {
                //start email intent
                startActivity(Intent.createChooser(intent, "Choose Email Client..."))
            }
            catch (e: Exception){
                //if any thing goes wrong for example no email client application or any exception
                //get and show exception message
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getImageToShare(bitmap: Bitmap): Uri? {
        var uri: Uri? = null
        try {
             val path = MediaStore.Images.Media.insertImage(
                 contentResolver,
                 bitmap,
                 "Image",
                null
             )
             uri = Uri.parse(path)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }
    private fun shareImageandText(bitmap: Bitmap) {
        val uri = getImageToShare(bitmap)
        val intent = Intent(Intent.ACTION_SEND)

        // Putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // Adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "Learn how to share image")

        // Setting type to image
        intent.type = "image/png"

        // Calling startActivity() to share
        startActivity(Intent.createChooser(intent, "Share Via"))
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            val imgBitmap = data?.extras?.get("data") as Bitmap
            imgView.setImageBitmap(imgBitmap)
        }
       else if(requestCode==pick && resultCode == RESULT_OK)
        {
           val imgUri = data?.data
            imgView.setImageURI(imgUri)
        }
       else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    }