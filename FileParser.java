import org.jnetpcap.packet.Payload;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.util.PcapPacketArrayList;
import java.util.ArrayList;


/**
 * Created by keson on 2017-04-03.
 */
public class FileParser {
    // Data Link Layer
    private ArrayList<String> arrivalTime;
    private ArrayList<Integer> frameLength;

    // Network Layer
    private ArrayList<String> numOfProtocols;
    private ArrayList<String> destinationIP;
    private ArrayList<String> sourceIP;
    private ArrayList<Integer> ipPacketLength;

    // Transport Layer
    private ArrayList<String> destinationPorts;
    private ArrayList<String> sourcePorts;
    private ArrayList<Integer> payloadLength;
    private ArrayList<Integer> windowSize;
    private ArrayList<Integer> tcpPacketLength;
    private ArrayList<Integer> udpPacketLength;
    private Integer numUDP;
    private Integer numTCP;
    private Integer numARP;
    private Integer numConnection;

    public FileParser(PcapPacketArrayList packetList){
        numUDP = 0;
        numTCP = 0;
        numARP = 0;
        numConnection = 0;

        // Data Link Layer
        arrivalTime = new ArrayList<String>();
        frameLength = new ArrayList<Integer>();

        // Network Layer
        numOfProtocols = new ArrayList<String>();
        destinationIP = new ArrayList<String>();
        sourceIP = new ArrayList<String>();
        ipPacketLength = new ArrayList<Integer>();

        // Transport Layer
        destinationPorts = new ArrayList<String>();
        sourcePorts = new ArrayList<String>();
        payloadLength = new ArrayList<Integer>();
        windowSize = new ArrayList<Integer>();
        tcpPacketLength =  new ArrayList<Integer>();
        udpPacketLength = new ArrayList<Integer>();

        parser(packetList);
    }
    private void parser(PcapPacketArrayList packetList) {

        FormatUtils format = new FormatUtils();

        Ip4 ip = new Ip4();
        Ip6 ip6 = new Ip6();
        Tcp tcp = new Tcp();
        Http http = new Http();
        Ethernet eth = new Ethernet();
        Payload payload = new Payload();
        byte[] sIP;
        byte[] dIP;
        String s = "";
        String d = "";
        String ipv6 = "";
        Tcp.Timestamp timestamp = new Tcp.Timestamp();
        Icmp icmp = new Icmp();
        Udp udp = new Udp();
        Arp arp = new Arp();

        for (int i = 0; i < packetList.size(); i++) {
            if (packetList.get(i).hasHeader(eth)) {
                numConnection++;
                payloadLength.add(packetList.get(i).getHeader(eth).getPayloadLength());
            }

            if (packetList.get(i).hasHeader(tcp)) {
                numTCP++;
            } else if (packetList.get(i).hasHeader(udp)) {
                numUDP++;
            }else if (packetList.get(i).hasHeader(arp)){
                numARP++;
            }

            frameLength.add(packetList.get(i).getCaptureHeader().caplen());


        }

        for (int i = 0; i < packetList.size(); i++) {
            if (packetList.get(i).getHeader(eth).type() == 2048) {
                sIP = packetList.get(i).getHeader(ip).source();
                dIP = packetList.get(i).getHeader(ip).destination();
                d = format.ip(dIP);
                s = format.ip(sIP);
                addSourceIP(s);
                addDestIP(d);
            } else if (packetList.get(i).getHeader(eth).type() == 34525) {
                sIP = packetList.get(i).getHeader(ip6).source();
                dIP = packetList.get(i).getHeader(ip6).destination();
                d = format.ip(dIP);
                s = format.ip(sIP);
                addSourceIP(d);
                addDestIP(s);
            }

            long ti = packetList.get(i).getCaptureHeader().timestampInMillis();
            String tim = format.formatTimeInMillis(ti);
            String[] split = tim.split(" ");

            if (split.length == 10) {
                String seconds = "00";
                String a = split[6] + ":" + seconds + ":" + split[8];
                //Time aT = Time.valueOf(a);
//                addTime(a);
                arrivalTime.add(a);
            } else if (split.length == 12) {
                String b = split[6] + ":" + split[8] + ":" + split[10];
                //Time bT = Time.valueOf(b);
//                addTime(b);
                arrivalTime.add(b);
            }
        }
    }


    public ArrayList<String> getArrivalTime() {
        return arrivalTime;
    }

    public ArrayList<Integer> getFrameLength() {
        return frameLength;
    }

    public ArrayList<String> getNumOfProtocols() {
        return numOfProtocols;
    }

    public ArrayList<String> getDestinationIP() {
        return destinationIP;
    }

    public ArrayList<String> getSourceIP() {
        return sourceIP;
    }

    public ArrayList<Integer> getIpPacketLength() {
        return ipPacketLength;
    }

    public ArrayList<String> getDestinationPorts() {
        return destinationPorts;
    }

    public ArrayList<String> getSourcePorts() {
        return sourcePorts;
    }

    public ArrayList<Integer> getPayloadLength() {
        return payloadLength;
    }

    public ArrayList<Integer> getWindowSize() {
        return windowSize;
    }

    public ArrayList<Integer> getTcpPacketLength() {
        return tcpPacketLength;
    }

    public ArrayList<Integer> getUdpPacketLength() {
        return udpPacketLength;
    }

    public Integer getNumUDP() {
        return numUDP;
    }

    public Integer getNumTCP() {
        return numTCP;
    }

    public Integer getNumARP() {
        return numARP;
    }

    public Integer getNumConnection() {
        return numConnection;
    }



    public void addDestIP(String address) {
        if (!(destinationIP.contains(address))) {
            destinationIP.add(address);
        }
    }

    public void addSourceIP(String address) {
        if (!(sourceIP.contains(address))) {
            sourceIP.add(address);
        }
    }













//    public int countPackets(){
//        return packetList.size();
//    }
//    public PcapPacketArrayList getTcpPackdts(){
//        final Tcp tcp = new Tcp();
//        PcapPacketArrayList tcpPacket = new PcapPacketArrayList();
//
//        for (PcapPacket pPacket : packetList){
//            if (pPacket.hasHeader(tcp)){
//                tcpPacket.add(pPacket);
//            }
//        }
//        return tcpPacket;
//    }
//    public PcapPacketArrayList getUDPPackets(){
//        final Udp udp = new Udp();
//        PcapPacketArrayList tcpPacket = new PcapPacketArrayList();
//        for (PcapPacket pPacket : packetList){
//            if (pPacket.hasHeader(udp)){
//                tcpPacket.add(pPacket);
//            }
//        }
//        return tcpPacket;
//    }
//    public PcapPacketArrayList getData(){
//        Date data = new Date();
//        for (PcapPacket pcapPacket:packetList){
//            if (pcapPacket.)
//        }
//    }


}
