import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
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

/**
 * Created by keson on 2017-04-13.
 */
public class PacketParser {
    public long getFramNO() {
        return framNO.get();
    }

    public SimpleLongProperty framNOProperty() {
        return framNO;
    }

    public void setFramNO(long framNO) {
        this.framNO.set(framNO);
    }

    public int getFrameLength() {
        return frameLength.get();
    }

    public SimpleIntegerProperty frameLengthProperty() {
        return frameLength;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength.set(frameLength);
    }

    public int getPacketLength() {
        return packetLength.get();
    }

    public SimpleIntegerProperty packetLengthProperty() {
        return packetLength;
    }

    public void setPacketLength(int packetLength) {
        this.packetLength.set(packetLength);
    }

    public int getPayLoadLength() {
        return payLoadLength.get();
    }

    public SimpleIntegerProperty payLoadLengthProperty() {
        return payLoadLength;
    }

    public void setPayLoadLength(int payLoadLength) {
        this.payLoadLength.set(payLoadLength);
    }

    public long getSequence() {
        return sequence.get();
    }

    public SimpleLongProperty sequenceProperty() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence.set(sequence);
    }

    public long getAcknowledgement() {
        return acknowledgement.get();
    }

    public SimpleLongProperty acknowledgementProperty() {
        return acknowledgement;
    }

    public void setAcknowledgement(long acknowledgement) {
        this.acknowledgement.set(acknowledgement);
    }

    public String getSourceIP() {
        return sourceIP.get();
    }

    public SimpleStringProperty sourceIPProperty() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP.set(sourceIP);
    }

    public int getSourcePort() {
        return sourcePort.get();
    }

    public SimpleIntegerProperty sourcePortProperty() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort.set(sourcePort);
    }

    public String getDestIP() {
        return destIP.get();
    }

    public SimpleStringProperty destIPProperty() {
        return destIP;
    }

    public void setDestIP(String destIP) {
        this.destIP.set(destIP);
    }

    public int getDestPort() {
        return destPort.get();
    }

    public SimpleIntegerProperty destPortProperty() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort.set(destPort);
    }

    public String getProtocol() {
        return protocol.get();
    }

    public SimpleStringProperty protocolProperty() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol.set(protocol);
    }

    public String getArrivalTime() {
        return arrivalTime.get();
    }

    public SimpleStringProperty arrivalTimeProperty() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime.set(arrivalTime);
    }

    private SimpleIntegerProperty frameLength;
    private SimpleIntegerProperty packetLength; //what happens if not ip4 and ip6
    private SimpleIntegerProperty payLoadLength;
    private SimpleLongProperty sequence;
    private SimpleLongProperty acknowledgement;
    private SimpleStringProperty sourceIP;
    private SimpleIntegerProperty sourcePort;
    private SimpleStringProperty destIP;
    private SimpleIntegerProperty destPort;
    private SimpleStringProperty protocol;
    private SimpleStringProperty arrivalTime;
    private SimpleLongProperty framNO;

    public PacketParser(){
        framNO = new SimpleLongProperty();
        frameLength = new SimpleIntegerProperty();
        packetLength = new SimpleIntegerProperty();
        payLoadLength = new SimpleIntegerProperty();
        sequence = new SimpleLongProperty();
        acknowledgement = new SimpleLongProperty();
        sourceIP = new SimpleStringProperty();
        sourcePort = new SimpleIntegerProperty();
        destPort = new SimpleIntegerProperty();
        destIP = new SimpleStringProperty();
        protocol = new SimpleStringProperty();
        arrivalTime = new SimpleStringProperty();
    }


    public void parser(PcapPacket packet) {

        FormatUtils format = new FormatUtils();

        Ip4 ip4 = new Ip4();
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

        framNO.set(packet.getFrameNumber());
        frameLength.set(packet.getCaptureHeader().caplen());
        if (packet.hasHeader(eth)){
            payLoadLength.set(eth.getPayloadLength());
        }


        if (packet.hasHeader(tcp)) {
            protocol.set("Tcp");
            destPort.set(tcp.destination());
            sourcePort.set(tcp.source());
            sequence.set(packet.getHeader(tcp).seq());
            acknowledgement.set(packet.getHeader(tcp).ack());
        } else if (packet.hasHeader(udp)) {
            protocol.set("Udp");
            destPort.set(udp.destination());
            sourcePort.set(udp.source());
//            sequence =  -1;
        } else if (packet.hasHeader(icmp)){
            protocol.set("ICMP");
        }

        if (packet.getHeader(eth).type() == 2048) {
            destIP.set(format.ip(packet.getHeader(new Ip4()).destination()));
            sourceIP.set(format.ip(packet.getHeader(new Ip4()).source()));
            packetLength.set(packet.getHeader(new Ip4()).length());
        } else if (packet.getHeader(eth).type() == 34525) {
            destIP.set(format.ip(packet.getHeader(new Ip6()).destination()));
            sourceIP.set(format.ip(packet.getHeader(new Ip6()).source()));
            packetLength.set(packet.getHeader(new Ip6()).length());
        } else if (packet.hasHeader(eth)) {
        }

        long ti = packet.getCaptureHeader().timestampInMillis();
        String tim = format.formatTimeInMillis(ti);
        String[] split = tim.split(" ");

        if (split.length == 10) {
            String seconds = "00";
            arrivalTime.set(split[6] + ":" + seconds + ":" + split[8]);
        } else if (split.length == 12) {
            arrivalTime.set(split[6] + ":" + split[8] + ":" + split[10]);
        }

    }

}
