package com.jdxy.hardware;

import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;

import com.lvrenyang.io.Pos;

public class Prints {

	public static int PrintTicket(Context ctx, Pos pos, int nPrintWidth, boolean bCutter, boolean bDrawer, boolean bBeeper, int nCount, int nPrintContent, int nCompressMethod)
	{
		int bPrintResult = -6;
		
		byte[] status = new byte[1];
		if (pos.POS_RTQueryStatus(status, 1, 3000, 2) && ((status[0] & 0x12) == 0x12)) {
			if ((status[0] & 0x08) == 0) {
				if(pos.POS_QueryStatus(status, 3000, 2)) {
					
					Bitmap bm1 = getTestImage1(nPrintWidth, nPrintWidth);
					Bitmap bm2 = getTestImage2(nPrintWidth, nPrintWidth);
					Bitmap bmBlackWhite = getImageFromAssetsFile(ctx, "blackwhite.png");
					Bitmap bmIu = getImageFromAssetsFile(ctx, "iu.jpeg");
					Bitmap bmYellowmen = getImageFromAssetsFile(ctx, "yellowmen.png");
					for(int i = 0; i < nCount; ++i)
					{
						if(!pos.GetIO().IsOpened())
							break;
						
						if(nPrintContent >= 1)
						{
							pos.POS_FeedLine();
							pos.POS_S_Align(1);
							pos.POS_S_TextOut("REC" + String.format("%03d", i) + "\r\nPrinter\r\n测试页\r\n\r\n", 0, 1, 1, 0, 0x100);
							pos.POS_S_TextOut("扫二维码\r\n", 0, 0, 0, 0, 0x100);
							pos.POS_TextOut("鳥インフル\r\n", 4, 0, 0, 0, 0, 0);
							pos.POS_S_SetQRcode("欢迎使用 Welcome", 8, 0, 3);
							//pos.POS_DoubleQRCode("abc", 120,3,0, "def", 340, 3, 0, 3);
							//pos.POS_DoubleQRCode("AB112233441020523999900000144000001540000000001234567ydXZt4LAN1UHN/j1juVcRA==:**********:3:3:1:乾電池:1:105:",120,3,0,"**口罩:1:210:牛奶:1:25",340,3,0,3);
							//pos.POS_DoubleQRCode("乾電池:1:105:",120,3,0,"**口罩:1:210:牛奶:1:25",340,3,0,1);
							//pos.POS_DoubleQRCode("乾電池:1:105", 0,3,0, "口罩:1:210:牛奶:1:25", 288, 3, 0, 1);
							pos.POS_FeedLine();
							pos.POS_S_SetBarcode("20160618", 0, 72, 3, 60, 0, 2);
							pos.POS_FeedLine();
						}
						
						if(nPrintContent >= 2)
						{
							if(bm1 != null)
							{
								pos.POS_PrintPicture(bm1, nPrintWidth, 1, nCompressMethod);
							}
							if(bm2 != null)
							{
								pos.POS_PrintPicture(bm2, nPrintWidth, 1, nCompressMethod);
							}
						}
						
						if(nPrintContent >= 3)
						{
							if(bmBlackWhite != null)
							{
								pos.POS_PrintPicture(bmBlackWhite, nPrintWidth, 1, nCompressMethod);
							}
							if(bmIu != null)
							{
								pos.POS_PrintPicture(bmIu, nPrintWidth, 0, nCompressMethod);
							}
							if(bmYellowmen != null)
							{
								pos.POS_PrintPicture(bmYellowmen, nPrintWidth, 0, nCompressMethod);
							}
						}
					}
					
					if(bBeeper)
						pos.POS_Beep(1, 5);
					if(bCutter)
						pos.POS_CutPaper();
					if(bDrawer)
						pos.POS_KickDrawer(0, 100);
					
					bPrintResult = pos.POS_TicketSucceed(0, 30000);
				} else {
					bPrintResult = -8;
				}
			} else {
				bPrintResult = -4;
			}
		} else {
			bPrintResult = -7;
		}
	
		return bPrintResult;
	}

	public static String ResultCodeToString(int code) {
		switch (code) {
			case 0:
				return "打印成功";
			case -1:
				return "连接断开";
			case -2:
				return "写入失败";
			case -3:
				return "读取失败";
			case -4:
				return "打印机脱机";
			case -5:
				return "打印机缺纸";
			case -7:
				return "实时状态查询失败";
			case -8:
				return "查询状态失败";
			case -6:
			default:
				return "未知错误";
		}
	}
	
	/**
	 * 从Assets中读取图片
	 */
	public static Bitmap getImageFromAssetsFile(Context ctx, String fileName) {
		Bitmap image = null;
		AssetManager am = ctx.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
	
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		// load the origial Bitmap
		Bitmap BitmapOrg = bitmap;

		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		// calculate the scale
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the Bitmap
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);

		// make a Drawable from Bitmap to allow to set the Bitmap
		// to the ImageView, ImageButton or what ever
		return resizedBitmap;
	}
	
	public static Bitmap getTestImage1(int width, int height)
	{
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();

		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, width, height, paint);
		
		paint.setColor(Color.BLACK);
		for(int i = 0; i < 8; ++i)
		{
			for(int x = i; x < width; x += 8)
			{
				for(int y = i; y < height; y += 8)
				{
					canvas.drawPoint(x, y, paint);
				}
			}
		}
		return bitmap;
	}

	public static Bitmap getTestImage2(int width, int height)
	{
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();

		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, width, height, paint);
		
		paint.setColor(Color.BLACK);
		for(int y = 0; y < height; y += 4)
		{
			for(int x = y%32; x < width; x += 32)
			{
				canvas.drawRect(x, y, x+4, y+4, paint);
			}
		}
		return bitmap;
	}
}
