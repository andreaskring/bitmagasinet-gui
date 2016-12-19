package dk.magenta.bitmagasinet.remote;

import javax.jms.JMSException;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public interface BitrepositoryConnector {

	public String getRemoteChecksum(FileChecksum fileChecksum) throws Exception;
	public void closeMessageBus() throws JMSException;
	
}
