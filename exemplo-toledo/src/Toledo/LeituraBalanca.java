package Toledo;


import java.text.DecimalFormat;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class LeituraBalanca {
	
	public String executaLeitura() {
		String text = null;

		SerialPort serialPort;
		
		String[] portNames = SerialPortList.getPortNames();
		System.out.println("portNames len: "+portNames.length);

		for (int i = 0; i < portNames.length; i++) {

			System.out.println("port: "+portNames[i]);
			serialPort = new SerialPort(portNames[i]);
	            
			try {
				System.out.println("Port opened: " + serialPort.openPort());
				System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
				
				System.out.println("successfully writen to port: " + serialPort.writeBytes(new byte[] { 0x05 }));
				
				byte[] buffer = null;
				while(buffer == null) {
					buffer = serialPort.readBytes();
				}
	
				text = new String(buffer);
	
				text = text.replaceAll("[^0123456789]", "");
				
				try {
					DecimalFormat decimal = new DecimalFormat("0.0000");
					Float peso = Float.parseFloat(text);
					peso = peso/10000;
					text = decimal.format(peso);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
	
			} catch (SerialPortException ex) {
				System.err.println(ex);
			} finally {
				try {
					System.out.println("Port closed: " + serialPort.closePort());
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
			}
		}

		return text;
	}
}
