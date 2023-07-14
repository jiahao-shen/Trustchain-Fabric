package chainmaker.demo.config;

import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainManager;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.config.NodeConfig;
import org.chainmaker.sdk.config.SdkConfig;
import org.chainmaker.sdk.utils.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitClient {

    static final String CLIENT1_KEY_PATH = "crypto-config/public/user/client1/client1.sign.key";
    static final String CLIENT1_CERT_PATH = "crypto-config/public/user/client1/client1.sign.crt";

    static String CLIENT1_TLS_KEY_PATH = "crypto-config/public/user/client1/client1.tls.key";
    static String CLIENT1_TLS_CERT_PATH = "crypto-config/public/user/client1/client1.tls.crt";

    static final String ORG_ID1 = "public";

    static String SDK_CONFIG = "sdk_config.yml";

    @Bean
    public User inItChainUser() throws Exception {
        return new User(ORG_ID1, FileUtils.getResourceFileBytes(CLIENT1_KEY_PATH),
                FileUtils.getResourceFileBytes(CLIENT1_CERT_PATH),
                FileUtils.getResourceFileBytes(CLIENT1_TLS_KEY_PATH),
                FileUtils.getResourceFileBytes(CLIENT1_TLS_CERT_PATH), false);
    }

    @Bean
    public ChainClient inItChainClient() throws Exception {
        ChainClient chainClient;
        ChainManager chainManager;
        Yaml yaml = new Yaml();
        InputStream in = InitClient.class.getClassLoader().getResourceAsStream(SDK_CONFIG);

        SdkConfig sdkConfig;
        sdkConfig = yaml.loadAs(in, SdkConfig.class);
        assert in != null;
        in.close();

        for (NodeConfig nodeConfig : sdkConfig.getChainClient().getNodes()) {
            List<byte[]> tlsCaCertList = new ArrayList<>();
            if (nodeConfig.getTrustRootPaths() != null) {
                for (String rootPath : nodeConfig.getTrustRootPaths()) {
                    List<String> filePathList = FileUtils.getFilesByPath(rootPath);
                    for (String filePath : filePathList) {
                        tlsCaCertList.add(FileUtils.getFileBytes(filePath));
                    }
                }
            }
            byte[][] tlsCaCerts = new byte[tlsCaCertList.size()][];
            tlsCaCertList.toArray(tlsCaCerts);
            nodeConfig.setTrustRootBytes(tlsCaCerts);
        }

        chainManager = ChainManager.getInstance();
        chainClient = chainManager.getChainClient(sdkConfig.getChainClient().getChainId());
//        System.out.println("---------- " + sdkConfig.getChainClient().getChainId());
        if (chainClient == null) {
            chainClient = chainManager.createChainClient(sdkConfig);
        }
        return chainClient;
    }

//    public static void inItChainClient() throws Exception {
//        Yaml yaml = new Yaml();
//        InputStream in = InitClient.class.getClassLoader().getResourceAsStream(SDK_CONFIG);
//
//        SdkConfig sdkConfig;
//        sdkConfig = yaml.loadAs(in, SdkConfig.class);
//        assert in != null;
//        in.close();
//
//        for (NodeConfig nodeConfig : sdkConfig.getChainClient().getNodes()) {
//            List<byte[]> tlsCaCertList = new ArrayList<>();
//            if (nodeConfig.getTrustRootPaths() != null) {
//                for (String rootPath : nodeConfig.getTrustRootPaths()) {
//                    List<String> filePathList = FileUtils.getFilesByPath(rootPath);
//                    for (String filePath : filePathList) {
//                        tlsCaCertList.add(FileUtils.getFileBytes(filePath));
//                    }
//                }
//            }
//            byte[][] tlsCaCerts = new byte[tlsCaCertList.size()][];
//            tlsCaCertList.toArray(tlsCaCerts);
//            nodeConfig.setTrustRootBytes(tlsCaCerts);
//        }
//
//        chainManager = ChainManager.getInstance();
//        chainClient = chainManager.getChainClient(sdkConfig.getChainClient().getChainId());
//
//        if (chainClient == null) {
//            chainClient = chainManager.createChainClient(sdkConfig);
//        }
//
//        user = new User(ORG_ID1, FileUtils.getResourceFileBytes(CLIENT1_KEY_PATH),
//                FileUtils.getResourceFileBytes(CLIENT1_CERT_PATH),
//                FileUtils.getResourceFileBytes(CLIENT1_TLS_KEY_PATH),
//                FileUtils.getResourceFileBytes(CLIENT1_TLS_CERT_PATH), false);
//    }
}
