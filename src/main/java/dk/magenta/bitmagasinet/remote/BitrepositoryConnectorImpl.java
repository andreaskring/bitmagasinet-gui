package dk.magenta.bitmagasinet.remote;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;

import org.bitrepository.access.AccessComponentFactory;
import org.bitrepository.access.ContributorQuery;
import org.bitrepository.access.getchecksums.GetChecksumsClient;
import org.bitrepository.bitrepositoryelements.ChecksumSpecTYPE;
import org.bitrepository.bitrepositoryelements.ChecksumType;
import org.bitrepository.client.componentid.UniqueCommandlineComponentID;
import org.bitrepository.client.eventhandler.OperationEvent;
import org.bitrepository.commandline.eventhandler.GetChecksumsEventHandler;
import org.bitrepository.commandline.output.OutputHandler;
import org.bitrepository.commandline.resultmodel.ChecksumResult;
import org.bitrepository.commandline.resultmodel.GetChecksumsResultModel;
import org.bitrepository.common.settings.Settings;
import org.bitrepository.common.settings.SettingsProvider;
import org.bitrepository.common.settings.XMLFileSettingsLoader;
import org.bitrepository.protocol.messagebus.MessageBus;
import org.bitrepository.protocol.messagebus.MessageBusManager;
import org.bitrepository.protocol.security.BasicMessageAuthenticator;
import org.bitrepository.protocol.security.BasicMessageSigner;
import org.bitrepository.protocol.security.BasicOperationAuthorizor;
import org.bitrepository.protocol.security.BasicSecurityManager;
import org.bitrepository.protocol.security.MessageAuthenticator;
import org.bitrepository.protocol.security.MessageSigner;
import org.bitrepository.protocol.security.OperationAuthorizor;
import org.bitrepository.protocol.security.PermissionStore;
import org.bitrepository.protocol.security.SecurityManager;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;

public class BitrepositoryConnectorImpl implements BitrepositoryConnector {

	private RepositoryConfiguration repositoryConfiguration;
	private FileChecksum fileChecksum;
	private List<ThreadStatusObserver> threadStatusObservers;
	private GetChecksumsResultModel model;

	public BitrepositoryConnectorImpl(RepositoryConfiguration repositoryConfiguration, FileChecksum fileChecksum) {
		this.repositoryConfiguration = repositoryConfiguration;
		this.fileChecksum = fileChecksum;
		threadStatusObservers = new ArrayList<ThreadStatusObserver>();
	}

	public void addObserver(ThreadStatusObserver observer) {
		threadStatusObservers.add(observer);
	}
	
	@Override
	public void setFileChecksum(FileChecksum fileChecksum) {
		this.fileChecksum = fileChecksum;
	}

	@Override
	public void run() {

		UniqueCommandlineComponentID uniqueCommandlineComponentID = new UniqueCommandlineComponentID();
		String clientId = uniqueCommandlineComponentID.getComponentID();

		SettingsProvider settingsLoader = new SettingsProvider(
				new XMLFileSettingsLoader(repositoryConfiguration.getPathToSettingsFiles().toString()), clientId);
		Settings settings = settingsLoader.getSettings();

		PermissionStore permissionStore = new PermissionStore();
		MessageAuthenticator authenticator = new BasicMessageAuthenticator(permissionStore);
		MessageSigner signer = new BasicMessageSigner();
		OperationAuthorizor authorizer = new BasicOperationAuthorizor(permissionStore);
		SecurityManager securityManager = new BasicSecurityManager(settings.getRepositorySettings(),
				repositoryConfiguration.getPathToCertificate().toString(), authenticator, signer, authorizer,
				permissionStore, settings.getComponentID());

		GetChecksumsClient client = AccessComponentFactory.getInstance().createGetChecksumsClient(settings,
				securityManager, clientId);

		OutputHandler outputHandler = new DNAOutputHandler();

		List<String> pillarIDs = new ArrayList<String>();
		pillarIDs.add(repositoryConfiguration.getPillarId());
		model = new GetChecksumsResultModel(pillarIDs);
		ContributorQuery[] queries = makeQuery(pillarIDs);

		long timeout = settings.getRepositorySettings().getClientSettings().getOperationTimeout().longValue();
		GetChecksumsEventHandler eventHandler = new GetChecksumsEventHandler(model, timeout, outputHandler);

		ChecksumSpecTYPE checksumRequest = new ChecksumSpecTYPE();
		checksumRequest.setChecksumType(ChecksumType.HMAC_MD5);
		checksumRequest.setChecksumSalt(fileChecksum.getSalt());

		String auditInfo = "Getting salted checksum for " + fileChecksum.getFilename();

		client.getChecksums(repositoryConfiguration.getCollectionId(), queries, fileChecksum.getFilename(),
				checksumRequest, null, eventHandler, auditInfo);

		OperationEvent event = eventHandler.getFinish();
		BitrepositoryConnectionResult bitrepositoryConnectionResult;
		if (event.getEventType().equals(OperationEvent.OperationEventType.COMPLETE)) {
			Iterator<ChecksumResult> iter = model.getCompletedResults().iterator();
			if (iter.hasNext()) {
				// Only returns one result
				ChecksumResult checksumResult = iter.next();
				String checksum = checksumResult.getChecksum(repositoryConfiguration.getPillarId());
				fileChecksum.setRemoteChecksum(checksum);
			}
			bitrepositoryConnectionResult = new BitrepositoryConnectionResultImpl(ThreadStatus.SUCCESS, fileChecksum);
		} else {
			// In case the file is not found in the bitrepository, the event type is just OperationEventType.FAILED
			fileChecksum.setRemoteChecksum("ERROR");
			bitrepositoryConnectionResult = new BitrepositoryConnectionResultImpl(ThreadStatus.ERROR, fileChecksum);
		}
		
		// Closing down after use
		MessageBus messageBus = MessageBusManager.getMessageBus();
		if (messageBus != null) {
			try {
				messageBus.close();
			} catch (JMSException e) {
				e.printStackTrace();
				messageBusError();
			}
		}

		//TODO: handle interruptions...
		
		notifyObservers(bitrepositoryConnectionResult);
		
	}
	

	private void notifyObservers(BitrepositoryConnectionResult bitrepositoryConnectionResult) {
		if (!threadStatusObservers.isEmpty()) {
			for (ThreadStatusObserver observer : threadStatusObservers) {
				observer.update(bitrepositoryConnectionResult);
			}
		}
	}
	
	private void messageBusError() {
		if (!threadStatusObservers.isEmpty()) {
			for (ThreadStatusObserver observer : threadStatusObservers) {
				observer.messageBusErrorCallback();
			}
		}
	}

	private ContributorQuery[] makeQuery(List<String> pillars) {
		List<ContributorQuery> res = new ArrayList<ContributorQuery>();
		for (String pillar : pillars) {
			Date latestResult = model.getLatestContribution(pillar);
			res.add(new ContributorQuery(pillar, latestResult, null, 10000));
		}
		return res.toArray(new ContributorQuery[pillars.size()]);
	}

}
