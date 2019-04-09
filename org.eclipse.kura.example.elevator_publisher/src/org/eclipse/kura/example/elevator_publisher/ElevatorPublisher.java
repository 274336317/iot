package org.eclipse.kura.example.elevator_publisher;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.cloud.CloudClient;
import org.eclipse.kura.cloud.CloudClientListener;
import org.eclipse.kura.cloud.CloudService;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.message.KuraPayload;
import org.eclipse.kura.message.KuraPosition;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElevatorPublisher implements ConfigurableComponent, CloudClientListener
{
    private static final Logger logger;
    private ServiceTrackerCustomizer<CloudService, CloudService> cloudServiceTrackerCustomizer;
    private ServiceTracker<CloudService, CloudService> cloudServiceTracker;
    private CloudService cloudService;
    private CloudClient cloudClient;
    private String oldSubscriptionTopic;
    private ScheduledExecutorService worker;

    private Map<String, Object> properties;
    private BundleContext bundleContext;
    private ElevatorPublisherOptions ElevatorPublisherOptions;
    
    private ElevatorMsgRecvClient recvClient;
    
    static 
    {
        logger = LoggerFactory.getLogger((Class)ElevatorPublisher.class);
    }
    
    protected void activate(final ComponentContext componentContext, final Map<String, Object> properties) 
    {
        logger.info("Activating ElevatorPublisher...");
        this.worker = Executors.newSingleThreadScheduledExecutor();
        dumpProperties("Activate", this.properties = properties);
        this.bundleContext = componentContext.getBundleContext();
        this.ElevatorPublisherOptions = new ElevatorPublisherOptions(properties);
        this.cloudServiceTrackerCustomizer = (ServiceTrackerCustomizer<CloudService, CloudService>)new CloudPublisherServiceTrackerCustomizer();
        this.initCloudServiceTracking();
        this.doUpdate();
        this.subscribe();
        this.recvClient = new ElevatorMsgRecvClient(this);
        this.recvClient.init();
        logger.info("Activating ElevatorPublisher... Done.");
    }
    
    protected void deactivate(final ComponentContext componentContext) {
        logger.info("Deactivating ElevatorPublisher...");
        this.worker.shutdown();
        logger.info("Releasing CloudApplicationClient for {}...", (Object)this.ElevatorPublisherOptions.getAppId());
        this.closeCloudClient();
        this.oldSubscriptionTopic = null;
        if (Objects.nonNull(this.cloudServiceTracker)) {
            this.cloudServiceTracker.close();
        }
        logger.info("Deactivating ElevatorPublisher... Done.");
    }
    
    public void updated(final Map<String, Object> properties) {
        logger.info("Updated ElevatorPublisher...");
        dumpProperties("Update", this.properties = properties);
        this.ElevatorPublisherOptions = new ElevatorPublisherOptions(properties);
        if (Objects.nonNull(this.cloudServiceTracker)) {
            this.cloudServiceTracker.close();
        }
        this.initCloudServiceTracking();
        this.doUpdate();
        this.subscribe();
        logger.info("Updated ElevatorPublisher... Done.");
    }
    
    public void onConnectionEstablished() {
        logger.info("Connection established");
        try {
            logger.info("Number of unpublished messages: {}", (Object)this.cloudClient.getUnpublishedMessageIds().size());
        }
        catch (KuraException ex) {
            logger.error("Cannot get the list of unpublished messages");
        }
        try {
            logger.info("Number of in-flight messages: {}", (Object)this.cloudClient.getInFlightMessageIds().size());
        }
        catch (KuraException ex2) {
            logger.error("Cannot get the list of in-flight messages");
        }
        try {
            logger.info("Number of dropped in-flight messages: {}", (Object)this.cloudClient.getDroppedInFlightMessageIds().size());
        }
        catch (KuraException ex3) {
            logger.error("Cannot get the list of dropped in-flight messages");
        }
        this.subscribe();
    }
    
    public void onConnectionLost() {
        logger.warn("Connection lost!");
    }
    
    public void onControlMessageArrived(final String deviceId, final String appTopic, final KuraPayload msg, final int qos, final boolean retain) {
        logger.info("Control message arrived on assetId: {} and semantic topic: {}", (Object)deviceId, (Object)appTopic);
        this.logReceivedMessage(msg);
    }
    
    public void onMessageArrived(final String deviceId, final String appTopic, final KuraPayload msg, final int qos, final boolean retain) {
        logger.info("Message arrived on assetId: {} and semantic topic: {}", (Object)deviceId, (Object)appTopic);
        this.logReceivedMessage(msg);
    }
    
    public void onMessagePublished(final int messageId, final String appTopic) {
        logger.info("Published message with ID: {} on application topic: {}", (Object)messageId, (Object)appTopic);
    }
    
    public void onMessageConfirmed(final int messageId, final String appTopic) {
        logger.info("Confirmed message with ID: {} on application topic: {}", (Object)messageId, (Object)appTopic);
    }
    
    private static void dumpProperties(final String action, final Map<String, Object> properties) {
        final Set<String> keys = new TreeSet<String>(properties.keySet());
        for (final String key : keys) {
            logger.info("{} - {}: {}", new Object[] { action, key, properties.get(key) });
        }
    }
    
    private void doUpdate() 
    {
//        if (this.handle != null) {
//            this.handle.cancel(true);
//        }
//        this.temperature = this.ElevatorPublisherOptions.getTempInitial();
//        final int pubrate = this.ElevatorPublisherOptions.getPublishRate();
//        this.handle = this.worker.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                ElevatorPublisher.this.doPublish();
//            }
//        }, 0L, pubrate, TimeUnit.MILLISECONDS);
    }
    
    public void doPublish(String message) {

        try {
        	KuraPayload payload = new KuraPayload();

            payload.setTimestamp(new Date());

            payload.addMetric("rate", System.currentTimeMillis());
            if (Objects.nonNull(this.cloudService) && Objects.nonNull(this.cloudClient)) {

                final int messageId = this.cloudClient.publish("data/metrics", payload, 0, true);
                logger.info("Published to {} message: {} with ID: {}", new Object[] { "data/metrics", payload, messageId });
            }
            else
            {
            	logger.warn("CloudService Or CloudClient Is Null !");
            }
        }
        catch (Exception e) {
            logger.error("Send Msg Failed!", (Object)e);
        }
    }
    
    private void initCloudServiceTracking() {
        final String selectedCloudServicePid = this.ElevatorPublisherOptions.getCloudPublisherPid();
        final String filterString = String.format("(&(%s=%s)(kura.service.pid=%s))", "objectClass", CloudService.class.getName(), selectedCloudServicePid);
        Filter filter = null;
        try {
            filter = this.bundleContext.createFilter(filterString);
        }
        catch (InvalidSyntaxException e) {
            logger.error("Filter setup exception ", (Throwable)e);
        }
        (this.cloudServiceTracker = (ServiceTracker<CloudService, CloudService>)new ServiceTracker(this.bundleContext, filter, (ServiceTrackerCustomizer)this.cloudServiceTrackerCustomizer)).open();
    }
    
    private void closeCloudClient() {
        if (Objects.nonNull(this.cloudClient)) {
            this.cloudClient.removeCloudClientListener((CloudClientListener)this);
            this.cloudClient.release();
            this.cloudClient = null;
        }
    }
    
    private void setupCloudClient() throws KuraException {
        this.closeCloudClient();
        final String appId = this.ElevatorPublisherOptions.getAppId();
        (this.cloudClient = this.cloudService.newCloudClient(appId)).addCloudClientListener((CloudClientListener)this);
    }
    
    private void logReceivedMessage(final KuraPayload msg) {
        final Date timestamp = msg.getTimestamp();
        if (timestamp != null) {
            logger.info("Message timestamp: {}", (Object)timestamp.getTime());
        }
        final KuraPosition position = msg.getPosition();
        if (position != null) {
            logger.info("Position latitude: {}", (Object)position.getLatitude());
            logger.info("         longitude: {}", (Object)position.getLongitude());
            logger.info("         altitude: {}", (Object)position.getAltitude());
            logger.info("         heading: {}", (Object)position.getHeading());
            logger.info("         precision: {}", (Object)position.getPrecision());
            logger.info("         satellites: {}", (Object)position.getSatellites());
            logger.info("         speed: {}", (Object)position.getSpeed());
            logger.info("         status: {}", (Object)position.getStatus());
            logger.info("         timestamp: {}", (Object)position.getTimestamp());
        }
        final byte[] body = msg.getBody();
        if (body != null && body.length != 0) {
            logger.info("Body lenght: {}", (Object)body.length);
        }
        if (msg.metrics() != null) {
            for (final Map.Entry<String, Object> entry : msg.metrics().entrySet()) {
                logger.info("Message metric: {}, value: {}", (Object)entry.getKey(), entry.getValue());
            }
        }
    }
    
    private void subscribe() {
//        try {
//            if (this.cloudClient != null && this.cloudClient.isConnected()) {
//                if (this.oldSubscriptionTopic != null) {
//                    this.cloudClient.unsubscribe(this.oldSubscriptionTopic);
//                }
//                final String newSubscriptionTopic = this.ElevatorPublisherOptions.getSubscribeTopic();
//                logger.info("Subscribing to application topic {}", (Object)newSubscriptionTopic);
//                this.cloudClient.subscribe(newSubscriptionTopic, 0);
//                this.oldSubscriptionTopic = newSubscriptionTopic;
//            }
//        }
//        catch (KuraStoreException e) {
//            logger.warn("Failed to request device shadow", (Throwable)e);
//        }
//        catch (KuraException e2) {
//            logger.warn("Failed to subscribe", (Throwable)e2);
//        }
    }
    
    static  void access$1(final ElevatorPublisher ElevatorPublisher, final CloudService cloudService) {
        ElevatorPublisher.cloudService = cloudService;
    }
    
    private final class CloudPublisherServiceTrackerCustomizer implements ServiceTrackerCustomizer<CloudService, CloudService>
    {
        public CloudService addingService(final ServiceReference<CloudService> reference) {
            ElevatorPublisher.access$1(ElevatorPublisher.this, (CloudService)ElevatorPublisher.this.bundleContext.getService((ServiceReference)reference));
            try {
                ElevatorPublisher.this.setupCloudClient();
            }
            catch (KuraException e) {
                logger.error("Cloud Client setup failed!", (Throwable)e);
            }
            return ElevatorPublisher.this.cloudService;
        }
        
        public void modifiedService(final ServiceReference<CloudService> reference, final CloudService service) {
            ElevatorPublisher.access$1(ElevatorPublisher.this, (CloudService)ElevatorPublisher.this.bundleContext.getService((ServiceReference)reference));
            try {
                ElevatorPublisher.this.setupCloudClient();
            }
            catch (KuraException e) {
                logger.error("Cloud Client setup failed!", (Throwable)e);
            }
        }
        
        public void removedService(final ServiceReference<CloudService> reference, final CloudService service) {
            ElevatorPublisher.access$1(ElevatorPublisher.this, null);
        }
    }
}
