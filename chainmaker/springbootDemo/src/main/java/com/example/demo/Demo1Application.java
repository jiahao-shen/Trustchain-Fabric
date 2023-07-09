package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.example.demo.config.InitClient.inItChainClient;

@SpringBootApplication
public class Demo1Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Demo1Application.class, args);

        inItChainClient();
//        查询链配置
//        ChainConfig.getChainConfig(InitClient.chainClient);
        //创建合约
//        Contract.createContract(InitClient.chainClient, InitClient.adminUser1, InitClient.adminUser2);
        //调用合约
//        Contract.invokeContract(InitClient.chainClient);
    }

}
