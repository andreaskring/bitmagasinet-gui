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

import dk.magenta.bitmagasinet.DNAOutputHandler;
import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;

public class BitrepositoryConnectorImpl implements BitrepositoryConnector {

	private String clientId;
	private SettingsProvider settingsLoader;
	private GetChecksumsClient client;
	private RepositoryConfiguration repositoryConfiguration;
	private Settings settings;
	private GetChecksumsResultModel model;
	private ContributorQuery[] queries;
	private GetChecksumsEventHandler eventHandler;
	
	public BitrepositoryConnectorImpl(RepositoryConfiguration repositoryConfiguration) {
		this.repositoryConfiguration = repositoryConfiguration;

		UniqueCommandlineComponentID uniqueCommandlineComponentID = new UniqueCommandlineComponentID();
		clientId = uniqueCommandlineComponentID.getComponentID();

		settingsLoader = new SettingsProvider(
				new XMLFileSettingsLoader(repositoryConfiguration.getPathToSettingsFiles().toString()), clientId);
		settings = settingsLoader.getSettings();

		PermissionStore permissionStore = new PermissionStore();
		MessageAuthenticator authenticator = new BasicMessageAuthenticator(permissionStore);
		MessageSigner signer = new BasicMessageSigner();
		OperationAuthorizor authorizer = new BasicOperationAuthorizor(permissionStore);
		SecurityManager securityManager = new BasicSecurityManager(settings.getRepositorySettings(),
				repositoryConfiguration.getPathToCertificate().toString(), authenticator, signer, authorizer,
				permissionStore, settings.getComponentID());

		client = AccessComponentFactory.getInstance().createGetChecksumsClient(settings, securityManager, clientId);

		OutputHandler outputHandler = new DNAOutputHandler();

		List<String> pillarIDs = new ArrayList<String>();
		pillarIDs.add(repositoryConfiguration.getPillarId());
		queries = makeQuery(pillarIDs);

		model = new GetChecksumsResultModel(pillarIDs);
		long timeout = settings.getRepositorySettings().getClientSettings().getOperationTimeout().longValue();
		eventHandler = new GetChecksumsEventHandler(model, timeout, outputHandler);

	}

	@Override
	public String getRemoteChecksum(FileChecksum fileChecksum) throws Exception {

		ChecksumSpecTYPE checksumRequest = new ChecksumSpecTYPE();
		checksumRequest.setChecksumType(ChecksumType.HMAC_MD5);
		checksumRequest.setChecksumSalt(fileChecksum.getSalt());

		String auditInfo = "Getting salted checksum for " + fileChecksum.getFilename();
		
		client.getChecksums(repositoryConfiguration.getCollectionId(), queries, fileChecksum.getFilename(), checksumRequest, null, eventHandler, auditInfo);

		OperationEvent event = eventHandler.getFinish();
		if (event.getEventType().equals(OperationEvent.OperationEventType.FAILED)) {
			throw new UnsuccessfulChecksumRetreivalException("Hentning af checksum fejlede");
		} else if (event.getEventType().equals(OperationEvent.OperationEventType.COMPLETE)) {
			Iterator<ChecksumResult> iter = model.getCompletedResults().iterator();
			String checksum = null;
			while (iter.hasNext()) {
				// Only returns one result
				ChecksumResult result = iter.next();
				checksum = result.getChecksum(repositoryConfiguration.getPillarId());
			}
			return checksum;
		} else {
			throw new UnsuccessfulChecksumRetreivalException("Hentning af checksum fejlede");
		}
	}
	
	@Override
	public void closeMessageBus() throws JMSException {
		 // Closing down after use
		 MessageBus messageBus = MessageBusManager.getMessageBus();
		 if (messageBus != null) {
			 messageBus.close();
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
