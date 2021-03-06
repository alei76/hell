package ps.hell.util.jpcap;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class ReceivePacket implements PacketReceiver {
 private String ipAddr = "";

 private static int packetCount = 0;
 private static int tcpPacketCount = 0;
 private static int udpPacketCount = 0;
 private static int tcppPacketLength = 0;
 private static int udpPacketLength = 0;
 
 public static HashSet<Integer> prot=new HashSet<Integer>();

 static{
	 int[] po=new int[]{5000,4000,4001,4001,4002,4003,4004};
	 for(int p:po){
		 prot.add(p);
	 }
 }
 public ReceivePacket(String ipAddr) {
  this.ipAddr = ipAddr;
 }

 public void receivePacket(Packet packet){
  this.packetCount++;
  // 如果截获的包为TCP包
  if (packet instanceof jpcap.packet.TCPPacket) {
   this.tcpPacketCount++;

   TCPPacket tcp = (TCPPacket) packet;
   if(!prot.contains(tcp.dst_port)&&!prot.contains(tcp.src_port)){
	   return;
   }
   this.tcppPacketLength += tcp.len;

   System.out
     .println("================================================================================");
   System.out.println("TCPPacket");
   System.out.println("|——*此数据包的源IP地址：" + tcp.src_ip+":"+tcp.src_port);
   System.out.println("|——*此数据包的目的IP地址：" + tcp.dst_ip+":"+tcp.dst_port);
   System.out.println("|——*此数据包的传输层协议类型：" + tcp.protocol + "(TCP)");
   System.out.println("|——*此数据包的长度：" + tcp.len);
   try {
	System.out.println("|--*data:"+new String(tcp.data,"utf-8"));
} catch (UnsupportedEncodingException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

  } else if (packet instanceof jpcap.packet.UDPPacket) {// 如果截获的包为UDP包
   this.udpPacketCount++;

   UDPPacket udp = (UDPPacket) packet;
   if(!prot.contains(udp.dst_port)&&!prot.contains(udp.src_port)){
	   return;
   }
   this.udpPacketLength += udp.len;

   System.out
     .println("================================================================================");
   System.out.println("UDPPacket");
   System.out.println("|——*此数据包的源IP地址：" + udp.src_ip+":"+udp.src_port);
   System.out.println("|——*此数据包的目的IP地址：" + udp.dst_ip+":"+udp.dst_port);
   System.out.println("|——*此数据包的传输层协议类型：" + udp.protocol + "(UDP)");
   System.out.println("|——*此数据包的长度：" + udp.len);
   try {
	System.out.println("|--*data:"+new String(udp.data,"utf-8"));
} catch (UnsupportedEncodingException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

  } else {
   // 如果截获的包非运输层协议包
   System.out.println("***捕获到非运输层协议的其他包！！！");
  }
  
//  System.out.println();
//  System.out.println("截止到目前：");
//  System.out.println("|捕获到的运输层协议包的总数为：" + this.packetCount);
//  System.out.println("|——*捕获到TCPPacket包的总数为：" + this.tcpPacketCount
//    + ",总数据包长度为" + this.tcppPacketLength);
//  System.out.println("|——*捕获到UDPPacket包的总数为：" + this.udpPacketCount
//    + ",总数据包长度为" + this.udpPacketLength);

 }
 
 private static String currentTime(){
  Date date = new Date();
  
   SimpleDateFormat sdf=new SimpleDateFormat("yyyy/mm/dd hh:ff:mm");
  
   String strTime = sdf.format(date);
  
   return strTime;
  
 }

}