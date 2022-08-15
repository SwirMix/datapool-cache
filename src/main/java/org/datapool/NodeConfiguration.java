package org.datapool;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.ConnectorConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.datapool.core.cache.CacheFactory;
import org.datapool.core.csv.PersistenceCsvService;
import org.datapool.core.datapool.RandomParamService;
import org.datapool.core.datapool.UniqueCountParamService;
import org.datapool.core.jwt.impl.JwtService;
import org.datapool.db.SettingsRepository;
import org.datapool.db.UserRepository;
import org.datapool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

@Configuration
public class NodeConfiguration {
    public static final String DEFAULT_REGION_NAME = "PersistenceDataRegion";
    @Value("${csv.repo}")
    public String csvPath;
    @Value("${datapools.nodes}")
    public String nodesIp;
    @Value("${ignite.instance.key}")
    public String instanceKey;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SettingsRepository settingsRepository;

    @Bean
    public UserService prepareService(){
        UserService userService = new UserService();
        userService.setSecret(getJwtSecret());
        userService.setUserRepository(userRepository);
        return userService;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(587801600);
        return multipartResolver;
    }

    @Bean
    public Ignite prepareIgnite(){
        instanceKey = instanceKey + "-" + UUID.randomUUID().toString();
        IgniteConfiguration cfg = new IgniteConfiguration();
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList(nodesIp.split(",")));
        spi.setIpFinder(ipFinder);

        cfg.setClientMode(false);
        cfg.setDiscoverySpi(spi);
        DataStorageConfiguration storageCfg = new DataStorageConfiguration();
        DataRegionConfiguration dataRegionConfiguration = storageCfg.getDefaultDataRegionConfiguration();
        dataRegionConfiguration.setName(NodeConfiguration.DEFAULT_REGION_NAME);
        dataRegionConfiguration.setPersistenceEnabled(true);
        dataRegionConfiguration.setInitialSize(150*1024*1024);
        dataRegionConfiguration.setMaxSize(800*1024*1024);
        storageCfg.setDefaultDataRegionConfiguration(dataRegionConfiguration);
        storageCfg.setMetricsEnabled(true);
        cfg.setDataStorageConfiguration(storageCfg);
        cfg.setIgniteInstanceName(instanceKey);

        Ignite ignite = Ignition.start(cfg);
        ignite.cluster().state(ClusterState.ACTIVE);
        ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());

        ClusterGroup cacheGrp = ignite.cluster();
        IgniteServices svcs = ignite.services(cacheGrp);
        svcs.deployNodeSingleton(UniqueCountParamService.class.getName(), new UniqueCountParamService());
        svcs.deployNodeSingleton(RandomParamService.class.getName(), new RandomParamService());
        return ignite;
    }

    @Bean
    public PersistenceCsvService preparePersistenceCsvRepo(){
        PersistenceCsvService persistenceCsvService = new PersistenceCsvService();
        persistenceCsvService.setBaseFolder(new File(csvPath));
        return persistenceCsvService;
    }

    @Bean
    public JwtService jwtService(){

        JwtService service = new JwtService(getJwtSecret());
        return service;
    }

    private String getJwtSecret(){
        return settingsRepository.findById("jwt_secret").get().getValue();
    }
}
