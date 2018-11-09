package kot.amits.com.kotsystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lvrenyang.pos.Cmd;
import com.phi.phiprintlib.Global;
import com.phi.phiprintlib.PrintService;
import com.phi.phiprintlib.SelectPrinter;
import com.phi.phiprintlib.printerSel;

import java.lang.ref.WeakReference;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.printer_sdk.BTPrinter;

public class printer_connect_activity extends AppCompatActivity implements printerSel, View.OnClickListener {

    private static Handler mPhiHandler = null;
    private SelectPrinter selectPrinter = SelectPrinter.INSTANCE;
    private SharedPreferences mSp;
    private TextView tvStatus;
    private EditText etPrint;
    private int RESULT_LOAD_IMAGE = 2;
    private Uri fileUri;
    private RadioButton rbTwoInch, rbThreeInch;
    Button connect,clear,print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_connect_activity);


        mSp = PreferenceManager.getDefaultSharedPreferences(this);
        rbTwoInch = (RadioButton) findViewById(R.id.two_inch);
        rbTwoInch.setOnClickListener(onRbClicked);
        rbThreeInch = (RadioButton) findViewById(R.id.three_inch);
        rbThreeInch.setOnClickListener(onRbClicked);

        connect=(Button)findViewById(R.id.action_connect);
        clear=(Button)findViewById(R.id.action_clear);
        print=(Button)findViewById(R.id.print);

        connect.setOnClickListener(this);
        clear.setOnClickListener(this);
        print.setOnClickListener(this);


        printerSelection();

        //Bluetooth Setting
        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(this, "Bluetooth Device Error", Toast.LENGTH_LONG).show();
            finish();
        } else if (!mAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BT = 200;
            (this).startActivityForResult(enableIntent,
                    REQUEST_ENABLE_BT);
        }
        //printer initialization
        final Intent intent = new Intent(this, PrintService.class);
        this.startService(intent);
        mPhiHandler = new MPhiHandler(this);
        PrintService.addHandler(mPhiHandler);
        String mac = mSp.getString("PHI_MAC", "");
        if (mac.length() != 17) {
            selectPrinter.initDeviceList(this);
            selectPrinter.createDialog(this);
        }

        tvStatus = (TextView) findViewById(R.id.tv_status);
        etPrint = (EditText) findViewById(R.id.et_print);
        Button btnPrintKanada = (Button) findViewById(R.id.btn_kanada);
        btnPrintKanada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextPaint paint = new TextPaint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(28);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                String txt = "ಮುದ್ರಿಸಲು ಸಲುವಾಗಿ, ಉಷ್ಣ ಸೂಕ್ಷ್ಮ" +
                        " ಕಾಗದದ ಉಷ್ಣ ತಲೆ ಮತ್ತು ಫಲಕವನ್ನು ನಡುವೆ ಸೇರಿಸಲ್ಪಡುತ್ತದೆ. ಪ್ರಿಂಟರ್ ಉಷ್ಣವನ್ನು ಉತ್ಪತ್ತಿ" +
                        " ಶಾಖದ ತಲೆಯ ಬಿಸಿ ಅಂಶಗಳನ್ನು ವಿದ್ಯುತ್ ಪ್ರವಾಹದಿಂದ ಕಳುಹಿಸುತ್ತದೆ. ಶಾಖ ಅಲ್ಲಿ ಬಣ್ಣದ ಬಿಸಿ ಬದಲಾಗುವಂತೆ " +
                        " ಕಾಗದದ ಉಷ್ಣ ಸೂಕ್ಷ್ಮ ಬಣ್ಣ ಪದರ ಸಕ್ರಿಯಗೊಳಿಸುತ್ತದೆ .";
                BTPrinter.printUnicodeText(txt, Layout.Alignment.ALIGN_NORMAL, paint);
            }
        });
        Button btnPrintHindi = (Button) findViewById(R.id.btn_hindi);
        btnPrintHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextPaint paint = new TextPaint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(28);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                String txt = "मुद्रित करने के लिए, थर्मो- संवेदनशील कागज थर्" +
                        "मल सिर और पट्ट के बीच डाला जाता है। प्रिंटर गर्मी पैदा जो थर्मल सिर के हीटिंग तत" +
                        "्वों, एक बिजली के वर्तमान भेजता है। गर्मी में जहां रंग गरम किया जो परिवर्तन थर्मल कागज " +
                        "के थर्मामीटरों संवेदनशील रंग की परत को सक्रिय करता है ।";
                BTPrinter.printUnicodeText(txt, Layout.Alignment.ALIGN_NORMAL, paint);
            }
        });
        Button btnPrintTelugu = (Button) findViewById(R.id.btn_telugu);
        btnPrintTelugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextPaint paint = new TextPaint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(28);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                String txt = "ఒక పరికరం Bluetooth సాంకేతిక పరిజ్ఞానాన్" +
                        "ని ఉపయోగిస్తుంది మార్గం దాని ప్రొఫైల్ సామర్థ్యాల" +
                        "ు ఆధారపడి ఉంటుంది. ప్రొఫైల్స్ తయారీదారులు పరికరాలు ఉద్దేశించిన" +
                        " పద్ధతిలో  ను ఉపయోగించడానికి అనుమతిస్తుంది అనుసరించండి " +
                        "ప్రమాణాలను అందించడానికి . ప్రొఫైల్స" +
                        "్ ఒక ప్రత్యేక సెట్ వర్తిస్తుంది ప్రకారం Bluetooth తక్కువ శక్తి స్టాక్ కోసం ै ।";
                BTPrinter.printUnicodeText(txt, Layout.Alignment.ALIGN_NORMAL, paint);
            }
        });
        Button btnUnicode = (Button) findViewById(R.id.btn_unicode);
        btnUnicode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextPaint paint = new TextPaint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(28);
                paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
                String txt = etPrint.getText().toString();
                if (txt.isEmpty()) {
                    Toast.makeText(printer_connect_activity.this, "Enter text to Print", Toast.LENGTH_SHORT).show();
                } else {
                    BTPrinter.printUnicodeText("مجموع     ضريبة      معدل     كمية     بند", Layout.Alignment.ALIGN_CENTER, paint);
                    BTPrinter.printUnicodeText("Item        Qty      Rate     Tax%      Total   ", Layout.Alignment.ALIGN_NORMAL, paint);
//                    Intent intent1=new Intent(PrinterMain. this,MainActivity.class);
//                    startActivity(intent1);
                }

            }
        });
        Button btnLineFeed = (Button) findViewById(R.id.btn_lf);
        btnLineFeed.hasFocusable();
        btnLineFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BTPrinter.printLineFeed()) {
                    Toast.makeText(printer_connect_activity.this, "Please connect to printer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnTextPrint = (Button) findViewById(R.id.btn_text);
        btnTextPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BTPrinter.printText(etPrint.getText().toString())) {
                    Toast.makeText(printer_connect_activity.this, "Please connect to printer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnBarCode = (Button) findViewById(R.id.btn_barcode);
        btnBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BTPrinter.printBarCode(etPrint.getText().toString())) {
                    Toast.makeText(printer_connect_activity.this, "Please connect to printer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnQRcode = (Button) findViewById(R.id.btn_qrcode);
        btnQRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BTPrinter.printQRcode(etPrint.getText().toString())) {
                    Toast.makeText(printer_connect_activity.this, "Please connect to printer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnPrintImage = (Button) findViewById(R.id.btn_printImage);
        btnPrintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent1=new Intent(MainActivity. this,MainActivity.class);
//                startActivity(intent1);
//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        Button btnFontStyle = (Button) findViewById(R.id.btn_fontstyle);
        btnFontStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontStyleDialog();
            }
        });
        Button btnAlignment = (Button) findViewById(R.id.btn_alignment);
        btnAlignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignmentDialog();
            }
        });
        Button btnFontSize = (Button) findViewById(R.id.btn_fontSize);
        btnFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontSizeDialog();
            }
        });
        Button btnWidth = (Button) findViewById(R.id.btn_width);
        btnWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontWidthDialog();
            }
        });
        Button btnHeight = (Button) findViewById(R.id.btn_height);
        btnHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontHeightDialog();
            }
        });
    }
    public View.OnClickListener onRbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Is the button now checked?
            boolean checked = ((RadioButton) v).isChecked();

            // Check which radio button was clicked
            switch (v.getId()) {
                case R.id.two_inch:
                    if (checked) {
                        BTPrinter.setPrinterType(BTPrinter.TWO_INCH_PRINTER);
                        mSp.edit().putInt("PRINTER", BTPrinter.TWO_INCH_PRINTER).apply();
                    }
                    break;
                case R.id.three_inch:
                    if (checked) {
                        BTPrinter.setPrinterType(BTPrinter.THREE_INCH_PRINTER);
                        mSp.edit().putInt("PRINTER", BTPrinter.THREE_INCH_PRINTER).apply();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("---error", String.valueOf(requestCode)+" "+resultCode);
        if (requestCode == RESULT_LOAD_IMAGE
                && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, opts);
            opts.inJustDecodeBounds = false;
            if (opts.outWidth > 1200) {
                opts.inSampleSize = opts.outWidth / 1200;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, opts);
            if (null != bitmap) {
                afterImageProcessedDialog(bitmap);
            }
//            fileUri = Uri.parse(picturePath);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_printermain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_connect) {
            String mac = mSp.getString("PHI_MAC", "");
            if (mac.length() == 17) {
                PrintService.printer.connectBt(mac);
            } else {
                selectPrinter.createDialog(printer_connect_activity.this);
            }
            return true;
        }
        if (id == R.id.action_clear) {
            String mac = mSp.getString("PHI_MAC", "");
            if (mac.length() == 17) {
                mSp.edit().putString("PHI_MAC", "").apply();
                PrintService.printer.disconnectBt();
                setStatusMsg("Not Connected to any Printer");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void printerSelected(String s) {
        if (s.length() == 17) {
            mSp.edit().putString("PHI_MAC", s).apply();
            PrintService.printer.connectBt(s);
            setStatusMsg("Connecting...");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_connect) {
            String mac = mSp.getString("PHI_MAC", "");
            if (mac.length() == 17) {
                PrintService.printer.connectBt(mac);
            } else {
                selectPrinter.createDialog(printer_connect_activity.this);
            }
        }
        if (v.getId() == R.id.action_clear) {
            String mac = mSp.getString("PHI_MAC", "");
            if (mac.length() == 17) {
                mSp.edit().putString("PHI_MAC", "").apply();
                PrintService.printer.disconnectBt();
                setStatusMsg("Not Connected to any Printer");
            }
        }
        if (v==print){
            BTPrinter.printText("print from button");
        }
    }

    class MPhiHandler extends Handler {

        WeakReference<Activity> mActivity;

        MPhiHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity theActivity = mActivity.get();
            if (msg.arg1 == 0) {
                setStatusMsg("Printer Connection Failed");
                tvStatus.setBackgroundColor(Color.RED);
            }
            switch (msg.what) {
                case Global.MSG_WORKTHREAD_SEND_CONNECTBTRESULT: {
                    int result = msg.arg1;
                    if (result == 1) {
                        setStatusMsg("Connected");
                        tvStatus.setBackgroundColor(Color.GREEN);
                        BTPrinter.setPrinterType(BTPrinter.THREE_INCH_PRINTER);

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",result);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();

//                        BTPrinter.printText("Connection with printer success!!!");
//                        finish();
                    } else {
                        setStatusMsg("not connected");
                        tvStatus.setBackgroundColor(Color.RED);
                    }
                    break;
                }
                case Global.MSG_ALLTHREAD_READY: {
                    String mac = mSp.getString("PHI_MAC", "");
                    if (mac.length() == 17) {
                        if (PrintService.printer != null) {
                            PrintService.printer.connectBt(mac);
                        }
                    }
                    break;
                }
            }
        }
    }
    private void setStatusMsg(String sts) {
        tvStatus.setText(sts);
    }
    public void alignmentDialog() {
        CharSequence storage[] = new CharSequence[]{"Left Align", "Center Align", "Right Align"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alignment");
        builder.setItems(storage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        BTPrinter.setAlignment(BTPrinter.ALIGN_LEFT);
                        break;
                    case 1:
                        BTPrinter.setAlignment(BTPrinter.ALIGN_CENTER);
                        break;
                    case 2:
                        BTPrinter.setAlignment(BTPrinter.ALIGN_RIGHT);
                        break;
                }
            }

        });
        builder.show();
    }

    public void fontStyleDialog() {
        CharSequence storage[] = new CharSequence[]{"Normal", "Bold"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Font Style");
        builder.setItems(storage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        BTPrinter.setFontStyle(Cmd.Constant.FONTSTYLE_NORMAL);
                        break;
                    case 1:
                        BTPrinter.setFontStyle(Cmd.Constant.FONTSTYLE_BOLD);
                        break;
                }
            }

        });
        builder.show();
    }

    public void fontSizeDialog() {
        CharSequence storage[] = new CharSequence[]{"Standard font", "Compress font", "No Assign"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Font Size");
        builder.setItems(storage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        BTPrinter.setFontSize(BTPrinter.STANDARD_FONT);
                        break;
                    case 1:
                        BTPrinter.setFontSize(BTPrinter.COMPRESS_FONT);
                        break;
                    case 2:
                        BTPrinter.setFontSize(BTPrinter.NO_ASSIGN);
                        break;
                }
            }

        });
        builder.show();
    }

    public void fontWidthDialog() {
        CharSequence storage[] = new CharSequence[]{"Normal Width", "Double Width"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Font Width");
        builder.setItems(storage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        BTPrinter.setFontWidth(BTPrinter.WIDTH_NORMAL);
                        break;
                    case 1:
                        BTPrinter.setFontWidth(BTPrinter.WIDTH_DOUBLE);
                        break;

                }
            }

        });
        builder.show();
    }

    public void fontHeightDialog() {
        CharSequence storage[] = new CharSequence[]{"Normal Height", "Double Height"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Font Height");
        builder.setItems(storage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        BTPrinter.setFontHeight(BTPrinter.HEIGHT_NORMAL);
                        break;
                    case 1:
                        BTPrinter.setFontHeight(BTPrinter.HEIGHT_DOUBLE);
                        break;

                }
            }

        });
        builder.show();
    }

    public void afterImageProcessedDialog(final Bitmap bmp) {

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bmp);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(imageView)
                .setPositiveButton("print", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BTPrinter.printImage(bmp);
                    }
                }).create();

        dialog.show();

    }

    private void printerSelection() {
        int printer = mSp.getInt("PRINTER", BTPrinter.TWO_INCH_PRINTER);
        if (printer == BTPrinter.THREE_INCH_PRINTER) {
            rbThreeInch.setChecked(true);
            BTPrinter.setPrinterType(BTPrinter.THREE_INCH_PRINTER);
        } else {
            rbTwoInch.setChecked(true);
            BTPrinter.setPrinterType(BTPrinter.TWO_INCH_PRINTER);
        }
    }
}
