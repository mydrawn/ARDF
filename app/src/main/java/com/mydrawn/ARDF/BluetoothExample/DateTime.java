package com.mydrawn.ARDF.BluetoothExample;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Calendar;
import java.util.Locale;


public class DateTime extends ADGattService {

	public static String readCharacteristic(BluetoothGattCharacteristic characteristic) {
		String value = String.format(Locale.getDefault(),
				"%04d/%02d/%02d %02d:%02d:%02d",
				characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 0),
				characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 2),
				characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 3),
				characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 4),
				characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 5),
				characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 6));
		return value;
	}
	
	public static BluetoothGattCharacteristic writeCharacteristic(BluetoothGattCharacteristic characteristic, Calendar calendar) {
		
		int year 	= calendar.get(Calendar.YEAR);
		int month 	= calendar.get(Calendar.MONTH)+1;
		int day 	= calendar.get(Calendar.DAY_OF_MONTH);
		int hour 	= calendar.get(Calendar.HOUR_OF_DAY);
		int min 	= calendar.get(Calendar.MINUTE);
		int sec 	= calendar.get(Calendar.SECOND);

		byte[] value = { 
				(byte)(year & 0x0FF),	// year 2bit
				(byte)(year >> 8),		//
				(byte)month,			// month
				(byte)day,				// day
				(byte)hour,				// hour
				(byte)min,				// min
				(byte)sec				// sec
				};
		characteristic.setValue(value);

		return characteristic;
	}
}
