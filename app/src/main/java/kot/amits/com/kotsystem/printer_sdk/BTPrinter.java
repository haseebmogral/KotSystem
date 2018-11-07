package kot.amits.com.kotsystem.printer_sdk;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.lvrenyang.pos.Cmd;
import com.phi.phiprintlib.Global;
import com.phi.phiprintlib.PrintService;

public class BTPrinter {
    public static int ALIGN_LEFT = 0;
    public static int ALIGN_CENTER = 1;
    public static int ALIGN_RIGHT = 2;
    private static int FONT_SIZE = 2;
    public static int STANDARD_FONT = 0;
    public static int COMPRESS_FONT = 1;
    public static int NO_ASSIGN = 2;
    private static int FONT_STYLE = 2;
    private static int FONT_WIDTH = 0;
    public static int WIDTH_DOUBLE = 1;
    public static int WIDTH_NORMAL = 0;
    private static int FONT_HEIGHT = 0;
    public static int HEIGHT_DOUBLE = 1;
    public static int HEIGHT_NORMAL = 0;
    private static int PRINTER_WIDTH = 384;
    public static int TWO_INCH_PRINTER = 384;
    public static int THREE_INCH_PRINTER = 576;

    public static boolean setAlignment(int align) {
        if (PrintService.printer.isConnected()) {
            Bundle data = new Bundle();
            data.putInt(Global.INTPARA1, align);
            PrintService.printer.handleCmd(Global.CMD_POS_SALIGN, data);
        } else {
            return false;
        }
        return true;
    }

    public static void setFontSize(int size) {
        FONT_SIZE = size;
    }

    public static void setFontStyle(int style) {
        FONT_STYLE = style;
    }

    public static void setFontWidth(int width) {
        FONT_WIDTH = width;
    }

    public static void setFontHeight(int height) {
        FONT_HEIGHT = height;
    }

    public static void setPrinterType(int type) {
        PRINTER_WIDTH = type;
    }

    public static boolean printText(String txt) {

        if (PrintService.printer.isConnected()) {
            Bundle dataTextOut = new Bundle();
            dataTextOut.putString(Global.STRPARA1, txt + "\n");
            dataTextOut.putString(Global.STRPARA2, "US-ASCII");
            dataTextOut.putInt(Global.INTPARA1, 0);
            dataTextOut.putInt(Global.INTPARA2, FONT_WIDTH);
            dataTextOut.putInt(Global.INTPARA3, FONT_HEIGHT);
            dataTextOut.putInt(Global.INTPARA4, FONT_SIZE);
            dataTextOut.putInt(Global.INTPARA5, FONT_STYLE);
            PrintService.printer.handleCmd(
                    Global.CMD_POS_STEXTOUT, dataTextOut);
        } else {
            return false;
        }
        return true;
    }

    public static boolean printUnicodeText(String txt, Layout.Alignment align, TextPaint paint) {
        StaticLayout layout = new StaticLayout(txt, paint, PRINTER_WIDTH, align, 1, 1,
                true);
        int tlHeight = layout.getHeight();
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap mBitmap = Bitmap.createBitmap(PRINTER_WIDTH, tlHeight, conf);
        Canvas canvas = new Canvas(mBitmap);
        canvas.drawColor(Color.WHITE);
        layout.draw(canvas);
        if (PrintService.printer.isConnected()) {
            Bundle data = new Bundle();
            //data.putParcelable(Global.OBJECT1, mBitmap);
            data.putParcelable(Global.PARCE1, mBitmap);
            data.putInt(Global.INTPARA1, PRINTER_WIDTH);
            data.putInt(Global.INTPARA2, 0);
            PrintService.printer.handleCmd(
                    Global.CMD_POS_PRINTPICTURE, data);
        } else {
            return false;
        }
        return true;
    }

    public static boolean printBarCode(String txt) {

        int nOrgx = 0;
        int nType = Cmd.Constant.BARCODE_TYPE_CODE128;
        int nWidthX = 1 + 2;
        int nHeight = (3 + 1) * 24;
        int nHriFontType = 0;
        int nHriFontPosition = 2;

        if (PrintService.printer.isConnected()) {
            Bundle data = new Bundle();
            data.putString(Global.STRPARA1, txt);
            data.putInt(Global.INTPARA1, nOrgx);
            data.putInt(Global.INTPARA2, nType);
            data.putInt(Global.INTPARA3, nWidthX);
            data.putInt(Global.INTPARA4, nHeight);
            data.putInt(Global.INTPARA5, nHriFontType);
            data.putInt(Global.INTPARA6, nHriFontPosition);
            PrintService.printer.handleCmd(Global.CMD_POS_SETBARCODE,
                    data);
        } else {
            //Toast.makeText(this, "Printer Not Connected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean printQRcode(String txt) {
        int nWidthX = 4 + 2;
        int necl = 3 + 1;
        if (PrintService.printer.isConnected()) {
            Bundle data = new Bundle();
            data.putString(Global.STRPARA1, txt);
            data.putInt(Global.INTPARA1, nWidthX);
            data.putInt(Global.INTPARA2, necl);

            PrintService.printer.handleCmd(
                    Global.CMD_POS_SETQRCODE, data);
        } else {
            // Toast.makeText(this, "Printer Not Connected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean sentCmd(String str) {

        if (PrintService.printer.isConnected()) {
            Bundle data = new Bundle();
            data.putInt(Global.INTPARA1, 1);
            PrintService.printer.handleCmd(Global.CMD_POS_SALIGN, data);
        } else {
            // Toast.makeText(this, "Printer Not Connected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean printLineFeed() {

        if (PrintService.printer.isConnected()) {
            Bundle data = new Bundle();
            data.putInt(Global.INTPARA1, 24);
            data.putString(Global.STRPARA1, "");
            PrintService.printer.handleCmd(
                    Global.CMD_POS_PRINTTXT, data);
        } else {
            return false;
        }
        return true;
    }

    public static boolean printImage(Bitmap bmp) {
        if (bmp != null) {
            if (PrintService.printer.isConnected()) {
                Bundle data = new Bundle();
                //data.putParcelable(Global.OBJECT1, bmp);
                data.putParcelable(Global.PARCE1, bmp);
                data.putInt(Global.INTPARA1, PRINTER_WIDTH);
                data.putInt(Global.INTPARA2, 0);
                PrintService.printer.handleCmd(
                        Global.CMD_POS_PRINTPICTURE, data);
            } else {
                return false;
            }
        }
        return true;
    }


}
