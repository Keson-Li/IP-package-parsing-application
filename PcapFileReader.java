import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.util.PcapPacketArrayList;


public class PcapFileReader {

    public PcapPacketArrayList readOfflineFiles(String filepath) throws ExceptionReadingPcapFiles{
        final StringBuilder errbuf = new StringBuilder(); // For any error msgs
        Pcap pcap = Pcap.openOffline(filepath, errbuf);
        if (pcap == null) {
            throw new ExceptionReadingPcapFiles(errbuf.toString());
         }
        PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new  PcapPacketHandler<PcapPacketArrayList>() {

            public void nextPacket(PcapPacket packet, PcapPacketArrayList PacketsList) {
                PacketsList.add(packet);
             }
        };

       try {

            PcapPacketArrayList packets = new PcapPacketArrayList();
            pcap.loop(10000,jpacketHandler,packets);
            return packets;
       } finally {

      //Last thing to do is close the pcap handle
           pcap.close();
        }
  }
}
