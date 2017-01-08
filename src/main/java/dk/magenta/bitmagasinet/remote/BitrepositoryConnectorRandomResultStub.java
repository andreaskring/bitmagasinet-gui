package dk.magenta.bitmagasinet.remote;

import java.util.concurrent.ThreadLocalRandom;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public class BitrepositoryConnectorRandomResultStub extends BitrepositoryConnectorStub {

	public BitrepositoryConnectorRandomResultStub(FileChecksum returnFileChecksum, ThreadStatus threadStatus) {
		super(returnFileChecksum, threadStatus);
	}
	
	@Override
	public void run() {
		
		int randomNumber = ThreadLocalRandom.current().nextInt(0, 2);
		long delay = ThreadLocalRandom.current().nextLong(200, 500);
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
		
		FileChecksum returnFileChecksum = super.getReturnFileChecksum();
		if (randomNumber == 0) {
			returnFileChecksum.setRemoteChecksum(returnFileChecksum.getLocalChecksum());
		} else {
			returnFileChecksum.setRemoteChecksum("00000000000000000000000000000000");
		}
		
		BitrepositoryConnectionResult bitrepositoryConnectionResult = new BitrepositoryConnectionResultImpl(
				ThreadStatus.SUCCESS, returnFileChecksum);
		
		super.notifyObservers(bitrepositoryConnectionResult);

	}

}
