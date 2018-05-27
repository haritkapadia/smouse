package com.example.dereck.mouseemulator;
import java.nio.*;
import org.usb4java.*;

public class MouseClient {
	static Context context;
	static Device device = null;
	static DeviceHandle handle;
	static short venID = 4100;
	static short prodID = 25081;
	static byte interfaceNumber = (byte)0x01;
	static int r;

	static void init() {
		context = new Context();
		r = LibUsb.init(context);
		if(r != LibUsb.SUCCESS)
			throw new LibUsbException("no init", r);
	}

	static void getDevices() {
		DeviceList list = new DeviceList();
		r = LibUsb.getDeviceList(null, list);
		if(r < 0)
			throw new LibUsbException("no devlist", r);

		try {
			for(Device ndev : list) {
				DeviceDescriptor desc = new DeviceDescriptor();
				r = LibUsb.getDeviceDescriptor(ndev, desc);
				if(r != LibUsb.SUCCESS)
					throw new LibUsbException("no read desc", r);
				System.out.println("V: " + desc.idVendor() + "\tP: " + desc.idProduct());
				if(desc.idVendor() == venID && desc.idProduct() == prodID)
					device = ndev;
			}
		} finally {
			LibUsb.freeDeviceList(list, false);
			if(device != null) {
				LibUsb.refDevice(device);
			}
		}
	}

	static void makeHandle() {
		handle = new DeviceHandle();

		r = LibUsb.open(device, handle);
		if(r != LibUsb.SUCCESS) 
			throw new LibUsbException("no open handle", r);
	}

	static void claimInterface() {
			r = LibUsb.claimInterface(handle, interfaceNumber);
			if(r != LibUsb.SUCCESS) 
				throw new LibUsbException("no claim interface", r);
	}

	static void write(byte prw[]) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(prw.length);
		buffer.put(prw);
		buffer.allocate(prw.length);
		IntBuffer transferred = IntBuffer.allocate(prw.length);
		r = LibUsb.bulkTransfer(handle, interfaceNumber, buffer, transferred, 5000);
		if(r != LibUsb.SUCCESS)
			throw new LibUsbException("no read", r);
		System.out.println(buffer.array());
	}

	static void releaseInterface() {
		r = LibUsb.releaseInterface(handle, interfaceNumber);
		if(r != LibUsb.SUCCESS) 
			throw new LibUsbException("no release interface", r);
	}

	static void destruct() {
		LibUsb.close(handle);
		LibUsb.exit(context);
	}
}

