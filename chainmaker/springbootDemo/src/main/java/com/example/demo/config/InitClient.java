package com.example.demo.config;

import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainManager;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.config.NodeConfig;
import org.chainmaker.sdk.config.SdkConfig;
import org.chainmaker.sdk.utils.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class InitClient {

    static final String ADMIN1_KEY_PATH = "crypto-config/public-trustchain/user/admin/admin.sign.key";
    static final String ADMIN1_CERT_PATH = "crypto-config/public-trustchain/user/admin/admin.sign.crt";
    static final String ADMIN2_KEY_PATH = "crypto-config/buaa/user/buaaAdmin/buaaAdmin.sign.key";
    static final String ADMIN2_CERT_PATH = "crypto-config/buaa/user/buaaAdmin/buaaAdmin.sign.crt";

    static String ADMIN1_TLS_KEY_PATH = "crypto-config/public-trustchain/user/admin/admin.tls.key";
    static String ADMIN1_TLS_CERT_PATH = "crypto-config/public-trustchain/user/admin/admin.tls.crt";
    static String ADMIN2_TLS_KEY_PATH = "crypto-config/buaa/user/buaaAdmin/buaaAdmin.tls.key";
    static String ADMIN2_TLS_CERT_PATH = "crypto-config/buaa/user/buaaAdmin/buaaAdmin.tls.crt";
    
    static final String ORG_ID1 = "public-trustchain";
    static final String ORG_ID2 = "buaa";
    static String SDK_CONFIG = "sdk_config.yml";
    public static ChainClient chainClient;
    static ChainManager chainManager;
    public static User adminUser1;
    public static User adminUser2;
    public static void inItChainClient() throws Exception {
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

        if (chainClient == null) {
            chainClient = chainManager.createChainClient(sdkConfig);
        }

        adminUser1 = new User(ORG_ID1, FileUtils.getResourceFileBytes(ADMIN1_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN1_CERT_PATH),
                FileUtils.getResourceFileBytes(ADMIN1_TLS_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN1_TLS_CERT_PATH));

        adminUser2 = new User(ORG_ID2, FileUtils.getResourceFileBytes(ADMIN2_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN2_CERT_PATH),
                FileUtils.getResourceFileBytes(ADMIN2_TLS_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN2_TLS_CERT_PATH));
    }
}
