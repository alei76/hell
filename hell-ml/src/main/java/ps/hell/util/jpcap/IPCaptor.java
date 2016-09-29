package ps.hell.util.jpcap;

import java.io.IOException;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.IPPacket;

public class IPCaptor {
	private static JpcapCaptor captor = null;

	public static void open() throws IOException {
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		captor = JpcapCaptor.openDevice(devices[0], 2000, true, 3000);
	}

	public static void setFilterType(int type) throws IOException {
		switch (type) {
		case 0:
			captor.setFilter("ip", true);
			break;
		case 1:
			captor.setFilter("tcp", true);
			break;
		case 2:
			captor.setFilter("udp", true);
			break;
		case 3:
			captor.setFilter("icmp", true);
			break;
		}
	}

	public static IPPacket cap() {
		IPPacket ip = null;
		while (true) { //
			// 循环直到抓到一个包
			ip = (IPPacket) captor.getPacket();
			if (ip != null) {
				return ip;
			}
		}
	}
	public static void main(String[] args) throws IOException {
		IPCaptor.open();
		IPCaptor.setFilterType(4);
		System.out.println(IPCaptor.cap().toString());
	}
}
