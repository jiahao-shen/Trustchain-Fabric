package chainmaker.sdk.demo;

import org.chainmaker.pb.common.ContractOuterClass;
import org.chainmaker.pb.common.Request;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.utils.FileUtils;
import org.chainmaker.sdk.utils.SdkUtils;

import java.util.HashMap;
import java.util.Map;


public class Contract {

    private static final String QUERY_CONTRACT_METHOD = "query";
    private static final String INVOKE_CONTRACT_METHOD_SAVE = "save";
    private static final String INVOKE_CONTRACT_METHOD_FIND = "findByFileHash";
    private static final Map<String, byte[]> SAVE_PARAMS = new HashMap<String, byte[]>() {{
        put("file_name", "name007".getBytes());
        put("file_hash", "ab3456df5799b87c77e7f88".getBytes());
        put("time", "6543234".getBytes());
    }};
    private static final Map<String, byte[]> QUERY_PARAMS = new HashMap<String, byte[]>() {{
        put("file_hash", "ab3456df5799b87c77e7f88".getBytes());
    }};
    private static final String CONTRACT_NAME = "fact";
    private static final String CONTRACT_FILE_PATH = "docker-fact.7z";

    public static void createContract(ChainClient chainClient, User user) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            byte[] byteCode = FileUtils.getResourceFileBytes(CONTRACT_FILE_PATH);

            // 1. create payload （DOCKER_GO控制合约类型）
            Request.Payload payload = chainClient.createContractCreatePayload(CONTRACT_NAME, "1", byteCode,
                    ContractOuterClass.RuntimeType.DOCKER_GO, null);
            //2. create payloads with endorsement
            Request.EndorsementEntry[] endorsementEntries = SdkUtils
                    .getEndorsers(payload, new User[]{user});

            // 3. send request
            responseInfo = chainClient.sendContractManageRequest(payload, endorsementEntries, 10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("创建合约结果：");
        System.out.println(responseInfo);
    }
    public static void invokeContractSave(ChainClient chainClient) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            responseInfo = chainClient.invokeContract(CONTRACT_NAME, INVOKE_CONTRACT_METHOD_SAVE,
                    null, SAVE_PARAMS,10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("执行合约结果：");
        System.out.println(responseInfo);
    }

    public static void invokeContractFind(ChainClient chainClient) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            responseInfo = chainClient.invokeContract(CONTRACT_NAME, INVOKE_CONTRACT_METHOD_FIND,
                    null, QUERY_PARAMS,10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("执行合约结果：");
        System.out.println(responseInfo);
    }

}
