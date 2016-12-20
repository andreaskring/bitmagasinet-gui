package dk.magenta.bitmagasinet;

import java.net.MalformedURLException;
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
import org.bitrepository.client.eventhandler.EventHandler;
import org.bitrepository.client.eventhandler.OperationEvent;
import org.bitrepository.client.eventhandler.OperationEvent.OperationEventType;
import org.bitrepository.commandline.eventhandler.GetChecksumsEventHandler;
import org.bitrepository.commandline.output.OutputHandler;
import org.bitrepository.commandline.resultmodel.ChecksumResult;
import org.bitrepository.commandline.resultmodel.GetChecksumsResultModel;
import org.bitrepository.common.settings.Settings;
import org.bitrepository.common.settings.SettingsProvider;
import org.bitrepository.common.settings.XMLFileSettingsLoader;
import org.bitrepository.common.utils.Base16Utils;
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

import dk.magenta.bitmagasinet.remote.DNAOutputHandler;

public class BitrepositoryClientExample {

	private GetChecksumsResultModel model;
	
    private class MyEventHandler implements EventHandler {

        private final Object finishLock = new Object();
        private boolean finished = false;
        private OperationEventType finishEventType;

        public void handleEvent(OperationEvent event) {
            switch(event.getEventType()) {
                case COMPLETE:
                	System.out.println("###################################################################");
                	System.out.println(event.getEventType());
                    finishEventType = OperationEventType.COMPLETE;
                    finish();
                    break;
                case FAILED:
                    finishEventType = OperationEventType.FAILED;
                    finish();
                    break;
                default:
                    break;
            }   
        }

        private void finish() {
            synchronized(finishLock) {
                finished = true;
                finishLock.notifyAll();
            }
        }

        public OperationEventType waitForFinish() throws InterruptedException {
            synchronized (finishLock) {
                if(finished == false) {
                    finishLock.wait();
                }
                return finishEventType;
            }
        }

    }


    public void example() throws MalformedURLException, JMSException, InterruptedException {

        // Creating the client    
        String pathToSettingDir = "/home/andreas/bitmagasinet/bitrepository-client-1.6/conf";
        String clientID = "myClientIDxyz1729";
        String certificateFile = "/home/andreas/bitmagasinet/bitrepository-client-1.6/conf/rigsark-store-client-certkey.pem";

        SettingsProvider settingsLoader = new SettingsProvider(
                        new XMLFileSettingsLoader(pathToSettingDir), clientID);
        Settings settings = settingsLoader.getSettings();

        PermissionStore permissionStore = new PermissionStore();
        MessageAuthenticator authenticator = new BasicMessageAuthenticator(permissionStore);
        MessageSigner signer = new BasicMessageSigner();
        OperationAuthorizor authorizer = new BasicOperationAuthorizor(permissionStore);
        SecurityManager securityManager = new BasicSecurityManager(
                        settings.getRepositorySettings(), certificateFile, authenticator, 
                        signer, authorizer, permissionStore, settings.getComponentID());
        
        GetChecksumsClient client = AccessComponentFactory.getInstance().createGetChecksumsClient(settings, securityManager, clientID);

        //Using the client
        String collectionID = "2";
        String fileID = "2016-04-26_00860.SA";
        
        ChecksumSpecTYPE checksumRequest = new ChecksumSpecTYPE();
        byte[] saltConverted = Base16Utils.encodeBase16("64");
        checksumRequest.setChecksumType(ChecksumType.HMAC_MD5);
        checksumRequest.setChecksumSalt(saltConverted);
        
        // MyEventHandler eventHandler = new MyEventHandler();
        
        String auditInformation = "Get checksum for file";
        
        OutputHandler dnaOutputHandler = new DNAOutputHandler();
        
        List<String> pillarIDs = new ArrayList<String>();
        String pillar = "rigsarkivnearline1"; 
        pillarIDs.add(pillar);
        
        model = new GetChecksumsResultModel(pillarIDs);
        GetChecksumsEventHandler eventHandler = new GetChecksumsEventHandler(model, (long) 3600000, dnaOutputHandler);
        ContributorQuery[] queries = makeQuery(pillarIDs);
        
        client.getChecksums(collectionID, queries, fileID, checksumRequest, null, eventHandler, auditInformation);

        OperationEvent event = eventHandler.getFinish();
        if(event.getEventType().equals(OperationEvent.OperationEventType.FAILED)) {
        	System.out.println("#################### ERROR ##################");
        }
        Iterator<ChecksumResult> iter = model.getCompletedResults().iterator();
        while (iter.hasNext()) {
        	ChecksumResult result = iter.next();
        	System.out.println("MD5SUM = " + result.getChecksum(pillar));
        }

//        OperationEventType finishType = eventHandler.waitForFinish();
//        // Add handling for finishType
        System.out.println("Done");
        


        // Closing down after use
        MessageBus messageBus = MessageBusManager.getMessageBus();
        if (messageBus != null) {
            messageBus.close();
        }

    }
    
    private ContributorQuery[] makeQuery(List<String> pillars) {
        List<ContributorQuery> res = new ArrayList<ContributorQuery>();
        for(String pillar : pillars) {
            Date latestResult = model.getLatestContribution(pillar);
            res.add(new ContributorQuery(pillar, latestResult, null, 10000));
        }
        return res.toArray(new ContributorQuery[pillars.size()]);
    }

}